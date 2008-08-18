package org.pentaho.pac.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SolutionRepositoryServiceAsync {

  public void getSolutionRepositoryAsXml( AsyncCallback<String> callback );
}
