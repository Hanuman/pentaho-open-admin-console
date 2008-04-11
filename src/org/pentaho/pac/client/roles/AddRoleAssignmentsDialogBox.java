package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddRoleAssignmentsDialogBox extends DialogBox implements ClickListener {
  
  Button okButton = new Button(PentahoAdminConsole.getLocalizedMessages().ok());
  Button cancelButton = new Button(PentahoAdminConsole.getLocalizedMessages().cancel());
  RolesList rolesList = new RolesList(true);
  boolean rolesAssigned = false;
  ProxyPentahoUser user;
  MessageDialog messageDialog = new MessageDialog(PentahoAdminConsole.getLocalizedMessages().addRole(), new int[]{MessageDialog.OK_BTN}); //$NON-NLS-1$
  
  public AddRoleAssignmentsDialogBox() {
    super();
    
    setText(PentahoAdminConsole.getLocalizedMessages().addRole());
    
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(rolesList);
    verticalPanel.add(footerPanel);
    
    setText(PentahoAdminConsole.getLocalizedMessages().addRole());
    
    verticalPanel.setWidth("100%");
    verticalPanel.setHeight("100%");
    
    verticalPanel.setCellHeight(rolesList, "100%");
    verticalPanel.setCellWidth(rolesList, "100%");
    
    rolesList.setWidth("100%"); //$NON-NLS-1$
    rolesList.setHeight("100%"); //$NON-NLS-1$
    
    setWidth("250px"); //$NON-NLS-1$
    setHeight("250px"); //$NON-NLS-1$
    
    setWidget(verticalPanel);
    
    okButton.addClickListener(this);
    cancelButton.addClickListener(this);
  }
  
  public AddRoleAssignmentsDialogBox(ProxyPentahoUser user) {
    this(); 
    setUser(user);
  }


  public ProxyPentahoUser getUser() {
    return user;
  }


  public void setUser(ProxyPentahoUser user) {
    rolesAssigned = false;
    this.user = user;
    if (user != null) {
      ArrayList unassignedRoles = new ArrayList();
      unassignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles()));
      unassignedRoles.removeAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles(user)));
      rolesList.setRoles((ProxyPentahoRole[])unassignedRoles.toArray(new ProxyPentahoRole[0]));
    } else {
      rolesList.setRoles(new ProxyPentahoRole[0]);
    }
  }


  public Button getOkButton() {
    return okButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  
  public RolesList getRolesList() {
    return rolesList;
  }


  public ProxyPentahoRole[] getSelectedRoles() {
    return rolesList.getSelectedRoles();
  }


  public void show() {
    rolesAssigned = false;
    super.show();
  }


  public void onClick(Widget sender) {
    if (sender == okButton) {
      assignSelectedRoles();
    } else if (sender == cancelButton) {
      hide();
    }
  }
  
  private void assignSelectedRoles() {
    ProxyPentahoRole[] selectedRoles = getSelectedRoles();
    if (selectedRoles.length > 0) {
      ArrayList assignedRoles = new ArrayList();
      assignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles(user)));
      assignedRoles.addAll(Arrays.asList(selectedRoles));
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          rolesAssigned = true;
          hide();
        }

        public void onFailure(Throwable caught) {
          if (caught instanceof PentahoSecurityException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().insufficientPrivileges());
          } else if (caught instanceof NonExistingRoleException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().roleDoesNotExist(caught.getMessage()));
          } else if (caught instanceof NonExistingUserException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().userDoesNotExist(caught.getMessage()));
          } else {
            messageDialog.setMessage(caught.getMessage());
          }
          messageDialog.center();
        }
      };
      UserAndRoleMgmtService.instance().setRoles(user, (ProxyPentahoRole[])assignedRoles.toArray(new ProxyPentahoRole[0]), callback);
    }
  }

  public boolean getRolesAssigned() {
    return rolesAssigned;
  }
}
