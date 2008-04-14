package org.pentaho.pac.client.users;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewUserDialogBox extends DialogBox implements ClickListener {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  Button okButton = new Button(MSGS.ok());
  Button cancelButton = new Button(MSGS.cancel());
  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  boolean userCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  
  public NewUserDialogBox() {
    super();
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(userDetailsPanel);
    verticalPanel.add(footerPanel);
    
    setText(MSGS.addUser());
    setWidget(verticalPanel);
    okButton.addClickListener(this);
    cancelButton.addClickListener(this);
  }

  public String getDescription() {
    return userDetailsPanel.getDescription();
  }

  public TextBox getDescriptionTextBox() {
    return userDetailsPanel.getDescriptionTextBox();
  }

  public String getPassword() {
    return userDetailsPanel.getPassword();
  }

  public String getPasswordConfirmation() {
    return userDetailsPanel.getPasswordConfirmation();
  }

  public PasswordTextBox getPasswordConfirmationTextBox() {
    return userDetailsPanel.getPasswordConfirmationTextBox();
  }

  public PasswordTextBox getPasswordTextBox() {
    return userDetailsPanel.getPasswordTextBox();
  }

  public String getUserName() {
    return userDetailsPanel.getUserName();
  }

  public TextBox getUserNameTextBox() {
    return userDetailsPanel.getUserNameTextBox();
  }

  public Button getOkButton() {
    return okButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public boolean isUserCreated() {
    return userCreated;
  }


  public ProxyPentahoUser getUser() {
    return userDetailsPanel.getUser();
  }

  public void setUser(ProxyPentahoUser user) {
    userDetailsPanel.setUser(user);
  }

  public void show() {
    userCreated = false;
    super.show();
  }
  
  public void setText(String text) {
    super.setText(text);
  }
  
  private boolean createUser() {
    if (getUserName().trim().length() == 0) {
      messageDialog.setMessage(MSGS.invalidUserName());
      messageDialog.center();
    } else if (!getPassword().equals(getPasswordConfirmation())) { 
      messageDialog.setMessage(MSGS.passwordConfirmationFailed());
      messageDialog.center();
    } else {
      ProxyPentahoUser user = getUser();
      if (user != null) {
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            userCreated = true;
            hide();
          }

          public void onFailure(Throwable caught) {
            if (caught instanceof PentahoSecurityException) {
              messageDialog.setMessage(MSGS.insufficientPrivileges());
            } else if (caught instanceof DuplicateUserException) {
              messageDialog.setMessage(MSGS.userAlreadyExist());
            } else {
              messageDialog.setMessage(caught.getMessage());
            }
            messageDialog.center();
          }
        };
        UserAndRoleMgmtService.instance().createUser(user, callback);
      }
    }
    
    return userCreated;
  }
  
  public void onClick(Widget sender) {
    if (sender == okButton) {
      createUser();
    } else if (sender == cancelButton) {
      hide();
    }
  }
}
