package org.pentaho.pac.server.datasources;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.pentaho.pac.client.datasources.Constants;
import org.pentaho.pac.client.datasources.IDataSource;
import org.pentaho.pac.client.datasources.SimpleDataSource;

/**
 * A Tomcat specific DataSourceManager.
 * 
 * This class fully implements the following actions:
 * <h4>Add datasource: </h4>
 * The 'add' method encapsulates three operations:
 * <ol><li>Adds a data source to Tomcat's global runtime context</li>
 * <li>Adds a &lt;Resource&gt; element under &lt;GlobalNamingResources&gt; in the server.xml file.</li>
 * <li>Adds a &lt;ResourceLink&gt; element linking the global DataSource to a specific web app context, as specified in the
 * <i>&lt;webapp&gt;</i>/META-INF/context.xml file. </ol>
 * <h4>Delete datasource: </h4>
 * Also encapsulates two atomic operations:
 * <ol><li>Deletes a data source from Tomcat's global runtime context</li>
 * <li>Deletes a &lt;Resource&gt; element under &lt;GlobalNamingResources&gt; in the server.xml file.</li>
 * <li>Deletes a &lt;ResourceLink&gt; element linking the global DataSource to a specific web app context, as specified in the
 * <i>&lt;webapp&gt;</i>/META-INF/context.xml file. </ol>
 * <BR>The other operation supported lists all the datasources currently deployed globally on the server. (on server.xml as GlobalResources.)
 * 
 * This implementation also uses remote JXM to connect to the running Tomcat instance.  As a result, it is necessary to start remote JXM in Tomcat.
 * One of the ways to do so is by adding the the following options to catalina startup script:
 * -Dcom.sun.management.jmxremote
 * -Dcom.sun.management.jmxremote.port="9004" <i>This is the port where remote JXM will be available at.</i>
 * -Dcom.sun.management.jmxremote.authenticate="true|false" <i>Username/password required for connection.</i>
 * -Dcom.sun.management.jmxremote.ssl="true|false"
 * 
 * 
 * Obviously, this class needs to be informed of the port where Tomcat JXM is running.  This is done by providing a parameter named
 *  'TOMCATJMX_PORT' that defines the port, as follows:
 *  adapter.addInitParameter(TOMCATJMX_PORT, PORT_NUMBER);
 *  
 *  Moreover, if authentication is turned on, two other parameters are required:
 *	adapter.addInitParameter(TOMCATJMX_USERNAME, USERNAME);
 *	adapter.addInitParameter(TOMCATJMX_PASSWORD, PASSWORD);
 *
 * Lastly, in order for resource links work, the datasource MUST HAVE THE CORRECT PATH AND HOST INFORMATION set before calling 
 * the methods on this class.
 * 
 * @author Alex Silva
 * 
 */
public class TomcatDSManager extends BaseDSManager {

  private static final String EMPTY_STRING = ""; //$NON-NLS-1$

  private static final String DOMAIN_NAME = "Catalina"; //$NON-NLS-1$

  private static final String TOMCAT_JMX_URL_END = "/jmxrmi";
  //$NON-NLS-1$
  private static final String TOMCAT_JMX_URL_BEGIN = "service:jmx:rmi:///jndi/rmi://"; //$NON-NLS-1$

  private static final String JMX_REMOTE_CREDENTIALS = "jmx.remote.credentials"; //$NON-NLS-1$

  private final static String RESOURCE_TYPE = ":type=Resource"; //$NON-NLS-1$

  private final static String CONTEXT_TYPE = ",resourcetype=Context"; //$NON-NLS-1$

  private final static String GLOBAL = "Global"; //$NON-NLS-1$

  private final static String GLOBAL_TYPE = ",resourcetype=Global"; //$NON-NLS-1$

  private final static String DATASOURCE_CLASS = "javax.sql.DataSource"; //$NON-NLS-1$

  private final static String NAMINGRESOURCES_TYPE = ":type=NamingResources"; //$NON-NLS-1$

  private final static String RESOURCELINK_TYPE = ":type=ResourceLink"; //$NON-NLS-1$

  private final static String HOST_DEFAULTCONTEXT_TYPE = ",resourcetype=HostDefaultContext"; //$NON-NLS-1$

  private final static String SERVICE_DEFAULTCONTEXT_TYPE = ",resourcetype=ServiceDefaultContext"; //$NON-NLS-1$

  private final static String[] SIG = new String[] { "java.lang.String", "java.lang.String" }; //$NON-NLS-1$

  /**
   * Initializes this manager implementation by trying to establish a remote JMX connection.
   * @throws DataSourceManagementException if the connection cannot be established. (Invalid port, host, credentials, etc.)
   */
  public void init() throws DataSourceManagementException {
    Object port = getInitParameter(Constants.JMX_PORT);

    if (port == null)
      throw new DataSourceManagementException("No remote JXM port specified.");

    Object host = getInitParameter(Constants.JMX_HOST);

    if (host == null)
      throw new DataSourceManagementException("No remote JXM host specified.");

    String urlForJMX = TOMCAT_JMX_URL_BEGIN + host + ":" + port + TOMCAT_JMX_URL_END; //$NON-NLS-1$

    Map<String, String[]> map = new HashMap<String, String[]>();
    Object un = getInitParameter(Constants.JMX_USERNAME);
    Object pwd = getInitParameter(Constants.JMX_PASSWORD);
    String[] credentials = new String[] { un != null ? un.toString() : null, pwd != null ? pwd.toString() : null };
    map.put(JMX_REMOTE_CREDENTIALS, credentials);

    try {
      mserver = JMXConnectorFactory.connect(new JMXServiceURL(urlForJMX), map).getMBeanServerConnection();
    } catch (IOException e) {
      throw new DataSourceManagementException("Unable to connect to Tomcat JMX Server: " + e.getMessage(), e);
    }
  }

  /**
   * Doesn't do anything
   */
  public void destroy() throws DataSourceManagementException {
    mserver = null;
  }

  public void addDataSource(IDataSource ds) throws DataSourceManagementException {
    Object params[] = new Object[2];
    params[0] = ds.getJndiName();
    params[1] = DATASOURCE_CLASS;
    String encodedJndiName = null;

    try {
      encodedJndiName = URLEncoder.encode(params[0].toString(), "UTF-8"); //$NON-NLS-1$

      ObjectName oname = new ObjectName(DOMAIN_NAME + RESOURCE_TYPE + GLOBAL_TYPE + ",class=" + params[1] + ",name=" //$NON-NLS-1$ //$NON-NLS-2$
          + params[0]);

      ObjectName encodedOName = new ObjectName(DOMAIN_NAME + RESOURCE_TYPE + GLOBAL_TYPE + ",class=" + params[1] //$NON-NLS-1$
          + ",name=" + encodedJndiName);

      if (mserver.isRegistered(oname) || mserver.isRegistered(encodedOName)) {
        throw new DataSourceManagementException("DataSource '" + ds.getJndiName()
            + "' is already registered with this server instance.");
      }

      String path = ds.getParameter(Constants.PATH);

      oname = getNamingResourceObjectName(DOMAIN_NAME, "Global", path, ds.getParameter(Constants.HOST)); //$NON-NLS-1$

      String objectName = (String) mserver.invoke(oname, "addResource", params, SIG); //$NON-NLS-1$
      updateInternal(objectName, ds);
      try {
        addResourceLink(null, ds);
      } catch (DataSourceManagementException e) {
        //we should delete the one we just added because it shouldnt have been added in the 1st place
        deleteDataSource(ds);
        throw e;
      }

    } catch (RuntimeException e) {
      throw e;

    } catch (Exception e) {
      throw new DataSourceManagementException(e);

    }

  }

  private void addResourceLink(String objectName, IDataSource ds) throws DataSourceManagementException {
    if (objectName == null) {
      Object params[] = new Object[2];
      params[0] = ds.getJndiName();
      params[1] = "javax.sql.DataSource"; //$NON-NLS-1$

      String resourcetype = "Context"; //$NON-NLS-1$
      String path = ds.getParameter(Constants.PATH);
      String host = ds.getParameter(Constants.HOST);

      ObjectName oname = null;

      try {

        oname = new ObjectName(DOMAIN_NAME + RESOURCELINK_TYPE + CONTEXT_TYPE + ",path=" + path + ",host=" + host //$NON-NLS-1$ //$NON-NLS-2$
            + ",name=" + params[0]); //$NON-NLS-1$

        if (mserver.isRegistered(oname)) {
          throw new DataSourceManagementException("ResourceLink '" + ds.getJndiName()
              + "' is already registered with this context.");
        }

        oname = getNamingResourceObjectName(DOMAIN_NAME, resourcetype, path, host);

        objectName = (String) mserver.invoke(oname, "addResourceLink", params, SIG); //$NON-NLS-1$

      } catch (RuntimeException e) {
        throw e;

      } catch (Exception e) {

        throw new DataSourceManagementException(e);
      }

    }
    try {

      ObjectName oname = new ObjectName(objectName);

      mserver.setAttribute(oname, new Attribute("global", ds.getJndiName())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("type", "javax.sql.DataSource")); //$NON-NLS-1$ //$NON-NLS-2$

    } catch (Exception e) {

      throw new DataSourceManagementException(e);
    }
  }

  private void deleteResourceLink(IDataSource ds) throws DataSourceManagementException {
    try {
      String path = ds.getParameter(Constants.PATH);
      String host = ds.getParameter(Constants.HOST);
      ObjectName dname = getNamingResourceObjectName(DOMAIN_NAME, "Context", path, host); //$NON-NLS-1$
      mserver.invoke(dname, "removeResourceLink", new Object[] { "\"" + ds.getJndiName() + "\"" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          new String[] { SIG[0] });

    } catch (Throwable t) {
      throw new DataSourceManagementException(t);
    }
  }

  private void updateInternal(ObjectName oname, IDataSource ds) throws DataSourceManagementException {
    try {
      mserver.setAttribute(oname, new Attribute("url", ds.getUrl())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("driverClassName", ds.getDriverClass())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("username", ds.getUserName())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("password", ds.getPassword())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("maxActive", ds.getActive())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("maxIdle", ds.getIdle())); //$NON-NLS-1$
      mserver.setAttribute(oname, new Attribute("maxWait", ds.getWait())); //$NON-NLS-1$
      String validationQuery = ds.getValidationQuery();
      if ((validationQuery != null) && (validationQuery.length() > 0)) {
        mserver.setAttribute(oname, new Attribute("validationQuery", validationQuery)); //$NON-NLS-1$
      }
    } catch (RuntimeException e) {
      throw e;

    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }
  }

  private void updateInternal(String dsName, IDataSource ds) throws DataSourceManagementException {
    try {
      updateInternal(new ObjectName(dsName), ds);
    } catch (RuntimeException e) {
      throw e;

    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }

  }

  private static ObjectName getNamingResourceObjectName(String domain, String resourcetype, String path, String host)
      throws Exception {

    ObjectName oname = null;
    if ((resourcetype == null) || (domain == null)) {
      return null;
    }
    // Construct the MBean Name for the naming source
    if (resourcetype.equals("Global")) { //$NON-NLS-1$
      oname = new ObjectName(domain + NAMINGRESOURCES_TYPE + GLOBAL_TYPE);
    } else if (resourcetype.equals("Context")) { //$NON-NLS-1$
      oname = new ObjectName(domain + NAMINGRESOURCES_TYPE + CONTEXT_TYPE + ",path=" + path + ",host=" + host); //$NON-NLS-1$ //$NON-NLS-2$
    } else if (resourcetype.equals("DefaultContext")) { //$NON-NLS-1$
      if (host.length() > 0) {
        oname = new ObjectName(domain + NAMINGRESOURCES_TYPE + HOST_DEFAULTCONTEXT_TYPE + ",host=" + host);
      } else {
        oname = new ObjectName(domain + NAMINGRESOURCES_TYPE + SERVICE_DEFAULTCONTEXT_TYPE);
      }
    }

    return oname;

  }

  public void updateDataSource(IDataSource ds) throws DataSourceManagementException {
    try {
      ObjectName oname = new ObjectName(DOMAIN_NAME + RESOURCE_TYPE + GLOBAL_TYPE + ",class=" + DATASOURCE_CLASS //$NON-NLS-1$
          + ",name=\"" + ds.getJndiName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
      updateInternal(oname, ds);
    } catch (RuntimeException e) {
      throw e;

    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }

  }

  public void deleteDataSource(IDataSource ds) throws DataSourceManagementException {
    try {
      ObjectName dname = getNamingResourceObjectName(DOMAIN_NAME, GLOBAL, ds.getParameter(Constants.PATH), ds
          .getParameter(Constants.HOST));

      mserver.invoke(dname, "removeResource", new Object[] { "\"" + ds.getJndiName() + "\"" }, new String[] { SIG[0] }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      deleteResourceLink(ds);

    } catch (RuntimeException e) {
      throw e;

    } catch (Exception t) {
      throw new DataSourceManagementException(t);

    }

  }

  public void deleteDataSource(String ds) throws DataSourceManagementException {
    try {
      ObjectName oname = new ObjectName(ds);
      ObjectName dname = getNamingResourceObjectName(DOMAIN_NAME, oname.getKeyProperty("resourcetype"), EMPTY_STRING, //$NON-NLS-1$
          EMPTY_STRING);
      String name = oname.getKeyProperty("name"); //$NON-NLS-1$
      if (!name.startsWith("\"")) //$NON-NLS-1$
        name = "\"" + name + "\""; //$NON-NLS-1$ //$NON-NLS-2$
      mserver.invoke(dname, "removeResource", new Object[] { name }, new String[] { SIG[0] }); //$NON-NLS-1$

    } catch (RuntimeException e) {
      throw e;

    } catch (Exception t) {
      throw new DataSourceManagementException(t);

    }

  }

  public Collection<String> getDataSources() throws DataSourceManagementException {
    try {
      ObjectName rname = new ObjectName(DOMAIN_NAME + RESOURCE_TYPE + GLOBAL_TYPE + ",class=" + DATASOURCE_CLASS + ",*"); //$NON-NLS-1$ //$NON-NLS-2$

      Iterator<ObjectInstance> iterator = (mserver.queryMBeans(rname, null).iterator());

      List<String> results = new ArrayList<String>();
      while (iterator.hasNext()) {
        ObjectName oname = iterator.next().getObjectName();
        try {
          // only add resource mbean if definition exists
          mserver.getAttribute(oname, "driverClassName"); //$NON-NLS-1$
          results.add(ObjectName.unquote(oname.getKeyProperty("name"))); //$NON-NLS-1$
        } catch (AttributeNotFoundException ex) {
          mserver.setAttribute(oname, new Attribute("driverClassName", EMPTY_STRING)); //$NON-NLS-1$
        }
      }

      Collections.sort(results);

      return results;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }
  }

  public IDataSource getDataSource(String name) throws DataSourceManagementException {
    try {
      ObjectName oname = new ObjectName(DOMAIN_NAME + RESOURCE_TYPE + GLOBAL_TYPE + ",class=" + DATASOURCE_CLASS //$NON-NLS-1$
          + ",name=\"" + name + "\""); //$NON-NLS-1$ //$NON-NLS-2$

      IDataSource ds = new SimpleDataSource();
      ds.setJndiName(name);
      ds.setUrl(mserver.getAttribute(oname, "url").toString()); //$NON-NLS-1$
      ds.setDriverClass(mserver.getAttribute(oname, "driverClassName").toString()); //$NON-NLS-1$
      ds.setUserName(mserver.getAttribute(oname, "username").toString()); //$NON-NLS-1$
      ds.setPassword(mserver.getAttribute(oname, "password").toString()); //$NON-NLS-1$
      ds.setActive(new Integer(mserver.getAttribute(oname, "maxActive").toString())); //$NON-NLS-1$
      ds.setIdle(new Integer(mserver.getAttribute(oname, "maxIdle").toString())); //$NON-NLS-1$
      ds.setWait(new Long(mserver.getAttribute(oname, "maxWait").toString())); //$NON-NLS-1$

      //get validation query
      Object valQuery = null;

      try {
        valQuery = mserver.getAttribute(oname, "validationQuery"); //$NON-NLS-1$
        ds.setValidationQuery(valQuery != null ? valQuery.toString() : EMPTY_STRING);
      } catch (AttributeNotFoundException e) {
        ds.setValidationQuery(EMPTY_STRING);
      }

      return ds;
    } catch (InstanceNotFoundException e) {
      return null;
    } catch (RuntimeException e) {

      throw e;
    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }
  }

  public void commitChanges() throws DataSourceManagementException {
    try {
      ObjectName sname = new ObjectName(DOMAIN_NAME + ":type=Server"); //$NON-NLS-1$

      mserver.invoke(sname, "storeConfig", null, null); //$NON-NLS-1$

    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }

  }
  
  public static void main(String[] args) throws Exception
  {
    IDataSourceManager dsa = new TomcatDSManager();
    dsa.addInitParameter(Constants.JMX_PORT, 9004);
    dsa.addInitParameter(Constants.JMX_HOST, "localhost"); //$NON-NLS-1$
    dsa.addInitParameter(Constants.JMX_USERNAME, "tomcat"); //$NON-NLS-1$
    dsa.addInitParameter(Constants.JMX_PASSWORD, "tomcat"); //$NON-NLS-1$
    dsa.init();
    SimpleDataSource ds = new SimpleDataSource();
    ds.setJndiName("jdbc/OracleDSTester"); //$NON-NLS-1$
    ds.setUrl("jdbc:oracle:thin:@localhost:1521:pentaho"); //$NON-NLS-1$
    ds.setDriverClass("oracle.jdbc.OracleDriver"); //$NON-NLS-1$
    ds.setIdle(2);
    ds.setWait(1000);
    ds.setActive(2);
    ds.setUserName("alex"); //$NON-NLS-1$
    ds.setPassword("pentaho"); //$NON-NLS-1$
    //TODO: Change this from Pentaho to something that's more test like!!!
    ds.setParameter(Constants.PATH, "/pentaho"); //$NON-NLS-1$
    dsa.addDataSource(ds);
    dsa.commitChanges();
  }
}
