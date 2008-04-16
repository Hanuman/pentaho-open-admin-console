package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.ICallbackHandler;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewDataSourceDialogBox extends ConfirmDialog {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  Button testButton;
  DataSourceDetailsPanel dataSourceDetailsPanel = new DataSourceDetailsPanel();
  boolean dataSourceCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  
  public NewDataSourceDialogBox() {
    super();

    setTitle( MSGS.addDataSource() );
    
    final NewDataSourceDialogBox localThis = this;
    dataSourceDetailsPanel.setStyleName( "newDataSourceDialogBox.detailsPanel" ); //$NON-NLS-1$
    addWidgetToClientArea( dataSourceDetailsPanel );
    testButton = new Button(MSGS.test(), new ClickListener() {
      public void onClick(Widget sender) {
        localThis.testDataSourceConnection();
      }
    });
    addWidgetToClientArea( testButton );

    setOnOkHandler( new ICallbackHandler() {
      public void onHandle( Object o ) {
        localThis.createDataSource();
      }
    });
  }

  public boolean isDataSourceCreated() {
    return dataSourceCreated;
  }

  public PentahoDataSource getDataSource() {
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


  public void setDataSource(PentahoDataSource dataSource) {
    dataSourceDetailsPanel.setDataSource(dataSource);
  }


  public void show() {
    dataSourceCreated = false;
    super.show();
  }
  
  private boolean createDataSource() {
    if (getJndiName().trim().length() == 0) {
      messageDialog.setMessage(MSGS.invalidConnectionName());
      messageDialog.center();
    } else if (getUrl().trim().length() == 0) { 
      messageDialog.setMessage(MSGS.missingDbUrl());
      messageDialog.center();
    } else if (getDriverClass().trim().length() == 0) { 
      messageDialog.setMessage(MSGS.missingDbDriver());
      messageDialog.center();
    } else if (getUserName().trim().length() == 0) { 
      messageDialog.setMessage(MSGS.missingDbUserName());
      messageDialog.center();
    } else {
      PentahoDataSource dataSource = getDataSource();
      if (dataSource != null) {
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            dataSourceCreated = true;
            hide();
            messageDialog.setText(MSGS.addDataSource());
            messageDialog.setMessage("New datasource successfully created");
            messageDialog.center();
          }

          public void onFailure(Throwable caught) {
            messageDialog.setText(MSGS.errorCreatingDataSource());
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
    if(sender == testButton) {
      testDataSourceConnection();      
    }
  }
  
  private void testDataSourceConnection() {
    final PentahoDataSource dataSource = dataSourceDetailsPanel.getDataSource();
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        messageDialog.setText(MSGS.testConnection());
        messageDialog.setMessage(MSGS.connectionTestSuccessful());
        messageDialog.center();
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText(MSGS.testConnection());
        messageDialog.setMessage( caught.getMessage() );
        messageDialog.center();
      }
    };
    PacServiceFactory.getPacService().testDataSourceConnection(dataSource, callback);
    
  }
  
}
