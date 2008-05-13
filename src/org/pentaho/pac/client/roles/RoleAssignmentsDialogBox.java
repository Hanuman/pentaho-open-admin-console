package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.AccumulatorDialog;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleAssignmentsDialogBox extends AccumulatorDialog {
  boolean roleAssignmentsModified = false;
  ProxyPentahoUser user;
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  RolesList availableRolesList;
  RolesList accumulatedRolesList;

  public RoleAssignmentsDialogBox() {
    super(new RolesList(true), new RolesList(true));
    availableRolesList = (RolesList)getAvailableItemsListBox();
    accumulatedRolesList = (RolesList)getAccumulatedItemsListBox();
    
    setTitle(MSGS.assignedRoles());

    setOnOkHandler( new ICallback() {
      public void onHandle( Object o ) {
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
      ArrayList unassignedRoles = new ArrayList();
      unassignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles()));
      ProxyPentahoRole[] assignedRoles = UserAndRoleMgmtService.instance().getRoles(user);
      unassignedRoles.removeAll(Arrays.asList(assignedRoles));
      availableRolesList.setRoles((ProxyPentahoRole[])unassignedRoles.toArray(new ProxyPentahoRole[0]));
      accumulatedRolesList.setRoles(assignedRoles);
    } else {
      availableRolesList.setRoles(new ProxyPentahoRole[0]);
      accumulatedRolesList.setRoles(new ProxyPentahoRole[0]);
    }
  }
  
  public void show() {
    roleAssignmentsModified = false;
    super.show();
  }
  
  private void assignSelectedRoles() {
    
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        roleAssignmentsModified = true;
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
    UserAndRoleMgmtService.instance().setRoles(user, accumulatedRolesList.getRoles(), callback);
  }
  
  public boolean getRoleAssignmentsModified() {
    return roleAssignmentsModified;
  }
}
