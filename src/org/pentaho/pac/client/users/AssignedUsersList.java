package org.pentaho.pac.client.users;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.roles.SecurityCache;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssignedUsersList extends BasicUsersList {
  
  MessageDialog messageDialog = new MessageDialog("Users", "", new int[]{MessageDialog.OK_BTN});
  ProxyPentahoRole role;
  
  public AssignedUsersList() {
    super(true);
  }
  
  public void setRole(ProxyPentahoRole role) {
    this.role = role;
    if (role != null) {
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          setUsers((ProxyPentahoUser[])result);
        }

        public void onFailure(Throwable caught) {
          messageDialog.setMessage("Unable to refresh roles list: " + caught.getMessage());
          messageDialog.center();
          setUsers(new ProxyPentahoUser[0]);
        }
      };
      
      SecurityCache.getUsers(role, callback);
    } else {
      setUsers(new ProxyPentahoUser[0]);
    }
    
  }

  public ProxyPentahoRole getRole() {
    return role;
  }
  
  
}
