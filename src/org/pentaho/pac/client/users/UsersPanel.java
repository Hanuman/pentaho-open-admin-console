package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.containers.SimpleGroupBox;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.IListBoxFilter;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
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
import com.google.gwt.user.client.ui.VerticalPanel;
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
  Button addRoleAssignmentBtn = new Button("+"); //$NON-NLS-1$
  Button deleteRoleAssignmentBtn = new Button("-"); //$NON-NLS-1$
  TextBox filterTextBox = new TextBox();
  NewUserDialogBox newUserDialogBox = new NewUserDialogBox();
  ConfirmDialog confirmDeleteUsersDialog = new ConfirmDialog();
  ConfirmDialog confirmRemoveRoleAssignmentDialog = new ConfirmDialog();
  
  RoleAssignmentsDialogBox roleAssignmentsDialog = new RoleAssignmentsDialogBox();
  
	public UsersPanel() {
	  DockPanel userListPanel = buildUsersListPanel();
	  userListPanel.setStyleName("borderPane"); //$NON-NLS-1$
	  userListPanel.setSpacing(4);
	  
	  VerticalPanel userDetailsDockPanel = buildUserDetailsDockPanel();

	  userDetailsDockPanel.setStyleName("borderPane"); //$NON-NLS-1$
    userDetailsDockPanel.setSpacing(4);
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
    confirmDeleteUsersDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmDeleteUsersDialog.hide();
        deleteSelectedUsers();
        assignedRoleSelectionChanged();
      }
    });
    
    confirmRemoveRoleAssignmentDialog.setText(MSGS.assignedRoles());
    confirmRemoveRoleAssignmentDialog.setMessage(MSGS.confirmRemoveRoleAssignmentMsg());
    confirmRemoveRoleAssignmentDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmRemoveRoleAssignmentDialog.hide();
        unassignSelectedRoles();
        assignedRoleSelectionChanged();
      }
    });
 	}

	public VerticalPanel buildUserDetailsDockPanel() {
	  
	  SimpleGroupBox assignedRolesPanel = buildAssignedRolesPanel();
    
    VerticalPanel mainUserDetailsPanel = new VerticalPanel();
    

    VerticalPanel userPanelFieldsetContent = new VerticalPanel();
    userPanelFieldsetContent.setSpacing(4);
    userPanelFieldsetContent.add(userDetailsPanel);
    userPanelFieldsetContent.add(updateUserBtn );
    userPanelFieldsetContent.setCellWidth(userDetailsPanel, "100%"); //$NON-NLS-1$
    
    userPanelFieldsetContent.setCellHorizontalAlignment(updateUserBtn, VerticalPanel.ALIGN_RIGHT);
    
    SimpleGroupBox fieldsetPanel = new SimpleGroupBox(MSGS.userDetails());
    fieldsetPanel.add(userPanelFieldsetContent);
    userPanelFieldsetContent.setWidth("100%"); //$NON-NLS-1$
    
    mainUserDetailsPanel.add(fieldsetPanel);
    mainUserDetailsPanel.add(assignedRolesPanel);
	  
    mainUserDetailsPanel.setCellHorizontalAlignment(updateUserBtn, HasHorizontalAlignment.ALIGN_RIGHT);
    
    mainUserDetailsPanel.setCellHeight(assignedRolesPanel, "100%"); //$NON-NLS-1$
    mainUserDetailsPanel.setCellWidth(assignedRolesPanel, "100%"); //$NON-NLS-1$
	  
	  userDetailsPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setHeight("100%"); //$NON-NLS-1$
	  
    updateUserBtn.addClickListener(this);
	  return mainUserDetailsPanel;
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
	
	public SimpleGroupBox buildAssignedRolesPanel() {
    DockPanel headerDockPanel = new DockPanel();
    
    SimpleGroupBox fieldsetPanel = new SimpleGroupBox(MSGS.assignedRoles());
    
    headerDockPanel.add(deleteRoleAssignmentBtn, DockPanel.EAST);
    headerDockPanel.add(addRoleAssignmentBtn, DockPanel.EAST);
    Label spacer = new Label(""); //$NON-NLS-1$
    headerDockPanel.add(spacer, DockPanel.WEST);
    headerDockPanel.setCellWidth(spacer, "100%"); //$NON-NLS-1$
    
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
	  

    fieldsetPanel.add(assignedRolesPanel);
    assignedRolesPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setHeight("100%"); //$NON-NLS-1$
	  return fieldsetPanel;
	}
	
	public void onClick(Widget sender) {
	  if (sender == updateUserBtn) {
	    updateUserDetails( sender );
	  } else if (sender == deleteUserBtn) {
	    if (usersList.getSelectedObjects().size() > 0) {
	      confirmDeleteUsersDialog.center();
	    }
	  } else if (sender == addUserBtn) {
	    addNewUser();
	  } else if (sender == deleteRoleAssignmentBtn) {
	    if (assignedRolesList.getSelectedObjects().size() > 0) {
        confirmRemoveRoleAssignmentDialog.center();
      }
	  } else if (sender == addRoleAssignmentBtn) {
	    List<ProxyPentahoUser> selectedUsers = usersList.getSelectedObjects();
      if (selectedUsers.size() == 1) {
        modifyRoleAssignments(selectedUsers.get(0));
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
	  final List<ProxyPentahoUser> selectedUsers = usersList.getSelectedObjects();
	  if (selectedUsers.size() > 0) {
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        usersList.removeObjects(selectedUsers);
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
	    UserAndRoleMgmtService.instance().deleteUsers(selectedUsers.toArray(new ProxyPentahoUser[0]), callback);
	  }
	}
	
  private void unassignSelectedRoles() {
    List<ProxyPentahoUser> selectedUsers = usersList.getSelectedObjects();
    if (selectedUsers.size() == 1) {
      ArrayList<ProxyPentahoRole> assignedRoles = new ArrayList<ProxyPentahoRole>();
      assignedRoles.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getRoles(selectedUsers.get(0))));
      final List<ProxyPentahoRole> rolesToUnassign = assignedRolesList.getSelectedObjects();
      assignedRoles.removeAll(rolesToUnassign);      
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          assignedRolesList.removeObjects(rolesToUnassign);
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
      UserAndRoleMgmtService.instance().setRoles(selectedUsers.get(0), (ProxyPentahoRole[])assignedRoles.toArray(new ProxyPentahoRole[0]), callback);
    }
  }
  
  private void assignedRoleSelectionChanged() {
    deleteRoleAssignmentBtn.setEnabled(assignedRolesList.getSelectedObjects().size() > 0);
  }
  
	private void userSelectionChanged() {
    List<ProxyPentahoUser> selectedUsers = usersList.getSelectedObjects();
    if (selectedUsers.size() == 1) {
      userDetailsPanel.setUser(selectedUsers.get(0));
      List<ProxyPentahoRole>roleList = Arrays.asList(UserAndRoleMgmtService.instance().getRoles(selectedUsers.get(0)));
      assignedRolesList.setObjects(roleList);
    } else {
      userDetailsPanel.setUser(null);
      List<ProxyPentahoRole> emptyRoleList = Collections.emptyList();
      assignedRolesList.setObjects(emptyRoleList);
    }
    userDetailsPanel.setEnabled(selectedUsers.size() == 1);
    updateUserBtn.setEnabled(selectedUsers.size() == 1);
    deleteUserBtn.setEnabled(selectedUsers.size() > 0);
    addRoleAssignmentBtn.setEnabled(selectedUsers.size() == 1);
    
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
	        // Since users are compared by name, removeObject() will remove the old user from the list
	        // and addObject() will add the new user to the list.
	        usersList.removeObject(user);
	        usersList.addObject(user);
	        usersList.setSelectedObject(user);
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
    List<ProxyPentahoUser> userList = Arrays.asList(UserAndRoleMgmtService.instance().getUsers());
    usersList.setObjects(userList);
	  userSelectionChanged();
	}
	
  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if ((sender == newUserDialogBox) && newUserDialogBox.isUserCreated()) {
      ProxyPentahoUser newUser = newUserDialogBox.getUser();
      IListBoxFilter filter = usersList.getFilter();
      if ((filter != null) && !filter.accepts(newUser)){
        filterTextBox.setText(""); //$NON-NLS-1$
        usersList.setFilter(null);
      }
      usersList.addObject(newUser);
      usersList.setSelectedObject(newUser);
      userSelectionChanged();
    } else if ((sender == roleAssignmentsDialog) && roleAssignmentsDialog.getRoleAssignmentsModified()) {
      List<ProxyPentahoRole>roleList = Arrays.asList(UserAndRoleMgmtService.instance().getRoles(roleAssignmentsDialog.getUser()));
      assignedRolesList.setObjects(roleList);
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