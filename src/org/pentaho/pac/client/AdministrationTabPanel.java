package org.pentaho.pac.client;

import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.ctlr.SchedulerController;
import org.pentaho.pac.client.scheduler.view.SchedulerPanel;
import org.pentaho.pac.client.services.AdminServicesPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdministrationTabPanel extends TabPanel implements IRefreshableAdminPage{

  protected static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
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
    
    servicesPanel = createAdminServicesPanel();
    usersAndRolesPanel = createUsersAndRolesPanel();
    dataSourcesPanel = createDataSourcesPanel();
    
    // Order that things are placed in the tab panel is important. There are
    // static constants defined within this class that assume a given tab position
    // for each of the panels on the tab panel.
    add(usersAndRolesPanel, MSGS.usersAndRoles());
    add(dataSourcesPanel, MSGS.dataSources());
    add(servicesPanel, MSGS.services());
    SchedulerPanel schedulerPanel = new SchedulerPanel();
    Panel p = new VerticalPanel();
    p.setStyleName( "schedulerPanelPadderPanel" );    //$NON-NLS-1$
    p.add(schedulerPanel );
    
    SimplePanel schedulerScrollWrapper = new SimplePanel();
    schedulerScrollWrapper.setStyleName("configConsolePanel");  //$NON-NLS-1$
    schedulerScrollWrapper.setWidth("100%");    //$NON-NLS-1$
    schedulerScrollWrapper.setHeight("570px");  //$NON-NLS-1$

    schedulerScrollWrapper.add(p);
    
    add(schedulerScrollWrapper, MSGS.scheduler());
    schedulerController = new SchedulerController( schedulerPanel );
    
    usersAndRolesPanel.setWidth("100%"); //$NON-NLS-1$
    usersAndRolesPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourcesPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesPanel.setHeight("100%"); //$NON-NLS-1$
    servicesPanel.setWidth("100%"); //$NON-NLS-1$
    servicesPanel.setHeight("100%"); //$NON-NLS-1$
    
    getDeckPanel().setHeight("100%"); //$NON-NLS-1$
    selectTab(AdministrationTabPanel.ADMIN_USERS_ROLES_TAB_INDEX);
  }
  
  @Override
  public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
    super.onTabSelected(sender, tabIndex);
    switch (tabIndex) {
      case ADMIN_USERS_ROLES_TAB_INDEX:
        // do nothing;
         break;
      case ADMIN_DATA_SOURCES_TAB_INDEX:
        // do nothing;
        break;
      case ADMIN_SERVICES_TAB_INDEX:
        // do nothing;
        break;
      case ADMIN_SCHEDULER_TAB_INDEX:
        // do nothing;
        break;
      default:
        throw new RuntimeException(MSGS.invalidTabIndex(Integer.toString(tabIndex)));
    }   
  }
  
  public void refresh() {
    initializeSecurityInfo();
    dataSourcesPanel.refresh();
    schedulerController.init();
  }
  private void initializeSecurityInfo() {
    AsyncCallback<Object> callback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        usersAndRolesPanel.refresh();
        securityInfoInitialized = true;
      }
    
      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog( MSGS.error() );
        messageDialog.setMessage(MSGS.securityRefreshError(caught.getMessage()));
        messageDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().refreshSecurityInfo(callback);

  }

  protected AdminServicesPanel createAdminServicesPanel() {
    return new AdminServicesPanel();
  }
  
  protected UsersAndRolesPanel createUsersAndRolesPanel() {
    return new UsersAndRolesPanel();
  }
  
  protected DataSourcesPanel createDataSourcesPanel() {
    return new DataSourcesPanel();
  }
  
  public AdminServicesPanel getServicesPanel() {
    return servicesPanel;
  }

  public UsersAndRolesPanel getUsersAndRolesPanel() {
    return usersAndRolesPanel;
  }

  public DataSourcesPanel getDataSourcesPanel() {
    return dataSourcesPanel;
  }
  
}
