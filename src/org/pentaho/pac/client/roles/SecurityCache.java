package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SecurityCache {

  static HashMap usersMap = null;
  static HashMap rolesMap = null;
  
  static public void clearRolesCache() {
    rolesMap = null;
  }
  
  static public void clearUsersCache() {
    rolesMap = null;
  }
  
  static public void getRoles(final AsyncCallback callback) {
    if (rolesMap != null) {
      callback.onSuccess((ProxyPentahoRole[])rolesMap.keySet().toArray(new ProxyPentahoRole[0]));
    } else {
      AsyncCallback innerCallback = new AsyncCallback() {
        public void onSuccess(Object result) {
          rolesMap = new HashMap();
          ProxyPentahoRole[] roles = (ProxyPentahoRole[])result;
          for (int i = 0; i < roles.length; i++) {
            rolesMap.put(roles[i], null);
          }
          callback.onSuccess(roles);
        }

        public void onFailure(Throwable caught) {
          callback.onFailure(caught);
        }
      };
      PacServiceFactory.getPacService().getRoles(innerCallback);
    }
  }
  
  static public void getUsers(final AsyncCallback callback) {
    if (usersMap != null) {
      callback.onSuccess((ProxyPentahoUser[])usersMap.keySet().toArray(new ProxyPentahoUser[0]));
    } else {
      AsyncCallback innerCallback = new AsyncCallback() {
        public void onSuccess(Object result) {
          usersMap = new HashMap();
          ProxyPentahoUser[] users = (ProxyPentahoUser[])result;
          for (int i = 0; i < users.length; i++) {
            usersMap.put(users[i], null);
          }
          callback.onSuccess(users);
        }

        public void onFailure(Throwable caught) {
          callback.onFailure(caught);
        }
      };
      PacServiceFactory.getPacService().getUsers(innerCallback);
    }
  }
  
  static public void getRoles(ProxyPentahoUser user, final AsyncCallback callback) {
    
  }
  
  static public void getUsers(ProxyPentahoRole role, final AsyncCallback callback) {
    
  }
  
}
