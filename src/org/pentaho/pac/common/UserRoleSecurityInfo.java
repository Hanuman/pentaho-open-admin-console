/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

public class UserRoleSecurityInfo implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 420L;

  List<ProxyPentahoUser> users = new ArrayList<ProxyPentahoUser>();
  
  List<ProxyPentahoRole> roles = new ArrayList<ProxyPentahoRole>();
  
  List<UserToRoleAssignment> assignments = new ArrayList<UserToRoleAssignment>();
  
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
