package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.common.datasources.SimpleDataSource;

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

  MessageDialog messageDialog = new MessageDialog("", new int[]{MessageDialog.OK_BTN}); //$NON-NLS-1$
  DataSourcesList dataSourcesList = new DataSourcesList();
  SimpleDataSource[] dataSources = null;
  DataSourceDetailsPanel dataSourceDetailsPanel = new DataSourceDetailsPanel();
  Button updateDataSourceBtn = new Button(PentahoAdminConsole.getLocalizedMessages().update());
  Button testDataSourceBtn = new Button(PentahoAdminConsole.getLocalizedMessages().test());
  Button addDataSourceBtn = new Button("+"); //$NON-NLS-1$
  Button deleteDataSourceBtn = new Button("-"); //$NON-NLS-1$
  NewDataSourceDialogBox newDataSourceDialogBox = new NewDataSourceDialogBox();
  MessageDialog confirmDataSourceDeleteDialog = new MessageDialog(PentahoAdminConsole.getLocalizedMessages().deleteDataSources(), PentahoAdminConsole.getLocalizedMessages().confirmDataSourceDeletionMsg(), new int[] {MessageDialog.OK_BTN, MessageDialog.CANCEL_BTN});
  
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
    confirmDataSourceDeleteDialog.addPopupListener(this);
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
    Label label = new Label(PentahoAdminConsole.getLocalizedMessages().dataSources());
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
	  final SimpleDataSource[] selectedDataSources = dataSourcesList.getSelectedDataSources();
	  if (selectedDataSources.length > 0) {
	    final int index = dataSourcesList.getSelectedIndex();
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        dataSourceSelectionChanged();
	      }

	      public void onFailure(Throwable caught) {
          messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().errorDeletingDataSource());
          messageDialog.setMessage(caught.getMessage());
          messageDialog.center();
	      }
	    };
	    PacServiceFactory.getPacService().deleteDataSources(selectedDataSources, callback);
	  }
	}
	
	private void dataSourceSelectionChanged() {
    SimpleDataSource[] selectedDataSources = dataSourcesList.getSelectedDataSources();
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
    messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().updateDataSource());
    if (dataSourceDetailsPanel.getJndiName().trim().length() == 0) {
      messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().invalidConnectionName());
      messageDialog.center();
    } else if (dataSourceDetailsPanel.getUrl().trim().length() == 0) { 
      messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().missingDbUrl());
      messageDialog.center();
    } else if (dataSourceDetailsPanel.getDriverClass().trim().length() == 0) { 
      messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().missingDbDriver());
      messageDialog.center();
    } else if (dataSourceDetailsPanel.getUserName().trim().length() == 0) { 
      messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().missingDbUserName());
      messageDialog.center();
    } else {
      final SimpleDataSource dataSource = dataSourceDetailsPanel.getDataSource();
      final int index = dataSourcesList.getSelectedIndex();
      
      ((Button)sender).setEnabled( false );
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          dataSourcesList.setDataSource(index, dataSource);
          ((Button)sender).setEnabled( true );
        }

        public void onFailure(Throwable caught) {
          messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().errorUpdatingDataSource());
          messageDialog.setMessage(caught.getMessage());
          messageDialog.center();
          ((Button)sender).setEnabled( true );
        }
      };
      PacServiceFactory.getPacService().updateDataSource(dataSource, callback);
    }
	}
	
	 private void testDataSourceConnection() {
	    final SimpleDataSource dataSource = dataSourceDetailsPanel.getDataSource();
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().testConnection());
	        messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().connectionTestSuccessful());
	        messageDialog.center();
	      }

	      public void onFailure(Throwable caught) {
	        messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().testConnection());
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
      SimpleDataSource dataSource = newDataSourceDialogBox.getDataSource();
      if (dataSourcesList.addDataSource(dataSource)) {
        dataSourcesList.setSelectedDataSource(dataSource);
        dataSourceSelectionChanged();
      }
    } else if ((sender == confirmDataSourceDeleteDialog) && (confirmDataSourceDeleteDialog.getButtonPressed() == MessageDialog.OK_BTN)) {
      deleteSelectedDataSources();
    }      

  }
  
  public boolean isInitialized() {
    return dataSourcesList.isInitialized();
  }
  
  public void clearDataSourcesCache() {
    dataSourcesList.clearDataSourcesCache();
  }
}