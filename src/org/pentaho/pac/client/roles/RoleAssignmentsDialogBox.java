package org.pentaho.pac.client.roles;

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

public class RoleAssignmentsDialogBox extends AccumulatorDialog<ProxyPentahoRole> {
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
        errorDialog.setMessage(ExceptionParser.getErrorMessage(caught.getMessage()));          
        errorDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().setRoles(user, accumulatedRolesList.getObjects().toArray(new ProxyPentahoRole[0]), callback);
  }
  
  public boolean getRoleAssignmentsModified() {
    return roleAssignmentsModified;
  }
}
