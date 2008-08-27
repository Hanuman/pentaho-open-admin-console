package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.dialog.AccumulatorDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
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
    
    setTitle(MSGS.assignRoles());

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
    UserAndRoleMgmtService.instance().setRoles(user, accumulatedRolesList.getObjects().toArray(new ProxyPentahoRole[0]), callback);
  }
  
  public boolean getRoleAssignmentsModified() {
    return roleAssignmentsModified;
  }
}
