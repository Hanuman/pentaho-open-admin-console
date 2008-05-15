package org.pentaho.pac.client.users;

import org.pentaho.pac.client.common.ui.GenericObjectListBox;
import org.pentaho.pac.common.users.ProxyPentahoUser;
import org.pentaho.pac.common.users.UserComparator;

public class UsersList extends GenericObjectListBox<ProxyPentahoUser> {
  
  public UsersList(boolean isMultiSelect) {
    super(isMultiSelect);
    setComparator(new UserComparator());
  }

  protected String getObjectText(ProxyPentahoUser object) {
    return object.getName();
  }
}
