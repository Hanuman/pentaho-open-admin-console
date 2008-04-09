package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssignedRolesList extends BasicRolesList {

  protected MessageDialog messageDialog = new MessageDialog("Roles", "", new int[]{MessageDialog.OK_BTN});
  ProxyPentahoUser user = null;
  
  public AssignedRolesList() {
    super(true);
  }
  
  public void setUser(ProxyPentahoUser user) {
    this.user = user;
    if (user != null) {
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          setRoles((ProxyPentahoRole[])result);
        }

        public void onFailure(Throwable caught) {
          messageDialog.setMessage("Unable to refresh roles list: " + caught.getMessage());
          messageDialog.center();
          setRoles(new ProxyPentahoRole[0]);
        }
      };
      SecurityCache.getRoles(callback);
    } else {
      setRoles(new ProxyPentahoRole[0]);
    }  
  }
}
