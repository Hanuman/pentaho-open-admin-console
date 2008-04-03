package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class UsersList extends ListBox {
  MessageDialog messageDialog = new MessageDialog("Users", "", new int[]{MessageDialog.OK_BTN});
  List users = new ArrayList();
  boolean isInitialized = false;
  
  public UsersList() {
    super(true);
  }

  public ProxyPentahoUser[] getUsers() {
    return (ProxyPentahoUser[])users.toArray(new ProxyPentahoUser[0]);
  }

  public void setUsers(ProxyPentahoUser[] users) {
    
    this.users.clear();
    this.users.addAll(Arrays.asList(users));
    clear();
    for (int i = 0; i < users.length; i++) {
      addItem(users[i].getName());
    }
    isInitialized = true;
  }

  public void setUser(int index, ProxyPentahoUser user) {
    users.set(index, user);
    setItemText(index, user.getName());
  }
  
  private Map getSelectedUsersAsMap() {
    Map m = new HashMap();
    ProxyPentahoUser[] users = getSelectedUsers();
    for ( int ii=0; ii<users.length; ++ii ) {
      ProxyPentahoUser u = users[ ii ];
      m.put(u.getName(), u );
    }
    return m;
  }
  
  private ProxyPentahoUser getUserByName( String name )
  {
    for ( int ii=0; ii<users.size(); ++ii )
    {
      ProxyPentahoUser u = (ProxyPentahoUser)users.get( ii );
      if ( u.getName().equals( name ) ) {
        return u;
      }
    }
    return null;
  }
  
  public ProxyPentahoUser[] getSelectedUsers() {
    List selectedUsers = new ArrayList();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        String userName = getItemText( i );
        ProxyPentahoUser u = getUserByName( userName );
        selectedUsers.add( u );
      }
    }
    return (ProxyPentahoUser[])selectedUsers.toArray(new ProxyPentahoUser[0]);
  }
  
  public void setSelectedUser(ProxyPentahoUser user) {
    setSelectedUsers(new ProxyPentahoUser[]{user});
  }
  
  public void setSelectedUsers(ProxyPentahoUser[] users) {
    List userNames = new ArrayList();
    for (int i = 0; i < users.length; i++) {
      userNames.add(users[i].getName());
    }
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      setItemSelected(i, userNames.contains(getItemText(i)));
    }
    
  }
  
  public void removeSelectedUsers() {
    int numUsersDeleted = 0;
    int closingSelection = -1;
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        users.remove(i);
        removeItem(i);
        numUsersDeleted++;
        selectedIndex = i;
      }
    }
    if (numUsersDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void removeUsers(ProxyPentahoUser[] usersToRemove) {
    int numUsersDeleted = 0;
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        selectedIndex = i;
      }
      for (int j = 0; j < usersToRemove.length; j++) {
        if (users.get(i).equals(usersToRemove[j])) {
          users.remove(i);
          removeItem(i);
          numUsersDeleted++;
        }
      }
    }
    if (numUsersDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void refresh() {
    setUsers(new ProxyPentahoUser[0]);
    isInitialized = false;
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        setUsers((ProxyPentahoUser[])result);
        isInitialized = true;
      }

      public void onFailure(Throwable caught) {
        messageDialog.setMessage("Unable to refresh users list: " + caught.getMessage());
        messageDialog.center();
      }
    };
    
    PacServiceFactory.getPacService().getUsers(callback);
  }
  
  public boolean addUser(ProxyPentahoUser user) {
    boolean result = false;
    if (!users.contains(user)) {
      users.add(user);
      addItem(user.getName());
      result = true;
    }
    return result;
  }
  
  public void filterList( String filter )
  {
    List newSelUsers = new ArrayList();
    Map previouslySelUsersMap = getSelectedUsersAsMap();
    this.clear();
    Iterator userIt = users.iterator();
    while ( userIt.hasNext() )
    {
      ProxyPentahoUser user = (ProxyPentahoUser)userIt.next();
      boolean doesMatch = doesFilterMatch( filter, user.getName() );
      if ( doesMatch ) {
        this.addItem( user.getName() );
        if( previouslySelUsersMap.containsKey( user.getName())) {
          newSelUsers.add( user );
        }
      }
    }
    setSelectedUsers( (ProxyPentahoUser[])newSelUsers.toArray( new ProxyPentahoUser[0] ) );
  }

  
  private boolean doesFilterMatch( String filterValue, String filterTarget )
  {
    int filterLen = filterValue.length();
    return ( filterLen <= filterTarget.length() )
      && filterValue.toLowerCase().substring( 0, filterLen )
        .equals( filterTarget.toLowerCase().substring( 0, filterLen ) ); 
  }

  public boolean isInitialized() {
    return isInitialized;
  }
  
  public void clearUsersCache() {
    setUsers(new ProxyPentahoUser[0]);
    isInitialized = false;
  }
}
