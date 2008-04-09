package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class UsersList extends ListBox {
  List users = new ArrayList();
  String userNameFilter;
  
  public UsersList(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  public ProxyPentahoUser[] getUsers() {
    return (ProxyPentahoUser[])users.toArray(new ProxyPentahoUser[0]);
  }

  public void setUsers(ProxyPentahoUser[] users) {
    
    this.users.clear();
    this.users.addAll(Arrays.asList(users));
    clear();
    applyFilter();
  }

  public void addUser(ProxyPentahoUser user) {
    int index = users.indexOf(user);
    if (index >= 0) {
      users.set(index, user);
    } else {
      users.add(user);
      if (doesFilterMatch(user.getName())) {
        addItem(user.getName());
      }
    }
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
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        ProxyPentahoUser  proxyPentahoUser = getUserByName(getItemText(i));
        if (proxyPentahoUser != null) {
          users.remove(i);
        }
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
    users.removeAll(Arrays.asList(usersToRemove));
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        selectedIndex = i;
      }
      for (int j = 0; j < usersToRemove.length; j++) {
        if (getItemText(i).equals(usersToRemove[j].getName())) {
          removeItem(i);
          numUsersDeleted++;
          break;
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
  
  public void setUserNameFilter(String userNameFilter) {
    if (userNameFilter != null) {
      this.userNameFilter = userNameFilter.trim();
    } else {
      this.userNameFilter = userNameFilter;
    }
    applyFilter();
  }
  
  private void applyFilter() {
    ProxyPentahoUser[] selectedUsers = getSelectedUsers();
    clear();
    Iterator userIt = users.iterator();
    while ( userIt.hasNext() )
    {
      ProxyPentahoUser user = (ProxyPentahoUser)userIt.next();
      boolean doesMatch = doesFilterMatch( user.getName() );
      if ( doesMatch ) {
        addItem( user.getName() );
      }
    }
    setSelectedUsers(selectedUsers);
  }
  
  private boolean doesFilterMatch( String filterTarget )
  {
    boolean result = ((userNameFilter == null) || (userNameFilter.length() == 0));
    if (!result) {
      int filterLen = userNameFilter.length();
      result = ( filterLen <= filterTarget.length() )
        && userNameFilter.toLowerCase().substring( 0, filterLen )
          .equals( filterTarget.toLowerCase().substring( 0, filterLen ) ); 
    }
    return result;
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
  
}
