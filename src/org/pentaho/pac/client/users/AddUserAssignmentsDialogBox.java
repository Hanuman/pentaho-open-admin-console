package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.ICallbackHandler;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddUserAssignmentsDialogBox extends ConfirmDialog  {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  UsersList usersList = new UsersList(true);
  boolean usersAssigned = false;
  ProxyPentahoRole role;
  
  public AddUserAssignmentsDialogBox() {
    super();
    
    setText(MSGS.addUser());
    
    final AddUserAssignmentsDialogBox localThis = this;
    usersList.setVisibleItemCount( 9 );
    usersList.setStyleName( "addUserAssignmentsDialogBox.usersList" ); //$NON-NLS-1$
    addWidgetToClientArea( usersList );
    
    setOnOkHandler( new ICallbackHandler() {
      public void onHandle( Object o ) {
        localThis.assignSelectedUsers();
      }
    });
  }
  
  public AddUserAssignmentsDialogBox(ProxyPentahoRole role) {
    this(); 
    setRole(role);
  }


  public ProxyPentahoRole getRole() {
    return role;
  }


  public void setRole(ProxyPentahoRole role) {
    usersAssigned = false;
    this.role = role;
    if (role != null) {
      ArrayList unassignedUsers = new ArrayList();
      unassignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers()));
      unassignedUsers.removeAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(role)));
      usersList.setUsers((ProxyPentahoUser[])unassignedUsers.toArray(new ProxyPentahoUser[0]));
    } else {
      usersList.setUsers(new ProxyPentahoUser[0]);
    }
  }
  
  public UsersList getUsersList() {
    return usersList;
  }


  public ProxyPentahoUser[] getSelectedUsers() {
    return usersList.getSelectedUsers();
  }


//  public void show() {
//    usersAssigned = false;
//    super.show();
//  }
  
  private void assignSelectedUsers() {
    ProxyPentahoUser[] selectedUsers = getSelectedUsers();
    if (selectedUsers.length > 0) {
      ArrayList assignedUsers = new ArrayList();
      assignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(role)));
      assignedUsers.addAll(Arrays.asList(selectedUsers));
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          usersAssigned = true;
          hide();
        }

        public void onFailure(Throwable caught) {
          MessageDialog errorDialog = new MessageDialog(MSGS.error() );
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
      UserAndRoleMgmtService.instance().setUsers(role, (ProxyPentahoUser[])assignedUsers.toArray(new ProxyPentahoUser[0]), callback);
    }
  }

  public boolean getUsersAssigned() {
    return usersAssigned;
  }
}
