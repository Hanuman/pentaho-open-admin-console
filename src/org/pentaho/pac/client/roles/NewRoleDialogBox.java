package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.DuplicateUserException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewRoleDialogBox extends DialogBox implements ClickListener {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  Button okButton = new Button(MSGS.ok());
  Button cancelButton = new Button(MSGS.cancel());
  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  boolean roleCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  
  public NewRoleDialogBox() {
    super();
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(roleDetailsPanel);
    verticalPanel.add(footerPanel);
    
    setText(MSGS.addRole());
    
    verticalPanel.setWidth("250px"); //$NON-NLS-1$
    roleDetailsPanel.setWidth("100%"); //$NON-NLS-1$
    
    setWidget(verticalPanel);
    okButton.addClickListener(this);
    cancelButton.addClickListener(this);
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

  public Button getOkButton() {
    return okButton;
  }

  public Button getCancelButton() {
    return cancelButton;
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
  
  public void setText(String text) {
    super.setText(text);
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
  
  public void onClick(Widget sender) {
    if (sender == okButton) {
      createRole();
    } else if (sender == cancelButton) {
      hide();
    }
  }
}
