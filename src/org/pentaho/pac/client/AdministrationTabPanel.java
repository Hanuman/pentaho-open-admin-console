/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.client;

import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.i18n.Messages;
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
    add(usersAndRolesPanel, Messages.getString("usersAndRoles")); //$NON-NLS-1$
    add(dataSourcesPanel, Messages.getString("dataSources")); //$NON-NLS-1$
    add(servicesPanel, Messages.getString("services")); //$NON-NLS-1$
    SchedulerPanel schedulerPanel = new SchedulerPanel();
    Panel p = new VerticalPanel();
    p.setStyleName( "schedulerPanelPadderPanel" );    //$NON-NLS-1$
    p.add(schedulerPanel );
    
    SimplePanel schedulerScrollWrapper = new SimplePanel();
    schedulerScrollWrapper.setStyleName("configConsolePanel");  //$NON-NLS-1$
    schedulerScrollWrapper.setWidth("100%");    //$NON-NLS-1$
    schedulerScrollWrapper.setHeight("570px");  //$NON-NLS-1$

    schedulerScrollWrapper.add(p);
    
    add(schedulerScrollWrapper, Messages.getString("scheduler")); //$NON-NLS-1$
    schedulerController = new SchedulerController( schedulerPanel );
    
    usersAndRolesPanel.setWidth("100%"); //$NON-NLS-1$
    usersAndRolesPanel.setHeight("565px"); //$NON-NLS-1$
    dataSourcesPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesPanel.setHeight("565px"); //$NON-NLS-1$
    servicesPanel.setWidth("100%"); //$NON-NLS-1$
    servicesPanel.setHeight("565px"); //$NON-NLS-1$
    
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
        throw new RuntimeException(Messages.getString("invalidTabIndex", Integer.toString(tabIndex))); //$NON-NLS-1$
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
        MessageDialogBox messageDialog = new MessageDialogBox( Messages.getString("error"), Messages.getString("securityRefreshError", caught.getMessage()), false, false, true); //$NON-NLS-1$ //$NON-NLS-2$
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
