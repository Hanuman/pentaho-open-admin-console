package org.pentaho.pac.server.common;

import java.util.Map;
import java.util.Properties;

import org.pentaho.pac.common.PacServiceException;

public class BiServerTrustedProxy extends ThreadSafeHttpClient {

  private static final String TRUSTED_USER_KEY = "_TRUST_USER_"; //$NON-NLS-1$
  //private String userName = p.getProperty( "pentaho.platform.username" );
  private static final String biServerBaseUrl;
  static {
    Properties p = AppConfigProperties.getProperties();
    biServerBaseUrl = p.getProperty( "biServerBaseURL" ); //$NON-NLS-1$
  }
  
  private static BiServerTrustedProxy instance = new BiServerTrustedProxy();
  
  private BiServerTrustedProxy()
  {
    super( biServerBaseUrl );
  }
  
  public static BiServerTrustedProxy getInstance() {
    return instance;
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
   *  Currently the PAC console uses SchedulerAdmin, Publish, and ResetRepository
   * 
   * @param baseUrl base url of server, for instance: http://localhost:8080/pentaho
   * @param serviceName name of service on server, for instance: SchedulerAdmin
   * @param params params to pass with request
   */
  public String execRemoteMethod( String serviceName, String userName, Map params )
    throws PacServiceException {
    
    params.put( TRUSTED_USER_KEY, userName );
    return super.execRemoteMethod( serviceName, params );
  }
}

