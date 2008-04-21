package org.pentaho.pac.client.users;

import java.util.Arrays;

import org.pentaho.pac.client.common.ui.ObjectListBox;
import org.pentaho.pac.common.users.ProxyPentahoUser;
import org.pentaho.pac.common.users.UserComparator;

public class UsersList extends ObjectListBox {
  
  public UsersList(boolean isMultiSelect) {
    super(isMultiSelect);
    setComparator(new UserComparator());
  }

  public ProxyPentahoUser[] getUsers() {
    return (ProxyPentahoUser[])getObjects().toArray(new ProxyPentahoUser[0]);
  }

  public void setUsers(ProxyPentahoUser[] users) {
    setObjects(Arrays.asList(users));
  }

  public void addUser(ProxyPentahoUser user) {
    addObject(user);
  }
  
  public ProxyPentahoUser[] getSelectedUsers() {
    return (ProxyPentahoUser[])getSelectedObjects().toArray(new ProxyPentahoUser[0]);
  }
  
  public void setSelectedUser(ProxyPentahoUser user) {
    setSelectedObject(user);
  }
  
  public void setSelectedUsers(ProxyPentahoUser[] users) {
    setSelectedObjects(Arrays.asList(users));
  }
  
  public void removeSelectedUsers() {
    removeSelectedObjects();
  }
  
  public void removeUsers(ProxyPentahoUser[] usersToRemove) {
    removeObjects(Arrays.asList(usersToRemove));
  }
  
  protected String getObjectText(Object object) {
    return object instanceof ProxyPentahoUser ? ((ProxyPentahoUser)object).getName() : super.getObjectText(object);
  }
}
