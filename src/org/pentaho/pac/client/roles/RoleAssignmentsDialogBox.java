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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.dialog.AccumulatorDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleAssignmentsDialogBox extends AccumulatorDialog<ProxyPentahoRole> {
  boolean roleAssignmentsModified = false;
  ProxyPentahoUser user;
  MessageDialog errorDialog = new MessageDialog( Messages.getString("error") ); //$NON-NLS-1$
  RolesList availableRolesList;
  RolesList accumulatedRolesList;

  public RoleAssignmentsDialogBox() {
    super(new RolesList(true), new RolesList(true));
    availableRolesList = (RolesList)getAvailableItemsListBox();
    accumulatedRolesList = (RolesList)getAccumulatedItemsListBox();
    
    setTitle(Messages.getString("assignRoles")); //$NON-NLS-1$

    setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle( MessageDialog o ) {
        assignSelectedRoles();
      }
    });
  }
  
  public RoleAssignmentsDialogBox(ProxyPentahoUser user) {
    this(); 
    setUser(user);
  }
  
  public ProxyPentahoUser getUser() {
    return user;
  }

  public void setUser(ProxyPentahoUser user) {
    roleAssignmentsModified = false;
    this.user = user;
    if (user != null) {
      ArrayList<ProxyPentahoRole> unassignedRoles = new ArrayList<ProxyPentahoRole>();
      unassignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles()));
      List<ProxyPentahoRole> assignedRoles = Arrays.asList(UserAndRoleMgmtService.instance().getRoles(user));
      unassignedRoles.removeAll(assignedRoles);
      availableRolesList.setObjects(unassignedRoles);
      accumulatedRolesList.setObjects(assignedRoles);
    } else {
      List<ProxyPentahoRole> emptyList = Collections.emptyList();
      availableRolesList.setObjects(emptyList);
      accumulatedRolesList.setObjects(emptyList);
    }
  }
  
  public void show() {
    roleAssignmentsModified = false;
    super.show();
  }
  
  private void assignSelectedRoles() {
    
    AsyncCallback<Object> callback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        roleAssignmentsModified = true;
        hide();
      }

      public void onFailure(Throwable caught) {
        errorDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
        errorDialog.setMessage(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorAssigningSelectedRoles")));           //$NON-NLS-1$
        errorDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().setRoles(user, accumulatedRolesList.getObjects().toArray(new ProxyPentahoRole[0]), callback);
  }
  
  public boolean getRoleAssignmentsModified() {
    return roleAssignmentsModified;
  }
}
