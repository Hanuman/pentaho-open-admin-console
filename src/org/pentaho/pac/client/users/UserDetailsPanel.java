package org.pentaho.pac.client.users;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserDetailsPanel extends VerticalPanel {
  TextBox userNameTextBox = new TextBox();
  PasswordTextBox passwordTextBox = new PasswordTextBox();
  PasswordTextBox passwordConfirmationTextBox = new PasswordTextBox();
  TextBox descriptionTextBox = new TextBox();
  
  public UserDetailsPanel() {
    add(new Label(PentahoAdminConsole.getLocalizedMessages().userName()));
    add(userNameTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().password()));
    add(passwordTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().passwordConfirmation()));
    add(passwordConfirmationTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().description()));
    add(descriptionTextBox);
    userNameTextBox.setWidth("100%"); //$NON-NLS-1$
    passwordTextBox.setWidth("100%"); //$NON-NLS-1$
    passwordConfirmationTextBox.setWidth("100%"); //$NON-NLS-1$
    descriptionTextBox.setWidth("100%"); //$NON-NLS-1$
  }

  public String getUserName() {
    return userNameTextBox.getText();
  }

  public void setUserName(String userName) {
    this.userNameTextBox.setText(userName);
  }

  public String getPassword() {
    return passwordTextBox.getText();
  }

  public void setPassword(String password) {
    this.passwordTextBox.setText(password);
  }

  public String getPasswordConfirmation() {
    return passwordConfirmationTextBox.getText();
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    passwordConfirmationTextBox.setText(passwordConfirmation);
  }

  public String getDescription() {
    return descriptionTextBox.getText();
  }

  public void setDescription(String description) {
    descriptionTextBox.setText(description);
  }

  public TextBox getUserNameTextBox() {
    return userNameTextBox;
  }

  public PasswordTextBox getPasswordTextBox() {
    return passwordTextBox;
  }

  public PasswordTextBox getPasswordConfirmationTextBox() {
    return passwordConfirmationTextBox;
  }

  public TextBox getDescriptionTextBox() {
    return descriptionTextBox;
  }

  public void setUser(ProxyPentahoUser user) {
    if (user == null) {
      setUserName(""); //$NON-NLS-1$
      setPassword(""); //$NON-NLS-1$
      setPasswordConfirmation(""); //$NON-NLS-1$
      setDescription(""); //$NON-NLS-1$
    } else {
      setUserName(user.getName());
      setPassword(user.getPassword());
      setPasswordConfirmation(user.getPassword());
      setDescription(user.getDescription());
    }
  }
  
  public ProxyPentahoUser getUser() {
    ProxyPentahoUser user = null;
    if (getPassword().equals(getPasswordConfirmation())) {
      user = new ProxyPentahoUser();
      user.setName(getUserName());
      user.setDescription(getDescription());
      user.setPassword(getPassword());
    }
    return user;
  }
  
  public void setEnabled(boolean enabled) {
    userNameTextBox.setEnabled(enabled);
    passwordConfirmationTextBox.setEnabled(enabled);
    passwordTextBox.setEnabled(enabled);
    descriptionTextBox.setEnabled(enabled);
    
  }
}
