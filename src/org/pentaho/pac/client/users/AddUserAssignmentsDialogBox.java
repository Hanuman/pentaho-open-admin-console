package org.pentaho.pac.client.users;

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

public class AddUserAssignmentsDialogBox extends DialogBox implements ClickListener {
  
  Button okButton = new Button(PentahoAdminConsole.getLocalizedMessages().ok());
  Button cancelButton = new Button(PentahoAdminConsole.getLocalizedMessages().cancel());
  UsersList usersList = new UsersList(true);
  boolean usersAssigned = false;
  ProxyPentahoRole role;
  MessageDialog messageDialog = new MessageDialog(PentahoAdminConsole.getLocalizedMessages().addUser(), new int[]{MessageDialog.OK_BTN}); //$NON-NLS-1$
  
  public AddUserAssignmentsDialogBox() {
    super();
    
    setText(PentahoAdminConsole.getLocalizedMessages().addUser());
    
    HorizontalPanel footerPanel = new HorizontalPanel();
    footerPanel.add(okButton);
    footerPanel.add(cancelButton);
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(usersList);
    verticalPanel.add(footerPanel);
    
    verticalPanel.setWidth("100%");
    verticalPanel.setHeight("100%");
    
    verticalPanel.setCellHeight(usersList, "100%");
    verticalPanel.setCellWidth(usersList, "100%");
    
    usersList.setWidth("100%"); //$NON-NLS-1$
    usersList.setHeight("100%"); //$NON-NLS-1$
    
    setWidth("250px"); //$NON-NLS-1$
    setHeight("250px"); //$NON-NLS-1$
    
    setWidget(verticalPanel);
    
    okButton.addClickListener(this);
    cancelButton.addClickListener(this);
  }
  
  public AddUserAssignmentsDialogBox(ProxyPentahoRole role) {
    this(); 
    setRole(role);
  }


  public ProxyPentahoRole getRole() {
    return role;
  }


  public void setRole(ProxyPentahoRole role) {
    usersAssigned = false;
    this.role = role;
    if (role != null) {
      ArrayList unassignedUsers = new ArrayList();
      unassignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers()));
      unassignedUsers.removeAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(role)));
      usersList.setUsers((ProxyPentahoUser[])unassignedUsers.toArray(new ProxyPentahoUser[0]));
    } else {
      usersList.setUsers(new ProxyPentahoUser[0]);
    }
  }


  public Button getOkButton() {
    return okButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  
  public UsersList getUsersList() {
    return usersList;
  }


  public ProxyPentahoUser[] getSelectedUsers() {
    return usersList.getSelectedUsers();
  }


  public void show() {
    usersAssigned = false;
    super.show();
  }


  public void onClick(Widget sender) {
    if (sender == okButton) {
      assignSelectedUsers();
    } else if (sender == cancelButton) {
      hide();
    }
  }
  
  private void assignSelectedUsers() {
    ProxyPentahoUser[] selectedUsers = getSelectedUsers();
    if (selectedUsers.length > 0) {
      ArrayList assignedUsers = new ArrayList();
      assignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(role)));
      assignedUsers.addAll(Arrays.asList(selectedUsers));
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          usersAssigned = true;
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
      UserAndRoleMgmtService.instance().setUsers(role, (ProxyPentahoUser[])assignedUsers.toArray(new ProxyPentahoUser[0]), callback);
    }
  }

  public boolean getUsersAssigned() {
    return usersAssigned;
  }
}
