package org.pentaho.pac.client;

import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.services.AdminServicesPanel;
import org.pentaho.pac.client.users.UsersPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PentahoAdminConsole implements EntryPoint {
  ToggleButton adminToggleBtn = new ToggleButton("Administration");
  ToggleButton homeToggleBtn = new ToggleButton("Home");
  VerticalPanel leftVerticalPanel = new VerticalPanel();
  TabPanel rightTabPanel = new TabPanel();
  DockPanel dockPanel = new DockPanel();
  DockPanel principalsPanel = new DockPanel();
  AdminServicesPanel servicesPanel = new AdminServicesPanel();
  UsersPanel usersPanel = new UsersPanel();
  DataSourcesPanel dataSourcesPanel = new DataSourcesPanel();
  

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

     homeToggleBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (homeToggleBtn.isDown()) {
          Window.alert("I have been toggled down");
        } else {
          Window.alert("I have been toggled up");
        }
      }
    });
    
    adminToggleBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (adminToggleBtn.isDown()) {
          Window.alert("I have been toggled down");
        } else {
          Window.alert("I have been toggled up");
        }
      }
    });
    
    leftVerticalPanel.add(homeToggleBtn);
    leftVerticalPanel.add(adminToggleBtn);
    
    TabPanel tp = new TabPanel();
    tp.add(usersPanel, "Principals");
    tp.add(dataSourcesPanel, "Data Sources");
    tp.add(servicesPanel, "Services");
    
    dockPanel.add(leftVerticalPanel, DockPanel.WEST);
    dockPanel.add(tp, DockPanel.CENTER);
    
    dockPanel.setCellWidth(tp, "100%");
    dockPanel.setCellHeight(tp, "100%");
    
    dockPanel.setSpacing(10);
    
    tp.setWidth("100%");
    tp.setHeight("100%");
    
    dockPanel.setWidth("100%");
    dockPanel.setHeight("100%");
    tp.setWidth("100%");
    tp.setHeight("100%");
    
    usersPanel.setWidth("100%");
    usersPanel.setHeight("100%");
    dataSourcesPanel.setWidth("100%");
    dataSourcesPanel.setHeight("100%");
    servicesPanel.setWidth("100%");
    servicesPanel.setHeight("100%");
    
   tp.selectTab(1);

    RootPanel.get().add(dockPanel);    
    
    usersPanel.refresh();
    dataSourcesPanel.refresh();
  }
  
}
