package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
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

  MessageDialog messageDialog = new MessageDialog("", new int[]{MessageDialog.OK_BTN}); //$NON-NLS-1$
  RolesList rolesList = new RolesList(true);
  UsersList assignedUsersList = new UsersList(true);
  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  Button updateRoleBtn = new Button(PentahoAdminConsole.getLocalizedMessages().update());
  Button addRoleBtn = new Button("+"); //$NON-NLS-1$
  Button deleteRoleBtn = new Button("-"); //$NON-NLS-1$
  TextBox filterTextBox = new TextBox();
  NewRoleDialogBox newRoleDialogBox = new NewRoleDialogBox();
  MessageDialog confirmRoleDeleteDialog = new MessageDialog(PentahoAdminConsole.getLocalizedMessages().deleteRoles(), PentahoAdminConsole.getLocalizedMessages().confirmRoleDeletionMsg(), new int[] {MessageDialog.OK_BTN, MessageDialog.CANCEL_BTN});
  
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
    confirmRoleDeleteDialog.addPopupListener(this);
    
    roleDetailsPanel.getRoleNameTextBox().setEnabled(false);
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
    Label label = new Label(PentahoAdminConsole.getLocalizedMessages().roles());
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    DockPanel roleListPanel = new DockPanel();
    roleListPanel.add(headerDockPanel, DockPanel.NORTH);
    roleListPanel.add(rolesList, DockPanel.CENTER);
    roleListPanel.add(filterTextBox, DockPanel.SOUTH  );
    roleListPanel.add(new Label(PentahoAdminConsole.getLocalizedMessages().roleListFilter()), DockPanel.SOUTH );

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
	  DockPanel assignedRolesPanel = new DockPanel();
	  assignedRolesPanel.add(new Label(PentahoAdminConsole.getLocalizedMessages().assignedUsers()), DockPanel.NORTH);
	  assignedRolesPanel.add(assignedUsersList, DockPanel.CENTER);
	  assignedRolesPanel.setCellHeight(assignedUsersList, "100%"); //$NON-NLS-1$
	  assignedRolesPanel.setCellWidth(assignedUsersList, "100%"); //$NON-NLS-1$
	  assignedUsersList.setHeight("100%"); //$NON-NLS-1$
	  assignedUsersList.setWidth("100%"); //$NON-NLS-1$
	  return assignedRolesPanel;
	}
	
	
	public void onClick(Widget sender) {
	  if (sender == updateRoleBtn) {
	    updateRoleDetails(sender);
	  } else if (sender == deleteRoleBtn) {
      if (rolesList.getSelectedRoles().length > 0) {
        confirmRoleDeleteDialog.center();
      }
	  } else if (sender == addRoleBtn) {
	    addNewRole();
	  }
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
          messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().deleteRoles());
          if (caught instanceof PentahoSecurityException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().insufficientPrivileges());
          } else if (caught instanceof NonExistingRoleException) {
            messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().roleDoesNotExist(caught.getMessage()));
          } else {
            messageDialog.setMessage(caught.getMessage());
          }
          messageDialog.center();
	      }
	    };
	    UserAndRoleMgmtService.instance().deleteRoles(selectedRoles, callback);
	  }
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
    
    roleDetailsPanel.getRoleNameTextBox().setEnabled(false);
	}
	
	private void updateRoleDetails(final Widget sender) {
    final ProxyPentahoRole role = roleDetailsPanel.getRole();
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        rolesList.addRole(role);
      }

      public void onFailure(Throwable caught) {
        messageDialog.setText(PentahoAdminConsole.getLocalizedMessages().updateRole());
        if (caught instanceof PentahoSecurityException) {
          messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().insufficientPrivileges());
        } else if (caught instanceof NonExistingRoleException) {
          messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().roleDoesNotExist(caught.getMessage()));
        } else if (caught instanceof NonExistingUserException) {
          messageDialog.setMessage(PentahoAdminConsole.getLocalizedMessages().cantAssignNonexistingUserToRole(caught.getMessage()));
        } else {
          messageDialog.setMessage(caught.getMessage());
        }
        messageDialog.center();
        ((Button)sender).setEnabled( true );
      }
    };
    UserAndRoleMgmtService.instance().updateRole(role, callback);
	}
	
	public void onChange(Widget sender) {
	  roleSelectionChanged();
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
    } else if ((sender == confirmRoleDeleteDialog) && (confirmRoleDeleteDialog.getButtonPressed() == MessageDialog.OK_BTN)) {
      deleteSelectedRoles();
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