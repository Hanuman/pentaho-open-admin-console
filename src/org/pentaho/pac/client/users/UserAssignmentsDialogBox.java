package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.dialog.AccumulatorDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserAssignmentsDialogBox extends AccumulatorDialog<ProxyPentahoUser> {
  boolean userAssignmentsModified = false;
  ProxyPentahoRole role;
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  UsersList availableUsersList = new UsersList(true);
  UsersList accumulatedUsersList = new UsersList(true);

  public UserAssignmentsDialogBox() {
    super(new UsersList(true), new UsersList(true));
    availableUsersList = (UsersList)getAvailableItemsListBox();
    accumulatedUsersList = (UsersList)getAccumulatedItemsListBox();
    
    setTitle(MSGS.assignUsers());

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
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
        messageDialog.setMessage(ExceptionParser.getErrorMessage(caught.getMessage(), MSGS.errorAssigningSelectedUsers()));    
      }
    };
    UserAndRoleMgmtService.instance().setUsers(role, accumulatedUsersList.getObjects().toArray(new ProxyPentahoUser[0]), callback);
  }
  
  public boolean getUserAssignmentsModified() {
    return userAssignmentsModified;
  }
}
