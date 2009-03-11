/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 *
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.client.services;

import java.util.HashMap;

import org.pentaho.pac.client.PacServiceAsync;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.GroupBox;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.utils.ExceptionParser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminServicesPanel extends VerticalPanel implements ClickListener {

  protected static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  Button refreshSolutionRepositoryBtn = new Button(MSGS.refreshSolutionRepository());
  Button cleanRepositoryBtn = new Button(MSGS.removeStaleContent());
  Button clearMondrianSchemaCacheBtn = new Button(MSGS.purgeMondrianSchemaCache());
  Button scheduleRepositoryCleaningBtn = new Button(MSGS.scheduleDailyRepositoryCleaning());
  Button resetRepositoryBtn = new Button(MSGS.restoreDefaultFilePermissions());
  Button refreshSystemSettingsBtn = new Button(MSGS.refreshSystemSettings());
  Button executeGlobalActionsBtn = new Button(MSGS.executeGlobalActions());
  Button refreshReportingMetadataBtn = new Button(MSGS.refreshReportingMetadata());
  FlexTable flexTable = new FlexTable();
  
  HashMap<Integer, FlexTable> groupMap = new HashMap<Integer, FlexTable>();
  
  public AdminServicesPanel() {
    flexTable.setCellSpacing(10);
    add(flexTable);
    
    addGroupBox(0, 0, 0, MSGS.contentRepositoryCleaning());
    addGroupBox(1, 0, 1, MSGS.solutionRepository());
    addGroupBox(2, 1, 0, MSGS.refreshBiServer());
    
    flexTable.getColumnFormatter().setWidth(0, "50%"); //$NON-NLS-1$
    flexTable.getColumnFormatter().setWidth(1, "50%");//$NON-NLS-1$
    
    addServiceButton(0, 0, 1, cleanRepositoryBtn);
    addServiceButton(0, 0, 0, scheduleRepositoryCleaningBtn);
    
    addServiceButton(1, 0, 0, refreshSolutionRepositoryBtn);
    addServiceButton(1, 0, 1, resetRepositoryBtn);
    
    addServiceButton(2, 0, 0, refreshSystemSettingsBtn);
    addServiceButton(2, 0, 1, executeGlobalActionsBtn);
    addServiceButton(2, 1, 0, refreshReportingMetadataBtn);
    addServiceButton(2, 1, 1, clearMondrianSchemaCacheBtn);
    
  }

  protected void addGroupBox(int groupId, int row, int column, String title) {
    FlexTable groupFlexTable = new FlexTable();
    groupFlexTable.setWidth("100%"); //$NON-NLS-1$   
    groupFlexTable.setCellSpacing(5);
    
    GroupBox groupBox = new GroupBox(title);
    groupBox.setContent(groupFlexTable);
    groupBox.setWidth("100%"); //$NON-NLS-1$   
    groupBox.setHeight("100%"); //$NON-NLS-1$
    
    flexTable.getCellFormatter().setHeight(row, column, "100%"); //$NON-NLS-1$
    flexTable.setWidget(row, column, groupBox);
    
    groupBox.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    groupMap.put(new Integer(groupId), groupFlexTable);
  }
  
  protected void addServiceButton(int parentGroupId, int rowWithinGroup, int columnWithinGroup, Button serviceButton) {
    FlexTable groupFlexTable = groupMap.get(new Integer(parentGroupId));
    if (groupFlexTable != null) {
      groupFlexTable.setWidget(rowWithinGroup, columnWithinGroup, serviceButton);
      serviceButton.setWidth("100%"); //$NON-NLS-1$
      serviceButton.addClickListener(this);
    }
  }
  
  protected void runService(final Button serviceButton) {
    AsyncCallback<String> callback = new AsyncCallback<String>() {
      
      public void onSuccess(String result) {
        MessageDialog messageDialog = new MessageDialog(MSGS.services(), 
            result);
        messageDialog.center();
        serviceButton.setEnabled(true);
      }

      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
        messageDialog.setMessage(ExceptionParser.getErrorMessage(caught.getMessage(), caught.getMessage()));   
        serviceButton.setEnabled(true);
      }
    }; // end AsyncCallback

    PacServiceAsync pacServiceAsync = PacServiceFactory.getPacService();
    
    serviceButton.setEnabled(false);
    if (serviceButton == refreshSolutionRepositoryBtn) {
      pacServiceAsync.refreshSolutionRepository(callback);
    } else if (serviceButton == cleanRepositoryBtn) {
      pacServiceAsync.cleanRepository(callback);
    } else if (serviceButton == clearMondrianSchemaCacheBtn) {
      pacServiceAsync.clearMondrianSchemaCache(callback);
    } else if (serviceButton == scheduleRepositoryCleaningBtn) {
      pacServiceAsync.scheduleRepositoryCleaning(callback);
    } else if (serviceButton == resetRepositoryBtn) {
      pacServiceAsync.resetRepository(callback);
    } else if (serviceButton == refreshSystemSettingsBtn) {
      pacServiceAsync.refreshSystemSettings(callback);
    } else if (serviceButton == executeGlobalActionsBtn) {
      pacServiceAsync.executeGlobalActions(callback);
    } else if (serviceButton == refreshReportingMetadataBtn) {
      pacServiceAsync.refreshReportingMetadata(callback);
    }
  }
  
  public void onClick(final Widget sender) {
    runService((Button)sender);
  }
  
}
