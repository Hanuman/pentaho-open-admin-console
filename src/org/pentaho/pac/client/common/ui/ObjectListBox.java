package org.pentaho.pac.client.common.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class ObjectListBox extends ListBox {
  List objects = new ArrayList();
  List sortedAndFilteredObjects = new ArrayList();
  IListBoxFilter filter;
  Comparator comparator;
  
  public ObjectListBox(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  protected List getObjects() {
    return objects;
  }

  protected void setObjects(List objects) {
    this.objects.clear();
    this.objects.addAll(objects);
    sortAndFilter();
  }

  public void setComparator(Comparator comparator) {
    this.comparator = comparator;
    sortAndFilter();
  }
  
  public Comparator getComparator() {
    return comparator;
  }
  
  public void addObject(Object object) {
    objects.add(object);
    sortAndFilter();
  }
  
  public List getSelectedObjects() {
    List selectedObjects = new ArrayList();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        selectedObjects.add(sortedAndFilteredObjects.get(i));
      }
    }
    return selectedObjects;
  }
  
  public void setSelectedObject(Object object) {
    List objects = new ArrayList();
    objects.add(object);
    setSelectedObjects(objects);
  }
  
  public void setSelectedObjects(List objects) {
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      setItemSelected(i, objects.contains(sortedAndFilteredObjects.get(i)));
    }
  }
  
  public void removeSelectedObjects() {
    removeObjects(getSelectedObjects());
  }
  
  public void removeObjects(List objectsToRemove) {
    List selectedObjects = getSelectedObjects();
    int selectedIndex = -1;
    if (selectedObjects.size() == 1) {
      selectedIndex = sortedAndFilteredObjects.indexOf(selectedObjects.get(0));
    }
    
    for (Object selectedObject : selectedObjects) {
      int index = sortedAndFilteredObjects.indexOf(selectedObject);
      sortedAndFilteredObjects.remove(selectedObject);
      objects.remove(selectedObject);
      removeItem(index);
    }
    
    if (selectedIndex >= getItemCount()) {
      selectedIndex = getItemCount() - 1;
    }
    if (selectedIndex >= 0) {
      setItemSelected(selectedIndex, true);
    }
  }
  
  public void setFilter(IListBoxFilter listBoxFilter) {
    this.filter = listBoxFilter;
    sortAndFilter();
  }
  
  public IListBoxFilter getFilter() {
    return filter;
  }


  private void sortAndFilter() {
    List selectedObjects = getSelectedObjects();
    clear();
    sortedAndFilteredObjects.clear();
    sortedAndFilteredObjects.addAll(objects);
    if (comparator != null) {
      Collections.sort(sortedAndFilteredObjects, comparator);
    }
    for (int i = sortedAndFilteredObjects.size() - 1; i >= 0; i--) {
      if (!doesFilterMatch(sortedAndFilteredObjects.get(i))) {
        sortedAndFilteredObjects.remove(i);
      }
    }
    for (Object object : sortedAndFilteredObjects) {
      addItem(getObjectText(object));
    }
    setSelectedObjects(selectedObjects);
  }
  
  private boolean doesFilterMatch( Object filterTarget )
  {
    return (filter == null) || (filter.accepts(filterTarget));
  }

  protected String getObjectText(Object object) {
    return object.toString();
  }

}
