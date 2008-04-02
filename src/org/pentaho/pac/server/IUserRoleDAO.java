package org.pentaho.pac.server;

import java.util.List;

import org.pentaho.pac.client.users.DuplicateUserException;

/*package private*/ interface IUserRoleDAO extends IGenericDAO {

  public void createUser(IPentahoUser newUser) throws DuplicateUserException, DAOException;
  
  public void deleteUser(IPentahoUser user) throws NonExistingUserException, DAOException;

  public IPentahoUser getUser(String name) throws DAOException;

  public List<IPentahoUser> getUsers() throws DAOException;

  public void updateUser(IPentahoUser user) throws NonExistingUserException, DAOException;

  public void createRole(IPentahoRole newRole) throws DAOException;
  
  public void deleteRole(IPentahoRole role) throws NonExistingRoleException, DAOException;

  public IPentahoRole getRole(String name) throws DAOException;

  public List<IPentahoRole> getRoles() throws DAOException;

  public void updateRole(IPentahoRole role) throws NonExistingRoleException, DAOException;
}
