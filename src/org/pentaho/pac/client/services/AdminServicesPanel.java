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

import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.pac.client.PacServiceAsync;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.common.ui.GroupBox;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminServicesPanel extends VerticalPanel implements ClickListener {

  Button refreshSolutionRepositoryBtn = new Button(Messages.getString("refreshSolutionRepository")); //$NON-NLS-1$
  Button cleanRepositoryBtn = new Button(Messages.getString("removeStaleContent")); //$NON-NLS-1$
  Button clearMondrianSchemaCacheBtn = new Button(Messages.getString("purgeMondrianSchemaCache")); //$NON-NLS-1$
  Button scheduleRepositoryCleaningBtn = new Button(Messages.getString("scheduleDailyRepositoryCleaning")); //$NON-NLS-1$
  Button resetRepositoryBtn = new Button(Messages.getString("restoreDefaultFilePermissions")); //$NON-NLS-1$
  Button refreshSystemSettingsBtn = new Button(Messages.getString("refreshSystemSettings")); //$NON-NLS-1$
  Button executeGlobalActionsBtn = new Button(Messages.getString("executeGlobalActions")); //$NON-NLS-1$
  Button refreshReportingMetadataBtn = new Button(Messages.getString("refreshReportingMetadata")); //$NON-NLS-1$
  FlexTable flexTable = new FlexTable();
  
  HashMap<Integer, FlexTable> groupMap = new HashMap<Integer, FlexTable>();
  
  public AdminServicesPanel() {
    flexTable.setCellSpacing(10);
    add(flexTable);
    
    addGroupBox(0, 0, 0, Messages.getString("contentRepositoryCleaning")); //$NON-NLS-1$
    addGroupBox(1, 0, 1, Messages.getString("solutionRepository")); //$NON-NLS-1$
    addGroupBox(2, 1, 0, Messages.getString("refreshBiServer")); //$NON-NLS-1$
    
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
        MessageDialogBox messageDialog = new MessageDialogBox(Messages.getString("services"), result, false, false, true); //$NON-NLS-1$
        messageDialog.center();
        messageDialog.show();
        serviceButton.setEnabled(true);        
      }

      public void onFailure(Throwable caught) {
        MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), caught.getMessage()), false, false, true);   
        messageDialog.center();
        messageDialog.show();
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
