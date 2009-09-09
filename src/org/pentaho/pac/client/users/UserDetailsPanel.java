/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.client.users;

import org.pentaho.pac.client.i18n.Messages;
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
    add(new Label(Messages.getString("userName"))); //$NON-NLS-1$
    add(userNameTextBox);
    add(new Label(Messages.getString("password"))); //$NON-NLS-1$
    add(passwordTextBox);
    add(new Label(Messages.getString("passwordConfirmation"))); //$NON-NLS-1$
    add(passwordConfirmationTextBox);
    add(new Label(Messages.getString("description"))); //$NON-NLS-1$
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
      setPassword(""); //$NON-NLS-1$
      setPasswordConfirmation(""); //$NON-NLS-1$
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
