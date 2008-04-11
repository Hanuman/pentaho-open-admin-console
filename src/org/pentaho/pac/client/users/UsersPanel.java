package org.pentaho.pac.client.users;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.roles.RolesList;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UsersPanel extends DockPanel implements ClickListener, ChangeListener, PopupListener, KeyboardListener {

  MessageDialog messageDialog = new MessageDialog("", new int[]{MessageDialog.OK_BTN}); //$NON-NLS-1$
  UsersList usersList = new UsersList(true);
  RolesList assignedRolesList = new RolesList(true);
  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  Button updateUserBtn = new Button(PentahoAdminConsole.getLocalizedMessages().update());
  Button addUserBtn = new Button("+"); //$NON-NLS-1$
  Button deleteUserBtn = new Button("-"); //$NON-NLS-1$
  TextBox filterTextBox = new TextBox();
  NewUserDialogBox newUserDialogBox = new NewUserDialogBox();
  MessageDialog confirmUserDeleteDialog = new MessageDialog(PentahoAdminConsole.getLocalizedMessages().deleteUsers(), PentahoAdminConsole.getLocalizedMessages().confirmUserDeletionMsg(), new int[] {MessageDialog.OK_BTN, MessageDialog.CANCEL_BTN});
  
	public UsersPanel() {
	  DockPanel userListPanel = buildUsersListPanel();
	  
	  DockPanel userDetailsDockPanel = buildUserDetailsDockPanel();
    add(userListPanel, DockPanel.WEST);
    add(userDetailsDockPanel, DockPanel.CENTER);
    
    setSpacing(10);
    
    setCellWidth(userListPanel, "30%"); //$NON-NLS-1$
    setCellWidth(userDetailsDockPanel, "70%"); //$NON-NLS-1$
    setCellHeight(userListPanel, "100%"); //$NON-NLS-1$
    setCellHeight(userDetailsDockPanel, "100%"); //$NON-NLS-1$
    userListPanel.setWidth("100%"); //$NON-NLS-1$
    userListPanel.setHeight("100%"); //$NON-NLS-1$
    userDetailsDockPanel.setWidth("100%"); //$NON-NLS-1$
    userDetailsDockPanel.setHeight("100%"); //$NON-NLS-1$
    
    userDetailsPanel.setEnabled(false);
    updateUserBtn.setEnabled(false);
    
    newUserDialogBox.addPopupListener(this);
    
    confirmUserDeleteDialog.addPopupListener(this);
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
    
 	}

	public DockPanel buildUserDetailsDockPanel() {
	  
    DockPanel assignedRolesPanel = buildAssignedRolesPanel();
    
	  DockPanel dockPanel = new DockPanel();
	  dockPanel.add(userDetailsPanel, DockPanel.NORTH);
    dockPanel.add(updateUserBtn, DockPanel.NORTH);
	  dockPanel.add(assignedRolesPanel, DockPanel.CENTER);
	  
	  dockPanel.setCellHorizontalAlignment(updateUserBtn, HasHorizontalAlignment.ALIGN_RIGHT);
    
	  dockPanel.setCellWidth(userDetailsPanel, "100%"); //$NON-NLS-1$
	  dockPanel.setCellHeight(assignedRolesPanel, "100%"); //$NON-NLS-1$
	  dockPanel.setCellWidth(assignedRolesPanel, "100%"); //$NON-NLS-1$
	  
	  userDetailsPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setHeight("100%"); //$NON-NLS-1$
	  
    updateUserBtn.addClickListener(this);
	  return dockPanel;
	}
	
	public DockPanel buildUsersListPanel() {
	  DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteUserBtn, DockPanel.EAST);
	  headerDockPanel.add(addUserBtn, DockPanel.EAST);
    Label label = new Label("Users"); //$NON-NLS-1$
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    DockPanel userListPanel = new DockPanel();
    userListPanel.add(headerDockPanel, DockPanel.NORTH);
    userListPanel.add(usersList, DockPanel.CENTER);    
    userListPanel.add(filterTextBox, DockPanel.SOUTH  );
    userListPanel.add(new Label(PentahoAdminConsole.getLocalizedMessages().userListFilter()), DockPanel.SOUTH );
    
    userListPanel.setCellHeight(usersList, "100%"); //$NON-NLS-1$
    userListPanel.setCellWidth(usersList, "100%"); //$NON-NLS-1$
    userListPanel.setHeight("100%"); //$NON-NLS-1$
    userListPanel.setWidth("100%"); //$NON-NLS-1$
    usersList.setHeight("100%"); //$NON-NLS-1$
    usersList.setWidth("100%"); //$NON-NLS-1$
    addUserBtn.setWidth("20px"); //$NON-NLS-1$
    deleteUserBtn.setWidth("20px"); //$NON-NLS-1$
    addUserBtn.setHeight("20px"); //$NON-NLS-1$
    deleteUserBtn.setHeight("20px"); //$NON-NLS-1$
    filterTextBox.setWidth( "100%" ); //$NON-NLS-1$
    deleteUserBtn.setEnabled(false);
    
    filterTextBox.addKeyboardListener( this );
    usersList.addChangeListener(this);
    addUserBtn.addClickListener(this);
    deleteUserBtn.addClickListener(this);
    return userListPanel;
	}
	
	public DockPanel buildAssignedRolesPanel() {
	  DockPanel assignedRolesPanel = new DockPanel();
	  assignedRolesPanel.add(new Label(PentahoAdminConsole.getLocalizedMessages().assignedRoles()), DockPanel.NORTH);
	  assignedRolesPanel.add(assignedRolesList, DockPanel.CENTER);
	  assignedRolesPanel.setCellHeight(assignedRolesList, "100%"); //$NON-NLS-1$
	  assignedRolesPanel.setCellWidth(assignedRolesList, "100%"); //$NON-NLS-1$
	  assignedRolesList.setHeight("100%"); //$NON-NLS-1$
	  assignedRolesList.setWidth("100%"); //$NON-NLS-1$
	  return assignedRolesPanel;
	}
	
	
	public void onClick(Widget sender) {
	  if (sender == updateUserBtn) {
	    updateUserDetails( sender );
	  } else if (sender == deleteUserBtn) {
	    if (usersList.getSelectedUsers().length > 0) {
	      confirmUserDeleteDialog.center();
	    }
	  } else if (sender == addUserBtn) {
	    addNewUser();
	  }
	}


	private void addNewUser() {
	  newUserDialogBox.setUser(null);
    newUserDialogBox.center();
   }
	
	private void deleteSelectedUsers() {
	  final ProxyPentahoUser[] selectedUsers = usersList.getSelectedUsers();
	  if (selectedUsers.length > 0) {
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        usersList.removeUsers(selectedUsers);
	        userSelectionChanged();
	      }

	      public void onFailure(Throwable caught) {
	        messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().deleteUsers());
          if (caught instanceof PentahoSecurityException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().insufficientPrivileges());
          } else if (caught instanceof NonExistingUserException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().userDoesNotExist(caught.getMessage()));
          } else {
            messageDialog.setMessage(caught.getMessage());
          }
          messageDialog.center();
	      }
	    };
	    UserAndRoleMgmtService.instance().deleteUsers(selectedUsers, callback);
	  }
	}
	
	private void userSelectionChanged() {
    ProxyPentahoUser[] selectedUsers = usersList.getSelectedUsers();
    if (selectedUsers.length == 1) {
      userDetailsPanel.setUser(selectedUsers[0]);
      assignedRolesList.setRoles(UserAndRoleMgmtService.instance().getRoles(selectedUsers[0]));
    } else {
      userDetailsPanel.setUser(null);
      assignedRolesList.setRoles(new ProxyPentahoRole[0]);
    }
    userDetailsPanel.setEnabled(selectedUsers.length == 1);
    updateUserBtn.setEnabled(selectedUsers.length == 1);
    deleteUserBtn.setEnabled(selectedUsers.length > 0);
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
	}
	
	private void updateUserDetails( final Widget sender ) {
	  if (!userDetailsPanel.getPassword().equals(userDetailsPanel.getPasswordConfirmation())) { 
	    messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().updateUser());
      messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().passwordConfirmationFailed());
      messageDialog.center();
	  } else {
      ((Button)sender).setEnabled( false );
	    final ProxyPentahoUser user = userDetailsPanel.getUser();
	    
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        usersList.addUser(user);
	        ((Button)sender).setEnabled( true );
	      }

	      public void onFailure(Throwable caught) {
          messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().updateUser());
          if (caught instanceof PentahoSecurityException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().insufficientPrivileges());
          } else if (caught instanceof NonExistingUserException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().userDoesNotExist(caught.getMessage()));
          } else {
            messageDialog.setMessage(caught.getMessage());
          }
          messageDialog.center();
          ((Button)sender).setEnabled( true );
	      }
	    }; // end AsyncCallback
	    UserAndRoleMgmtService.instance().updateUser(user, callback);
	  }
	}
	
	public void onChange(Widget sender) {
	  userSelectionChanged();
	}
	
	public void refresh() {
    usersList.setUsers(UserAndRoleMgmtService.instance().getUsers());
	  userSelectionChanged();
	}
	
  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if ((sender == newUserDialogBox) && newUserDialogBox.isUserCreated()) {
      ProxyPentahoUser newUser = newUserDialogBox.getUser();
      usersList.addUser(newUser);
      usersList.setSelectedUser(newUser);
      userSelectionChanged();
    } else if ((sender == confirmUserDeleteDialog) && (confirmUserDeleteDialog.getButtonPressed() == MessageDialog.OK_BTN)) {
      deleteSelectedUsers();
    }
  }

  public void onKeyDown(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    if (filterTextBox == sender) {
      usersList.setUserNameFilter(filterTextBox.getText());
      userSelectionChanged();
    }
  }
  
}