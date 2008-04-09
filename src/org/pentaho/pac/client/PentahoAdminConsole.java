package org.pentaho.pac.client;

import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.home.HomePanel;
import org.pentaho.pac.client.scheduler.SchedulerPanel;
import org.pentaho.pac.client.services.AdminServicesPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PentahoAdminConsole implements EntryPoint, ClickListener, TabListener {
  ToggleButton adminToggleBtn = new ToggleButton("Administration");
  ToggleButton homeToggleBtn = new ToggleButton("Home");
  ToggleButton testToggleBtn = new ToggleButton("Test");
  
  VerticalPanel leftVerticalPanel = new VerticalPanel();
  TabPanel rightTabPanel = new TabPanel();
  DockPanel dockPanel = new DockPanel();
  DeckPanel deckPanel = new DeckPanel();
  
  AdminServicesPanel servicesPanel = new AdminServicesPanel();
  UsersAndRolesPanel usersAndRolesPanel = new UsersAndRolesPanel();
  //HomePanel homePanel = new HomePanel();
  DataSourcesPanel dataSourcesPanel = new DataSourcesPanel();
  SchedulerPanel schedulerPanel = new SchedulerPanel();
  TabPanel adminTabPanel = new TabPanel();
  
  boolean securityInfoInitialized = false;
  MessageDialog messageDialog = new MessageDialog("Security", "", new int[]{MessageDialog.OK_BTN});
  
  // TODO can this be a "real" Java 5 enum?
  public static final int ADMIN_USERS_ROLES_TAB_INDEX = 0;
  public static final int ADMIN_DATA_SOURCES_TAB_INDEX = 1;
  public static final int ADMIN_SERVICES_TAB_INDEX = 2;
  public static final int ADMIN_SCHEDULER_TAB_INDEX = 3;
  

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
    
    // Order that things are placed in the tab panel is important. There are
    // static constants defined within this class that assume a given tab position
    // for each of the panels on the tab panel.
    adminTabPanel.add(usersAndRolesPanel, "Users & Roles");
    adminTabPanel.add(dataSourcesPanel, "Data Sources");
    adminTabPanel.add(servicesPanel, "Services");
    adminTabPanel.add(schedulerPanel, "Scheduler");
    
    usersAndRolesPanel.setBorderWidth(2);    
    HomePanel homePanel = new HomePanel("http://www.pentaho.com/console_home");
    deckPanel.add(homePanel);
    deckPanel.add(adminTabPanel);

    
    dockPanel.add(leftVerticalPanel, DockPanel.WEST);
    dockPanel.add(deckPanel, DockPanel.CENTER);
    
    
    dockPanel.setCellWidth(deckPanel, "100%");
    dockPanel.setCellHeight(deckPanel, "100%");
    
    dockPanel.setSpacing(10);
    
    dockPanel.setWidth("100%");
    dockPanel.setHeight("100%");
    adminTabPanel.setWidth("100%");
    adminTabPanel.setHeight("100%");
    
    usersAndRolesPanel.setWidth("100%");
    usersAndRolesPanel.setHeight("100%");
    dataSourcesPanel.setWidth("100%");
    dataSourcesPanel.setHeight("100%");
    servicesPanel.setWidth("100%");
    servicesPanel.setHeight("100%");
    
    schedulerPanel.setWidth("100%");
    schedulerPanel.setHeight("100%");
    
    deckPanel.setWidth("100%");
    deckPanel.setHeight("100%");
    adminTabPanel.selectTab(ADMIN_USERS_ROLES_TAB_INDEX);

    RootPanel.get().add(dockPanel);    
    deckPanel.showWidget(0);
    homeToggleBtn.setDown(true);
    
    adminTabPanel.addTabListener(this);
  }


public void onClick(Widget sender) {
    if (sender == homeToggleBtn) {
      if (homeToggleBtn.isDown()) {
        adminToggleBtn.setDown(false);
        testToggleBtn.setDown(false);
        deckPanel.showWidget(0);
      } else {
        homeToggleBtn.setDown(true);
      }
    } else if (sender == adminToggleBtn) {
      if (adminToggleBtn.isDown()) {
        homeToggleBtn.setDown(false);
        testToggleBtn.setDown(false);
        deckPanel.showWidget(1);
        int selectedTab = adminTabPanel.getDeckPanel().getVisibleWidget();
        switch (selectedTab) {
          case ADMIN_USERS_ROLES_TAB_INDEX:
            if (!securityInfoInitialized) {
              initializeSecurityInfo();
            }
            break;
          case ADMIN_DATA_SOURCES_TAB_INDEX: 
            if (!dataSourcesPanel.isInitialized()) {
              dataSourcesPanel.refresh();
            }
            break;
        }   
      } else {
        adminToggleBtn.setDown(true);
      }
    } else if (sender == testToggleBtn) {
      homeToggleBtn.setDown(false);
      adminToggleBtn.setDown(false);
    }
  }

  private void initializeSecurityInfo() {
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        usersAndRolesPanel.refresh();
        securityInfoInitialized = true;
      }
    
      public void onFailure(Throwable caught) {
        messageDialog.setMessage("Unable to refresh security information: " + caught.getMessage());
        messageDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().refreshSecurityInfo(callback);

  }

  public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
    return true;
  }
  
  
  public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
    switch (tabIndex) {
      case ADMIN_USERS_ROLES_TAB_INDEX:
        if (!securityInfoInitialized) {
          initializeSecurityInfo();
        }
        break;
      case ADMIN_DATA_SOURCES_TAB_INDEX: 
        if (!dataSourcesPanel.isInitialized()) {
          dataSourcesPanel.refresh();
        }
        break;
      case ADMIN_SCHEDULER_TAB_INDEX: 
        if (!schedulerPanel.isInitialized()) {
          schedulerPanel.refresh();
        }
        break;
      default:
        throw new RuntimeException( "Invalid tabIndex: " + tabIndex );
    }   
  }
  
}
 