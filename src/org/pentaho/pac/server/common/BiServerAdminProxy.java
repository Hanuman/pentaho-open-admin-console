package org.pentaho.pac.server.common;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.messages.Messages;
import org.pentaho.pac.common.PacServiceException;

public class BiServerAdminProxy {
  private static final Log logger = LogFactory.getLog(BiServerAdminProxy.class);

  private static final int NUM_RETRIES = 3;
  
  // TODO sbarkdull, need to be init params, not hardcoded
  String proxyURL = "http://localhost:8080/pentaho"; // "http://localhost:8080/pentaho";
  String errorURL = null; // The URL to redirect to if the user is invalid
  // TODO
  String userName = "joe";

  /**
   * Base Constructor
   */
  public BiServerAdminProxy() {
    super();
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    // TODO sbarkdull
    proxyURL = "http://localhost:8080/pentaho";
    if ((proxyURL == null)) {
      logger.error(Messages.getString("ProxyServlet.ERROR_0001_NO_PROXY_URL_SPECIFIED")); //$NON-NLS-1$
    } else {
      // TODO sbarkdull, is trim necessary?
      proxyURL.trim();
      try {
        URL url = new URL(proxyURL); // Just doing this to verify
        // it's good
        logger.info(Messages.getString("ProxyServlet.INFO_0001_URL_SELECTED", url.toExternalForm())); // using 'url' to get rid of unused var compiler warning //$NON-NLS-1$
      } catch (Throwable t) {
        logger.error(Messages.getErrorString("ProxyServlet.ERROR_0002_INVALID_URL", proxyURL)); //$NON-NLS-1$
        proxyURL = null;
      }
    }

    errorURL = "http://www.yahoo.com"; // TODO sbarkdull
  }

  // TODO sbarkdull, better name?
  public String proxyRemoteMethod( String serviceName, NameValuePair[] params)
      throws PacServiceException {

    GetMethod method = null;

    try {
      HttpClient client = new HttpClient();

      // Create a method instance.
      method = new GetMethod(proxyURL + "/" + serviceName); //$NON-NLS-1$

      method.addRequestHeader( "Content-Type", "text/xml" ); //$NON-NLS-1$ //$NON-NLS-2$
      method.setQueryString(params);
// TODO sbarkdull, clean up
//      method.setQueryString( method.getQueryString()
//          + "&_TRUST_USER_=" + "joe" );
      
      //method.addParameter("_TRUST_USER_", userName );
      
//      method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//          new DefaultHttpMethodRetryHandler(NUM_RETRIES, false));

      
      if (client.executeMethod(method) != HttpStatus.SC_OK) {
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
