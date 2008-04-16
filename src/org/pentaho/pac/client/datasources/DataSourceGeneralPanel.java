package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataSourceGeneralPanel extends VerticalPanel {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();  
  TextBox userNameTextBox = new TextBox();
  PasswordTextBox passwordTextBox = new PasswordTextBox();
  TextBox jndiNameTextBox = new TextBox();
  TextBox driverClassTextBox = new TextBox();
  TextBox urlTextBox = new TextBox();
  
  public DataSourceGeneralPanel() {
    add(new Label(MSGS.jndiName()));
    add(jndiNameTextBox);
    add(new Label(MSGS.jdbcDriverClass()));
    add(driverClassTextBox);
    add(new Label(MSGS.dbUserName()));
    add(userNameTextBox);
    add(new Label(MSGS.dbPassword()));
    add(passwordTextBox);
    add(new Label(MSGS.dbUrl()));
    add(urlTextBox);
    jndiNameTextBox.setWidth("100%"); //$NON-NLS-1$
    driverClassTextBox.setWidth("100%"); //$NON-NLS-1$
    userNameTextBox.setWidth("100%"); //$NON-NLS-1$
    passwordTextBox.setWidth("100%"); //$NON-NLS-1$
    urlTextBox.setWidth("100%"); //$NON-NLS-1$
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

  public String getJndiName() {
    return jndiNameTextBox.getText();
  }

  public void setJndiName(String jndiName) {
    jndiNameTextBox.setText(jndiName);
  }

  public String getDriverClass() {
    return driverClassTextBox.getText();
  }

  public void setDriverClass(String className) {
    driverClassTextBox.setText(className);
  }
  
  public String getUrl() {
    return urlTextBox.getText();
  }

  public void setUrl(String url) {
    urlTextBox.setText(url);
  }

  public TextBox getUserNameTextBox() {
    return userNameTextBox;
  }

  public PasswordTextBox getPasswordTextBox() {
    return passwordTextBox;
  }

  public TextBox getJndiNameTextBox() {
    return jndiNameTextBox;
  }

  public TextBox getDriverClassTextBox() {
    return driverClassTextBox;
  }
  
  public TextBox getUrlTextBox() {
    return urlTextBox;
  }

  public void setDataSource(PentahoDataSource dataSource) {
    if (dataSource == null) {
      setUserName(""); //$NON-NLS-1$
      setPassword(""); //$NON-NLS-1$
      setJndiName(""); //$NON-NLS-1$
      setDriverClass(""); //$NON-NLS-1$
      setUrl(""); //$NON-NLS-1$
    } else {
      setUserName(dataSource.getUserName());
      setPassword(dataSource.getPassword());
      setJndiName(dataSource.getJndiName());
      setDriverClass(dataSource.getDriverClass());
      setUrl(dataSource.getUrl());
    }
  }
  
  public PentahoDataSource getDataSource() {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setUserName(getUserName());
    dataSource.setPassword(getPassword());
    dataSource.setJndiName(getJndiName());
    dataSource.setDriverClass(getDriverClass());
    dataSource.setUrl(getUrl());
    return dataSource;
  }
  
  public void setEnabled(boolean enabled) {
    userNameTextBox.setEnabled(enabled);
    passwordTextBox.setEnabled(enabled);
    jndiNameTextBox.setEnabled(enabled);
    driverClassTextBox.setEnabled(enabled);
    urlTextBox.setEnabled(enabled);
  }
}
