package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.DuplicateUserException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

public class NewRoleDialogBox extends ConfirmDialog {

  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  boolean roleCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  
  public NewRoleDialogBox() {
    super();
    
    setTitle(MSGS.addRole());
    
    roleDetailsPanel.setStyleName( "newRoleDialogBox.detailsPanel" ); //$NON-NLS-1$
    addWidgetToClientArea( roleDetailsPanel );
    
    setOnOkHandler( new ICallback() {
      public void onHandle( Object o ) {
        createRole();
      }
    });
  }

  public String getDescription() {
    return roleDetailsPanel.getDescription();
  }

  public TextBox getDescriptionTextBox() {
    return roleDetailsPanel.getDescriptionTextBox();
  }

  public String getRoleName() {
    return roleDetailsPanel.getRoleName();
  }

  public TextBox getUserNameTextBox() {
    return roleDetailsPanel.getRoleNameTextBox();
  }

  public boolean isRoleCreated() {
    return roleCreated;
  }

  public ProxyPentahoRole getRole() {
    return roleDetailsPanel.getRole();
  }

  public void setUser(ProxyPentahoRole role) {
    roleDetailsPanel.setRole(role);
  }

  public void show() {
    roleCreated = false;
    super.show();
  }
  
  private boolean createRole() {
    if (getRoleName().trim().length() == 0) {
      messageDialog.setMessage(MSGS.invalidRoleName());
      messageDialog.center();
    } else {
      ProxyPentahoRole role = getRole();
      if (role != null) {
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            roleCreated = true;
            hide();
          }

          public void onFailure(Throwable caught) {
            if (caught instanceof PentahoSecurityException) {
              messageDialog.setMessage(MSGS.insufficientPrivileges());
            } else if (caught instanceof DuplicateUserException) {
              messageDialog.setMessage(MSGS.roleAlreadyExists());
            } else {
              messageDialog.setMessage(caught.getMessage());
            }
            messageDialog.center();
          }
        };
        UserAndRoleMgmtService.instance().createRole(role, callback);
      }
    }
    return roleCreated;
  }
}
