package org.pentaho.pac.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class PentahoAdminConsoleEntryPoint implements EntryPoint{

  public void onModuleLoad() {

    //attach all to the page
    RootPanel.get("canvas").add(new PentahoAdminConsole());  //$NON-NLS-1$
  }
}
