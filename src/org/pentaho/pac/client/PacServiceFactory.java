package org.pentaho.pac.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class PacServiceFactory {

  private static PacServiceAsync pacService = null;

  public static PacServiceAsync getPacService() {
    if (pacService == null) {
      pacService = (PacServiceAsync) GWT.create(PacService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) pacService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "pacsvc";
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return pacService;
  }
}
