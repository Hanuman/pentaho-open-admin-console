package org.pentaho.pac.common.users;

import java.util.Comparator;

public class UserComparator implements Comparator<ProxyPentahoUser> {

  public int compare(ProxyPentahoUser user1, ProxyPentahoUser user2) {
    String userName1 = user1.getName().toLowerCase();
    String userName2 = user2.getName().toLowerCase();
    
    int result = userName1.compareTo(userName2);
    if (result == 0) {
      result = user1.getName().compareTo(user2.getName());
    }
    
    return result;
  }

}
