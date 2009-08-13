/*
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
*/
package org.pentaho.pac.server.biplatformproxy;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.SolutionRepositoryServiceException;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.SolutionRepositoryXmlSerializer;
import org.pentaho.pac.server.common.AppConfigProperties;
import org.pentaho.pac.server.common.BiServerTrustedProxy;
import org.pentaho.pac.server.common.ProxyException;
import org.pentaho.pac.server.common.ThreadSafeHttpClient.HttpMethodType;
import org.pentaho.pac.server.i18n.Messages;

public class SolutionRepositoryServiceProxy {

  private static final String SOLUTION_REPOSITORY_SERVICE_NAME = "SolutionRepositoryService"; //$NON-NLS-1$
  
  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SolutionRepositoryServiceProxy() {
  }

  public String getSolutionRepositoryXml() throws SolutionRepositoryServiceException {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "component", "getSolutionRepositoryDoc" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "filter", "*.xaction,*.url" ); //$NON-NLS-1$  //$NON-NLS-2$
  
      String responseStrXml = executeGetMethod( params );
      
      SolutionRepositoryXmlSerializer s = new SolutionRepositoryXmlSerializer();
  
      s.detectSubscriptionRepositoryErrorInXml( responseStrXml ); // throws SolutionRepositoryServiceException
      return responseStrXml;
    } catch(SolutionRepositoryServiceException sse) {
      throw new SolutionRepositoryServiceException(Messages.getErrorString("SolutionRepositoryServiceProxy.ERROR_0001_UNABLE_RETRIEVE_SOLUTION_REPOSITORY", sse.getMessage()), sse); //$NON-NLS-1$
    }     
  }
  
  private String executeGetMethod( Map<String, Object> params ) throws SolutionRepositoryServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SOLUTION_REPOSITORY_SERVICE_NAME, HttpMethodType.GET, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SolutionRepositoryServiceException(ExceptionParser.getErrorMessage(e.getMessage(), e.getMessage()), e );
    }
//    SolutionRepositoryXmlSerializer s = new SolutionRepositoryXmlSerializer();

    return strXmlResponse;
  }
  
  private String executePostMethod(Map<String, Object> params ) throws SolutionRepositoryServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SOLUTION_REPOSITORY_SERVICE_NAME, HttpMethodType.POST, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SolutionRepositoryServiceException(ExceptionParser.getErrorMessage(e.getMessage(), e.getMessage()), e );
    }
//    SolutionRepositoryXmlSerializer s = new SolutionRepositoryXmlSerializer();

    return strXmlResponse;
  }
}
