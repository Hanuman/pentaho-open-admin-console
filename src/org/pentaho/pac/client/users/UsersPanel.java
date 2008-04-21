package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.ICallbackHandler;
import org.pentaho.pac.client.common.ui.IListBoxFilter;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.roles.RoleAssignmentsDialogBox;
import org.pentaho.pac.client.roles.RolesList;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
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

  
  class UserNameFilter implements IListBoxFilter {
    String userNameFilter;
    
    UserNameFilter(String userNameFilter) {
      this.userNameFilter = userNameFilter;
    }
    
    public boolean accepts(Object object) {
      boolean result = false;
      if (object instanceof ProxyPentahoUser) {
        String userName = ((ProxyPentahoUser)object).getName();
        result = ((userNameFilter == null) || (userNameFilter.length() == 0));
        if (!result) {
          int filterLen = userNameFilter.length();
          result = ( filterLen <= userName.length() )
            && userNameFilter.toLowerCase().substring( 0, filterLen )
              .equals( userName.toLowerCase().substring( 0, filterLen ) ); 
        }
      }
      return result;
    }  
  }
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  UsersList usersList = new UsersList(true);
  RolesList assignedRolesList = new RolesList(true);
  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  Button updateUserBtn = new Button(MSGS.update());
  Button addUserBtn = new Button("+"); //$NON-NLS-1$
  Button deleteUserBtn = new Button("-"); //$NON-NLS-1$
  Button addRoleAssignmentBtn = new Button("+");
  Button deleteRoleAssignmentBtn = new Button("-");
  TextBox filterTextBox = new TextBox();
  NewUserDialogBox newUserDialogBox = new NewUserDialogBox();
  ConfirmDialog confirmDeleteUsersDialog = new ConfirmDialog();
  ConfirmDialog confirmRemoveRoleAssignmentDialog = new ConfirmDialog();
  
  RoleAssignmentsDialogBox roleAssignmentsDialog = new RoleAssignmentsDialogBox();
  
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
    roleAssignmentsDialog.addPopupListener(this);
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
    
    confirmDeleteUsersDialog.setText(MSGS.deleteUsers());
    confirmDeleteUsersDialog.setMessage(MSGS.confirmUserDeletionMsg());
    confirmDeleteUsersDialog.setOnOkHandler( new ICallbackHandler() {
      public void onHandle(Object o) {
        confirmDeleteUsersDialog.hide();
        deleteSelectedUsers();
        assignedRoleSelectionChanged();
      }
    });
    
    confirmRemoveRoleAssignmentDialog.setText(MSGS.assignedRoles());
    confirmRemoveRoleAssignmentDialog.setMessage(MSGS.confirmRemoveRoleAssignmentMsg());
    confirmRemoveRoleAssignmentDialog.setOnOkHandler( new ICallbackHandler() {
      public void onHandle(Object o) {
        confirmRemoveRoleAssignmentDialog.hide();
        unassignSelectedRoles();
        assignedRoleSelectionChanged();
      }
    });
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
    userListPanel.add(new Label(MSGS.filter()), DockPanel.SOUTH );
    
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
    DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteRoleAssignmentBtn, DockPanel.EAST);
    headerDockPanel.add(addRoleAssignmentBtn, DockPanel.EAST);
    Label label = new Label(MSGS.assignedRoles()); //$NON-NLS-1$
    headerDockPanel.add(label, DockPanel.WEST);
    headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    
	  DockPanel assignedRolesPanel = new DockPanel();
	  assignedRolesPanel.add(headerDockPanel, DockPanel.NORTH);
	  assignedRolesPanel.add(assignedRolesList, DockPanel.CENTER);
	  assignedRolesPanel.setCellHeight(assignedRolesList, "100%"); //$NON-NLS-1$
	  assignedRolesPanel.setCellWidth(assignedRolesList, "100%"); //$NON-NLS-1$
	  assignedRolesList.setHeight("100%"); //$NON-NLS-1$
	  assignedRolesList.setWidth("100%"); //$NON-NLS-1$
	  
	  deleteRoleAssignmentBtn.setHeight("20px"); //$NON-NLS-1$
	  deleteRoleAssignmentBtn.setWidth("20px"); //$NON-NLS-1$
	  addRoleAssignmentBtn.setHeight("20px"); //$NON-NLS-1$
	  addRoleAssignmentBtn.setWidth("20px"); //$NON-NLS-1$
	  
    assignedRolesList.addChangeListener(this);
	  deleteRoleAssignmentBtn.addClickListener(this);
	  addRoleAssignmentBtn.addClickListener(this);
	  return assignedRolesPanel;
	}
	
	public void onClick(Widget sender) {
	  if (sender == updateUserBtn) {
	    updateUserDetails( sender );
	  } else if (sender == deleteUserBtn) {
	    if (usersList.getSelectedUsers().length > 0) {
	      confirmDeleteUsersDialog.center();
	    }
	  } else if (sender == addUserBtn) {
	    addNewUser();
	  } else if (sender == deleteRoleAssignmentBtn) {
	    if (assignedRolesList.getSelectedRoles().length > 0) {
        confirmRemoveRoleAssignmentDialog.center();
      }
	  } else if (sender == addRoleAssignmentBtn) {
	    ProxyPentahoUser[] selectedUsers = usersList.getSelectedUsers();
      if (selectedUsers.length == 1) {
        modifyRoleAssignments(selectedUsers[0]);
      }
	  }
	}


	private void modifyRoleAssignments(ProxyPentahoUser user) {
	  roleAssignmentsDialog.setUser(user);
	  roleAssignmentsDialog.center();
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
	        errorDialog.setText(MSGS.deleteUsers());
          if (caught instanceof PentahoSecurityException) {
            errorDialog.setMessage(MSGS.insufficientPrivileges());
          } else if (caught instanceof NonExistingUserException) {
            errorDialog.setMessage(MSGS.userDoesNotExist(caught.getMessage()));
          } else {
            errorDialog.setMessage(caught.getMessage());
          }
          errorDialog.center();
	      }
	    };
	    UserAndRoleMgmtService.instance().deleteUsers(selectedUsers, callback);
	  }
	}
	
  private void unassignSelectedRoles() {
    ProxyPentahoUser[] selectedUsers = usersList.getSelectedUsers();
    if (selectedUsers.length == 1) {
      ArrayList assignedRoles = new ArrayList();
      assignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles(selectedUsers[0])));
      final ProxyPentahoRole[] rolesToUnassign = assignedRolesList.getSelectedRoles();
      assignedRoles.removeAll(Arrays.asList(rolesToUnassign));      
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          assignedRolesList.removeRoles(rolesToUnassign);
        }

        public void onFailure(Throwable caught) {
          errorDialog.setText(MSGS.removeRoles());
          if (caught instanceof PentahoSecurityException) {
            errorDialog.setMessage(MSGS.insufficientPrivileges());
          } else if (caught instanceof NonExistingUserException) {
            errorDialog.setMessage(MSGS.userDoesNotExist(caught.getMessage()));
          } else if (caught instanceof NonExistingRoleException) {
            errorDialog.setMessage(MSGS.roleDoesNotExist(caught.getMessage()));
          } else {
            errorDialog.setMessage(caught.getMessage());
          }
          errorDialog.center();
        }
      };
      UserAndRoleMgmtService.instance().setRoles(selectedUsers[0], (ProxyPentahoRole[])assignedRoles.toArray(new ProxyPentahoRole[0]), callback);
    }
  }
  
  private void assignedRoleSelectionChanged() {
    deleteRoleAssignmentBtn.setEnabled(assignedRolesList.getSelectedRoles().length > 0);
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
    addRoleAssignmentBtn.setEnabled(selectedUsers.length == 1);
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
    assignedRoleSelectionChanged();
	}
	
	private void updateUserDetails( final Widget sender ) {
	  if (!userDetailsPanel.getPassword().equals(userDetailsPanel.getPasswordConfirmation())) { 
	    errorDialog.setText(MSGS.updateUser());
      errorDialog.setMessage(MSGS.passwordConfirmationFailed());
      errorDialog.center();
	  } else {
      ((Button)sender).setEnabled( false );
	    final ProxyPentahoUser user = userDetailsPanel.getUser();
	    
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        usersList.addUser(user);
	        ((Button)sender).setEnabled( true );
	      }

	      public void onFailure(Throwable caught) {
          errorDialog.setText(MSGS.updateUser());
          if (caught instanceof PentahoSecurityException) {
            errorDialog.setMessage(MSGS.insufficientPrivileges());
          } else if (caught instanceof NonExistingUserException) {
            errorDialog.setMessage(MSGS.userDoesNotExist(caught.getMessage()));
          } else {
            errorDialog.setMessage(caught.getMessage());
          }
          errorDialog.center();
          ((Button)sender).setEnabled( true );
	      }
	    }; // end AsyncCallback
	    UserAndRoleMgmtService.instance().updateUser(user, callback);
	  }
	}
	
	public void onChange(Widget sender) {
	  if (sender == usersList) {
	    userSelectionChanged();
	  } else if (sender == assignedRolesList) {
	    assignedRoleSelectionChanged();
	  }
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
    } else if ((sender == roleAssignmentsDialog) && roleAssignmentsDialog.getRoleAssignmentsModified()) {
      assignedRolesList.setRoles(UserAndRoleMgmtService.instance().getRoles(roleAssignmentsDialog.getUser()));
      assignedRoleSelectionChanged();
    }
  }

  public void onKeyDown(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    if (filterTextBox == sender) {
      usersList.setFilter(new UserNameFilter(filterTextBox.getText()));
      userSelectionChanged();
    }
  }
  
}