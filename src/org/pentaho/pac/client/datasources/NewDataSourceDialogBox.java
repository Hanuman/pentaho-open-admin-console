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
package org.pentaho.pac.client.datasources;

import org.pentaho.gwt.widgets.client.buttons.RoundedButton;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class NewDataSourceDialogBox extends PromptDialogBox {

  private static final int GENERAL_PANEL_ID = 0;
  private static final int ADVANCE_PANEL_ID = 1;

  RoundedButton testButton;
  DataSourceGeneralPanel dataSourceGeneralPanel;
  DataSourceAdvancePanel dataSourceAdvancePanel;
  boolean dataSourceCreated = false;
  HTML msgBoxHtml = new HTML();
  PromptDialogBox messageDialog = new PromptDialogBox(Messages.getString("error"),Messages.getString("ok"), null, false, true, msgBoxHtml); //$NON-NLS-1$ //$NON-NLS-2$
  DeckPanel deckPanel;
  ToggleButton generalButton;
  ToggleButton advanceButton;
  public NewDataSourceDialogBox() {
    super(Messages.getString("addDataSource"), Messages.getString("ok"), Messages.getString("cancel"), false, true);   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    DockPanel dockPanel = new DockPanel();
    
    generalButton = new ToggleButton( Messages.getString("general"), Messages.getString("general") ); //$NON-NLS-1$ //$NON-NLS-2$
    advanceButton = new ToggleButton( Messages.getString("advance"), Messages.getString("advance") ); //$NON-NLS-1$ //$NON-NLS-2$
    testButton = new RoundedButton(Messages.getString("test")); //$NON-NLS-1$
    testButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        testDataSourceConnection();
      }
    });
    
    setTitle( Messages.getString("addDataSource") ); //$NON-NLS-1$
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    dataSourceGeneralPanel = new DataSourceGeneralPanel();
    horizontalPanel.add(generalButton);
    dataSourceAdvancePanel = new DataSourceAdvancePanel();
    horizontalPanel.add(advanceButton);
    dockPanel.add(horizontalPanel, DockPanel.NORTH);
    dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    dockPanel.setSpacing(10);
    generalButton.setTitle( Messages.getString("clickAddGeneral") ); //$NON-NLS-1$
    advanceButton.setTitle( Messages.getString("clickAddAdvance") ); //$NON-NLS-1$
    
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
    dockPanel.add(testButton, DockPanel.SOUTH);
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
    
    setContent(dockPanel);
    dockPanel.setWidth("350px");
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
      msgBoxHtml.setHTML(Messages.getString("invalidConnectionName")); //$NON-NLS-1$
      messageDialog.center();
    } else if (getUrl().trim().length() == 0) {
      msgBoxHtml.setHTML(Messages.getString("missingDbUrl")); //$NON-NLS-1$
      messageDialog.center();
    } else if (getDriverClass().trim().length() == 0) {
      msgBoxHtml.setHTML(Messages.getString("missingDbDriver")); //$NON-NLS-1$
      messageDialog.center();
    } else if (getUserName().trim().length() == 0) {
      msgBoxHtml.setHTML(Messages.getString("missingDbUserName")); //$NON-NLS-1$
      messageDialog.center();
    } else {
      PentahoDataSource dataSource = getDataSource();
      if (dataSource != null) {
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
          public void onSuccess(Boolean result) {
            dataSourceCreated = true;
            hide();
            okButton.setEnabled(true);
            cancelButton.setEnabled(true);
            if (getCallback() != null) {
              getCallback().okPressed();
            }
          }

          public void onFailure(Throwable caught) {
            messageDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
            msgBoxHtml.setHTML(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorCreatingDataSource")));           //$NON-NLS-1$
            messageDialog.center();
            okButton.setEnabled(true);
            cancelButton.setEnabled(true);
          }
        };
        okButton.setEnabled(false);
        cancelButton.setEnabled(false);
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

    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        messageDialog.setText(Messages.getString("testConnection")); //$NON-NLS-1$
        msgBoxHtml.setHTML(Messages.getString("connectionTestSuccessful")); //$NON-NLS-1$
        messageDialog.center();
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
        msgBoxHtml.setHTML(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorTestingDataSourceConnection")));           //$NON-NLS-1$
        messageDialog.center();
      }
    };
    PacServiceFactory.getPacService().testDataSourceConnection(dataSource, callback);

  }
  
  public void refresh(NameValue[] drivers) {
    dataSourceGeneralPanel.refresh(drivers);
  }
  
  protected void onOk() {
    createDataSource();
  }
}
