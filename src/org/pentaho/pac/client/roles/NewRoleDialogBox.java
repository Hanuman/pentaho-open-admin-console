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
package org.pentaho.pac.client.roles;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

public class NewRoleDialogBox extends ConfirmDialog {

  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  boolean roleCreated = false;
  MessageDialog messageDialog = new MessageDialog( Messages.getString("error") ); //$NON-NLS-1$
  
  public NewRoleDialogBox() {
    super();
    
    setTitle(Messages.getString("addRole")); //$NON-NLS-1$
    
    roleDetailsPanel.setStyleName( "newRoleDialogBox.detailsPanel" ); //$NON-NLS-1$
    addWidgetToClientArea( roleDetailsPanel );
    
    setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle( MessageDialog o ) {
        createRole();
      }
    });
  }

  public String getDescription() {
    return roleDetailsPanel.getDescription();
  }

  public TextBox getDescriptionTextBox() {
    return roleDetailsPanel.getDescriptionTextBox();
  }

  public String getRoleName() {
    return roleDetailsPanel.getRoleName();
  }

  public TextBox getUserNameTextBox() {
    return roleDetailsPanel.getRoleNameTextBox();
  }

  public boolean isRoleCreated() {
    return roleCreated;
  }

  public ProxyPentahoRole getRole() {
    return roleDetailsPanel.getRole();
  }

  public void setUser(ProxyPentahoRole role) {
    roleDetailsPanel.setRole(role);
  }

  public void show() {
    roleCreated = false;
    super.show();
  }
  
  private boolean createRole() {
    if (getRoleName().trim().length() == 0) {
      messageDialog.setMessage(Messages.getString("invalidRoleName")); //$NON-NLS-1$
      messageDialog.center();
    } else {
      ProxyPentahoRole role = getRole();
      if (role != null) {
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
          public void onSuccess(Boolean result) {
            okBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
            roleCreated = true;
            hide();
          }

          public void onFailure(Throwable caught) {
            messageDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
            messageDialog.setMessage(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorCreatingRole")));           //$NON-NLS-1$
            messageDialog.center();
            okBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
          }
        };
        okBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        UserAndRoleMgmtService.instance().createRole(role, callback);
      }
    }
    return roleCreated;
  }
}
