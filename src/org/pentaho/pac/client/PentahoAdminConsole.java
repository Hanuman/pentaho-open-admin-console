package org.pentaho.pac.client;

import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.home.HomePanel;
import org.pentaho.pac.client.services.AdminServicesPanel;
import org.pentaho.pac.client.users.UsersPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PentahoAdminConsole implements EntryPoint, ClickListener {
  ToggleButton adminToggleBtn = new ToggleButton("Administration");
  ToggleButton homeToggleBtn = new ToggleButton("Home");
  ToggleButton testToggleBtn = new ToggleButton("Test");
  VerticalPanel leftVerticalPanel = new VerticalPanel();
  TabPanel rightTabPanel = new TabPanel();
  DockPanel dockPanel = new DockPanel();
  DeckPanel deckPanel = new DeckPanel();
  DockPanel principalsPanel = new DockPanel();
  AdminServicesPanel servicesPanel = new AdminServicesPanel();
  UsersAndRolesPanel usersAndRolesPanel = new UsersAndRolesPanel();
  //HomePanel homePanel = new HomePanel();
  DataSourcesPanel dataSourcesPanel = new DataSourcesPanel();
  

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    homeToggleBtn.addClickListener(this);
    
    adminToggleBtn.addClickListener(this);

    testToggleBtn.addClickListener(this);
    
    leftVerticalPanel.add(homeToggleBtn);
    leftVerticalPanel.add(adminToggleBtn);
    leftVerticalPanel.add(testToggleBtn);
    

    TabPanel tp = new TabPanel();
    tp.add(usersAndRolesPanel, "Principals");
    tp.add(dataSourcesPanel, "Data Sources");
    tp.add(servicesPanel, "Services");
    usersAndRolesPanel.setBorderWidth(2);    
    HomePanel homePanel = new HomePanel("http://www.pentaho.com/console_home");
    deckPanel.add(homePanel);
    deckPanel.add(tp);

    
    dockPanel.add(leftVerticalPanel, DockPanel.WEST);
    dockPanel.add(deckPanel, DockPanel.CENTER);
    
    
    dockPanel.setCellWidth(deckPanel, "100%");
    dockPanel.setCellHeight(deckPanel, "100%");
    
    dockPanel.setSpacing(10);
    
    tp.setWidth("50%");
    tp.setHeight("50%");
    
    dockPanel.setWidth("100%");
    dockPanel.setHeight("100%");
    tp.setWidth("50%");
    tp.setHeight("50%");
    
    usersAndRolesPanel.setWidth("100%");
    usersAndRolesPanel.setHeight("100%");
    dataSourcesPanel.setWidth("100%");
    dataSourcesPanel.setHeight("100%");
    servicesPanel.setWidth("100%");
    servicesPanel.setHeight("100%");
    deckPanel.setWidth("100%");
    deckPanel.setHeight("100%");
    tp.selectTab(1);

    RootPanel.get().add(dockPanel);    
    deckPanel.showWidget(0);
  }


public void onClick(Widget sender) {
	if (sender == homeToggleBtn) {
        if (homeToggleBtn.isDown()) {
            Window.alert("I have been toggled down");
            adminToggleBtn.setDown(false);
            testToggleBtn.setDown(false);
            deckPanel.showWidget(0);
          } else {
            Window.alert("I have been toggled up");
          }
	} else if (sender == adminToggleBtn) {
        if (adminToggleBtn.isDown()) {
            Window.alert("I have been toggled down");
            homeToggleBtn.setDown(false);
            testToggleBtn.setDown(false);
            deckPanel.showWidget(1);            
          } else if(sender == testToggleBtn){
            Window.alert("I have been toggled down");
            homeToggleBtn.setDown(false);
            adminToggleBtn.setDown(false);
          } else {
              Window.alert("I have been toggled up");        	  
          }
	}
	
}
  
}
 