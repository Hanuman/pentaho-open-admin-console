package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.ICallbackHandler;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.users.AddUserAssignmentsDialogBox;
import org.pentaho.pac.client.users.UsersList;
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

public class RolesPanel extends DockPanel implements ClickListener, ChangeListener, PopupListener, KeyboardListener {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  RolesList rolesList = new RolesList(true);
  UsersList assignedUsersList = new UsersList(true);
  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  Button updateRoleBtn = new Button(MSGS.update());
  Button addRoleBtn = new Button("+"); //$NON-NLS-1$
  Button deleteRoleBtn = new Button("-"); //$NON-NLS-1$
  Button addRoleAssignmentBtn = new Button("+");
  Button deleteRoleAssignmentBtn = new Button("-");
  TextBox filterTextBox = new TextBox();
  NewRoleDialogBox newRoleDialogBox = new NewRoleDialogBox();
  ConfirmDialog confirmDeleteRolesDialog = new ConfirmDialog();
  ConfirmDialog confirmRemoveRoleAssignmentDialog = new ConfirmDialog();
  AddUserAssignmentsDialogBox addUserAssignmentsDialogBox = new AddUserAssignmentsDialogBox();
  
	public RolesPanel() {
	  DockPanel roleListPanel = buildRolesListPanel();
	  
	  DockPanel roleDetailsDockPanel = buildRoleDetailsPanel();
    add(roleListPanel, DockPanel.WEST);
    add(roleDetailsDockPanel, DockPanel.CENTER);
    
    setSpacing(10);
    
    setCellWidth(roleListPanel, "30%"); //$NON-NLS-1$
    setCellWidth(roleDetailsDockPanel, "70%"); //$NON-NLS-1$
    setCellHeight(roleListPanel, "100%"); //$NON-NLS-1$
    setCellHeight(roleDetailsDockPanel, "100%"); //$NON-NLS-1$
    roleListPanel.setWidth("100%"); //$NON-NLS-1$
    roleListPanel.setHeight("100%"); //$NON-NLS-1$
    roleDetailsDockPanel.setWidth("100%"); //$NON-NLS-1$
    roleDetailsDockPanel.setHeight("100%"); //$NON-NLS-1$
    
    roleDetailsPanel.setEnabled(false);
    updateRoleBtn.setEnabled(false);
    
    newRoleDialogBox.addPopupListener(this);
    addUserAssignmentsDialogBox.addPopupListener(this);
    
    roleDetailsPanel.getRoleNameTextBox().setEnabled(false);
    
    confirmDeleteRolesDialog.setText(MSGS.deleteRoles());
    confirmDeleteRolesDialog.setMessage(MSGS.confirmRoleDeletionMsg());
    final RolesPanel localThis = this;
    confirmDeleteRolesDialog.setOnOkHandler( new ICallbackHandler() {
      public void onHandle(Object o) {
        localThis.deleteSelectedRoles();
        localThis.assignedUserSelectionChanged();
      }
    });
    
    confirmRemoveRoleAssignmentDialog.setText(MSGS.assignedUsers());
    confirmRemoveRoleAssignmentDialog.setMessage(MSGS.confirmRemoveRoleAssignmentMsg());
    confirmRemoveRoleAssignmentDialog.setOnOkHandler( new ICallbackHandler() {
      public void onHandle(Object o) {
        localThis.unassignSelectedUsers();
        localThis.assignedUserSelectionChanged();
      }
    });
 	}

	public DockPanel buildRoleDetailsPanel() {
	  
    DockPanel assignedRolesPanel = buildAssignedUsersPanel();
    
	  DockPanel dockPanel = new DockPanel();
	  dockPanel.add(roleDetailsPanel, DockPanel.NORTH);
    dockPanel.add(updateRoleBtn, DockPanel.NORTH);
	  dockPanel.add(assignedRolesPanel, DockPanel.CENTER);
	  
	  dockPanel.setCellHorizontalAlignment(updateRoleBtn, HasHorizontalAlignment.ALIGN_RIGHT);
    
	  dockPanel.setCellWidth(roleDetailsPanel, "100%"); //$NON-NLS-1$
	  dockPanel.setCellHeight(assignedRolesPanel, "100%"); //$NON-NLS-1$
	  dockPanel.setCellWidth(assignedRolesPanel, "100%"); //$NON-NLS-1$
	  
	  roleDetailsPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setWidth("100%"); //$NON-NLS-1$
    assignedRolesPanel.setHeight("100%"); //$NON-NLS-1$
	  
    updateRoleBtn.addClickListener(this);
	  return dockPanel;
	}
	
	public DockPanel buildRolesListPanel() {
	  DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteRoleBtn, DockPanel.EAST);
	  headerDockPanel.add(addRoleBtn, DockPanel.EAST);
    Label label = new Label(MSGS.roles());
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    DockPanel roleListPanel = new DockPanel();
    roleListPanel.add(headerDockPanel, DockPanel.NORTH);
    roleListPanel.add(rolesList, DockPanel.CENTER);
    roleListPanel.add(filterTextBox, DockPanel.SOUTH  );
    roleListPanel.add(new Label(MSGS.roleListFilter()), DockPanel.SOUTH );

    roleListPanel.setCellHeight(rolesList, "100%"); //$NON-NLS-1$
    roleListPanel.setCellWidth(rolesList, "100%"); //$NON-NLS-1$
    roleListPanel.setHeight("100%"); //$NON-NLS-1$
    roleListPanel.setWidth("100%"); //$NON-NLS-1$
    rolesList.setHeight("100%"); //$NON-NLS-1$
    rolesList.setWidth("100%"); //$NON-NLS-1$
    addRoleBtn.setWidth("20px"); //$NON-NLS-1$
    deleteRoleBtn.setWidth("20px"); //$NON-NLS-1$
    addRoleBtn.setHeight("20px"); //$NON-NLS-1$
    deleteRoleBtn.setHeight("20px"); //$NON-NLS-1$
    filterTextBox.setWidth( "100%" ); //$NON-NLS-1$
    deleteRoleBtn.setEnabled(false);
    
    filterTextBox.addKeyboardListener( this );
    rolesList.addChangeListener(this);
    addRoleBtn.addClickListener(this);
    deleteRoleBtn.addClickListener(this);
    return roleListPanel;
	}
	
	public DockPanel buildAssignedUsersPanel() {
    DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteRoleAssignmentBtn, DockPanel.EAST);
    headerDockPanel.add(addRoleAssignmentBtn, DockPanel.EAST);
    Label label = new Label(MSGS.assignedUsers()); //$NON-NLS-1$
    headerDockPanel.add(label, DockPanel.WEST);
    headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    
    DockPanel assignedUsersPanel = new DockPanel();
    assignedUsersPanel.add(headerDockPanel, DockPanel.NORTH);
    assignedUsersPanel.add(assignedUsersList, DockPanel.CENTER);
    assignedUsersPanel.setCellHeight(assignedUsersList, "100%"); //$NON-NLS-1$
    assignedUsersPanel.setCellWidth(assignedUsersList, "100%"); //$NON-NLS-1$
    assignedUsersList.setHeight("100%"); //$NON-NLS-1$
    assignedUsersList.setWidth("100%"); //$NON-NLS-1$
    
    deleteRoleAssignmentBtn.setHeight("20px"); //$NON-NLS-1$
    deleteRoleAssignmentBtn.setWidth("20px"); //$NON-NLS-1$
    addRoleAssignmentBtn.setHeight("20px"); //$NON-NLS-1$
    addRoleAssignmentBtn.setWidth("20px"); //$NON-NLS-1$
    
    assignedUsersList.addChangeListener(this);
    deleteRoleAssignmentBtn.addClickListener(this);
    addRoleAssignmentBtn.addClickListener(this);
    return assignedUsersPanel;
	}
	
	
	public void onClick(Widget sender) {
	  if (sender == updateRoleBtn) {
	    updateRoleDetails(sender);
	  } else if (sender == deleteRoleBtn) {
      if (rolesList.getSelectedRoles().length > 0) {
        confirmDeleteRolesDialog.center();
      }
	  } else if (sender == addRoleBtn) {
	    addNewRole();
	  } else if (sender == deleteRoleAssignmentBtn) {
      if (assignedUsersList.getSelectedUsers().length > 0) {
        confirmRemoveRoleAssignmentDialog.center();
      }
    } else if (sender == addRoleAssignmentBtn) {
      ProxyPentahoRole[] selectedRoles = rolesList.getSelectedRoles();
      if (selectedRoles.length == 1) {
        addUserAssignments(selectedRoles[0]);
      }
    }
	}

  private void addUserAssignments(ProxyPentahoRole role) {
    addUserAssignmentsDialogBox.setRole(role);
    addUserAssignmentsDialogBox.center();
  }

	private void addNewRole() {
	  newRoleDialogBox.setUser(null);
    newRoleDialogBox.center();
   }
	
	private void deleteSelectedRoles() {
	  final ProxyPentahoRole[] selectedRoles = rolesList.getSelectedRoles();
	  if (selectedRoles.length > 0) {
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        rolesList.removeRoles(selectedRoles);
	        roleSelectionChanged();
	      }

	      public void onFailure(Throwable caught) {
          if (caught instanceof PentahoSecurityException) {
            errorDialog.setMessage(MSGS.insufficientPrivileges());
          } else if (caught instanceof NonExistingRoleException) {
            errorDialog.setMessage(MSGS.roleDoesNotExist(caught.getMessage()));
          } else {
            errorDialog.setMessage(caught.getMessage());
          }
          errorDialog.center();
	      }
	    };
	    UserAndRoleMgmtService.instance().deleteRoles(selectedRoles, callback);
	  }
	}
	
  private void unassignSelectedUsers() {
    ProxyPentahoRole[] selectedRoles = rolesList.getSelectedRoles();
    if (selectedRoles.length == 1) {
      ArrayList assignedUsers = new ArrayList();
      assignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(selectedRoles[0])));
      final ProxyPentahoUser[] usersToUnassign = assignedUsersList.getSelectedUsers();
      assignedUsers.removeAll(Arrays.asList(usersToUnassign));      
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          assignedUsersList.removeUsers(usersToUnassign);
        }

        public void onFailure(Throwable caught) {
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
      UserAndRoleMgmtService.instance().setUsers(selectedRoles[0], (ProxyPentahoUser[])assignedUsers.toArray(new ProxyPentahoUser[0]), callback);
    }
  }
  
  private void assignedUserSelectionChanged() {
    deleteRoleAssignmentBtn.setEnabled(assignedUsersList.getSelectedUsers().length > 0);
  }
  
	private void roleSelectionChanged() {
    ProxyPentahoRole[] selectedRoles = rolesList.getSelectedRoles();
    if (selectedRoles.length == 1) {
      roleDetailsPanel.setRole(selectedRoles[0]);
      assignedUsersList.setUsers(UserAndRoleMgmtService.instance().getUsers(selectedRoles[0]));
    } else {
      roleDetailsPanel.setRole(null);
      assignedUsersList.setUsers(new ProxyPentahoUser[0]);
    }
    roleDetailsPanel.setEnabled(selectedRoles.length == 1);
    updateRoleBtn.setEnabled(selectedRoles.length == 1);
    deleteRoleBtn.setEnabled(selectedRoles.length > 0);
    addRoleAssignmentBtn.setEnabled(selectedRoles.length == 1);
    
    roleDetailsPanel.getRoleNameTextBox().setEnabled(false);
    assignedUserSelectionChanged();
	}
	
	private void updateRoleDetails(final Widget sender) {
    final ProxyPentahoRole role = roleDetailsPanel.getRole();
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        rolesList.addRole(role);
      }

      public void onFailure(Throwable caught) {
        if (caught instanceof PentahoSecurityException) {
          errorDialog.setMessage(MSGS.insufficientPrivileges());
        } else if (caught instanceof NonExistingRoleException) {
          errorDialog.setMessage(MSGS.roleDoesNotExist(caught.getMessage()));
        } else if (caught instanceof NonExistingUserException) {
          errorDialog.setMessage(MSGS.cantAssignNonexistingUserToRole(caught.getMessage()));
        } else {
          errorDialog.setMessage(caught.getMessage());
        }
        errorDialog.center();
        ((Button)sender).setEnabled( true );
      }
    };
    UserAndRoleMgmtService.instance().updateRole(role, callback);
	}
	
	public void onChange(Widget sender) {
    if (sender == rolesList) {
      roleSelectionChanged();
    } else if (sender == assignedUsersList) {
      assignedUserSelectionChanged();
    }
	}
	
	public void refresh() {
	  rolesList.setRoles(UserAndRoleMgmtService.instance().getRoles());
	  roleSelectionChanged();
	}
	
  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if ((sender == newRoleDialogBox) && newRoleDialogBox.isRoleCreated()) {
      ProxyPentahoRole newRole = newRoleDialogBox.getRole();
      rolesList.addRole(newRole);
      rolesList.setSelectedRole(newRole);
      roleSelectionChanged();
    } else if ((sender == addUserAssignmentsDialogBox) && addUserAssignmentsDialogBox.getUsersAssigned()) {
      assignedUsersList.setUsers(UserAndRoleMgmtService.instance().getUsers(addUserAssignmentsDialogBox.getRole()));
      assignedUserSelectionChanged();
    }
  }
  
  public void onKeyDown(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    if (filterTextBox == sender) {
      rolesList.setRoleNameFilter(filterTextBox.getText());
      roleSelectionChanged();
    }
  }
  
}