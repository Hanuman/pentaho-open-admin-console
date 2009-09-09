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
package org.pentaho.pac.client.users;

import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class NewUserDialogBox extends PromptDialogBox {

  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  boolean userCreated = false;
  HTML dialogMsg = new HTML();
  PromptDialogBox messageDialog = new PromptDialogBox(Messages.getString("error"), Messages.getString("ok"), null, false, true, dialogMsg);  //$NON-NLS-1$//$NON-NLS-2$
  
  public NewUserDialogBox() {
    super(Messages.getString("addUser"), Messages.getString("ok"), Messages.getString("cancel"), false, true);   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    setContent(userDetailsPanel);
    
    setTitle( Messages.getString("addUser") ); //$NON-NLS-1$
    
    userDetailsPanel.setStyleName( "newUserDialogBox.detailsPanel" ); //$NON-NLS-1$    
  }
  
  public String getDescription() {
    return userDetailsPanel.getDescription();
  }

  public TextBox getDescriptionTextBox() {
    return userDetailsPanel.getDescriptionTextBox();
  }

  public String getPassword() {
    return userDetailsPanel.getPassword();
  }

  public String getPasswordConfirmation() {
    return userDetailsPanel.getPasswordConfirmation();
  }

  public PasswordTextBox getPasswordConfirmationTextBox() {
    return userDetailsPanel.getPasswordConfirmationTextBox();
  }

  public PasswordTextBox getPasswordTextBox() {
    return userDetailsPanel.getPasswordTextBox();
  }

  public String getUserName() {
    return userDetailsPanel.getUserName();
  }

  public TextBox getUserNameTextBox() {
    return userDetailsPanel.getUserNameTextBox();
  }

  public boolean isUserCreated() {
    return userCreated;
  }


  public ProxyPentahoUser getUser() {
    return userDetailsPanel.getUser();
  }

  public void setUser(ProxyPentahoUser user) {
    userDetailsPanel.setUser(user);
  }

  public void show() {
    userCreated = false;
    super.show();
  }
  
  private boolean createUser() {
    if (getUserName().trim().length() == 0) {
      dialogMsg.setHTML(Messages.getString("invalidUserName")); //$NON-NLS-1$
      messageDialog.center();
    } else if (!getPassword().equals(getPasswordConfirmation())) { 
      dialogMsg.setHTML(Messages.getString("passwordConfirmationFailed")); //$NON-NLS-1$
      messageDialog.center();
    } else {
      final ProxyPentahoUser user = getUser();
      if (user != null) {
        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
          public void onSuccess(Object result) {
            okButton.setEnabled(true);
            cancelButton.setEnabled(true);
            userCreated = true;
            
            // begin default roles
            ProxyPentahoRole[] defaultRoles = UserAndRoleMgmtService.instance().getDefaultRoles();
            UserAndRoleMgmtService.instance().setRoles(user, defaultRoles, new AsyncCallback<Object>() {

              public void onSuccess(Object result) {
                hide();
                if (getCallback() != null) {
                  getCallback().okPressed();
                }
              }
              
              public void onFailure(Throwable caught) {
                MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorAddingRolesForUser")), false, false, true); //$NON-NLS-1$
                messageDialog.center();
                if (getCallback() != null) {
                  getCallback().okPressed();
                }
              }
            });
            
            // end default roles
          
          }

          public void onFailure(Throwable caught) {
            MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorCreatingUser")), false, false, true); //$NON-NLS-1$
            messageDialog.center();
            okButton.setEnabled(true);
            cancelButton.setEnabled(true);
            if (getCallback() != null) {
              getCallback().okPressed();
            }
          }
        };
        okButton.setEnabled(false);
        cancelButton.setEnabled(false);
        UserAndRoleMgmtService.instance().createUser(user, callback);
      }
    }
    
    return userCreated;
  }

  protected void onOk() {
    createUser();
  }
}
