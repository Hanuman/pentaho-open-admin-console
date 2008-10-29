package org.pentaho.pac.client;

import org.pentaho.pac.common.SolutionRepositoryServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SolutionRepositoryService extends RemoteService {

  public String getSolutionRepositoryAsXml() throws SolutionRepositoryServiceException;
}
