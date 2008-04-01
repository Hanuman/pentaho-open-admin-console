package org.pentaho.pac.server.datasources;

import java.util.Collection;
import java.util.Properties;

import javax.management.MBeanServerConnection;
import javax.naming.InitialContext;

import org.pentaho.pac.client.datasources.IDataSource;

/**
 * Add the following options to catalina.bat -Dcom.sun.management.jmxremote
 * -Dcom.sun.management.jmxremote.port="9004"
 * -Dcom.sun.management.jmxremote.authenticate="false"
 * -Dcom.sun.management.jmxremote.ssl="false"
 * 
 * @author Alex Silva
 * 
 */
public class JBossDSManager extends BaseDSManager {

  public void init() throws DataSourceManagementException {
    Properties props = new Properties();
    props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory"); //$NON-NLS-1$ //$NON-NLS-2$
    props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces"); //$NON-NLS-1$ //$NON-NLS-2$
    props.put("java.naming.provider.url", "jnp://localhost:1099"); //$NON-NLS-1$ //$NON-NLS-2$
    props.put("java.naming.factory.url.pkgs", "org.jboss.naming.client"); //$NON-NLS-1$ //$NON-NLS-2$

    try {
      InitialContext ctx = new InitialContext(props);
      mserver = (MBeanServerConnection) ctx.lookup("jmx/invoker/RMIAdaptor"); //$NON-NLS-1$
    } catch (Exception e) {
      throw new DataSourceManagementException(e);
    }

  }

  public void addDataSource(IDataSource ds) throws DataSourceManagementException {

  }

  public void deleteDataSource(IDataSource ds) throws DataSourceManagementException {

  }

  public void destroy() throws DataSourceManagementException {

  }

  public void updateDataSource(IDataSource ds) throws DataSourceManagementException {

  }

  public void commitChanges() throws DataSourceManagementException {

  }

  public void deleteDataSource(String name) throws DataSourceManagementException {

  }

  public Collection<String> getDataSources() throws DataSourceManagementException {
    return null;
  }

  public IDataSource getDataSource(String name) throws DataSourceManagementException {
    return null;
  }
}
