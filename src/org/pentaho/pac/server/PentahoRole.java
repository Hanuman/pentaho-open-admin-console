package org.pentaho.pac.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.DAOFactory;

public class PentahoRole implements IPentahoRole {

  String name;
  String description;
  Set<IPentahoUser> assignedUsers = new HashSet<IPentahoUser>();

  public PentahoRole() {
  }
  
  public PentahoRole(String roleName) {
    this.name = roleName;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  protected Set<IPentahoUser> getAssignedUsers() {
    return assignedUsers;
  }

  protected void setAssignedUsers(Set<IPentahoUser> users) {
    this.assignedUsers = users;
  }

  public Set<IPentahoUser> getUsers() {
    return Collections.unmodifiableSet(getAssignedUsers());
  }
  
  public void addUser(String userName) throws NonExistingUserException {
    IPentahoUser user = null;
    try {
      user = DAOFactory.getUserRoleDAO().getUser(userName);
    } catch (DAOException e) {
      throw new RuntimeException(e);
    }
    if (user != null) {
      getAssignedUsers().add(user);
      ((PentahoUser)user).getAssignedRoles().add(this);
    } else {
      throw new NonExistingUserException( userName );
    }
  }
  
  public void addUser(IPentahoUser user) throws NonExistingUserException {
    addUser(user.getName());
  }
  
  public void removeUser(String userName) {
    IPentahoUser user = null;
    try {
      user = DAOFactory.getUserRoleDAO().getUser(userName);
      if (user != null) {
        ((PentahoUser)user).getAssignedRoles().remove(this);
      } else {
        user = new PentahoUser(userName);
      }
      getAssignedUsers().remove(user);
    } catch (DAOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void removeUser(IPentahoUser user) {
    removeUser(user.getName());
  }
  
  public void setUsers(List<String> userNames) throws NonExistingUserException {
    ArrayList<IPentahoUser> persistedUsers = new ArrayList<IPentahoUser>();
    for (String userName : userNames) {
      IPentahoUser persistedUser = null;
      try {
        persistedUser = DAOFactory.getUserRoleDAO().getUser(userName);
      } catch (DAOException e) {
        throw new RuntimeException(e);
      }
      if (persistedUser == null) {
        throw new NonExistingUserException( userName );
      }
      persistedUsers.add(persistedUser);
    }
    
    for (IPentahoUser user : getAssignedUsers()) {
      ((PentahoUser)user).getAssignedRoles().remove(this);
    } 
    getAssignedUsers().clear();
    
    for (IPentahoUser user : persistedUsers) {
      ((PentahoUser)user).getAssignedRoles().add(this);
    } 
    getAssignedUsers().addAll(persistedUsers);    
  }
  
  public void setUsers(Collection<IPentahoUser> users) throws NonExistingUserException {
    ArrayList<String> userNames = new ArrayList<String>();
    for (IPentahoUser user : users) {
      userNames.add(user.getName());
    }
    setUsers( userNames );
  }
  
  public boolean equals(Object o) {
    return ((o instanceof PentahoRole) ? name.equals(((PentahoRole) o).getName()) : false);
  }
  
  public int hashCode() {
    return name.hashCode();
  }
}
