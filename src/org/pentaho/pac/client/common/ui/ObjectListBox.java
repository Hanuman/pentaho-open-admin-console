package org.pentaho.pac.client.common.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class ObjectListBox extends ListBox {
  List objects = new ArrayList();
  IListBoxFilter filter;
  Comparator comparator;
  
  public ObjectListBox(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  protected List getObjects() {
    return objects;
  }

  protected void setObjects(List objects) {
    if (comparator != null) {
      Collections.sort(objects, comparator);
    }
    this.objects.clear();
    this.objects.addAll(objects);
    clear();
    applyFilter();
  }

  public void setComparator(Comparator comparator) {
    this.comparator = comparator;
    if (comparator != null) {
      setObjects(objects);
    }
  }
  
  public Comparator getComparator() {
    return comparator;
  }
  
  public void addObject(Object object) {
    if ((comparator == null) || (objects.size() == 0)) {
      objects.add(object);
      applyFilter();
    } else {
      int index = objects.indexOf(object);
      if (index >= 0) {
        objects.set(index, object);
      } else {
        index = 0;
        int comparison = 0;
        for (Iterator iterator = objects.iterator(); iterator.hasNext();) {
          comparison = comparator.compare(iterator.next(), object);
          if (comparison < 0) {
            index++;
          } else {
            break;
          }
        }
        if (comparison == 0) {
          objects.set(index, object);
        } else if (comparison > 0) {
          objects.add(index, object);
          applyFilter();
        } else {
          objects.add(object);
          applyFilter();
        }
      }
    }
  }
  
  public List getSelectedObjects() {
    List selectedObjects = new ArrayList();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        String objectLabel = getItemText( i );
        Object o = getObject( objectLabel );
        selectedObjects.add( o );
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
    List objectLabels = new ArrayList();
    for (Iterator objIt = objects.iterator(); objIt.hasNext();) {
      objectLabels.add(getObjectText(objIt.next()));
    }
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      setItemSelected(i, objectLabels.contains(getItemText(i)));
    }
  }
  
  public void removeSelectedObjects() {
    int numObjectsDeleted = 0;
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        Object object = getObject(getItemText(i));
        if (object != null) {
          objects.remove(i);
        }
        removeItem(i);
        numObjectsDeleted++;
        selectedIndex = i;
      }
    }
    if (numObjectsDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void removeObjects(List objectsToRemove) {
    int numObjectsDeleted = 0;
    int selectedIndex = -1;
    objects.removeAll(objectsToRemove);
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        selectedIndex = i;
      }
      for (Iterator objIt = objectsToRemove.iterator(); objIt.hasNext();) {
        if (getItemText(i).equals(getObjectText(objIt.next()))) {
          removeItem(i);
          numObjectsDeleted++;
          break;
        }
      }
    }
    if (numObjectsDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void setFilter(IListBoxFilter listBoxFilter) {
    this.filter = listBoxFilter;
    applyFilter();
  }
  
  public IListBoxFilter getFilter() {
    return filter;
  }

  private void applyFilter() {
    List selectedObjects = getSelectedObjects();
    clear();
    Iterator objectIt = objects.iterator();
    while ( objectIt.hasNext() )
    {
      Object object = objectIt.next();
      if ( doesFilterMatch( object ) ) {
        addItem(getObjectText(object));
      }
    }
    setSelectedObjects(selectedObjects);
  }
  
  private boolean doesFilterMatch( Object filterTarget )
  {
    return (filter == null) || (filter.accepts(filterTarget));
  }

  protected Object getObject(String text)
  {
    for ( int ii=0; ii<objects.size(); ++ii )
    {
      Object o = objects.get( ii );
      if ( getObjectText(o).equals(text) ) {
        return o;
      }
    }
    return null;
  }
  
  protected String getObjectText(Object object) {
    return object.toString();
  }

}
