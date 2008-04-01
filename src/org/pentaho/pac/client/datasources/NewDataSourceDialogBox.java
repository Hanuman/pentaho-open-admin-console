package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PacService;
import org.pentaho.pac.client.PacServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
  Button cancelButton = new Button("Cancel");
  DataSourceDetailsPanel dataSourceDetailsPanel = new DataSourceDetailsPanel();
  boolean dataSourceCreated = false;
  PacServiceAsync pacService;
  
  public NewDataSourceDialogBox() {
    super();
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(dataSourceDetailsPanel);
    verticalPanel.add(footerPanel);
    
    setText("Add Data Source");
    setWidget(verticalPanel);
    okButton.addClickListener(this);
    cancelButton.addClickListener(this);
  }


  public Button getOkButton() {
    return okButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public boolean isDataSourceCreated() {
    return dataSourceCreated;
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


//  public void show() {
//    dataSourceCreated = false;
//    super.show();
//  }
  
  private boolean createDataSource() {
    boolean result = false;
    SimpleDataSource dataSource = getDataSource();
    if (dataSource != null) {
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          dataSourceCreated = true;
          hide();
        }

        public void onFailure(Throwable caught) {
          int x = 1;
        }
      };
      getPacService().createDataSource(dataSource, callback);
    }
    return result;
  }
  
  private PacServiceAsync getPacService() {
    if (pacService == null) {
      pacService = (PacServiceAsync) GWT.create(PacService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) pacService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "pacsvc";
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return pacService;
  }
  
  public void onClick(Widget sender) {
    if (sender == okButton) {
      createDataSource();
    } else if (sender == cancelButton) {
      hide();
    }
  }
}
