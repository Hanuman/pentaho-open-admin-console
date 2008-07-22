package org.pentaho.pac.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
}
