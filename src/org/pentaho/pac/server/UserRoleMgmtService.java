package org.pentaho.pac.server;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.DuplicateRoleException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.DAOFactory;
import org.pentaho.platform.engine.security.userroledao.AlreadyExistsException;
import org.pentaho.platform.engine.security.userroledao.IPentahoRole;
import org.pentaho.platform.engine.security.userroledao.IPentahoUser;
import org.pentaho.platform.engine.security.userroledao.IUserRoleDao;
import org.pentaho.platform.engine.security.userroledao.NotFoundException;
import org.pentaho.platform.engine.security.userroledao.UncategorizedUserRoleDaoException;

/*package private */ class UserRoleMgmtService implements IUserRoleMgmtService {
  IUserRoleDao userRoleDAO = null;

  public UserRoleMgmtService() {
    userRoleDAO = DAOFactory.getUserRoleDAO();
  }
  
  public void createRole(IPentahoRole newRole) throws DuplicateRoleException, DAOException, PentahoSecurityException {
    if (hasCreateRolePerm(newRole)) {
      try {
        userRoleDAO.createRole(newRole);
      } catch (AlreadyExistsException e) {
        throw new DuplicateRoleException(e);
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(e);
      }
    } else {
      throw new PentahoSecurityException();
    }
  }

  public void createUser(IPentahoUser newUser) throws DuplicateUserException, DAOException, PentahoSecurityException {
    if (hasCreateUserPerm(newUser)) {
    try {
      userRoleDAO.createUser(newUser);
    } catch (AlreadyExistsException e) {
      throw new DuplicateUserException(e);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
    } else {
      throw new PentahoSecurityException();
    }
  }

  public void deleteRole(String roleName) throws NonExistingRoleException, DAOException, PentahoSecurityException {
    IPentahoRole role;
    try {
      role = userRoleDAO.getRole(roleName);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
    if (role != null) {
      deleteRole(role);
    } else {
      throw new NonExistingRoleException(roleName);
    }
  }
  
  public void deleteRole(IPentahoRole role) throws NonExistingRoleException, DAOException, PentahoSecurityException {
    if (hasDeleteRolePerm(role)) {
      try {
        userRoleDAO.deleteRole(role);
      } catch (NotFoundException e) {
        throw new NonExistingRoleException(e);
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(e);
      }
    } else {
      throw new PentahoSecurityException();
    }
  }

  public void deleteUser(String userName) throws NonExistingUserException, DAOException, PentahoSecurityException {
    IPentahoUser user;
    try {
      user = userRoleDAO.getUser(userName);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
    if (user != null) {
      deleteUser(user);
    } else {
      throw new NonExistingUserException( userName );
    }
  }
  
  public void deleteUser(IPentahoUser user) throws NonExistingUserException, DAOException, PentahoSecurityException {
    if (hasDeleteUserPerm(user)) {
      try {
        userRoleDAO.deleteUser(user);
      } catch (NotFoundException e) {
        throw new NonExistingUserException(e);
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(e);
      }
    } else {
      throw new PentahoSecurityException();
    }    
  }

  public IPentahoRole getRole(String name) throws DAOException {
    try {
      return userRoleDAO.getRole(name);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
  }

  public List<IPentahoRole> getRoles() throws DAOException {
    try {
      return userRoleDAO.getRoles();
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
  }

  public IPentahoUser getUser(String name) throws DAOException {
    try {
      return userRoleDAO.getUser(name);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
  }

  public List<IPentahoUser> getUsers() throws DAOException {
    try {
      return userRoleDAO.getUsers();
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(e);
    }
  }

  public void updateRole(IPentahoRole role) throws DAOException, PentahoSecurityException, NonExistingRoleException {
    if (hasUpdateRolePerm(role)) {
      try {
        userRoleDAO.updateRole(role);
      } catch (NotFoundException e) {
        throw new NonExistingRoleException(e);
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(e);
      }
    } else {
      throw new PentahoSecurityException();
    }    
  }

  public void updateUser(IPentahoUser user) throws DAOException, PentahoSecurityException, NonExistingUserException {
    if (hasUpdateUserPerm(user)) {
      try {
        userRoleDAO.updateUser(user);
      } catch (NotFoundException e) {
        throw new NonExistingUserException(e);
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(e);
      }
    } else {
      throw new PentahoSecurityException();
    }    
  }
  

  protected boolean hasCreateUserPerm(IPentahoUser user) {
    return true;
  }
  
  protected boolean hasCreateRolePerm(IPentahoRole role) {
    return true;
  }
  
  protected boolean hasUpdateUserPerm(IPentahoUser user) {
    return true;
  }
  
  protected boolean hasUpdateRolePerm(IPentahoRole role) {
    return true;
  }
  
  protected boolean hasDeleteUserPerm(IPentahoUser user) {
    return true;
  }
  
  protected boolean hasDeleteRolePerm(IPentahoRole role) {
    return true;
  }
}
