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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.dialog.AccumulatorDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserAssignmentsDialogBox extends AccumulatorDialog<ProxyPentahoUser> {
  boolean userAssignmentsModified = false;
  ProxyPentahoRole role;
  UsersList availableUsersList = new UsersList(true);
  UsersList accumulatedUsersList = new UsersList(true);

  public UserAssignmentsDialogBox() {
    super(new UsersList(true), new UsersList(true));
    availableUsersList = (UsersList)getAvailableItemsListBox();
    accumulatedUsersList = (UsersList)getAccumulatedItemsListBox();
    
    setTitle(Messages.getString("assignUsers")); //$NON-NLS-1$

    setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle( MessageDialog o ) {
        assignSelectedUsers();
      }
    });
  }
  
  public UserAssignmentsDialogBox(ProxyPentahoRole role) {
    this(); 
    setRole(role);
  }
  
  public ProxyPentahoRole getRole() {
    return role;
  }

  public void setRole(ProxyPentahoRole role) {
    userAssignmentsModified = false;
    this.role = role;
    if (role != null) {
      ArrayList<ProxyPentahoUser> unassignedUsers = new ArrayList<ProxyPentahoUser>();
      unassignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers()));
      ArrayList<ProxyPentahoUser> assignedUsers = new ArrayList<ProxyPentahoUser>();
      assignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(role)));
      unassignedUsers.removeAll(assignedUsers);
      availableUsersList.setObjects(unassignedUsers);
      accumulatedUsersList.setObjects(assignedUsers);
    } else {
      List<ProxyPentahoUser> emptyList = Collections.emptyList();
      availableUsersList.setObjects(emptyList);
      accumulatedUsersList.setObjects(emptyList);
    }
  }
  
  public void show() {
    userAssignmentsModified = false;
    super.show();
  }
  
  private void assignSelectedUsers() {
    
    AsyncCallback<Object> callback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        userAssignmentsModified = true;
        hide();
      }

      public void onFailure(Throwable caught) {
        MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorAssigningSelectedUsers")), false, false, true); //$NON-NLS-1$
        messageDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().setUsers(role, accumulatedUsersList.getObjects().toArray(new ProxyPentahoUser[0]), callback);
  }
  
  public boolean getUserAssignmentsModified() {
    return userAssignmentsModified;
  }
}
