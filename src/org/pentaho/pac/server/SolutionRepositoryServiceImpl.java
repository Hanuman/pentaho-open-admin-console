package org.pentaho.pac.server;

import org.apache.commons.lang.StringUtils;
import org.pentaho.pac.client.SolutionRepositoryService;
import org.pentaho.pac.common.SolutionRepositoryServiceException;
import org.pentaho.pac.server.biplatformproxy.SolutionRepositoryServiceProxy;
import org.pentaho.pac.server.common.AppConfigProperties;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SolutionRepositoryServiceImpl extends RemoteServiceServlet implements SolutionRepositoryService {

  private static final long serialVersionUID = 420L;
  private static SolutionRepositoryServiceProxy solutionRepositoryProxy = null;
  static {
    String userName = StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getProperty("pentaho.platform.userName"), System.getProperty("pentaho.platform.userName") ); //$NON-NLS-1$ //$NON-NLS-2$
    String biServerBaseURL = AppConfigProperties.getInstance().getProperty( "biServerBaseURL" ); //$NON-NLS-1$
    solutionRepositoryProxy = new SolutionRepositoryServiceProxy( userName, biServerBaseURL );
  }

  public SolutionRepositoryServiceImpl() {
    
  }

  public String getSolutionRepositoryAsXml() throws SolutionRepositoryServiceException {
    return solutionRepositoryProxy.getSolutionRepositoryXml();
  }
}
