package org.pentaho.pac.server;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.DuplicateRoleException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;

/*package private */ interface IUserRoleMgmtService {

  public void createRole(IPentahoRole newRole) throws DuplicateRoleException, DAOException, PentahoSecurityException;

  public void createUser(IPentahoUser newUser) throws DuplicateUserException, DAOException, PentahoSecurityException;

  public void deleteRole(String roleName) throws NonExistingRoleException, DAOException, PentahoSecurityException;
  
  public void deleteRole(IPentahoRole role) throws NonExistingRoleException, DAOException, PentahoSecurityException;
  
  public void deleteUser(String userName) throws NonExistingUserException, DAOException, PentahoSecurityException;

  public void deleteUser(IPentahoUser role) throws NonExistingUserException, DAOException, PentahoSecurityException;

  public IPentahoRole getRole(String name) throws DAOException;

  public List<IPentahoRole> getRoles() throws DAOException;

  public IPentahoUser getUser(String name) throws DAOException;

  public List<IPentahoUser> getUsers() throws DAOException;

  public void updateRole(IPentahoRole role) throws NonExistingRoleException, DAOException, PentahoSecurityException;

  public void updateUser(IPentahoUser user) throws NonExistingUserException, DAOException, PentahoSecurityException;
  
  public void beginTransaction() throws DAOException;
    
  public void commitTransaction() throws DAOException;
  
  public void rollbackTransaction() throws DAOException;
  
  public void closeSession();
}
