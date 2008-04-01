package org.pentaho.pac.client.users;

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
    add(new Label("User Name"));
    add(userNameTextBox);
    add(new Label("Password"));
    add(passwordTextBox);
    add(new Label("Password Confirmation"));
    add(passwordConfirmationTextBox);
    add(new Label("Description"));
    add(descriptionTextBox);
    userNameTextBox.setWidth("100%");
    passwordTextBox.setWidth("100%");
    passwordConfirmationTextBox.setWidth("100%");
    descriptionTextBox.setWidth("100%");
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
      setUserName("");
      setPassword("");
      setPasswordConfirmation("");
      setDescription("");
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
