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
package org.pentaho.pac.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pentaho.pac.common.UserRoleSecurityInfo;
import org.pentaho.pac.common.UserToRoleAssignment;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserAndRoleMgmtService {

  UserRoleSecurityInfo userRoleSecurityInfo;

  static UserAndRoleMgmtService userAndRoleMgmtService = new UserAndRoleMgmtService();

  private UserAndRoleMgmtService() {

  }

  public static UserAndRoleMgmtService instance() {
    return userAndRoleMgmtService;
  }

  public void refreshSecurityInfo(final AsyncCallback<Object> callback) {
    AsyncCallback<UserRoleSecurityInfo> innerCallback = new AsyncCallback<UserRoleSecurityInfo>() {
      public void onSuccess(UserRoleSecurityInfo result) {
        userRoleSecurityInfo = result;
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        userRoleSecurityInfo = null;
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().getUserRoleSecurityInfo(innerCallback);
  }

  public ProxyPentahoRole[] getRoles() {
    ProxyPentahoRole[] roles = new ProxyPentahoRole[0];
    if (userRoleSecurityInfo != null) {
      roles = new ProxyPentahoRole[userRoleSecurityInfo.getRoles().size()];
      for (int i = 0; i < roles.length; i++) {
        roles[i] = (ProxyPentahoRole) (userRoleSecurityInfo.getRoles().get(i)).clone();
      }
    }
    return roles;
  }

  public ProxyPentahoUser[] getUsers() {
    ProxyPentahoUser[] users = new ProxyPentahoUser[0];
    if (userRoleSecurityInfo != null) {
      users = new ProxyPentahoUser[userRoleSecurityInfo.getUsers().size()];
      for (int i = 0; i < users.length; i++) {
        users[i] = (ProxyPentahoUser) (userRoleSecurityInfo.getUsers().get(i)).clone();
      }
    }
    return users;
  }

  public ProxyPentahoRole[] getRoles(ProxyPentahoUser user) {
    ProxyPentahoRole[] roles = new ProxyPentahoRole[0];
    if (userRoleSecurityInfo != null) {
      List<String> roleNames = new ArrayList<String>();
      List<ProxyPentahoRole> assignedRoles = new ArrayList<ProxyPentahoRole>();
      for( UserToRoleAssignment userToRoleAssignment : userRoleSecurityInfo.getAssignments() ) {
        if (userToRoleAssignment.getUserId().equals(user.getName())) {
          roleNames.add(userToRoleAssignment.getRoleId());
        }
      }
      for ( String roleName : roleNames ) {
        for( ProxyPentahoRole role : userRoleSecurityInfo.getRoles() ) {
          if (role.getName().equals(roleName)) {
            assignedRoles.add(role);
            break;
          }
        }
      }
      roles = new ProxyPentahoRole[assignedRoles.size()];
      for (int i = 0; i < roles.length; i++) {
        roles[i] = (ProxyPentahoRole) (assignedRoles.get(i)).clone();
      }
      
      // FF3 bug requires roles array to have duplicates removed
      // Beware this must be compiled by GWT
      List<ProxyPentahoRole> finalRoles = new ArrayList<ProxyPentahoRole>();
      outer:
      for(ProxyPentahoRole role : roles){
        for(ProxyPentahoRole uniqueRole : finalRoles){
          if(role.getName().equals(uniqueRole.getName())){
            continue outer;
          }
        }
        finalRoles.add(role);
      }
      
      roles = new ProxyPentahoRole[finalRoles.size()];
      
      for(int i = 0; i < finalRoles.size(); i++){
        roles[i] = finalRoles.get(i);
      }
    }
    return roles;
  }

  public ProxyPentahoUser[] getUsers(ProxyPentahoRole role) {
    ProxyPentahoUser[] users = new ProxyPentahoUser[0];
    if (userRoleSecurityInfo != null) {
      List<String> userNames = new ArrayList<String>();
      List<ProxyPentahoUser> assignedUsers = new ArrayList<ProxyPentahoUser>();
      
      for ( UserToRoleAssignment userToRoleAssignment : userRoleSecurityInfo.getAssignments() ) {
        if (userToRoleAssignment.getRoleId().equals(role.getName())) {
          userNames.add(userToRoleAssignment.getUserId());
        }
      }

      for ( String userName : userNames ) {
        for ( ProxyPentahoUser user : userRoleSecurityInfo.getUsers() ) {
          if (user.getName().equals(userName)) {
            assignedUsers.add(user);
            break;
          }
        }
      }
      users = new ProxyPentahoUser[assignedUsers.size()];
      for (int i = 0; i < users.length; i++) {
        users[i] = (ProxyPentahoUser) (assignedUsers.get(i)).clone();
      }
      
      // FF3 bug requires users array to have duplicates removed
      // Beware this must be compiled by GWT
      List<ProxyPentahoUser> finalUsers = new ArrayList<ProxyPentahoUser>();
      outer:
      for(ProxyPentahoUser user : users){
        for(ProxyPentahoUser uniqueUser : finalUsers){
          if(user.getName().equals(uniqueUser.getName())){
            continue outer;
          }
        }
        finalUsers.add(user);
      }
      
      users = new ProxyPentahoUser[finalUsers.size()];
      
      for(int i = 0; i < finalUsers.size(); i++){
        users[i] = finalUsers.get(i);
      }
    }
    return users;
  }

  public void createUser(final ProxyPentahoUser user, final AsyncCallback<Object> callback) {
    AsyncCallback<Boolean> innerCallback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        userRoleSecurityInfo.getUsers().add((ProxyPentahoUser)user.clone());
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().createUser(user, innerCallback);
  }

  public void updateUser(final ProxyPentahoUser user, final AsyncCallback<Boolean> callback) {
    AsyncCallback<Boolean> innerCallback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        int index = userRoleSecurityInfo.getUsers().indexOf(user);
        if (index >= 0) {
          userRoleSecurityInfo.getUsers().set(index, (ProxyPentahoUser)user.clone());
        } else {
          userRoleSecurityInfo.getUsers().add((ProxyPentahoUser)user.clone());
        }
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().updateUser(user, innerCallback);
  }

  public void deleteUsers(final ProxyPentahoUser[] users, final AsyncCallback<Boolean> callback) {

    AsyncCallback<Boolean> innerCallback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        userRoleSecurityInfo.getUsers().removeAll(Arrays.asList(users));
        List<UserToRoleAssignment> assignmentsToRemove = new ArrayList<UserToRoleAssignment>();
        for (int i = 0; i < users.length; i++) {
          for ( UserToRoleAssignment userToRoleAssignment : userRoleSecurityInfo.getAssignments() ) {
            if (userToRoleAssignment.getUserId().equals(users[i].getName())) {
              assignmentsToRemove.add(userToRoleAssignment);
            }
          }
        }
        userRoleSecurityInfo.getAssignments().removeAll(assignmentsToRemove);
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().deleteUsers(users, innerCallback);
  }

  public void createRole(final ProxyPentahoRole role, final AsyncCallback<Boolean> callback) {
    AsyncCallback<Boolean> innerCallback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        userRoleSecurityInfo.getRoles().add((ProxyPentahoRole)role.clone());
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().createRole(role, innerCallback);
  }

  public void updateRole(final ProxyPentahoRole role, final AsyncCallback<Boolean> callback) {
    AsyncCallback<Boolean> innerCallback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        int index = userRoleSecurityInfo.getRoles().indexOf(role);
        if (index >= 0) {
          userRoleSecurityInfo.getRoles().set(index, (ProxyPentahoRole)role.clone());
        } else {
          userRoleSecurityInfo.getRoles().add((ProxyPentahoRole)role.clone());
        }
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().updateRole(role, innerCallback);
  }

  public void deleteRoles(final ProxyPentahoRole[] roles, final AsyncCallback<Boolean> callback) {

    AsyncCallback<Boolean> innerCallback = new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        userRoleSecurityInfo.getRoles().removeAll(Arrays.asList(roles));
        List<UserToRoleAssignment> assignmentsToRemove = new ArrayList<UserToRoleAssignment>();
        for (int i = 0; i < roles.length; i++) {
          for ( UserToRoleAssignment userToRoleAssignment : userRoleSecurityInfo.getAssignments() ) {
            if (userToRoleAssignment.getRoleId().equals(roles[i].getName())) {
              assignmentsToRemove.add(userToRoleAssignment);
            }
          }
        }
        userRoleSecurityInfo.getAssignments().removeAll(assignmentsToRemove);
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().deleteRoles(roles, innerCallback);
  }

  public void setUsers(final ProxyPentahoRole role, final ProxyPentahoUser[] assignedUsers, final AsyncCallback<Object> callback) {
    AsyncCallback<Object> innerCallback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        if (userRoleSecurityInfo != null) {
          List<UserToRoleAssignment> currentMappings = new ArrayList<UserToRoleAssignment>();
          List<UserToRoleAssignment> mappingsToAdd = new ArrayList<UserToRoleAssignment>();
          List<UserToRoleAssignment> mappingsToRemove = new ArrayList<UserToRoleAssignment>();

          for (int i = 0; i < assignedUsers.length; i++) {
            currentMappings.add(new UserToRoleAssignment(assignedUsers[i].getName(), role.getName()));
          }
          for ( UserToRoleAssignment userToRoleAssignment : userRoleSecurityInfo.getAssignments() ) {
            if (userToRoleAssignment.getRoleId().equals(role.getName())
                && !currentMappings.contains(userToRoleAssignment)) {
              mappingsToRemove.add(userToRoleAssignment);
            }
          }

          for ( UserToRoleAssignment userToRoleAssignment : currentMappings ) {
            if (!userRoleSecurityInfo.getAssignments().contains(userToRoleAssignment)) {
              mappingsToAdd.add(userToRoleAssignment);
            }
          }
          userRoleSecurityInfo.getAssignments().removeAll(mappingsToRemove);
          userRoleSecurityInfo.getAssignments().addAll(mappingsToAdd);
        }
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().setUsers(role, assignedUsers, innerCallback);
  }

  public void setRoles(final ProxyPentahoUser user, final ProxyPentahoRole[] assignedRoles, final AsyncCallback<Object> callback) {
    AsyncCallback<Object> innerCallback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        if (userRoleSecurityInfo != null) {
          List<UserToRoleAssignment> currentMappings = new ArrayList<UserToRoleAssignment>();
          List<UserToRoleAssignment> mappingsToAdd = new ArrayList<UserToRoleAssignment>();
          List<UserToRoleAssignment> mappingsToRemove = new ArrayList<UserToRoleAssignment>();

          for (int i = 0; i < assignedRoles.length; i++) {
            currentMappings.add(new UserToRoleAssignment(user.getName(), assignedRoles[i].getName()));
          }

          for ( UserToRoleAssignment userToRoleAssignment : userRoleSecurityInfo.getAssignments() ) {
            if (userToRoleAssignment.getUserId().equals(user.getName())
                && !currentMappings.contains(userToRoleAssignment)) {
              mappingsToRemove.add(userToRoleAssignment);
            }
          }

          for ( UserToRoleAssignment userToRoleAssignment : currentMappings ) {
            if (!userRoleSecurityInfo.getAssignments().contains(userToRoleAssignment)) {
              mappingsToAdd.add(userToRoleAssignment);
            }
          }
          userRoleSecurityInfo.getAssignments().removeAll(mappingsToRemove);
          userRoleSecurityInfo.getAssignments().addAll(mappingsToAdd);
        }
        callback.onSuccess(null);
      }

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().setRoles(user, assignedRoles, innerCallback);
  }
  
  public ProxyPentahoRole[] getDefaultRoles() {
    ProxyPentahoRole[] roles = new ProxyPentahoRole[0];
    if (userRoleSecurityInfo != null) {
      return userRoleSecurityInfo.getDefaultRoles().toArray(new ProxyPentahoRole[0]);
    }
    return roles;
  }
}
