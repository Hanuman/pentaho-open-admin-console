package org.pentaho.pac.client.datasources;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataSourceDetailsPanel extends VerticalPanel {
  TextBox userNameTextBox = new TextBox();
  PasswordTextBox passwordTextBox = new PasswordTextBox();
  TextBox jndiNameTextBox = new TextBox();
  TextBox maxActiveConnTextBox = new TextBox();
  TextBox driverClassTextBox = new TextBox();
  TextBox idleConnTextBox = new TextBox();
  TextBox urlTextBox = new TextBox();
  TextBox validationQueryTextBox = new TextBox();
  TextBox waitTextBox = new TextBox();
  
  public DataSourceDetailsPanel() {
    add(new Label("JNDI Name:"));
    add(jndiNameTextBox);
    add(new Label("Max Active Conn:"));
    add(maxActiveConnTextBox);
    add(new Label("Driver Class:"));
    add(driverClassTextBox);
    add(new Label("#Idle Conn:"));
    add(idleConnTextBox);
    add(new Label("User Name:"));
    add(userNameTextBox);
    add(new Label("Password:"));
    add(passwordTextBox);
    add(new Label("URL:"));
    add(urlTextBox);
    add(new Label("Validation Query:"));
    add(validationQueryTextBox);
    add(new Label("Wait:"));
    add(waitTextBox);
    jndiNameTextBox.setWidth("100%");
    maxActiveConnTextBox.setWidth("100%");
    driverClassTextBox.setWidth("100%");
    idleConnTextBox.setWidth("100%");
    userNameTextBox.setWidth("100%");
    passwordTextBox.setWidth("100%");
    urlTextBox.setWidth("100%");
    validationQueryTextBox.setWidth("100%");
    waitTextBox.setWidth("100%");
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

  public int getMaxActiveConnections() {
    int count = -1;
    try {
      count = Integer.parseInt(maxActiveConnTextBox.getText());
    } catch (Exception ex) {
      // Do nothing.
    }
    return count;
  }

  public void setMaxActiveConnections(int count) {
    maxActiveConnTextBox.setText(count > 0 ? Integer.toString(count) : "");
  }
  
  public String getDriverClass() {
    return driverClassTextBox.getText();
  }

  public void setDriverClass(String className) {
    driverClassTextBox.setText(className);
  }

  public int getIdleConnections() {
    int count = -1;
    try {
      count = Integer.parseInt(idleConnTextBox.getText());
    } catch (Exception ex) {
      // Do nothing.
    }
    return count;
  }
  
  public void setIdleConnections(int count) {
    idleConnTextBox.setText(count > 0 ? Integer.toString(count) : "");
  }
  
  public String getUrl() {
    return urlTextBox.getText();
  }

  public void setUrl(String url) {
    urlTextBox.setText(url);
  }

  public String getValidationQuery() {
    return validationQueryTextBox.getText();
  }

  public void setValidationQuery(String query) {
    validationQueryTextBox.setText(query);
  }

  public long getWait() {
    int count = -1;
    try {
      count = Integer.parseInt(waitTextBox.getText());
    } catch (Exception ex) {
      // Do nothing.
    }
    return count;
  }
  
  public void setWait(long count) {
    waitTextBox.setText(count > 0 ? Long.toString(count) : "");
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

  public TextBox getMaxActiveConnectionsTextBox() {
    return maxActiveConnTextBox;
  }

  public TextBox getDriverClassTextBox() {
    return driverClassTextBox;
  }

  public TextBox getIdleConnectionsTextBox() {
    return maxActiveConnTextBox;
  }
  
  public TextBox getUrlTextBox() {
    return urlTextBox;
  }

  public TextBox getValidationQueryTextBox() {
    return validationQueryTextBox;
  }

  public TextBox getWaitTextBox() {
    return waitTextBox;
  }
  
  public void setDataSource(SimpleDataSource dataSource) {
    if (dataSource == null) {
      setUserName("");
      setPassword("");
      setJndiName("");
      setMaxActiveConnections(-1);
      setDriverClass("");
      setIdleConnections(-1);
      setUrl("");
      setValidationQuery("");
      setWait(-1);
    } else {
      setUserName(dataSource.getUserName());
      setPassword(dataSource.getPassword());
      setJndiName(dataSource.getJndiName());
      setMaxActiveConnections(dataSource.getActive());
      setDriverClass(dataSource.getDriverClass());
      setIdleConnections(dataSource.getIdle());
      setUrl(dataSource.getUrl());
      setValidationQuery(dataSource.getValidationQuery());
      setWait(dataSource.getWait());
    }
  }
  
  public SimpleDataSource getDataSource() {
    SimpleDataSource dataSource = new SimpleDataSource();
    dataSource.setUserName(getUserName());
    dataSource.setPassword(getPassword());
    dataSource.setJndiName(getJndiName());
    dataSource.setActive(getMaxActiveConnections());
    dataSource.setDriverClass(getDriverClass());
    dataSource.setIdle(getIdleConnections());
    dataSource.setUrl(getUrl());
    dataSource.setValidationQuery(getValidationQuery());
    dataSource.setWait(getWait());
    return dataSource;
  }
  
  public void setEnabled(boolean enabled) {
    userNameTextBox.setEnabled(enabled);
    passwordTextBox.setEnabled(enabled);
    jndiNameTextBox.setEnabled(enabled);
    maxActiveConnTextBox.setEnabled(enabled);
    driverClassTextBox.setEnabled(enabled);
    idleConnTextBox.setEnabled(enabled);
    urlTextBox.setEnabled(enabled);
    validationQueryTextBox.setEnabled(enabled);
    waitTextBox.setEnabled(enabled);
  }
}
