/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
 *
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.server.common;

import java.util.HashMap;
import java.util.Map;

public class BiServerTrustedProxy extends ThreadSafeHttpClient {

  private static final String TRUSTED_USER_KEY = "_TRUST_USER_"; //$NON-NLS-1$
  
  private static BiServerTrustedProxy instance = new BiServerTrustedProxy();
  
  private BiServerTrustedProxy()
  {
    super();
  }
  
  public void createNewInstance() {
    instance = new BiServerTrustedProxy();
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
  public String execRemoteMethod(String baseUrl, String serviceName, HttpMethodType methodType, String userName, Map<String,Object> params )
    throws ProxyException  {
    
    if (params == null) {
      params = new HashMap<String, Object>();
    }
    params.put( TRUSTED_USER_KEY, userName );
    return super.execRemoteMethod(baseUrl, serviceName, methodType, params );
  }
}

