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

  private static final String SCHEDULER_SERVICE_NAME = "SchedulerAdmin";
  private static final int NUM_RETRIES = 3;
  private static final String TRUSTED_USER_KEY = "_TRUST_USER_";
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
  /**
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
    params[0] = new NameValuePair("schedulerAction", "deleteJob" ); //$NON-NLS-1$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJobNow( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "executeJob" ); //$NON-NLS-1$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=getJobNames
   * @throws PacServiceException 
   */
  public List<Job> getJobNames() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "getJobNames" ); //$NON-NLS-1$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

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
  public void isSchedulerPaused() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "isSchedulerPaused" ); //$NON-NLS-1$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=pauseAll
   * @throws PacServiceException 
   */
  public void pauseAll() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "pauseAll" ); //$NON-NLS-1$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "pauseJob" ); //$NON-NLS-1$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=resumeAll
   * @throws PacServiceException 
   */
  public void resumeAll() throws PacServiceException {
    NameValuePair[] params = new NameValuePair[2];
    params[0] = new NameValuePair("schedulerAction", "resumeAll" ); //$NON-NLS-1$
    params[1] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException {
    NameValuePair[] params = new NameValuePair[4];
    params[0] = new NameValuePair("schedulerAction", "resumeJob" ); //$NON-NLS-1$
    params[1] = new NameValuePair("jobName", jobName); //$NON-NLS-1$
    params[2] = new NameValuePair("jobGroup", jobGroup); //$NON-NLS-1$
    params[3] = new NameValuePair( TRUSTED_USER_KEY, userName ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.proxyRemoteMethod( SCHEDULER_SERVICE_NAME, params );
  }

}
