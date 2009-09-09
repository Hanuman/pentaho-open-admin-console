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
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.home.HomePanel;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PentahoAdminConsole extends DockPanel implements IRefreshableAdminConsole {
  
  public static final String DEFAULT_HOMEPAGE_URL = "http://www.pentaho.com/console_home"; //$NON-NLS-1$  
  protected AdminConsoleToolbar toolbar = new AdminConsoleToolbar(this);
  protected AdminConsoleMasterDetailsPanel adminConsoleMasterDetailsPanel = new AdminConsoleMasterDetailsPanel();
  
  public enum AdminConsolePageId {
    HOME_PAGE, ADMIN_PAGE
  };
  
  public PentahoAdminConsole() {
    super();
    
    HorizontalPanel topPanel = buildTopPanel() ;
    topPanel.setWidth("100%");    //$NON-NLS-1$
    add(topPanel, DockPanel.NORTH);
    setCellWidth(topPanel, "100%"); //$NON-NLS-1$    
    
    add(adminConsoleMasterDetailsPanel, DockPanel.CENTER);
    adminConsoleMasterDetailsPanel.setWidth("100%");//$NON-NLS-1$
    adminConsoleMasterDetailsPanel.setHeight("100%");   //$NON-NLS-1$
    setCellWidth(adminConsoleMasterDetailsPanel, "100%"); //$NON-NLS-1$
    setCellHeight(adminConsoleMasterDetailsPanel, "100%"); //$NON-NLS-1$    
    
    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean >() {
        public void onSuccess(Boolean valid) {
        	if(!valid) {
	            MessageDialogBox errorDialog = new MessageDialogBox(Messages.getString("invalidConfiguration"), Messages.getString("notValidConfiguration"), true, false, true); //$NON-NLS-1$ //$NON-NLS-2$
	            errorDialog.center();
	            setVisible(false);
        	} else {
        	    AsyncCallback<String> homepageUrlcallback = new AsyncCallback<String>() {
        	        public void onSuccess(String result) {
        	          adminConsoleMasterDetailsPanel.addPage(AdminConsolePageId.HOME_PAGE.ordinal(), Messages.getString("home"), new HomePanel(result)); //$NON-NLS-1$
        	          adminConsoleMasterDetailsPanel.addPage(AdminConsolePageId.ADMIN_PAGE.ordinal(), Messages.getString("administration"), new AdministrationTabPanel());    //$NON-NLS-1$
        	          adminConsoleMasterDetailsPanel.selectPage(AdminConsolePageId.HOME_PAGE.ordinal());
        	          refresh();
        	        }
        	        public void onFailure(Throwable caught) {
        	          adminConsoleMasterDetailsPanel.addPage(AdminConsolePageId.HOME_PAGE.ordinal(), Messages.getString("home"), new HomePanel(DEFAULT_HOMEPAGE_URL)); //$NON-NLS-1$
        	          adminConsoleMasterDetailsPanel.addPage(AdminConsolePageId.ADMIN_PAGE.ordinal(), Messages.getString("administration"), new AdministrationTabPanel());    //$NON-NLS-1$
        	          adminConsoleMasterDetailsPanel.selectPage(AdminConsolePageId.HOME_PAGE.ordinal());
        	          refresh();
        	        }
        	      };
        	      PacServiceFactory.getPacService().getHomepageUrl(homepageUrlcallback);  
        	      setStyleName("main-panel"); //$NON-NLS-1$
        	}
          
        }
        public void onFailure(Throwable caught) {
          MessageDialogBox errorDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorInitializingPacService")), false, false, true); //$NON-NLS-1$
          errorDialog.center();
          setVisible(false);
        }
  };
  PacServiceFactory.getPacService().isValidConfiguration(callback);
  }
  
  protected HorizontalPanel buildTopPanel() {
    HorizontalPanel topPanel = new HorizontalPanel();
    SimplePanel logo = new SimplePanel();
    logo.setStyleName("logo"); //$NON-NLS-1$
    topPanel.setWidth("100%");  //$NON-NLS-1$
    topPanel.add(logo);
    topPanel.add(toolbar);
    topPanel.setCellWidth(toolbar, "100%"); //$NON-NLS-1$
    return topPanel;
  }

  public void refresh() {
    for (Widget page : adminConsoleMasterDetailsPanel.getPages()) {
      if (page instanceof IRefreshableAdminPage) {
        ((IRefreshableAdminPage)page).refresh();
      }
    }
  }
  
}
 