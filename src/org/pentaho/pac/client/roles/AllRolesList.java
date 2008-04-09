package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class AllRolesList extends BasicRolesList {
  protected MessageDialog messageDialog = new MessageDialog("Roles", "", new int[]{MessageDialog.OK_BTN});
  boolean isInitialized = false;
  
  public AllRolesList() {
    super(true);
  }

  public void setRoles(ProxyPentahoRole[] roles) {
    super.setRoles(roles);
    isInitialized = true;
  }
  
  public void refresh() {
    setRoles(new ProxyPentahoRole[0]);
    isInitialized = false;
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        setRoles((ProxyPentahoRole[])result);
        isInitialized = true;
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText("Error Loading Roles");
        messageDialog.setMessage("Unable to refresh roles list: " + caught.getMessage());
        messageDialog.center();
      }
    };
    SecurityCache.getRoles(callback);
  }
  
  public boolean isInitialized() {
    return isInitialized;
  }
  
  public void clearRolesCache() {
    setRoles(new ProxyPentahoRole[0]);
    isInitialized = false;
  }
  
  
}
