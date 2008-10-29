package org.pentaho.pac.server.biplatformproxy;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
      throw new SolutionRepositoryServiceException( e.getMessage(), e );
    }
//    SolutionRepositoryXmlSerializer s = new SolutionRepositoryXmlSerializer();

    return strXmlResponse;
  }
  
  private String executePostMethod(Map<String, Object> params ) throws SolutionRepositoryServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SOLUTION_REPOSITORY_SERVICE_NAME, HttpMethodType.POST, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SolutionRepositoryServiceException( e.getMessage(), e );
    }
//    SolutionRepositoryXmlSerializer s = new SolutionRepositoryXmlSerializer();

    return strXmlResponse;
  }
}
