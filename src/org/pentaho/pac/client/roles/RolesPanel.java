package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.IListBoxFilter;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.users.UserAssignmentsDialogBox;
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

  class RoleNameFilter implements IListBoxFilter {
    String roleNameFilter;
    
    RoleNameFilter(String roleNameFilter) {
      this.roleNameFilter = roleNameFilter;
    }
    
    public boolean accepts(Object object) {
      boolean result = false;
      if (object instanceof ProxyPentahoRole) {
        String roleName = ((ProxyPentahoRole)object).getName();
        result = ((roleNameFilter == null) || (roleNameFilter.length() == 0));
        if (!result) {
          int filterLen = roleNameFilter.length();
          result = ( filterLen <= roleName.length() )
            && roleNameFilter.toLowerCase().substring( 0, filterLen )
              .equals( roleName.toLowerCase().substring( 0, filterLen ) ); 
        }
      }
      return result;
    }  
  }
  
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  MessageDialog errorDialog = new MessageDialog( MSGS.error() );
  RolesList rolesList = new RolesList(true);
  UsersList assignedUsersList = new UsersList(true);
  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  Button updateRoleBtn = new Button(MSGS.update());
  Button addRoleBtn = new Button("+"); //$NON-NLS-1$
  Button deleteRoleBtn = new Button("-"); //$NON-NLS-1$
  Button addRoleAssignmentBtn = new Button("+"); //$NON-NLS-1$
  Button deleteRoleAssignmentBtn = new Button("-"); //$NON-NLS-1$
  TextBox filterTextBox = new TextBox();
  NewRoleDialogBox newRoleDialogBox = new NewRoleDialogBox();
  ConfirmDialog confirmDeleteRolesDialog = new ConfirmDialog();
  ConfirmDialog confirmRemoveRoleAssignmentDialog = new ConfirmDialog();
  
  UserAssignmentsDialogBox userAssignmentsDialog = new UserAssignmentsDialogBox();
  
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
    userAssignmentsDialog.addPopupListener(this);
    
    roleDetailsPanel.getRoleNameTextBox().setEnabled(false);
    
    confirmDeleteRolesDialog.setText(MSGS.deleteRoles());
    confirmDeleteRolesDialog.setMessage(MSGS.confirmRoleDeletionMsg());
    confirmDeleteRolesDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmDeleteRolesDialog.hide();
        deleteSelectedRoles();
        assignedUserSelectionChanged();
      }
    });
    
    confirmRemoveRoleAssignmentDialog.setText(MSGS.unassignUsers());
    confirmRemoveRoleAssignmentDialog.setMessage(MSGS.confirmUnassignUsersMsg());
    confirmRemoveRoleAssignmentDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmRemoveRoleAssignmentDialog.hide();
        unassignSelectedUsers();
        assignedUserSelectionChanged();
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
    roleListPanel.add(new Label(MSGS.filter()), DockPanel.SOUTH );

    roleListPanel.setCellHeight(rolesList, "100%"); //$NON-NLS-1$
    roleListPanel.setCellWidth(rolesList, "100%"); //$NON-NLS-1$
    roleListPanel.setHeight("100%"); //$NON-NLS-1$
    roleListPanel.setWidth("100%"); //$NON-NLS-1$
    rolesList.setHeight("100%"); //$NON-NLS-1$
    rolesList.setWidth("100%"); //$NON-NLS-1$
    addRoleBtn.setWidth("20px"); //$NON-NLS-1$
    addRoleBtn.setTitle(MSGS.addRole());
    deleteRoleBtn.setWidth("20px"); //$NON-NLS-1$
    deleteRoleBtn.setTitle(MSGS.deleteRoles());
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
    Label label = new Label(MSGS.assignedUsers());
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
    deleteRoleAssignmentBtn.setTitle(MSGS.unassignUsers());
    addRoleAssignmentBtn.setHeight("20px"); //$NON-NLS-1$
    addRoleAssignmentBtn.setWidth("20px"); //$NON-NLS-1$
    addRoleAssignmentBtn.setTitle(MSGS.assignUsers());
    
    assignedUsersList.addChangeListener(this);
    deleteRoleAssignmentBtn.addClickListener(this);
    addRoleAssignmentBtn.addClickListener(this);
    return assignedUsersPanel;
	}
	
	
	public void onClick(Widget sender) {
	  if (sender == updateRoleBtn) {
	    updateRoleDetails(sender);
	  } else if (sender == deleteRoleBtn) {
      if (rolesList.getSelectedObjects().size() > 0) {
        confirmDeleteRolesDialog.center();
      }
	  } else if (sender == addRoleBtn) {
	    addNewRole();
	  } else if (sender == deleteRoleAssignmentBtn) {
      if (assignedUsersList.getSelectedObjects().size() > 0) {
        confirmRemoveRoleAssignmentDialog.center();
      }
    } else if (sender == addRoleAssignmentBtn) {
      List<ProxyPentahoRole> selectedRoles = rolesList.getSelectedObjects();
      if (selectedRoles.size() == 1) {
        modifyUserAssignments(selectedRoles.get(0));
      }
    }
	}

  private void modifyUserAssignments(ProxyPentahoRole role) {
    userAssignmentsDialog.setRole(role);
    userAssignmentsDialog.center();
  }

	private void addNewRole() {
	  newRoleDialogBox.setUser(null);
    newRoleDialogBox.center();
   }
	
	private void deleteSelectedRoles() {
	  final List<ProxyPentahoRole> selectedRoles = rolesList.getSelectedObjects();
	  if (selectedRoles.size() > 0) {
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        rolesList.removeObjects(selectedRoles);
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
	    UserAndRoleMgmtService.instance().deleteRoles(selectedRoles.toArray(new ProxyPentahoRole[0]), callback);
	  }
	}
	
  private void unassignSelectedUsers() {
    List<ProxyPentahoRole> selectedRoles = rolesList.getSelectedObjects();
    if (selectedRoles.size() == 1) {
      ArrayList<ProxyPentahoUser> assignedUsers = new ArrayList<ProxyPentahoUser>();
      assignedUsers.addAll(Arrays.asList(UserAndRoleMgmtService.instance().getUsers(selectedRoles.get(0))));
      final List<ProxyPentahoUser> usersToUnassign = assignedUsersList.getSelectedObjects();
      assignedUsers.removeAll(usersToUnassign);      
      
      AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
          assignedUsersList.removeObjects(usersToUnassign);
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
      UserAndRoleMgmtService.instance().setUsers(selectedRoles.get(0), (ProxyPentahoUser[])assignedUsers.toArray(new ProxyPentahoUser[0]), callback);
    }
  }
  
  private void assignedUserSelectionChanged() {
    deleteRoleAssignmentBtn.setEnabled(assignedUsersList.getSelectedObjects().size() > 0);
  }
  
	private void roleSelectionChanged() {
    List<ProxyPentahoRole> selectedRoles = rolesList.getSelectedObjects();
    if (selectedRoles.size() == 1) {
      roleDetailsPanel.setRole(selectedRoles.get(0));
      List<ProxyPentahoUser>userList = Arrays.asList(UserAndRoleMgmtService.instance().getUsers(selectedRoles.get(0)));
      assignedUsersList.setObjects(userList);
    } else {
      roleDetailsPanel.setRole(null);
      List<ProxyPentahoUser> emptyUserList = Collections.emptyList();
      assignedUsersList.setObjects(emptyUserList);
    }
    roleDetailsPanel.setEnabled(selectedRoles.size() == 1);
    updateRoleBtn.setEnabled(selectedRoles.size() == 1);
    deleteRoleBtn.setEnabled(selectedRoles.size() > 0);
    addRoleAssignmentBtn.setEnabled(selectedRoles.size() == 1);
    
    roleDetailsPanel.getRoleNameTextBox().setEnabled(false);
    assignedUserSelectionChanged();
	}
	
	private void updateRoleDetails(final Widget sender) {
    final ProxyPentahoRole role = roleDetailsPanel.getRole();
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        // Since roles are compared by name, removeObject() will remove the old role from the list
        // and addObject() will add the new role to the list.
        rolesList.removeObject(role);
        rolesList.addObject(role);
        rolesList.setSelectedObject(role);
        ((Button)sender).setEnabled( true );
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
    ((Button)sender).setEnabled( false );
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
	  List<ProxyPentahoRole> roleList = Arrays.asList(UserAndRoleMgmtService.instance().getRoles());
	  rolesList.setObjects(roleList);
	  roleSelectionChanged();
	}
	
  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if ((sender == newRoleDialogBox) && newRoleDialogBox.isRoleCreated()) {
      ProxyPentahoRole newRole = newRoleDialogBox.getRole();
      IListBoxFilter filter = rolesList.getFilter();
      if ((filter != null) && !filter.accepts(newRole)){
        filterTextBox.setText(""); //$NON-NLS-1$
        rolesList.setFilter(null);
      }
      rolesList.addObject(newRole);
      rolesList.setSelectedObject(newRole);
      roleSelectionChanged();
    } else if ((sender == userAssignmentsDialog) && userAssignmentsDialog.getUserAssignmentsModified()) {
      List<ProxyPentahoUser>userList = Arrays.asList(UserAndRoleMgmtService.instance().getUsers(userAssignmentsDialog.getRole()));
      assignedUsersList.setObjects(userList);
      assignedUserSelectionChanged();
    }
  }
  
  public void onKeyDown(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    if (filterTextBox == sender) {
      rolesList.setFilter(new RoleNameFilter(filterTextBox.getText()));
      roleSelectionChanged();
    }
  }
  
}