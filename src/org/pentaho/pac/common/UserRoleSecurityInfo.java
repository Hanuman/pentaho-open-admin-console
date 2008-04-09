package org.pentaho.pac.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserRoleSecurityInfo implements Serializable {
  
  /**
   * @gwt.typeArgs <org.pentaho.pac.common.users.ProxyPentahoUser>
   */
  ArrayList users = new ArrayList();
  
  /**
   * @gwt.typeArgs <org.pentaho.pac.common.roles.ProxyPentahoRole>
   */
  ArrayList roles = new ArrayList();
  
  /**
   * @gwt.typeArgs <org.pentaho.pac.common.UserToRoleAssignment>
   */
  ArrayList assignments = new ArrayList();
  
  public UserRoleSecurityInfo() {
    
  }
  
  public List getUsers() {
    return users;
  }
  
  public List getRoles() {
    return roles;
  }
  
  public List getAssignments() {
    return assignments;
  }
}
