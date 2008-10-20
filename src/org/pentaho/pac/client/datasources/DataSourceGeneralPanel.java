package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataSourceGeneralPanel extends VerticalPanel {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  public static final int PASSWORD_MAX_LENGTH = 50;
  HorizontalPanel jdbcClassNamePanel = new HorizontalPanel(); 
  TextBox userNameTextBox = new TextBox();
  PasswordTextBox passwordTextBox = new PasswordTextBox();
  TextBox jndiNameTextBox = new TextBox();
  TextBox urlTextBox = new TextBox();
  private final ListBox driverList = new ListBox();
  TextBox driverClassNameTextBox = new TextBox();
  boolean driverClassListBoxHasValue = false;
  public DataSourceGeneralPanel() {
    PacServiceFactory.getJdbcDriverDiscoveryService().getAvailableJdbcDrivers(
        new AsyncCallback<NameValue[]>() {
          public void onFailure(Throwable caught) {
            jdbcClassNamePanel.add(driverClassNameTextBox);
            driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
            constructDatasourcePanel();
          }

          public void onSuccess(NameValue[] result) {
        	  if (result!=null) {
	            for (NameValue res : result)
	              driverList.addItem(res.getName(), res.getValue());
    		  }
            
            driverClassListBoxHasValue = result != null && result.length > 0; 
            if(driverClassListBoxHasValue) {
              jdbcClassNamePanel.add(driverList);
              driverList.setWidth("100%"); //$NON-NLS-1$
              constructDatasourcePanel();
            } else {
              jdbcClassNamePanel.add(driverClassNameTextBox);
              driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
              constructDatasourcePanel();
            }
          }
        });      
  }

  private void constructDatasourcePanel() {
    add(new Label(MSGS.jndiName()));
    add(jndiNameTextBox);
    add(new Label(MSGS.jdbcDriverClass()));
    add(jdbcClassNamePanel);
    jdbcClassNamePanel.setWidth("100%");
    add(new Label(MSGS.dbUserName()));
    add(userNameTextBox);
    add(new Label(MSGS.dbPassword()));
    add(passwordTextBox);
    passwordTextBox.setMaxLength(PASSWORD_MAX_LENGTH);
    add(new Label(MSGS.dbUrl()));
    add(urlTextBox);
    jndiNameTextBox.setWidth("100%"); //$NON-NLS-1$
    driverList.setWidth("100%"); //$NON-NLS-1$
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
    String returnValue = null;
    if(driverClassListBoxHasValue) {
      if((driverList.getSelectedIndex() >= 0) && (driverList.getSelectedIndex() < driverList.getItemCount())){
        returnValue = driverList.getValue(driverList.getSelectedIndex());
      }
    } else {
      returnValue = driverClassNameTextBox.getText();
    }
      
    return returnValue;
  }

  public void setDriverClass(String className) {
    if(driverClassListBoxHasValue) {
      for (int i = 0; i < driverList.getItemCount(); i++) {
        if (driverList.getValue(i).equals(className)) {
          driverList.setSelectedIndex(i);
        }
      }
    } else {
      driverClassNameTextBox.setText(className);
    }
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

  public ListBox getDriverClassListBox() {
    return driverList;
  }

  public TextBox getDriverClassTextBox() {
    return driverClassNameTextBox;
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
      setJndiName(dataSource.getName());
      setDriverClass(dataSource.getDriverClass());
      setUrl(dataSource.getUrl());
    }
  }
  
  public PentahoDataSource getDataSource() {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setUserName(getUserName());
    dataSource.setPassword(getPassword());
    dataSource.setName(getJndiName());
    dataSource.setDriverClass(getDriverClass());
    dataSource.setUrl(getUrl());
    return dataSource;
  }
  
  public void setEnabled(boolean enabled) {
    userNameTextBox.setEnabled(enabled);
    passwordTextBox.setEnabled(enabled);
    jndiNameTextBox.setEnabled(enabled);
    driverList.setEnabled(enabled);
    urlTextBox.setEnabled(enabled);
    driverClassNameTextBox.setEnabled(enabled);
  }
  
  public void refresh() {
    // First remove all the item in the list and then add the latest ones
    for(int i=0; i< driverList.getItemCount();i++) {
      driverList.removeItem(i);
    }
    PacServiceFactory.getJdbcDriverDiscoveryService().getAvailableJdbcDrivers(
        new AsyncCallback<NameValue[]>() {
          public void onFailure(Throwable caught) {
            driverList.removeFromParent();
            jdbcClassNamePanel.add(driverClassNameTextBox);
            driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
          }

          public void onSuccess(NameValue[] result) {
            for (NameValue res : result)
              driverList.addItem(res.getName(), res.getValue());
            driverClassListBoxHasValue = result != null && result.length > 0;
            if(driverClassListBoxHasValue) {
              driverClassNameTextBox.removeFromParent();
              jdbcClassNamePanel.add(driverList);
              driverList.setWidth("100%"); //$NON-NLS-1$
            } else {
              driverList.removeFromParent();
              jdbcClassNamePanel.add(driverClassNameTextBox);
              driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
            }
          }
        }); 
  
  }
}
