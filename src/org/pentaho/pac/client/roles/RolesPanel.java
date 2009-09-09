/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.buttons.ImageButton;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.IListBoxFilter;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.users.UserAssignmentsDialogBox;
import org.pentaho.pac.client.users.UsersList;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
  
  
  HTML errorMsgHtml = new HTML();
  PromptDialogBox errorDialog = new PromptDialogBox(Messages.getString("error"), Messages.getString("ok"), null, false, true, errorMsgHtml); //$NON-NLS-1$ //$NON-NLS-2$
  RolesList rolesList = new RolesList(true);
  UsersList assignedUsersList = new UsersList(true);
  RoleDetailsPanel roleDetailsPanel = new RoleDetailsPanel();
  Button updateRoleBtn = new Button(Messages.getString("update")); //$NON-NLS-1$
  ImageButton addRoleBtn = new ImageButton("style/images/add.png", "style/images/add_disabled.png", Messages.getString("addRole"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  ImageButton deleteRoleBtn = new ImageButton("style/images/remove.png", "style/images/remove_disabled.png", Messages.getString("deleteRoles"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
  ImageButton addRoleAssignmentBtn = new ImageButton("style/images/add.png", "style/images/add_disabled.png", Messages.getString("assignRoles"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
  ImageButton deleteRoleAssignmentBtn = new ImageButton("style/images/remove.png", "style/images/remove_disabled.png", Messages.getString("unassignRoles"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  TextBox filterTextBox = new TextBox();
  NewRoleDialogBox newRoleDialogBox = new NewRoleDialogBox();
  ConfirmDialog confirmDeleteRolesDialog = new ConfirmDialog();
  ConfirmDialog confirmRemoveRoleAssignmentDialog = new ConfirmDialog();
  
  UserAssignmentsDialogBox userAssignmentsDialog = new UserAssignmentsDialogBox();
  
  @SuppressWarnings("unchecked")
	public RolesPanel() {
	  DockPanel roleListPanel = buildRolesListPanel();
	  roleListPanel.setStyleName("borderPane"); //$NON-NLS-1$
	  
	  DockPanel roleDetailsDockPanel = buildRoleDetailsPanel();
	  roleDetailsDockPanel.setStyleName("borderPane"); //$NON-NLS-1$
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
    
    confirmDeleteRolesDialog.setText(Messages.getString("deleteRoles")); //$NON-NLS-1$
    confirmDeleteRolesDialog.setMessage(Messages.getString("confirmRoleDeletionMsg")); //$NON-NLS-1$
    confirmDeleteRolesDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmDeleteRolesDialog.hide();
        deleteSelectedRoles();
        assignedUserSelectionChanged();
      }
    });
    
    confirmRemoveRoleAssignmentDialog.setText(Messages.getString("unassignUsers")); //$NON-NLS-1$
    confirmRemoveRoleAssignmentDialog.setMessage(Messages.getString("confirmUnassignUsersMsg")); //$NON-NLS-1$
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
	  dockPanel.add(new Label(Messages.getString("userDetails")), DockPanel.NORTH); //$NON-NLS-1$
	  dockPanel.add(roleDetailsPanel, DockPanel.NORTH);
    dockPanel.add(updateRoleBtn, DockPanel.NORTH);
    
    SimplePanel spacerPanel = new SimplePanel();
    spacerPanel.add(new Label(" ")); //$NON-NLS-1$
    dockPanel.add(spacerPanel, DockPanel.NORTH);
    dockPanel.setCellHeight(spacerPanel, "10px"); //$NON-NLS-1$
    
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
    VerticalPanel spacer = new VerticalPanel();
    spacer.setWidth("2"); //$NON-NLS-1$
    headerDockPanel.add(spacer, DockPanel.EAST);
	  headerDockPanel.add(addRoleBtn, DockPanel.EAST);
    Label label = new Label(Messages.getString("roles")); //$NON-NLS-1$
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    DockPanel roleListPanel = new DockPanel();
    roleListPanel.add(headerDockPanel, DockPanel.NORTH);
    roleListPanel.add(rolesList, DockPanel.CENTER);
    roleListPanel.add(filterTextBox, DockPanel.SOUTH  );
    roleListPanel.add(new Label(Messages.getString("filter")), DockPanel.SOUTH ); //$NON-NLS-1$

    roleListPanel.setCellHeight(rolesList, "100%"); //$NON-NLS-1$
    roleListPanel.setCellWidth(rolesList, "100%"); //$NON-NLS-1$
    roleListPanel.setHeight("100%"); //$NON-NLS-1$
    roleListPanel.setWidth("100%"); //$NON-NLS-1$
    rolesList.setHeight("100%"); //$NON-NLS-1$
    rolesList.setWidth("100%"); //$NON-NLS-1$
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
    VerticalPanel spacer = new VerticalPanel();
    spacer.setWidth("2"); //$NON-NLS-1$
    headerDockPanel.add(spacer, DockPanel.EAST);
    headerDockPanel.add(addRoleAssignmentBtn, DockPanel.EAST);
    Label label = new Label(Messages.getString("assignedUsers")); //$NON-NLS-1$
    headerDockPanel.add(label, DockPanel.WEST);
    headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
    
    DockPanel assignedUsersPanel = new DockPanel();
    assignedUsersPanel.add(headerDockPanel, DockPanel.NORTH);
    assignedUsersPanel.add(assignedUsersList, DockPanel.CENTER);
    assignedUsersPanel.setCellHeight(assignedUsersList, "100%"); //$NON-NLS-1$
    assignedUsersPanel.setCellWidth(assignedUsersList, "100%"); //$NON-NLS-1$
    assignedUsersList.setHeight("100%"); //$NON-NLS-1$
    assignedUsersList.setWidth("100%"); //$NON-NLS-1$
    
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
	
	@SuppressWarnings("unchecked")
	private void deleteSelectedRoles() {
	  final List<ProxyPentahoRole> selectedRoles = rolesList.getSelectedObjects();
	  if (selectedRoles.size() > 0) {
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        rolesList.removeObjects(selectedRoles);
	        roleSelectionChanged();
	      }

	      public void onFailure(Throwable caught) {
	        errorDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
	        errorMsgHtml.setHTML(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorDeletingRoles")));           //$NON-NLS-1$
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
      
      AsyncCallback<Object> callback = new AsyncCallback<Object>() {
        public void onSuccess(Object result) {
          assignedUsersList.removeObjects(usersToUnassign);
        }

        public void onFailure(Throwable caught) {
          errorDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
          errorMsgHtml.setHTML(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorUnassigningSelectedUsersFromRole")));           //$NON-NLS-1$
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
	
	@SuppressWarnings("unchecked")
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
        errorDialog.setText(ExceptionParser.getErrorHeader(caught.getMessage()));
        errorMsgHtml.setHTML(ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorUpdatingRole")));           //$NON-NLS-1$
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