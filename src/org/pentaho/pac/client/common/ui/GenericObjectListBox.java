package org.pentaho.pac.client.common.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class GenericObjectListBox<T> extends ListBox {
  List<T> objects = new ArrayList<T>();
  List<T> sortedAndFilteredObjects = new ArrayList<T>();
  IListBoxFilter filter;
  Comparator<T> comparator;
  
  public GenericObjectListBox(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  public List<T> getObjects() {
    return objects;
  }

  public List<T> getSortedAndFilteredObjects() {
    return sortedAndFilteredObjects;
  }
  
  public void setObjects(List<T> objects) {
    this.objects.clear();
    this.objects.addAll(objects);
    sortAndFilter();
  }

  public void setComparator(Comparator<T> comparator) {
    this.comparator = comparator;
    sortAndFilter();
  }
  
  public Comparator<T> getComparator() {
    return comparator;
  }
  
  public void addObject(T object) {
    objects.add(object);
    sortAndFilter();
  }
  
  public List<T> getSelectedObjects() {
    List<T> selectedObjects = new ArrayList<T>();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        selectedObjects.add(sortedAndFilteredObjects.get(i));
      }
    }
    return selectedObjects;
  }
  
  public void setSelectedObject(T object) {
    List<T> objects = new ArrayList<T>();
    objects.add(object);
    setSelectedObjects(objects);
  }
  
  public void setSelectedObjects(List<T> objects) {
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      setItemSelected(i, objects.contains(sortedAndFilteredObjects.get(i)));
    }
  }
  
  public void removeSelectedObjects() {
    removeObjects(getSelectedObjects());
  }
  
  public void removeObjects(List<T> objectsToRemove) {
    List<T> selectedObjects = getSelectedObjects();
    int selectedIndex = -1;
    if (selectedObjects.size() == 1) {
      selectedIndex = sortedAndFilteredObjects.indexOf(selectedObjects.get(0));
    }
    
    for (T selectedObject : selectedObjects) {
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
    List<T> selectedObjects = getSelectedObjects();
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
    for (T object : sortedAndFilteredObjects) {
      addItem(getObjectText(object));
    }
    setSelectedObjects(selectedObjects);
  }
  
  private boolean doesFilterMatch(T filterTarget )
  {
    return (filter == null) || (filter.accepts(filterTarget));
  }

  protected String getObjectText(T object) {
    return object.toString();
  }

}
