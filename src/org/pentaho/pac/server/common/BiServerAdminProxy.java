package org.pentaho.pac.server.common;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.common.PacServiceException;

public class BiServerAdminProxy {
  private static final Log logger = LogFactory.getLog(BiServerAdminProxy.class);
  
  /*
   * see: http://hc.apache.org/httpclient-3.x/threading.html
   */
  private static final HttpClient CLIENT;
  static {
    MultiThreadedHttpConnectionManager connectionManager = 
      new MultiThreadedHttpConnectionManager();
    CLIENT = new HttpClient( connectionManager );
  }
  private String proxyUrl = null;
  
  /**
   * Base Constructor
   */
  public BiServerAdminProxy( String proxyUrl ) {
    super();
    this.proxyUrl = proxyUrl;
  }

  // TODO sbarkdull, better name?
  public String proxyRemoteMethod( String serviceName, NameValuePair[] params)
      throws PacServiceException {

    GetMethod method = null;
    try {
      // Create a method instance.
      method = new GetMethod( proxyUrl + "/" + serviceName ); //$NON-NLS-1$

      method.addRequestHeader( "Content-Type", "text/xml" ); //$NON-NLS-1$ //$NON-NLS-2$
      method.setQueryString(params);
      if (CLIENT.executeMethod(method) != HttpStatus.SC_OK) {
        String status = method.getStatusLine().toString();
        throw new PacServiceException(status);
      }
      // TODO sbarkdull, check for bad returns
      // trim() is necessary because SchedulerAdmin.jsp puts \n\r at the beginning of
      // the returned text, and the xml processor chokes on \n\r at the beginning.
      return IOUtils.toString(method.getResponseBodyAsStream()).trim();
    } catch (IOException e) {
      throw new PacServiceException(e);
    } finally {
      method.releaseConnection();
    }
  }
  

}
