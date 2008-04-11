package org.pentaho.pac.server.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.common.PacServiceException;


public class ThreadSafeHttpClient {
    private static final Log logger = LogFactory.getLog(ThreadSafeHttpClient.class);
    
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
    public ThreadSafeHttpClient( String proxyUrl ) {
      super();
      this.proxyUrl = proxyUrl;
    }

    public String execRemoteMethod( String serviceName, Map mapParams )
        throws PacServiceException {
      return execRemoteMethod( serviceName, mapParams, "text/xml" ); //$NON-NLS-1$
    }

    public String execRemoteMethod( String serviceName, Map mapParams, String contentType )
        throws PacServiceException {

      GetMethod method = null;
      NameValuePair[] params = mapToNameValuePair( mapParams );
      try {
        String serviceUrl = proxyUrl 
          + ( ( StringUtils.isEmpty( serviceName ) ) ? "" : "/" + serviceName ); //$NON-NLS-1$ //$NON-NLS-2$
        method = new GetMethod( serviceUrl );

        method.addRequestHeader( "Content-Type", contentType ); //$NON-NLS-1$
        
// TODO sbarkdull, do retries? if so, put number of retries in config file.
        //method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        
        if ( null != params ) {
          method.setQueryString(params);
        }
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
    
    private static NameValuePair[] mapToNameValuePair( Map paramMap )
    {
      NameValuePair[] pairAr = null; 
      if ( null != paramMap ) {
        pairAr = new NameValuePair[ paramMap.size() ];
        
        Iterator it = paramMap.keySet().iterator();
        
        for ( int ii=0; ii<pairAr.length; ++ii ) {
          String key = (String)it.next();
          String val = (String)paramMap.get( key );
          pairAr[ii] = new NameValuePair( key, val );
        }
      }
      return pairAr;
    }
  }