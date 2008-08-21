package org.pentaho.pac.client;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommonTasksPanel extends SimplePanel {

  VerticalPanel hyperlinksPanel = new VerticalPanel();
  
  public CommonTasksPanel() {
    VerticalPanel vertPanel = new VerticalPanel();
    
    SimplePanel headerPanel = new SimplePanel();
    headerPanel.setStyleName("CommonTasksHeader"); //$NON-NLS-1$
    
    Label header = new Label("Common Tasks");
    header.setStyleName("commonTasksHeaderText"); //$NON-NLS-1$
    headerPanel.add(header);
    vertPanel.add(headerPanel);
    
    hyperlinksPanel.setStyleName("CommonTasksLinks"); //$NON-NLS-1$
    vertPanel.add(hyperlinksPanel);
    
    setStyleName("CommonTasks"); //$NON-NLS-1$
    add(vertPanel);
  }
  
  public void addQuickLink(Hyperlink hyperlink) {
    hyperlinksPanel.add(hyperlink);
  }
}
