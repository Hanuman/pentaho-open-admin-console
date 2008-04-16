package org.pentaho.pac.client.roles;

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
import com.google.gwt.user.client.ui.Button;

public class AddRoleAssignmentsDialogBox extends ConfirmDialog  {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  Button okButton = new Button(MSGS.ok());
  Button cancelButton = new Button(MSGS.cancel());
  RolesList rolesList = new RolesList(true);
  boolean rolesAssigned = false;
  ProxyPentahoUser user;
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  
  public AddRoleAssignmentsDialogBox() {
    super();
    
    setTitle(MSGS.addRole());

    final AddRoleAssignmentsDialogBox localThis = this;
    rolesList.setVisibleItemCount( 9 );
    rolesList.setStyleName( "addRoleAssignmentsDialogBox.rolesList" ); //$NON-NLS-1$
    addWidgetToClientArea( rolesList );
    
    setOnOkHandler( new ICallbackHandler() {
      public void onHandle( Object o ) {
        localThis.assignSelectedRoles();
      }
    });
  }
  
  public AddRoleAssignmentsDialogBox(ProxyPentahoUser user) {
    this(); 
    setUser(user);
  }
  
  public ProxyPentahoUser getUser() {
    return user;
  }

  public void setUser(ProxyPentahoUser user) {
    rolesAssigned = false;
    this.user = user;
    if (user != null) {
      ArrayList unassignedRoles = new ArrayList();
      unassignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles()));
      unassignedRoles.removeAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles(user)));
      rolesList.setRoles((ProxyPentahoRole[])unassignedRoles.toArray(new ProxyPentahoRole[0]));
    } else {
      rolesList.setRoles(new ProxyPentahoRole[0]);
    }
  }


  public Button getOkButton() {
    return okButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  
  public RolesList getRolesList() {
    return rolesList;
  }


  public ProxyPentahoRole[] getSelectedRoles() {
    return rolesList.getSelectedRoles();
  }


//  public void show() {
//    rolesAssigned = false;
//    super.show();
//  }
  
  private void assignSelectedRoles() {
    ProxyPentahoRole[] selectedRoles = getSelectedRoles();
    if (selectedRoles.length > 0) {
      ArrayList assignedRoles = new ArrayList();
      assignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles(user)));
      assignedRoles.addAll(Arrays.asList(selectedRoles));
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          rolesAssigned = true;
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
      UserAndRoleMgmtService.instance().setRoles(user, (ProxyPentahoRole[])assignedRoles.toArray(new ProxyPentahoRole[0]), callback);
    }
  }

  public boolean getRolesAssigned() {
    return rolesAssigned;
  }
}
