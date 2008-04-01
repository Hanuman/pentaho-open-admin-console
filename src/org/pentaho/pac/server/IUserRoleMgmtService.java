package org.pentaho.pac.server;

import java.util.List;

/*package private */ interface IUserRoleMgmtService {

  public void createRole(IPentahoRole newRole) throws DuplicateRoleException, DAOException, UserRoleSecurityException;

  public void createUser(IPentahoUser newUser) throws DuplicateUserException, DAOException, UserRoleSecurityException;

  public void deleteRole(String roleName) throws NonExistingRoleException, DAOException, UserRoleSecurityException;
  
  public void deleteRole(IPentahoRole role) throws NonExistingRoleException, DAOException, UserRoleSecurityException;
  
  public void deleteUser(String userName) throws NonExistingUserException, DAOException, UserRoleSecurityException;

  public void deleteUser(IPentahoUser role) throws NonExistingUserException, DAOException, UserRoleSecurityException;

  public IPentahoRole getRole(String name) throws DAOException;

  public List<IPentahoRole> getRoles() throws DAOException;

  public IPentahoUser getUser(String name) throws DAOException;

  public List<IPentahoUser> getUsers() throws DAOException;

  public void updateRole(IPentahoRole role) throws NonExistingRoleException, DAOException, UserRoleSecurityException;

  public void updateUser(IPentahoUser user) throws NonExistingUserException, DAOException, UserRoleSecurityException;
  
  public void beginTransaction() throws DAOException;
    
  public void commitTransaction() throws DAOException;
  
  public void rollbackTransaction() throws DAOException;
  
  public void closeSession();
}
