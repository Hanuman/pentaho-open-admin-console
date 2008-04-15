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
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class DataSourcesPanel extends DockPanel implements ClickListener, ChangeListener, PopupListener {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  MessageDialog messageDialog = new MessageDialog(); //$NON-NLS-1$
  DataSourcesList dataSourcesList = new DataSourcesList();
  PentahoDataSource[] dataSources = null;
  DataSourceDetailsPanel dataSourceDetailsPanel = new DataSourceDetailsPanel();
  Button updateDataSourceBtn = new Button(MSGS.update());
  Button testDataSourceBtn = new Button(MSGS.test());
  Button addDataSourceBtn = new Button("+"); //$NON-NLS-1$
  Button deleteDataSourceBtn = new Button("-"); //$NON-NLS-1$
  NewDataSourceDialogBox newDataSourceDialogBox = new NewDataSourceDialogBox();
  ConfirmDialog confirmDataSourceDeleteDialog = new ConfirmDialog(MSGS.deleteDataSources(), MSGS.confirmDataSourceDeletionMsg() );
  
	public DataSourcesPanel() {
	  DockPanel dataSourcesListPanel = buildDataSourcesListPanel();
	  
	  DockPanel dataSourceDetailsDockPanel = buildDataSourceDetailsDockPanel();
    add(dataSourcesListPanel, DockPanel.WEST);
    add(dataSourceDetailsDockPanel, DockPanel.CENTER);
    
    setSpacing(10);
    
    setCellWidth(dataSourcesListPanel, "30%"); //$NON-NLS-1$
    setCellWidth(dataSourceDetailsDockPanel, "70%"); //$NON-NLS-1$
    setCellHeight(dataSourcesListPanel, "100%"); //$NON-NLS-1$
    setCellHeight(dataSourceDetailsDockPanel, "100%"); //$NON-NLS-1$
    dataSourcesListPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesListPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourceDetailsDockPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceDetailsDockPanel.setHeight("100%"); //$NON-NLS-1$
    
    dataSourceDetailsPanel.setEnabled(false);
    updateDataSourceBtn.setEnabled(false);
    testDataSourceBtn.setEnabled(false);
    newDataSourceDialogBox.addPopupListener(this);

    final DataSourcesPanel localThis = this;
    confirmDataSourceDeleteDialog.setOnOkHandler( new ICallbackHandler() {
      public void onHandle(Object o) {
        localThis.deleteSelectedDataSources();
      }
    });
 	}

	public DockPanel buildDataSourceDetailsDockPanel() {
	  DockPanel dockPanel = new DockPanel();
    HorizontalPanel horizontalPanel = new  HorizontalPanel();
    horizontalPanel.add(testDataSourceBtn);
    horizontalPanel.add(updateDataSourceBtn);
    dockPanel.add(dataSourceDetailsPanel, DockPanel.CENTER);
    dockPanel.add(horizontalPanel, DockPanel.SOUTH);
    dockPanel.setCellHeight(dataSourceDetailsPanel, "100%"); //$NON-NLS-1$
    dockPanel.setCellWidth(dataSourceDetailsPanel, "100%"); //$NON-NLS-1$
    dataSourceDetailsPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourceDetailsPanel.setHeight("100%"); //$NON-NLS-1$
    dockPanel.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_RIGHT);
    updateDataSourceBtn.addClickListener(this);
    testDataSourceBtn.addClickListener(this);
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
      updateDataSourceDetails( sender );;
    } else if (sender == testDataSourceBtn) {
      testDataSourceConnection();
    } else if (sender == deleteDataSourceBtn) {
      if (dataSourcesList.getSelectedDataSources().length > 0) {
        confirmDataSourceDeleteDialog.center();
      }
    } else if (sender == addDataSourceBtn) {
      addNewDataSource();
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
          messageDialog.setMessage("Successfully Deleted the selected datasource(s)");
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
      dataSourceDetailsPanel.setDataSource(selectedDataSources[0]);
    } else {
      dataSourceDetailsPanel.setDataSource(null);
   }
    dataSourceDetailsPanel.setEnabled(selectedDataSources.length == 1);
    updateDataSourceBtn.setEnabled(selectedDataSources.length == 1);
    testDataSourceBtn.setEnabled(selectedDataSources.length == 1);
    deleteDataSourceBtn.setEnabled(selectedDataSources.length > 0);
    
	}
	
	private void updateDataSourceDetails( final Widget sender ) {
    messageDialog.setText(MSGS.updateDataSource());
    if (dataSourceDetailsPanel.getJndiName().trim().length() == 0) {
      messageDialog.setMessage(MSGS.invalidConnectionName());
      messageDialog.center();
    } else if (dataSourceDetailsPanel.getUrl().trim().length() == 0) { 
      messageDialog.setMessage(MSGS.missingDbUrl());
      messageDialog.center();
    } else if (dataSourceDetailsPanel.getDriverClass().trim().length() == 0) { 
      messageDialog.setMessage(MSGS.missingDbDriver());
      messageDialog.center();
    } else if (dataSourceDetailsPanel.getUserName().trim().length() == 0) { 
      messageDialog.setMessage(MSGS.missingDbUserName());
      messageDialog.center();
    } else {
      final PentahoDataSource dataSource = dataSourceDetailsPanel.getDataSource();
      final int index = dataSourcesList.getSelectedIndex();
      
      ((Button)sender).setEnabled( false );
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          messageDialog.setText(MSGS.updateDataSource());
          messageDialog.setMessage("Successfully updated the DataSource");
          messageDialog.center();
          dataSourcesList.setDataSource(index, dataSource);
          ((Button)sender).setEnabled( true );
          
        }

        public void onFailure(Throwable caught) {
          messageDialog.setText(MSGS.errorUpdatingDataSource());
          messageDialog.setMessage(caught.getMessage());
          messageDialog.center();
          ((Button)sender).setEnabled( true );
        }
      };
      PacServiceFactory.getPacService().updateDataSource(dataSource, callback);
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

	
	public boolean validate() {return true;}

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
}