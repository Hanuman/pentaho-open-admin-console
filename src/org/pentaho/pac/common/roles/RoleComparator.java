package org.pentaho.pac.common.roles;

import java.util.Comparator;

public class RoleComparator implements Comparator<ProxyPentahoRole> {

  public int compare(ProxyPentahoRole role1, ProxyPentahoRole role2) {
    
    String userName1 = role1.getName().toLowerCase();
    String userName2 = role2.getName().toLowerCase();
    
    int result = userName1.compareTo(userName2);
    if (result == 0) {
      result = role1.getName().compareTo(role2.getName());
    }
    
    return result;
  }

}
