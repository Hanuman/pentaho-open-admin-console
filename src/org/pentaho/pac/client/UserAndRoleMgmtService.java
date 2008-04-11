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
  
  public void refreshSecurityInfo(final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        userRoleSecurityInfo = (UserRoleSecurityInfo)result;
        callback.onSuccess(null);
      }
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().getUserRoleSecurityInfo(innerCallback);
  }
  
  public ProxyPentahoRole[] getRoles() {
    ProxyPentahoRole[] roles;
    if (userRoleSecurityInfo != null) {
      roles = (ProxyPentahoRole[])userRoleSecurityInfo.getRoles().toArray(new ProxyPentahoRole[0]);
    } else {
      roles = new ProxyPentahoRole[0];
    }
    return roles;
  }
  
  public ProxyPentahoUser[] getUsers() {
    ProxyPentahoUser[] users;
    if (userRoleSecurityInfo != null) {
      users = (ProxyPentahoUser[])userRoleSecurityInfo.getUsers().toArray(new ProxyPentahoUser[0]);
    } else {
      users = new ProxyPentahoUser[0];
    }
    return users;
  }
  
  public ProxyPentahoRole[] getRoles(ProxyPentahoUser user) {
    List roleNames = new ArrayList();
    List assignedRoles = new ArrayList();
    for (Iterator iter = userRoleSecurityInfo.getAssignments().iterator(); iter.hasNext();) {
      UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
      if (userToRoleAssignment.getUserId().equals(user.getName())) {
        roleNames.add(userToRoleAssignment.getRoleId());
      }
    }
    for (Iterator iter = roleNames.iterator(); iter.hasNext();) {
      String roleName = iter.next().toString();
      for (Iterator iter2 = userRoleSecurityInfo.getRoles().iterator(); iter2.hasNext();) {
        ProxyPentahoRole role = (ProxyPentahoRole)iter2.next();
        if (role.getName().equals(roleName)) {
          assignedRoles.add(role);
          break;
        }
      }
    }
    return (ProxyPentahoRole[])assignedRoles.toArray(new ProxyPentahoRole[0]);
  }
  
  public ProxyPentahoUser[] getUsers(ProxyPentahoRole role) {
    List userNames = new ArrayList();
    List assignedUsers = new ArrayList();
    for (Iterator iter = userRoleSecurityInfo.getAssignments().iterator(); iter.hasNext();) {
      UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
      if (userToRoleAssignment.getRoleId().equals(role.getName())) {
        userNames.add(userToRoleAssignment.getUserId());
      }
    }
    for (Iterator iter = userNames.iterator(); iter.hasNext();) {
      String userName = iter.next().toString();
      for (Iterator iter2 = userRoleSecurityInfo.getUsers().iterator(); iter.hasNext();) {
        ProxyPentahoUser user = (ProxyPentahoUser)iter2.next();
        if (user.getName().equals(userName)) {
          assignedUsers.add(user);
          break;
        }
      }
    }
    return (ProxyPentahoUser[])assignedUsers.toArray(new ProxyPentahoUser[0]);
  }
  
  public void createUser(final ProxyPentahoUser user, final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        userRoleSecurityInfo.getUsers().add(user);
        callback.onSuccess(null);
      }
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().createUser(user, innerCallback);
  }
  
  public void updateUser(final ProxyPentahoUser user, final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        int index = userRoleSecurityInfo.getUsers().indexOf(user);
        if (index >= 0) {
          userRoleSecurityInfo.getUsers().set(index, user);
        } else {
          userRoleSecurityInfo.getUsers().add(user);
        }
        callback.onSuccess(null);
      }
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().updateUser(user, innerCallback);
  }
  
  public void deleteUsers(final ProxyPentahoUser[] users, final AsyncCallback callback) {
    
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        userRoleSecurityInfo.getUsers().removeAll(Arrays.asList(users));
        ArrayList assignmentsToRemove = new ArrayList();
        for (int i = 0; i < users.length; i++) {
          for (Iterator iter = userRoleSecurityInfo.getAssignments().iterator(); iter.hasNext();) {
            UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
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
  
  public void createRole(final ProxyPentahoRole role, final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        userRoleSecurityInfo.getRoles().add(role);
        callback.onSuccess(null);
      }
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().createRole(role, innerCallback);
  }
  
  public void updateRole(final ProxyPentahoRole role, final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        int index = userRoleSecurityInfo.getRoles().indexOf(role);
        if (index >= 0) {
          userRoleSecurityInfo.getRoles().set(index, role);
        } else {
          userRoleSecurityInfo.getRoles().add(role);
        }
        callback.onSuccess(null);
      }
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    };
    PacServiceFactory.getPacService().updateRole(role, innerCallback);
  }
  
  public void deleteRoles(final ProxyPentahoRole[] roles, final AsyncCallback callback) {
    
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        userRoleSecurityInfo.getRoles().removeAll(Arrays.asList(roles));
        ArrayList assignmentsToRemove = new ArrayList();
        for (int i = 0; i < roles.length; i++) {
          for (Iterator iter = userRoleSecurityInfo.getAssignments().iterator(); iter.hasNext();) {
            UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
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

  public void setUsers(final ProxyPentahoRole role, final ProxyPentahoUser[] assignedUsers, final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {        
        if (userRoleSecurityInfo != null) {
          ArrayList currentMappings = new ArrayList();
          ArrayList mappingsToAdd = new ArrayList();
          ArrayList mappingsToRemove = new ArrayList();
          
          for (int i = 0; i < assignedUsers.length; i++) {
            currentMappings.add(new UserToRoleAssignment(assignedUsers[i].getName(), role.getName()));
          }
          
          for (Iterator iter = userRoleSecurityInfo.getAssignments().iterator(); iter.hasNext();) {
            UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
            if (userToRoleAssignment.getRoleId().equals(role.getName())
                && !currentMappings.contains(userToRoleAssignment)) {
              mappingsToRemove.add(userToRoleAssignment);
            }
          }
          
          for (Iterator iter = currentMappings.iterator(); iter.hasNext();) {
            UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
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
  
  public void setRoles(final ProxyPentahoUser user, final ProxyPentahoRole[] assignedRoles, final AsyncCallback callback) {
    AsyncCallback innerCallback = new AsyncCallback() {
      public void onSuccess(Object result) {
        if (userRoleSecurityInfo != null) {
          ArrayList currentMappings = new ArrayList();
          ArrayList mappingsToAdd = new ArrayList();
          ArrayList mappingsToRemove = new ArrayList();
          
          for (int i = 0; i < assignedRoles.length; i++) {
            currentMappings.add(new UserToRoleAssignment(user.getName(), assignedRoles[i].getName()));
          }
          
          for (Iterator iter = userRoleSecurityInfo.getAssignments().iterator(); iter.hasNext();) {
            UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
            if (userToRoleAssignment.getUserId().equals(user.getName())
                && !currentMappings.contains(userToRoleAssignment)) {
              mappingsToRemove.add(userToRoleAssignment);
            }
          }
          
          for (Iterator iter = currentMappings.iterator(); iter.hasNext();) {
            UserToRoleAssignment userToRoleAssignment = (UserToRoleAssignment)iter.next();
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
