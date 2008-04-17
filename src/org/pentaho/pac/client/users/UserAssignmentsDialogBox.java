package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.AccumulatorDialog;
import org.pentaho.pac.client.common.ui.ICallbackHandler;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.roles.RolesList;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserAssignmentsDialogBox extends AccumulatorDialog {
  boolean userAssignmentsModified = false;
  ProxyPentahoRole role;
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  UsersList availableUsersList = new UsersList(true);
  UsersList accumulatedUsersList = new UsersList(true);

  public UserAssignmentsDialogBox() {
    super(new UsersList(true), new UsersList(true));
    availableUsersList = (UsersList)getAvailableItemsListBox();
    accumulatedUsersList = (UsersList)getAccumulatedItemsListBox();
    
    setTitle(MSGS.assignedUsers());

    setOnOkHandler( new ICallbackHandler() {
      public void onHandle( Object o ) {
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
      ArrayList unassignedUsers = new ArrayList();
      unassignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers()));
      ProxyPentahoUser[] assignedUsers = UserAndRoleMgmtService.instance().getUsers(role);
      unassignedUsers.removeAll(Arrays.asList(assignedUsers));
      availableUsersList.setUsers((ProxyPentahoUser[])unassignedUsers.toArray(new ProxyPentahoUser[0]));
      accumulatedUsersList.setUsers(assignedUsers);
    } else {
      availableUsersList.setUsers(new ProxyPentahoUser[0]);
      accumulatedUsersList.setUsers(new ProxyPentahoUser[0]);
    }
  }
  
  public void show() {
    userAssignmentsModified = false;
    super.show();
  }
  
  private void assignSelectedUsers() {
    
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        userAssignmentsModified = true;
        hide();
      }

      public void onFailure(Throwable caught) {
        if (caught instanceof PentahoSecurityException) {
          errorDialog.setMessage(MSGS.insufficientPrivileges());
        } else if (caught instanceof NonExistingRoleException) {
          errorDialog.setMessage(MSGS.roleDoesNotExist(caught.getMessage()));
        } else if (caught instanceof NonExistingUserException) {
          errorDialog.setMessage(MSGS.userDoesNotExist(caught.getMessage()));
        } else {
          errorDialog.setMessage(caught.getMessage());
        }
        errorDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().setUsers(role, accumulatedUsersList.getUsers(), callback);
  }
  
  public boolean getUserAssignmentsModified() {
    return userAssignmentsModified;
  }
}
