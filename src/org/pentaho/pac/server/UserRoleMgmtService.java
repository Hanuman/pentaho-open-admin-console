/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.server;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.DuplicateRoleException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.DAOFactory;
import org.pentaho.pac.server.i18n.Messages;
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
        throw new DuplicateRoleException(Messages.getErrorString("PacService.ERROR_0049_ROLE_ALREADY_EXIST", newRole.getName()), e); //$NON-NLS-1$
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(Messages.getErrorString("PacService.ERROR_0050_UNRECOGNIZED_ROLE_CREATION",e.getLocalizedMessage()), e); //$NON-NLS-1$
      }
    } else {
      throw new PentahoSecurityException(Messages.getErrorString("PacService.ERROR_0002_NO_CREATE_ROLE_PERMISSION",newRole.getName())); //$NON-NLS-1$      
    }
  }

  public void createUser(IPentahoUser newUser) throws DuplicateUserException, DAOException, PentahoSecurityException {
    if (hasCreateUserPerm(newUser)) {
    try {
      userRoleDAO.createUser(newUser);
    } catch (AlreadyExistsException e) {
      throw new DuplicateUserException(Messages.getErrorString("PacService.ERROR_0051_USER_ALREADY_EXIST", newUser.getUsername()), e); //$NON-NLS-1$      
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0052_UNRECOGNIZED_USER_CREATION",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
    } else {
      throw new PentahoSecurityException(Messages.getErrorString("PacService.ERROR_0005_NO_CREATE_USER_PERMISSION",newUser.getUsername())); //$NON-NLS-1$      
    }
  }

  public void deleteRole(String roleName) throws NonExistingRoleException, DAOException, PentahoSecurityException {
    IPentahoRole role;
    try {
      role = userRoleDAO.getRole(roleName);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0053_UNRECOGNIZED_ROLE_DELETION",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
    if (role != null) {
      deleteRole(role);
    } else {
      throw new NonExistingRoleException(Messages.getErrorString("PacService.ERROR_0010_ROLE_DELETION_FAILED_NO_ROLE",roleName)); //$NON-NLS-1$
    }
  }
  
  public void deleteRole(IPentahoRole role) throws NonExistingRoleException, DAOException, PentahoSecurityException {
    if (hasDeleteRolePerm(role)) {
      try {
        userRoleDAO.deleteRole(role);
      } catch (NotFoundException e) {
        throw new NonExistingRoleException(Messages.getErrorString("PacService.ERROR_0010_ROLE_DELETION_FAILED_NO_ROLE",role.getName()), e); //$NON-NLS-1$
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(Messages.getErrorString("PacService.ERROR_0053_UNRECOGNIZED_ROLE_DELETION",e.getLocalizedMessage()), e); //$NON-NLS-1$        
      }
    } else {
      throw new PentahoSecurityException(Messages.getErrorString("PacService.ERROR_0012_ROLE_DELETION_FAILED_NO_PERMISSION",role.getName())); //$NON-NLS-1$
    }
  }

  public void deleteUser(String userName) throws NonExistingUserException, DAOException, PentahoSecurityException {
    IPentahoUser user;
    try {
      user = userRoleDAO.getUser(userName);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0054_UNRECOGNIZED_USER_DELETION",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
    if (user != null) {
      deleteUser(user);
    } else {
      throw new NonExistingUserException(Messages.getErrorString("PacService.ERROR_0013_USER_DELETION_FAILED_NO_USER",userName)); //$NON-NLS-1$            
    }
  }
  
  public void deleteUser(IPentahoUser user) throws NonExistingUserException, DAOException, PentahoSecurityException {
    if (hasDeleteUserPerm(user)) {
      try {
        userRoleDAO.deleteUser(user);
      } catch (NotFoundException e) {
        throw new NonExistingUserException(Messages.getErrorString("PacService.ERROR_0013_USER_DELETION_FAILED_NO_USER",user.getUsername())); //$NON-NLS-1$        
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(Messages.getErrorString("PacService.ERROR_0054_UNRECOGNIZED_USER_DELETION",e.getLocalizedMessage()), e); //$NON-NLS-1$        
      }
    } else {
      throw new PentahoSecurityException(Messages.getErrorString("PacService.ERROR_0015_USER_DELETION_FAILED_NO_PERMISSION",user.getUsername())); //$NON-NLS-1$      
    }    
  }

  public IPentahoRole getRole(String name) throws DAOException {
    try {
      return userRoleDAO.getRole(name);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0055_UNRECOGNIZED_ROLE_RETRIEVAL",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
  }

  public List<IPentahoRole> getRoles() throws DAOException {
    try {
      return userRoleDAO.getRoles();
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0057_UNRECOGNIZED_ROLES_RETRIEVAL",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
  }

  public IPentahoUser getUser(String name) throws DAOException {
    try {
      return userRoleDAO.getUser(name);
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0056_UNRECOGNIZED_USER_RETRIEVAL",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
  }

  public List<IPentahoUser> getUsers() throws DAOException {
    try {
      return userRoleDAO.getUsers();
    } catch (UncategorizedUserRoleDaoException e) {
      throw new DAOException(Messages.getErrorString("PacService.ERROR_0058_UNRECOGNIZED_USERS_RETRIEVAL",e.getLocalizedMessage()), e); //$NON-NLS-1$      
    }
  }

  public void updateRole(IPentahoRole role) throws DAOException, PentahoSecurityException, NonExistingRoleException {
    if (hasUpdateRolePerm(role)) {
      try {
        userRoleDAO.updateRole(role);
      } catch (NotFoundException e) {
        throw new NonExistingRoleException(Messages.getErrorString("PacService.ERROR_0036_ROLE_UPDATE_FAILED_DOES_NOT_EXIST",role.getName()), e); //$NON-NLS-1$        
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(Messages.getErrorString("PacService.ERROR_0059_UNRECOGNIZED_ROLES_UPDATE",e.getLocalizedMessage()), e); //$NON-NLS-1$
      }
    } else {
      throw new PentahoSecurityException(Messages.getErrorString("PacService.ERROR_0035_ROLE_UPDATE_FAILED_NO_PERMISSION",role.getName())); //$NON-NLS-1$      
    }    
  }

  public void updateUser(IPentahoUser user) throws DAOException, PentahoSecurityException, NonExistingUserException {
    if (hasUpdateUserPerm(user)) {
      try {
        userRoleDAO.updateUser(user);
      } catch (NotFoundException e) {
        throw new NonExistingUserException(Messages.getErrorString("PacService.ERROR_0039_USER_UPDATE_FAILED_DOES_NOT_EXIST",user.getUsername()), e); //$NON-NLS-1$
      } catch (UncategorizedUserRoleDaoException e) {
        throw new DAOException(Messages.getErrorString("PacService.ERROR_0060_UNRECOGNIZED_USERS_UPDATE",e.getLocalizedMessage()), e); //$NON-NLS-1$        
      }
    } else {
      throw new PentahoSecurityException(Messages.getErrorString("PacService.ERROR_0061_USER_UPDATE_FAILED_NO_PERMISSION",user.getUsername())); //$NON-NLS-1$
    }    
  }
  
  public void refreshUserRoleDAO() throws DAOException{
    userRoleDAO = DAOFactory.getUserRoleDAO();
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
