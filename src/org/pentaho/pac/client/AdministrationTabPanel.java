package org.pentaho.pac.client;

import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.scheduler.SchedulerController;
import org.pentaho.pac.client.scheduler.SchedulerPanel;
import org.pentaho.pac.client.services.AdminServicesPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabPanel;

public class AdministrationTabPanel extends TabPanel {

  public static final int ADMIN_USERS_ROLES_TAB_INDEX = 0;
  public static final int ADMIN_DATA_SOURCES_TAB_INDEX = 1;
  public static final int ADMIN_SERVICES_TAB_INDEX = 2;
  public static final int ADMIN_SCHEDULER_TAB_INDEX = 3;
  
  AdminServicesPanel servicesPanel = new AdminServicesPanel();
  UsersAndRolesPanel usersAndRolesPanel = new UsersAndRolesPanel();
  DataSourcesPanel dataSourcesPanel = new DataSourcesPanel();
  SchedulerController schedulerController = null;
  
  boolean securityInfoInitialized = false;  
  
  public AdministrationTabPanel() {
    super();
    
    // Order that things are placed in the tab panel is important. There are
    // static constants defined within this class that assume a given tab position
    // for each of the panels on the tab panel.
    add(usersAndRolesPanel, PentahoAdminConsole.MSGS.usersAndRoles());
    add(dataSourcesPanel, PentahoAdminConsole.MSGS.dataSources());
    add(servicesPanel, PentahoAdminConsole.MSGS.services());
    SchedulerPanel schedulerPanel = new SchedulerPanel();
    add(schedulerPanel, PentahoAdminConsole.MSGS.scheduler());
    schedulerController = new SchedulerController( schedulerPanel );
    
    usersAndRolesPanel.setWidth("100%"); //$NON-NLS-1$
    usersAndRolesPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourcesPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesPanel.setHeight("100%"); //$NON-NLS-1$
    servicesPanel.setWidth("100%"); //$NON-NLS-1$
    servicesPanel.setHeight("100%"); //$NON-NLS-1$
  }
  
  public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
    super.onTabSelected(sender, tabIndex);
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
      case ADMIN_SERVICES_TAB_INDEX:
        // do nothing;
        break;
      case ADMIN_SCHEDULER_TAB_INDEX:
        schedulerController.init();
        break;
      default:
        throw new RuntimeException(PentahoAdminConsole.MSGS.invalidTabIndex(Integer.toString(tabIndex)));
    }   
  }
  
  private void initializeSecurityInfo() {
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        usersAndRolesPanel.refresh();
        securityInfoInitialized = true;
      }
    
      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog( PentahoAdminConsole.MSGS.error() );
        messageDialog.setMessage(PentahoAdminConsole.MSGS.securityRefreshError(caught.getMessage()));
        messageDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().refreshSecurityInfo(callback);

  }
}
