package org.pentaho.pac.client.common.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class ObjectListBox extends ListBox {
  List objects = new ArrayList();
  IListBoxFilter listBoxFilter;
  
  public ObjectListBox(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  protected List getObjects() {
    return objects;
  }

  protected void setObjects(List objects) {
    
    this.objects.clear();
    this.objects.addAll(objects);
    clear();
    applyFilter();
  }

  public void addObject(Object object) {
    int index = objects.indexOf(object);
    if (index >= 0) {
      objects.set(index, object);
    } else {
      objects.add(object);
      if (doesFilterMatch(object)) {
        addItem(getObjectText(object));
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
    this.listBoxFilter = listBoxFilter;
    applyFilter();
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
    return (listBoxFilter == null) || (listBoxFilter.accepts(filterTarget));
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
