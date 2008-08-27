package org.pentaho.pac.server;

import org.pentaho.pac.client.SolutionRepositoryService;
import org.pentaho.pac.common.SolutionRepositoryServiceException;
import org.pentaho.pac.server.biplatformproxy.SolutionRepositoryServiceProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SolutionRepositoryServiceImpl extends RemoteServiceServlet implements SolutionRepositoryService {

  private static final long serialVersionUID = 420L;
  private static SolutionRepositoryServiceProxy solutionRepositoryProxy = null;
  static {
    solutionRepositoryProxy = new SolutionRepositoryServiceProxy();
  }

  public SolutionRepositoryServiceImpl() {
    
  }

  public String getSolutionRepositoryAsXml() throws SolutionRepositoryServiceException {
    return solutionRepositoryProxy.getSolutionRepositoryXml();
  }
}
