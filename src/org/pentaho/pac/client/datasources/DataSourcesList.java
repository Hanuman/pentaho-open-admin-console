package org.pentaho.pac.client.datasources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class DataSourcesList extends ListBox {
  ArrayList dataSources = new ArrayList();
  
  public DataSourcesList() {
    super(true);
  }

  public SimpleDataSource[] getDataSources() {
    return (SimpleDataSource[])dataSources.toArray(new ProxyPentahoUser[0]);
  }

  public void setDataSources(IDataSource[] dataSources) {
    
    this.dataSources.clear();
    this.dataSources.addAll(Arrays.asList(dataSources));
    clear();
    for (int i = 0; i < dataSources.length; i++) {
      addItem(dataSources[i].getJndiName());
    }
  }
  
  public void setDataSource(int index, SimpleDataSource dataSource) {
    dataSources.set(index, dataSource);
    setItemText(index, dataSource.getJndiName());
  }
  
  public SimpleDataSource[] getSelectedDataSources() {
    ArrayList selectedDataSources = new ArrayList();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        selectedDataSources.add(dataSources.get(i));
      }
    }
    return (SimpleDataSource[])selectedDataSources.toArray(new SimpleDataSource[0]);
  }
  
  public void setSelectedDataSource(SimpleDataSource dataSource) {
    setSelectedDataSources(new SimpleDataSource[]{dataSource});
  }
  
  public void setSelectedDataSources(SimpleDataSource[] dataSources) {
    ArrayList dataSourceNames = new ArrayList();
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
  
  public void removeDataSources(SimpleDataSource[] dataSourcesToRemove) {
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
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        setDataSources((IDataSource[])result);
      }

      public void onFailure(Throwable caught) {
        int x = 1;
      }
    };
    
    PacServiceFactory.getPacService().getDataSources(callback);
  }
  
  public boolean addDataSource(SimpleDataSource dataSource) {
    boolean result = false;
    boolean dataSourceNameExists = false;
    for (Iterator iter = dataSources.iterator(); iter.hasNext() && !dataSourceNameExists;) {
      SimpleDataSource tmpDataSource = (SimpleDataSource)iter.next();
      dataSourceNameExists = tmpDataSource.getJndiName().equals(dataSource.getJndiName());      
    }
    if (!dataSourceNameExists) {
      dataSources.add(dataSource);
      addItem(dataSource.getJndiName());
      result = true;
    }
    return result;
  }
}
