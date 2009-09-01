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
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class NewUserDialogBox extends ConfirmDialog {

  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  boolean userCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  
  public NewUserDialogBox() {
    super();

    setTitle( MSGS.addUser() );
    
    userDetailsPanel.setStyleName( "newUserDialogBox.detailsPanel" ); //$NON-NLS-1$
    addWidgetToClientArea( userDetailsPanel );
    
    setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle( MessageDialog o ) {
        createUser();
      }
    });
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
      messageDialog.setMessage(MSGS.invalidUserName());
      messageDialog.center();
    } else if (!getPassword().equals(getPasswordConfirmation())) { 
      messageDialog.setMessage(MSGS.passwordConfirmationFailed());
      messageDialog.center();
    } else {
      final ProxyPentahoUser user = getUser();
      if (user != null) {
        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
          public void onSuccess(Object result) {
            okBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
            userCreated = true;
            
            // begin default roles
            ProxyPentahoRole[] defaultRoles = UserAndRoleMgmtService.instance().getDefaultRoles();
            UserAndRoleMgmtService.instance().setRoles(user, defaultRoles, new AsyncCallback<Object>() {

              public void onSuccess(Object result) {
                hide();
              }
              
              public void onFailure(Throwable caught) {
                MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), MSGS.errorAddingRolesForUser()), false, false, true);
                messageDialog.center();
              }
            });
            
            // end default roles
          
          }

          public void onFailure(Throwable caught) {
            MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), MSGS.errorCreatingUser()), false, false, true);
            messageDialog.center();
            okBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
          }
        };
        okBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        UserAndRoleMgmtService.instance().createUser(user, callback);
      }
    }
    
    return userCreated;
  }
}
