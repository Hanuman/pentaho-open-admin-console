package org.pentaho.pac.server.scheduler;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.pentaho.pac.client.scheduler.Job;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.BiServerAdminProxy;

public class SchedulerAdminUIComponentProxy {

  private static final String SCHEDULER_SERVICE_NAME = "SchedulerAdmin"; //$NON-NLS-1$
  private static final int NUM_RETRIES = 3;
  private static final String TRUSTED_USER_KEY = "_TRUST_USER_"; //$NON-NLS-1$
  private String baseUrl = null;
  private String userName = null;
  // TODO sbarkdull, is this thread safe enough?
  private static BiServerAdminProxy biServerProxy = new BiServerAdminProxy();
  
  public SchedulerAdminUIComponentProxy( String baseUrl, String userName ) {
    this.baseUrl = baseUrl;
    this.userName = userName;
  }

  // TODO sbarkdull, probably need to throw a different exception
  // move to utils class
  // TODO, need to have a single instance of HTTP client (as opposed to
  // creating a new one on each method call, to maintain an ongoing session w/ the remote server
  // to understand threading issues:
  // http://hc.apache.org/httpclient-3.x/performance.html
  // http://hc.apache.org/httpclient-3.x/threading.html
  /**
   * Configuration note: In order to use this method to contact the pentaho bi server,
   * the bi server must be configured to trust the host (ie IP address) that this 
   * method is executing on. To do that:
   * Edit the bi server application's (ie the PCI) web.xml file and add this
   * filter definition:
   *   <filter>
   *       <filter-name>Proxy Trusting Filter</filter-name>
   *       <filter-class>org.pentaho.ui.servlet.ProxyTrustingFilter</filter-class>
   *       <init-param>
   *         <param-name>TrustedIpAddrs</param-name>
   *         <param-value>127.0.0.1</param-value>
   *         <description>Comma separated list of IP addresses of a trusted hosts.</description>
   *       </init-param>
   *     </filter>
   *  The param-value for the param named TrustedIpAddrs is a comma separater list of
   *  IP addresses/hostnames that the bi server will trust. The hostname or IP address
   *  of the machine running this method must be in the TrustedIpAddrs list.
   *  
   *  In addition, you must have a filter mapping that maps the above filter name
   *  (Proxy Trusting Filter) to a url that this method will be accessing on
   *  the bi server.
   *  
   *    <filter-mapping>
   *      <filter-name>Proxy Trusting Filter</filter-name>
   *      <url-pattern>/SchedulerAdmin</url-pattern>
   *    </filter-mapping>
   * 
   * @param baseUrl base url of server, for instance: http://localhost:8080/pentaho
   * @param serviceName name of service on server, for instance: SchedulerAdmin
   * @param params params to pass with request
   */
  private String executeRemoteMethod(String baseUrl, String serviceName, NameValuePair[] params)
      throws PacServiceException {

    GetMethod method = null;
    
    try {
      HttpClient client = new HttpClient();

      // Create a method instance.
      method = new GetMethod(baseUrl + "/" + serviceName); //$NON-NLS-1$

      method.setQueryString(params);
      method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(NUM_RETRIES, false));

      if (client.executeMethod(method) != HttpStatus.SC_OK) {
        String status = method.getStatusLine().toString();
        throw new PacServiceException(status);
      }
      return IOUtils.toString( method.getResponseBodyAsStream() );
    } catch (IOException e) {
      throw new PacServiceException(e);
    } finally {
      method.releaseConnection();
    }
  }

  private String getBaseUrl()
  {
    return baseUrl;
  }
  /**
   * API description for SchedulerAdmin (see actual class for up to date docs):
   * Base URL is: http://<servername>:<portnum>/<contextname>/SchedulerAdmin
   *  or
   *  http://localhost:8080/pentaho/SchedulerAdmin
   *  
   */

  /**
   * query string: schedulerAction=deleteJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void deleteJob( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "deleteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJobNow( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "executeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=getJobNames
   * @throws PacServiceException 
   */
  public List<Job> getJobNames() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "getJobNames" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml = biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );

    // TODO sbarkdull, should XmlSerializer be a static class?
    XmlSerializer s = new XmlSerializer();
    List<Job> l = s.getJobNamesFromXml( responseStrXml );
    return l;
  }
  
  /**
   * query string: schedulerAction=isSchedulerPaused
   * @throws PacServiceException 
   */
  public boolean isSchedulerPaused() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "isSchedulerPaused" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
    XmlSerializer s = new XmlSerializer();
    boolean isRunning = s.getSchedulerStatusFromXml( responseStrXml );
    
    return isRunning;
  }

  /**
   * query string: schedulerAction=suspendScheduler
   * @throws PacServiceException 
   */
  public void pauseAll() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "suspendScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "pauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=resumeScheduler
   * @throws PacServiceException 
   */
  public void resumeAll() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "resumeScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "resumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName );

    String responseStrXml=  biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

}
