package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.PacServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class DataSourcesPanel extends DockPanel implements ClickListener, ChangeListener, PopupListener {

  DataSourcesList dataSourcesList = new DataSourcesList();
  SimpleDataSource[] dataSources = null;
  DataSourceDetailsPanel dataSourceDetailsPanel = new DataSourceDetailsPanel();
  Button updateDataSourceBtn = new Button("Update");
  Button addDataSourceBtn = new Button("+");
  Button deleteDataSourceBtn = new Button("-");
  NewDataSourceDialogBox newDataSourceDialogBox = new NewDataSourceDialogBox();
  
	public DataSourcesPanel() {
	  DockPanel dataSourcesListPanel = buildDataSourcesListPanel();
	  
	  DockPanel dataSourceDetailsDockPanel = buildDataSourceDetailsDockPanel();
    add(dataSourcesListPanel, DockPanel.WEST);
    add(dataSourceDetailsDockPanel, DockPanel.CENTER);
    
    setSpacing(10);
    
    setCellWidth(dataSourcesListPanel, "30%");
    setCellWidth(dataSourceDetailsDockPanel, "70%");
    setCellHeight(dataSourcesListPanel, "100%");
    setCellHeight(dataSourceDetailsDockPanel, "100%");
    dataSourcesListPanel.setWidth("100%");
    dataSourcesListPanel.setHeight("100%");
    dataSourceDetailsDockPanel.setWidth("100%");
    dataSourceDetailsDockPanel.setHeight("100%");
    
    dataSourceDetailsPanel.setEnabled(false);
    updateDataSourceBtn.setEnabled(false);
    
    newDataSourceDialogBox.addPopupListener(this);
 	}

	public DockPanel buildDataSourceDetailsDockPanel() {
	  DockPanel dockPanel = new DockPanel();
	  dockPanel.add(dataSourceDetailsPanel, DockPanel.CENTER);
	  dockPanel.add(updateDataSourceBtn, DockPanel.SOUTH);
	  dockPanel.setCellHeight(dataSourceDetailsPanel, "100%");
	  dockPanel.setCellWidth(dataSourceDetailsPanel, "100%");
	  dataSourceDetailsPanel.setWidth("100%");
	  dataSourceDetailsPanel.setHeight("100%");
	  dockPanel.setCellHorizontalAlignment(updateDataSourceBtn, HasHorizontalAlignment.ALIGN_RIGHT);
    updateDataSourceBtn.addClickListener(this);
	  return dockPanel;
	}
	
	public DockPanel buildDataSourcesListPanel() {
	  DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteDataSourceBtn, DockPanel.EAST);
	  headerDockPanel.add(addDataSourceBtn, DockPanel.EAST);
    Label label = new Label("Data Sourcess");
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%");
    DockPanel dataSourceListPanel = new DockPanel();
    dataSourceListPanel.add(headerDockPanel, DockPanel.NORTH);
    dataSourceListPanel.add(dataSourcesList, DockPanel.CENTER);
    dataSourceListPanel.setCellHeight(dataSourcesList, "100%");
    dataSourceListPanel.setCellWidth(dataSourcesList, "100%");
    dataSourceListPanel.setHeight("100%");
    dataSourceListPanel.setWidth("100%");
    dataSourcesList.setHeight("100%");
    dataSourcesList.setWidth("100%");
    addDataSourceBtn.setWidth("20px");
    deleteDataSourceBtn.setWidth("20px");
    addDataSourceBtn.setHeight("20px");
    deleteDataSourceBtn.setHeight("20px");
    deleteDataSourceBtn.setEnabled(false);
    dataSourcesList.addChangeListener(this);
    addDataSourceBtn.addClickListener(this);
    deleteDataSourceBtn.addClickListener(this);
    return dataSourceListPanel;
	}
	
	
	
	public void onClick(Widget sender) {
	  if (sender == updateDataSourceBtn) {
	    updateDataSourceDetails();
	  } else if (sender == deleteDataSourceBtn) {
	    deleteSelectedDataSources();
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
	        int x = 1;
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
    deleteDataSourceBtn.setEnabled(selectedDataSources.length > 0);
	}
	
	private void updateDataSourceDetails() {
    final SimpleDataSource dataSource = dataSourceDetailsPanel.getDataSource();
    final int index = dataSourcesList.getSelectedIndex();
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        dataSourcesList.setDataSource(index, dataSource);
      }

      public void onFailure(Throwable caught) {
        int x = 1;
      }
    };
    PacServiceFactory.getPacService().updateDataSource(dataSource, callback);
	}
	
	public boolean validate() {return true;}

	public void onChange(Widget sender) {
	  dataSourceSelectionChanged();
	}
	
	public void refresh() {
	  dataSourcesList.refresh();
	}
	
  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if (newDataSourceDialogBox.isDataSourceCreated()) {
      SimpleDataSource dataSource = newDataSourceDialogBox.getDataSource();
      if (dataSourcesList.addDataSource(dataSource)) {
        dataSourcesList.setSelectedDataSource(dataSource);
        dataSourceSelectionChanged();
      }
    }
  }
  

}