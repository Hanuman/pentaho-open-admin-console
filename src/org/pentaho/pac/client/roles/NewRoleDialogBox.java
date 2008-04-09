package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;
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
  
  Button okButton = new Button("OK");
  Button cancelButton = new Button("Cancel");
  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  boolean roleCreated = false;
  MessageDialog messageDialog = new MessageDialog("", new int[]{MessageDialog.OK_BTN});
  
  public NewRoleDialogBox() {
    super();
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(roleDetailsPanel);
    verticalPanel.add(footerPanel);
    
    setText("Add Role");
    
    verticalPanel.setWidth("250px");
    roleDetailsPanel.setWidth("100%");
    
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
    messageDialog.setText(text);
    super.setText(text);
  }
  
  private boolean createRole() {
    if (getRoleName().trim().length() == 0) {
      messageDialog.setMessage("Invalid role name.");
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
              messageDialog.setMessage("Insufficient privileges.");
            } else if (caught instanceof DuplicateUserException) {
              messageDialog.setMessage("Role already exists.");
            } else {
              messageDialog.setMessage(caught.getMessage());
            }
            messageDialog.center();
          }
        };
        PacServiceFactory.getPacService().createRole(role, callback);
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
