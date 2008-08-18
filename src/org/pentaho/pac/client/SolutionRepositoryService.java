package org.pentaho.pac.client;

import org.pentaho.pac.common.SolutionRepositoryServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.xml.client.Document;

public interface SolutionRepositoryService extends RemoteService {

  public String getSolutionRepositoryAsXml() throws SolutionRepositoryServiceException;
}
