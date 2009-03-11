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
import org.pentaho.platform.engine.security.userroledao.IPentahoRole;
import org.pentaho.platform.engine.security.userroledao.IPentahoUser;

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
  
  public void refreshUserRoleDAO() throws DAOException;

}
