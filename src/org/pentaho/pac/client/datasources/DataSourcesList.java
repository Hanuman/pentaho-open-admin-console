package org.pentaho.pac.client.datasources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class DataSourcesList extends ListBox {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  MessageDialog messageDialog = new MessageDialog(MSGS.dataSources(), "", new int[]{MessageDialog.OK_BTN}); //$NON-NLS-1$
  List dataSources = new ArrayList();
  boolean isInitialized = false;
  
  public DataSourcesList() {
    super(true);
  }

  public PentahoDataSource[] getDataSources() {
    return (PentahoDataSource[])dataSources.toArray(new PentahoDataSource[0]);
  }

  public void setDataSources(IPentahoDataSource[] dataSources) {
    
    this.dataSources.clear();
    this.dataSources.addAll(Arrays.asList(dataSources));
    clear();
    for (int i = 0; i < dataSources.length; i++) {
      addItem(dataSources[i].getJndiName());
    }
    isInitialized = true;
  }
  
  public void setDataSource(int index, PentahoDataSource dataSource) {
    dataSources.set(index, dataSource);
    setItemText(index, dataSource.getJndiName());
  }
  
  public PentahoDataSource[] getSelectedDataSources() {
    List selectedDataSources = new ArrayList();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        selectedDataSources.add(dataSources.get(i));
      }
    }
    return (PentahoDataSource[])selectedDataSources.toArray(new PentahoDataSource[0]);
  }
  
  public void setSelectedDataSource(PentahoDataSource dataSource) {
    setSelectedDataSources(new PentahoDataSource[]{dataSource});
  }
  
  public void setSelectedDataSources(PentahoDataSource[] dataSources) {
    List dataSourceNames = new ArrayList();
    for (int i = 0; i < dataSources.length; i++) {
      dataSourceNames.add(dataSources[i].getJndiName());
    }
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      setItemSelected(i, dataSourceNames.contains(getItemText(i)));
    }
    
  }
  
  public void removeSelectedDataSources() {
    int numDataSourcesDeleted = 0;
    int closingSelection = -1;
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        dataSources.remove(i);
        removeItem(i);
        numDataSourcesDeleted++;
        selectedIndex = i;
      }
    }
    if (numDataSourcesDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void removeDataSources(PentahoDataSource[] dataSourcesToRemove) {
    int numDataSourcesDeleted = 0;
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        selectedIndex = i;
      }
      for (int j = 0; j < dataSourcesToRemove.length; j++) {
        if (dataSources.get(i).equals(dataSourcesToRemove[j])) {
          dataSources.remove(i);
          removeItem(i);
          numDataSourcesDeleted++;
        }
      }
    }
    if (numDataSourcesDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void refresh() {
    setDataSources(new PentahoDataSource[0]);
    isInitialized = false;
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        setDataSources((IPentahoDataSource[])result);
        isInitialized = true;
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText(MSGS.errorLoadingDataSources());
        messageDialog.setMessage(MSGS.dataSourcesRefreshError(caught.getMessage()));
        messageDialog.center();
      }
    };
    
    PacServiceFactory.getPacService().getDataSources(callback);
  }
  
  public boolean addDataSource(PentahoDataSource dataSource) {
    boolean result = false;
    boolean dataSourceNameExists = false;
    for (Iterator iter = dataSources.iterator(); iter.hasNext() && !dataSourceNameExists;) {
      PentahoDataSource tmpDataSource = (PentahoDataSource)iter.next();
      dataSourceNameExists = tmpDataSource.getJndiName().equals(dataSource.getJndiName());      
    }
    if (!dataSourceNameExists) {
      dataSources.add(dataSource);
      addItem(dataSource.getJndiName());
      result = true;
    }
    return result;
  }
  
  public boolean isInitialized() {
    return isInitialized;
  }
  
  public void clearDataSourcesCache() {
    setDataSources(new PentahoDataSource[0]);
    isInitialized = false;
  }
}
