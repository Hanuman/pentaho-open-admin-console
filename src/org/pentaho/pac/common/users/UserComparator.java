package org.pentaho.pac.common.users;

import java.util.Comparator;

public class UserComparator implements Comparator {

  public int compare(Object o1, Object o2) {
    ProxyPentahoUser user1 = (ProxyPentahoUser)o1;
    ProxyPentahoUser user2 = (ProxyPentahoUser)o2;
    
    String userName1 = user1.getName().toLowerCase();
    String userName2 = user2.getName().toLowerCase();
    
    int result = userName1.compareTo(userName2);
    if (result == 0) {
      result = user1.getName().compareTo(user2.getName());
    }
    
    return result;
  }

}
