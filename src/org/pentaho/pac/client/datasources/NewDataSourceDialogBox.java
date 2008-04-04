package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.datasources.SimpleDataSource;
import org.pentaho.pac.common.users.DuplicateUserException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewDataSourceDialogBox extends DialogBox implements ClickListener {
  
  Button okButton = new Button("OK");
  Button testButton = new Button("Test");
  Button cancelButton = new Button("Cancel");
  DataSourceDetailsPanel dataSourceDetailsPanel = new DataSourceDetailsPanel();
  boolean dataSourceCreated = false;
  MessageDialog messageDialog = new MessageDialog("", new int[]{MessageDialog.OK_BTN});
  
  public NewDataSourceDialogBox() {
    super();
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(testButton);    
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(dataSourceDetailsPanel);
    verticalPanel.add(footerPanel);
    
    setText("Add Data Source");
    
    verticalPanel.setWidth("250px");
    dataSourceDetailsPanel.setWidth("100%");
    
    setWidget(verticalPanel);
    
    okButton.addClickListener(this);
    testButton.addClickListener(this);        
    cancelButton.addClickListener(this);
  }


  public Button getOkButton() {
    return okButton;
  }

  public Button getTestButton() {
    return testButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public boolean isDataSourceCreated() {
    return dataSourceCreated;
  }

  public void setText(String text) {
    messageDialog.setText(text);
    super.setText(text);
  }

  public SimpleDataSource getDataSource() {
    return dataSourceDetailsPanel.getDataSource();
  }


  public String getDriverClass() {
    return dataSourceDetailsPanel.getDriverClass();
  }


  public TextBox getDriverClassTextBox() {
    return dataSourceDetailsPanel.getDriverClassTextBox();
  }


  public int getIdleConnections() {
    return dataSourceDetailsPanel.getIdleConnections();
  }


  public TextBox getIdleConnectionsTextBox() {
    return dataSourceDetailsPanel.getIdleConnectionsTextBox();
  }


  public String getJndiName() {
    return dataSourceDetailsPanel.getJndiName();
  }


  public TextBox getJndiNameTextBox() {
    return dataSourceDetailsPanel.getJndiNameTextBox();
  }


  public int getMaxActiveConnections() {
    return dataSourceDetailsPanel.getMaxActiveConnections();
  }


  public TextBox getMaxActiveConnectionsTextBox() {
    return dataSourceDetailsPanel.getMaxActiveConnectionsTextBox();
  }


  public String getPassword() {
    return dataSourceDetailsPanel.getPassword();
  }


  public PasswordTextBox getPasswordTextBox() {
    return dataSourceDetailsPanel.getPasswordTextBox();
  }


  public String getUrl() {
    return dataSourceDetailsPanel.getUrl();
  }


  public TextBox getUrlTextBox() {
    return dataSourceDetailsPanel.getUrlTextBox();
  }


  public String getUserName() {
    return dataSourceDetailsPanel.getUserName();
  }


  public TextBox getUserNameTextBox() {
    return dataSourceDetailsPanel.getUserNameTextBox();
  }


  public String getValidationQuery() {
    return dataSourceDetailsPanel.getValidationQuery();
  }


  public TextBox getValidationQueryTextBox() {
    return dataSourceDetailsPanel.getValidationQueryTextBox();
  }


  public void setDriverClass(String className) {
    dataSourceDetailsPanel.setDriverClass(className);
  }


  public void setIdleConnections(int count) {
    dataSourceDetailsPanel.setIdleConnections(count);
  }


  public void setJndiName(String jndiName) {
    dataSourceDetailsPanel.setJndiName(jndiName);
  }


  public void setMaxActiveConnections(int count) {
    dataSourceDetailsPanel.setMaxActiveConnections(count);
  }


  public void setPassword(String password) {
    dataSourceDetailsPanel.setPassword(password);
  }


  public void setUrl(String url) {
    dataSourceDetailsPanel.setUrl(url);
  }


  public void setUserName(String userName) {
    dataSourceDetailsPanel.setUserName(userName);
  }


  public void setDataSource(SimpleDataSource dataSource) {
    dataSourceDetailsPanel.setDataSource(dataSource);
  }


  public void show() {
    dataSourceCreated = false;
    super.show();
  }
  
  private boolean createDataSource() {
    if (getJndiName().trim().length() == 0) {
      messageDialog.setMessage("Invalid connection name.");
      messageDialog.center();
    } else if (getUrl().trim().length() == 0) { 
      messageDialog.setMessage("Missing database URL.");
      messageDialog.center();
    } else if (getDriverClass().trim().length() == 0) { 
      messageDialog.setMessage("Missing database driver class.");
      messageDialog.center();
    } else if (getUserName().trim().length() == 0) { 
      messageDialog.setMessage("Missing user name.");
      messageDialog.center();
    } else {
      SimpleDataSource dataSource = getDataSource();
      if (dataSource != null) {
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            dataSourceCreated = true;
            hide();
          }

          public void onFailure(Throwable caught) {
            messageDialog.setText("Error Creating Data Source");
            messageDialog.setMessage(caught.getMessage());
            messageDialog.center();
          }
        };
        PacServiceFactory.getPacService().createDataSource(dataSource, callback);
      }
    }
    return dataSourceCreated;
  }
  
  public void onClick(Widget sender) {
    if (sender == okButton) {
      createDataSource();
    } else if (sender == cancelButton) {
      hide();
    } else if(sender == testButton) {
      testDataSourceConnection();      
    }
  }
  
  private void testDataSourceConnection() {
    final SimpleDataSource dataSource = dataSourceDetailsPanel.getDataSource();
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        messageDialog.setText("Test Conneciton");
        messageDialog.setMessage("Connection Test Successful.");
        messageDialog.center();
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText("Test Conneciton");
        messageDialog.setMessage( caught.getMessage() );
        messageDialog.center();
      }
    };
    PacServiceFactory.getPacService().testDataSourceConnection(dataSource, callback);
    
  }
  
}
