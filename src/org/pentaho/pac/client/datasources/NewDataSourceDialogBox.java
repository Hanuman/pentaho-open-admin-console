package org.pentaho.pac.client.datasources;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class NewDataSourceDialogBox extends ConfirmDialog {

  private static final int GENERAL_PANEL_ID = 0;
  private static final int ADVANCE_PANEL_ID = 1;

  Button testButton;
  DataSourceGeneralPanel dataSourceGeneralPanel;
  DataSourceAdvancePanel dataSourceAdvancePanel;
  boolean dataSourceCreated = false;
  MessageDialog messageDialog = new MessageDialog( MSGS.error() );
  DeckPanel deckPanel;
  ToggleButton generalButton;
  ToggleButton advanceButton;
  public NewDataSourceDialogBox() {
    super();
    DockPanel dockPanel = new DockPanel();
    
    generalButton = new ToggleButton( MSGS.general(), MSGS.general() );
    advanceButton = new ToggleButton( MSGS.advance(), MSGS.advance() );
    
    setTitle( MSGS.addDataSource() );
    setClientSize( "350px", "450px" ); //$NON-NLS-1$ //$NON-NLS-2$
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    dataSourceGeneralPanel = new DataSourceGeneralPanel();
    horizontalPanel.add(generalButton);
    dataSourceAdvancePanel = new DataSourceAdvancePanel();
    horizontalPanel.add(advanceButton);
    dockPanel.add(horizontalPanel, DockPanel.NORTH);
    dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    dockPanel.setSpacing(10);
    generalButton.setTitle( MSGS.clickAddGeneral() );
    advanceButton.setTitle( MSGS.clickAddAdvance() );
    
    generalButton.setStylePrimaryName( "generalToggleBtn" ); //$NON-NLS-1$
    advanceButton.setStylePrimaryName( "advanceToggleBtn" ); //$NON-NLS-1$
    deckPanel = new DeckPanel();
    deckPanel.add(dataSourceGeneralPanel);
    deckPanel.add(dataSourceAdvancePanel);
    dockPanel.add(deckPanel, DockPanel.CENTER);
    dockPanel.setCellWidth(deckPanel, "100%"); //$NON-NLS-1$
    dockPanel.setCellHeight(deckPanel, "100%"); //$NON-NLS-1$
    deckPanel.setWidth("100%"); //$NON-NLS-1$
    deckPanel.setHeight("100%"); //$NON-NLS-1$
    deckPanel.setStyleName( "newDataSourceDialogBox.detailsPanel" ); //$NON-NLS-1$
    deckPanel.showWidget(GENERAL_PANEL_ID);
    generalButton.setDown(true);
    advanceButton.setDown(false);
    generalButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (!generalButton.isDown()) {
          generalButton.setDown(true);
        } else {
          advanceButton.setDown(false);
          deckPanel.showWidget(GENERAL_PANEL_ID);
        }
      }
    });
    advanceButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (!advanceButton.isDown()) {
          advanceButton.setDown(true);
        } else {
          generalButton.setDown(false);
          deckPanel.showWidget(ADVANCE_PANEL_ID);
        }
      }
    });

    dataSourceGeneralPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceGeneralPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceAdvancePanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceAdvancePanel.setHeight("100%"); //$NON-NLS-1$
    
    dockPanel.setWidth("100%"); //$NON-NLS-1$
    dockPanel.setHeight("100%"); //$NON-NLS-1$
    addWidgetToClientArea( dockPanel );
    testButton = new Button(MSGS.test(), new ClickListener() {
      public void onClick(Widget sender) {
        testDataSourceConnection();
      }
    });

    
    addWidgetToClientArea( testButton );

    setOnOkHandler( new ICallback() {
      public void onHandle( Object o ) {
        createDataSource();
      }
    });
  }

  public boolean isDataSourceCreated() {
    return dataSourceCreated;
  }

  private PentahoDataSource getNormalDataSource() {
    return dataSourceGeneralPanel.getDataSource();
  }

  public PentahoDataSource getDataSource() {
    PentahoDataSource normalDataSource = getNormalDataSource();
    PentahoDataSource advanceDataSource = getAdvanceDataSource();
    PentahoDataSource dataSource = consolidateNormalAndAdvance(normalDataSource, advanceDataSource);
    return dataSource;
  }

  private PentahoDataSource getAdvanceDataSource() {
    return dataSourceAdvancePanel.getDataSource();
  }

  public String getDriverClass() {
    return dataSourceGeneralPanel.getDriverClass();
  }

  public ListBox getDriverClassTextBox() {
    return dataSourceGeneralPanel.getDriverClassListBox();
  }

  public int getIdleConnections() {
    return dataSourceAdvancePanel.getIdleConnections();
  }

  public TextBox getIdleConnectionsTextBox() {
    return dataSourceAdvancePanel.getIdleConnectionsTextBox();
  }

  public String getJndiName() {
    return dataSourceGeneralPanel.getJndiName();
  }

  public TextBox getJndiNameTextBox() {
    return dataSourceGeneralPanel.getJndiNameTextBox();
  }

  public int getMaxActiveConnections() {
    return dataSourceAdvancePanel.getMaxActiveConnections();
  }

  public TextBox getMaxActiveConnectionsTextBox() {
    return dataSourceAdvancePanel.getMaxActiveConnectionsTextBox();
  }

  public String getPassword() {
    return dataSourceGeneralPanel.getPassword();
  }

  public PasswordTextBox getPasswordTextBox() {
    return dataSourceGeneralPanel.getPasswordTextBox();
  }

  public String getUrl() {
    return dataSourceGeneralPanel.getUrl();
  }

  public TextBox getUrlTextBox() {
    return dataSourceGeneralPanel.getUrlTextBox();
  }

  public String getUserName() {
    return dataSourceGeneralPanel.getUserName();
  }

  public TextBox getUserNameTextBox() {
    return dataSourceGeneralPanel.getUserNameTextBox();
  }

  public String getValidationQuery() {
    return dataSourceAdvancePanel.getValidationQuery();
  }

  public TextBox getValidationQueryTextBox() {
    return dataSourceAdvancePanel.getValidationQueryTextBox();
  }

  public void setDriverClass(String className) {
    dataSourceGeneralPanel.setDriverClass(className);
  }

  public void setIdleConnections(int count) {
    dataSourceAdvancePanel.setIdleConnections(count);
  }

  public void setJndiName(String jndiName) {
    dataSourceGeneralPanel.setJndiName(jndiName);
  }

  public void setMaxActiveConnections(int count) {
    dataSourceAdvancePanel.setMaxActiveConnections(count);
  }

  public void setPassword(String password) {
    dataSourceGeneralPanel.setPassword(password);
  }

  public void setUrl(String url) {
    dataSourceGeneralPanel.setUrl(url);
  }

  public void setUserName(String userName) {
    dataSourceGeneralPanel.setUserName(userName);
  }

  private void setAdvanceDataSource(PentahoDataSource dataSource) {
    dataSourceAdvancePanel.setDataSource(dataSource);
  }

  public void setDataSource(PentahoDataSource dataSource) {
    setAdvanceDataSource(dataSource);
    setNormalDataSource(dataSource);
  }

  private void setNormalDataSource(PentahoDataSource dataSource) {
    dataSourceGeneralPanel.setDataSource(dataSource);
  }

  private PentahoDataSource consolidateNormalAndAdvance(PentahoDataSource normalDataSource,
      PentahoDataSource advanceDataSource) {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setDriverClass(normalDataSource.getDriverClass());
    dataSource.setPassword(normalDataSource.getPassword());
    dataSource.setName(normalDataSource.getName());
    dataSource.setUrl(normalDataSource.getUrl());
    dataSource.setUserName(normalDataSource.getUserName());
    int idleConn = advanceDataSource.getIdleConn();
    int maxActConn = advanceDataSource.getMaxActConn();
    long wait = advanceDataSource.getWait();
    String query = advanceDataSource.getQuery();

    if (idleConn >= 0) {
      dataSource.setIdleConn(idleConn);
    } else {
      dataSource.setIdleConn(0);
    }
    if (maxActConn >= 0) {
      dataSource.setMaxActConn(maxActConn);
    } else {
      dataSource.setMaxActConn(0);
    }
    if (query != null && query.length() > 0) {
      dataSource.setQuery(query);
    } else {
      dataSource.setQuery(""); //$NON-NLS-1$
    }
    if (wait >= 0) {
      dataSource.setWait(wait);
    } else {
      dataSource.setWait(0);
    }
    return dataSource;
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
    final PentahoDataSource dataSource = getDataSource();

    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        messageDialog.setText(MSGS.testConnection());
        messageDialog.setMessage(MSGS.connectionTestSuccessful());
        messageDialog.center();
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText(MSGS.testConnection());
        messageDialog.setMessage(caught.getMessage());
        messageDialog.center();
      }
    };
    PacServiceFactory.getPacService().testDataSourceConnection(dataSource, callback);

  }
  
  public void refreshDriversList() {
    dataSourceGeneralPanel.refresh();
  }

}
