package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.common.datasources.PentahoDataSource;

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
    add(new Label(PentahoAdminConsole.getLocalizedMessages().jndiName()));
    add(jndiNameTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().maxActiveDbConnections()));
    add(maxActiveConnTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().jdbcDriverClass()));
    add(driverClassTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().numIdleDbConnnections()));
    add(idleConnTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().dbUserName()));
    add(userNameTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().dbPassword()));
    add(passwordTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().dbUrl()));
    add(urlTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().dbValidationQuery()));
    add(validationQueryTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().dbWaitTime()));
    add(waitTextBox);
    jndiNameTextBox.setWidth("100%"); //$NON-NLS-1$
    maxActiveConnTextBox.setWidth("100%"); //$NON-NLS-1$
    driverClassTextBox.setWidth("100%"); //$NON-NLS-1$
    idleConnTextBox.setWidth("100%"); //$NON-NLS-1$
    userNameTextBox.setWidth("100%"); //$NON-NLS-1$
    passwordTextBox.setWidth("100%"); //$NON-NLS-1$
    urlTextBox.setWidth("100%"); //$NON-NLS-1$
    validationQueryTextBox.setWidth("100%"); //$NON-NLS-1$
    waitTextBox.setWidth("100%"); //$NON-NLS-1$
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
    maxActiveConnTextBox.setText(count > 0 ? Integer.toString(count) : ""); //$NON-NLS-1$
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
    idleConnTextBox.setText(count > 0 ? Integer.toString(count) : ""); //$NON-NLS-1$
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
    waitTextBox.setText(count > 0 ? Long.toString(count) : ""); //$NON-NLS-1$
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
  
  public void setDataSource(PentahoDataSource dataSource) {
    if (dataSource == null) {
      setUserName(""); //$NON-NLS-1$
      setPassword(""); //$NON-NLS-1$
      setJndiName(""); //$NON-NLS-1$
      setMaxActiveConnections(-1);
      setDriverClass(""); //$NON-NLS-1$
      setIdleConnections(-1);
      setUrl(""); //$NON-NLS-1$
      setValidationQuery(""); //$NON-NLS-1$
      setWait(-1);
    } else {
      setUserName(dataSource.getUserName());
      setPassword(dataSource.getPassword());
      setJndiName(dataSource.getJndiName());
      setMaxActiveConnections(dataSource.getMaxActConn());
      setDriverClass(dataSource.getDriverClass());
      setIdleConnections(dataSource.getIdleConn());
      setUrl(dataSource.getUrl());
      setValidationQuery(dataSource.getQuery());
      setWait(dataSource.getWait());
    }
  }
  
  public PentahoDataSource getDataSource() {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setUserName(getUserName());
    dataSource.setPassword(getPassword());
    dataSource.setJndiName(getJndiName());
    dataSource.setMaxActConn(getMaxActiveConnections());
    dataSource.setDriverClass(getDriverClass());
    dataSource.setIdleConn(getIdleConnections());
    dataSource.setUrl(getUrl());
    dataSource.setQuery(getValidationQuery());
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
