package org.pentaho.pac.client.users;

import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class NewUserDialogBox extends ConfirmDialog {

  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  boolean userCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  
  public NewUserDialogBox() {
    super();

    setTitle( MSGS.addUser() );
    
    userDetailsPanel.setStyleName( "newUserDialogBox.detailsPanel" ); //$NON-NLS-1$
    addWidgetToClientArea( userDetailsPanel );
    
    setOnOkHandler( new ICallback() {
      public void onHandle( Object o ) {
        createUser();
      }
    });
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
}
