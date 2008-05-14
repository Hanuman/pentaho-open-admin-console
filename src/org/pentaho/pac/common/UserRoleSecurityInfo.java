package org.pentaho.pac.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

public class UserRoleSecurityInfo implements Serializable {
  
  ArrayList<ProxyPentahoUser> users = new ArrayList<ProxyPentahoUser>();
  
  ArrayList<ProxyPentahoRole> roles = new ArrayList<ProxyPentahoRole>();
  
  ArrayList<UserToRoleAssignment> assignments = new ArrayList<UserToRoleAssignment>();
  
  public UserRoleSecurityInfo() {
    
  }
  
  public List<ProxyPentahoUser> getUsers() {
    return users;
  }
  
  public List<ProxyPentahoRole> getRoles() {
    return roles;
  }
  
  public List<UserToRoleAssignment> getAssignments() {
    return assignments;
  }
}
