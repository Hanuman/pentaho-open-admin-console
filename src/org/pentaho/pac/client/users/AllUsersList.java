package org.pentaho.pac.client.users;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.roles.SecurityCache;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AllUsersList extends BasicUsersList {
  MessageDialog messageDialog = new MessageDialog("Users", "", new int[]{MessageDialog.OK_BTN});
  boolean isInitialized = false;
  
  public AllUsersList() {
    super(true);
  }

  public void setUsers(ProxyPentahoUser[] users) {
    super.setUsers(users);
    isInitialized = true;
  }

  public void refresh() {
    setUsers(new ProxyPentahoUser[0]);
    isInitialized = false;
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        setUsers((ProxyPentahoUser[])result);
        isInitialized = true;
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText("Error Loading Users");
        messageDialog.setMessage("Unable to refresh users list: " + caught.getMessage());
        messageDialog.center();
      }
    };
    
    SecurityCache.getUsers(callback);
  }
  
  public boolean isInitialized() {
    return isInitialized;
  }
  
  public void clearUsersCache() {
    setUsers(new ProxyPentahoUser[0]);
    isInitialized = false;
  }
  
}
