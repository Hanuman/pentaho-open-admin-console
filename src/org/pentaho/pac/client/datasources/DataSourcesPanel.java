package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class DataSourcesPanel extends DockPanel implements ClickListener, ChangeListener, PopupListener {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

  public static final int GENERAL_PANEL_ID = 0;

  public static final int ADVANCE_PANEL_ID = 1;

//  TabPanel generalAdvanceDbPanel = new TabPanel();

  MessageDialog messageDialog = new MessageDialog();

  DataSourcesList dataSourcesList = new DataSourcesList();

  PentahoDataSource[] dataSources = null;

  DataSourceGeneralPanel dataSourceGeneralPanel = new DataSourceGeneralPanel();

  DataSourceAdvancePanel dataSourceAdvancePanel = new DataSourceAdvancePanel();

  DeckPanel deckPanel = new DeckPanel();

  ToggleButton generalButton = new ToggleButton(MSGS.general(), MSGS.general());

  ToggleButton advanceButton = new ToggleButton(MSGS.advance(), MSGS.advance());

  Button updateDataSourceBtn = new Button(MSGS.update());

  Button testDataSourceBtn = new Button(MSGS.test());

  Button addDataSourceBtn = new Button("+"); //$NON-NLS-1$

  Button deleteDataSourceBtn = new Button("-"); //$NON-NLS-1$

  NewDataSourceDialogBox newDataSourceDialogBox = new NewDataSourceDialogBox();

  ConfirmDialog confirmDataSourceDeleteDialog = new ConfirmDialog(MSGS.deleteDataSources(), MSGS
      .confirmDataSourceDeletionMsg());

  public DataSourcesPanel() {
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(testDataSourceBtn);
    footerPanel.add(updateDataSourceBtn);
    DockPanel dataSourcesListPanel = buildDataSourcesListPanel();
    DockPanel dataSourceDetailsDockPanel = buildDataSourceDetailsDockPanel();
    add(dataSourcesListPanel, DockPanel.WEST);
    add(dataSourceDetailsDockPanel, DockPanel.CENTER);
    add(footerPanel, DockPanel.SOUTH);
    setCellHorizontalAlignment(footerPanel, HasHorizontalAlignment.ALIGN_RIGHT);
    setSpacing(10);
    setCellWidth(dataSourcesListPanel, "30%"); //$NON-NLS-1$
    setCellWidth(dataSourceDetailsDockPanel, "70%"); //$NON-NLS-1$
    setCellHeight(dataSourcesListPanel, "100%"); //$NON-NLS-1$
    setCellHeight(dataSourceDetailsDockPanel, "100%"); //$NON-NLS-1$
    dataSourcesListPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesListPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceDetailsDockPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceDetailsDockPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceGeneralPanel.setEnabled(false);
    dataSourceGeneralPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceGeneralPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceAdvancePanel.setEnabled(false);
    dataSourceAdvancePanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceAdvancePanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceAdvancePanel.setVisible(false);
    updateDataSourceBtn.setEnabled(false);
    testDataSourceBtn.setEnabled(false);
    newDataSourceDialogBox.addPopupListener(this);
    updateDataSourceBtn.addClickListener(this);
    testDataSourceBtn.addClickListener(this);
    confirmDataSourceDeleteDialog.setOnOkHandler(new ICallback() {
      public void onHandle(Object o) {
        confirmDataSourceDeleteDialog.hide();
        deleteSelectedDataSources();

      }
    });
  }

  public DockPanel buildDataSourceDetailsDockPanel() {
    DockPanel dockPanel = new DockPanel();

    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.add(generalButton);
    horizontalPanel.add(advanceButton);
    dockPanel.add(horizontalPanel, DockPanel.NORTH);
    dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    dockPanel.setSpacing(10);
    generalButton.setTitle(MSGS.clickEditGeneral());
    advanceButton.setTitle(MSGS.clickEditAdvance());

    generalButton.setStylePrimaryName("generalToggleBtn"); //$NON-NLS-1$
    advanceButton.setStylePrimaryName("advanceToggleBtn"); //$NON-NLS-1$

    deckPanel.add(dataSourceGeneralPanel);
    deckPanel.add(dataSourceAdvancePanel);
    dockPanel.add(deckPanel, DockPanel.CENTER);

    dockPanel.setCellWidth(deckPanel, "100%"); //$NON-NLS-1$
    dockPanel.setCellHeight(deckPanel, "100%"); //$NON-NLS-1$

    deckPanel.setWidth("100%"); //$NON-NLS-1$
    deckPanel.setHeight("100%"); //$NON-NLS-1$
    deckPanel.setStyleName("newDataSourceDialogBox.detailsPanel"); //$NON-NLS-1$
    deckPanel.showWidget(GENERAL_PANEL_ID);
    generalButton.setDown(false);
    advanceButton.setDown(true);
    generalButton.addClickListener(this);
    advanceButton.addClickListener(this);

    dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    dataSourceGeneralPanel.getJndiNameTextBox().setReadOnly(true);
    return dockPanel;

  }

  public DockPanel buildDataSourcesListPanel() {
    DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteDataSourceBtn, DockPanel.EAST);
    headerDockPanel.add(addDataSourceBtn, DockPanel.EAST);
    Label label = new Label(MSGS.dataSources());
    headerDockPanel.add(label, DockPanel.WEST);
    headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    DockPanel dataSourceListPanel = new DockPanel();
    dataSourceListPanel.add(headerDockPanel, DockPanel.NORTH);
    dataSourceListPanel.add(dataSourcesList, DockPanel.CENTER);
    dataSourceListPanel.setCellHeight(dataSourcesList, "100%"); //$NON-NLS-1$
    dataSourceListPanel.setCellWidth(dataSourcesList, "100%"); //$NON-NLS-1$
    dataSourceListPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceListPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesList.setHeight("100%"); //$NON-NLS-1$
    dataSourcesList.setWidth("100%"); //$NON-NLS-1$
    addDataSourceBtn.setWidth("20px"); //$NON-NLS-1$
    deleteDataSourceBtn.setWidth("20px"); //$NON-NLS-1$
    addDataSourceBtn.setHeight("20px"); //$NON-NLS-1$
    deleteDataSourceBtn.setHeight("20px"); //$NON-NLS-1$
    deleteDataSourceBtn.setEnabled(false);
    dataSourcesList.addChangeListener(this);
    addDataSourceBtn.addClickListener(this);
    deleteDataSourceBtn.addClickListener(this);
    return dataSourceListPanel;
  }

  public void onClick(Widget sender) {
    if (sender == updateDataSourceBtn) {
      updateDataSourceDetails(sender);
    } else if (sender == testDataSourceBtn) {
      testDataSourceConnection();
    } else if (sender == deleteDataSourceBtn) {
      if (dataSourcesList.getSelectedDataSources().length > 0) {
        confirmDataSourceDeleteDialog.center();
      }
    } else if (sender == addDataSourceBtn) {
      addNewDataSource();
    } else if (sender == generalButton) {
      if (!generalButton.isDown()) {
        generalButton.setDown(true);
      } else {
        advanceButton.setDown(false);
        deckPanel.showWidget(GENERAL_PANEL_ID);
      }
    } else if (sender == advanceButton) {
      if (!advanceButton.isDown()) {
        advanceButton.setDown(true);
      } else {
        generalButton.setDown(false);
        deckPanel.showWidget(ADVANCE_PANEL_ID);
      }
    }    
  }

  private void addNewDataSource() {
    newDataSourceDialogBox.setDataSource(null);
    newDataSourceDialogBox.center();
  }

  private void deleteSelectedDataSources() {
    final PentahoDataSource[] selectedDataSources = dataSourcesList.getSelectedDataSources();
    if (selectedDataSources.length > 0) {
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          messageDialog.setText(MSGS.deleteDataSources());
          messageDialog.setMessage(MSGS.successfulDataSourceDelete()); 
          messageDialog.center();
          refresh();
        }

        public void onFailure(Throwable caught) {
          messageDialog.setText(MSGS.errorDeletingDataSource());
          messageDialog.setMessage(caught.getMessage());
          messageDialog.center();
        }
      };
      PacServiceFactory.getPacService().deleteDataSources(selectedDataSources, callback);
    }
  }

  private void dataSourceSelectionChanged() {
    PentahoDataSource[] selectedDataSources = dataSourcesList.getSelectedDataSources();
    if (selectedDataSources.length == 1) {
      dataSourceGeneralPanel.setDataSource(selectedDataSources[0]);
      dataSourceAdvancePanel.setDataSource(selectedDataSources[0]);
    } else {
      dataSourceGeneralPanel.setDataSource(null);
      dataSourceAdvancePanel.setDataSource(null);
    }
    dataSourceGeneralPanel.setEnabled(selectedDataSources.length == 1);
    dataSourceAdvancePanel.setEnabled(selectedDataSources.length == 1);
    updateDataSourceBtn.setEnabled(selectedDataSources.length == 1);
    testDataSourceBtn.setEnabled(selectedDataSources.length == 1);
    deleteDataSourceBtn.setEnabled(selectedDataSources.length > 0);

  }

  private void updateDataSourceDetails(final Widget sender) {
    messageDialog.setText(MSGS.updateDataSource());
    if (dataSourceGeneralPanel.getJndiName().trim().length() == 0) {
      messageDialog.setMessage(MSGS.invalidConnectionName());
      messageDialog.center();
    } else if (dataSourceGeneralPanel.getUrl().trim().length() == 0) {
      messageDialog.setMessage(MSGS.missingDbUrl());
      messageDialog.center();
    } else if (dataSourceGeneralPanel.getDriverClass().trim().length() == 0) {
      messageDialog.setMessage(MSGS.missingDbDriver());
      messageDialog.center();
    } else if (dataSourceGeneralPanel.getUserName().trim().length() == 0) {
      messageDialog.setMessage(MSGS.missingDbUserName());
      messageDialog.center();
    } else {
      final PentahoDataSource dataSource = getDataSource();
      final int index = dataSourcesList.getSelectedIndex();

      ((Button) sender).setEnabled(false);
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          messageDialog.setText(MSGS.updateDataSource());
          messageDialog.setMessage(MSGS.successfulDataSourceUpdate()); 
          messageDialog.center();
          dataSourcesList.setDataSource(index, dataSource);
          ((Button) sender).setEnabled(true);

        }

        public void onFailure(Throwable caught) {
          messageDialog.setText(MSGS.errorUpdatingDataSource());
          messageDialog.setMessage(caught.getMessage());
          messageDialog.center();
          ((Button) sender).setEnabled(true);
        }
      };
      PacServiceFactory.getPacService().updateDataSource(dataSource, callback);
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

  public boolean validate() {
    return true;
  }

  public void onChange(Widget sender) {
    dataSourceSelectionChanged();
  }

  public void refresh() {
    dataSourcesList.refresh();
    dataSourceSelectionChanged();
  }

  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if ((sender == newDataSourceDialogBox) && newDataSourceDialogBox.isDataSourceCreated()) {
      PentahoDataSource dataSource = newDataSourceDialogBox.getDataSource();
      if (dataSourcesList.addDataSource(dataSource)) {
        dataSourcesList.setSelectedDataSource(dataSource);
        dataSourceSelectionChanged();
      }
    }
  }

  public boolean isInitialized() {
    return dataSourcesList.isInitialized();
  }

  public void clearDataSourcesCache() {
    dataSourcesList.clearDataSourcesCache();
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

  private PentahoDataSource consolidateNormalAndAdvance(PentahoDataSource normalDataSource,
      PentahoDataSource advanceDataSource) {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setDriverClass(normalDataSource.getDriverClass());
    dataSource.setPassword(normalDataSource.getPassword());
    dataSource.setJndiName(normalDataSource.getJndiName());
    dataSource.setUrl(normalDataSource.getUrl());
    dataSource.setUserName(normalDataSource.getUserName());
    dataSource.setIdleConn(advanceDataSource.getIdleConn());
    dataSource.setMaxActConn(advanceDataSource.getMaxActConn());
    dataSource.setQuery(advanceDataSource.getQuery());
    dataSource.setWait(advanceDataSource.getWait());

    return dataSource;
  }
}