package org.pentaho.pac.server;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pentaho.pac.common.users.NonExistingUserException;

public interface IPentahoRole {

  public String getName();
  public String getDescription();
  public void setDescription(String description);
  public Set<IPentahoUser> getUsers();
  public void addUser(String userName) throws NonExistingUserException;
  public void addUser(IPentahoUser user) throws NonExistingUserException;
  public void removeUser(String userName);
  public void removeUser(IPentahoUser user);
  public void setUsers(Collection<IPentahoUser> users) throws NonExistingUserException;
  public void setUsers(List<String> userNames) throws NonExistingUserException;
}
