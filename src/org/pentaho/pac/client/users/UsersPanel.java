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
package org.pentaho.pac.client.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pentaho.gwt.widgets.client.buttons.ImageButton;
import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.UserAndRoleMgmtService;
import org.pentaho.pac.client.common.ui.IListBoxFilter;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.roles.RoleAssignmentsDialogBox;
import org.pentaho.pac.client.roles.RolesList;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UsersPanel extends HorizontalPanel implements ClickListener, ChangeListener, PopupListener, KeyboardListener {
  
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
  
  UsersList usersList = new UsersList(true);
  RolesList assignedRolesList = new RolesList(true);
  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  Button updateUserBtn = new Button(Messages.getString("update")); //$NON-NLS-1$
  ImageButton addUserBtn = new ImageButton("style/images/add.png", "style/images/add_disabled.png", Messages.getString("addUser"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  ImageButton deleteUserBtn = new ImageButton("style/images/remove.png", "style/images/remove_disabled.png", Messages.getString("deleteUsers"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
  ImageButton addRoleAssignmentBtn = new ImageButton("style/images/add.png", "style/images/add_disabled.png", Messages.getString("assignRoles"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
  ImageButton deleteRoleAssignmentBtn = new ImageButton("style/images/remove.png", "style/images/remove_disabled.png", Messages.getString("unassignRoles"), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
  TextBox filterTextBox = new TextBox();
  NewUserDialogBox newUserDialogBox = new NewUserDialogBox();
  ConfirmDialog confirmDeleteUsersDialog = new ConfirmDialog();
  ConfirmDialog confirmRemoveRoleAssignmentDialog = new ConfirmDialog();
  
  RoleAssignmentsDialogBox roleAssignmentsDialog = new RoleAssignmentsDialogBox();
  
  @SuppressWarnings("unchecked")
	public UsersPanel() {
	  //User List Panel
	  VerticalPanel userListPanel = buildUsersListPanel();
	  userListPanel.setStyleName("borderPane"); //$NON-NLS-1$
	  // CSS this userListPanel.setSpacing(4);
	  
	  //User Details Panel
	  VerticalPanel userDetailsDockPanel = buildUserDetailsDockPanel();
	  userDetailsDockPanel.setStyleName("borderPane"); //$NON-NLS-1$
    // CSS this userDetailsDockPanel.setSpacing(4);
    
    add(userListPanel);
    add(userDetailsDockPanel);
    
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
    
    newUserDialogBox.setCallback(new IDialogCallback() {
      public void cancelPressed() {
      }

      public void okPressed() {
        if (newUserDialogBox.isUserCreated()) {
          ProxyPentahoUser newUser = newUserDialogBox.getUser();
          IListBoxFilter filter = usersList.getFilter();
          if ((filter != null) && !filter.accepts(newUser)){
            filterTextBox.setText(""); //$NON-NLS-1$
            usersList.setFilter(null);
          }
          usersList.addObject(newUser);
          usersList.setSelectedObject(newUser);
          userSelectionChanged();
          // default roles might have been added; update assigned roles list
          List<ProxyPentahoRole> roleList = Arrays.asList(UserAndRoleMgmtService.instance().getRoles(newUser));
          assignedRolesList.setObjects(roleList);
          assignedRoleSelectionChanged();
        }      }
      
    });
    roleAssignmentsDialog.addPopupListener(this);
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
    
    confirmDeleteUsersDialog.setText(Messages.getString("deleteUsers")); //$NON-NLS-1$
    confirmDeleteUsersDialog.setMessage(Messages.getString("confirmUserDeletionMsg")); //$NON-NLS-1$
    confirmDeleteUsersDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmDeleteUsersDialog.hide();
        deleteSelectedUsers();
        assignedRoleSelectionChanged();
      }
    });
    
    confirmRemoveRoleAssignmentDialog.setText(Messages.getString("unassignRoles")); //$NON-NLS-1$
    confirmRemoveRoleAssignmentDialog.setMessage(Messages.getString("confirmUnassignRolesMsg")); //$NON-NLS-1$
    confirmRemoveRoleAssignmentDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        confirmRemoveRoleAssignmentDialog.hide();
        unassignSelectedRoles();
        assignedRoleSelectionChanged();
      }
    });
 	}

	public VerticalPanel buildUserDetailsDockPanel() {
	  
	  VerticalPanel assignedRolesPanel = buildAssignedRolesPanel();
    
    VerticalPanel mainUserDetailsPanel = new VerticalPanel();
    

    VerticalPanel userPanelFieldsetContent = new VerticalPanel();
    userPanelFieldsetContent.setSpacing(4);
    userPanelFieldsetContent.add(userDetailsPanel);
    userPanelFieldsetContent.add(updateUserBtn );
    userPanelFieldsetContent.setCellWidth(userDetailsPanel, "100%"); //$NON-NLS-1$
    
    userPanelFieldsetContent.setCellHorizontalAlignment(updateUserBtn, VerticalPanel.ALIGN_RIGHT);
    
    VerticalPanel fieldsetPanel = new VerticalPanel();
    fieldsetPanel.add(new Label(Messages.getString("userDetails"))); //$NON-NLS-1$
    fieldsetPanel.add(userPanelFieldsetContent);
    userPanelFieldsetContent.setWidth("100%"); //$NON-NLS-1$

    fieldsetPanel.setWidth("100%"); //$NON-NLS-1$
    
    mainUserDetailsPanel.add(fieldsetPanel);  
    mainUserDetailsPanel.setCellWidth(fieldsetPanel, "100%"); //$NON-NLS-1$
    SimplePanel spacerPanel = new SimplePanel();
    spacerPanel.add(new Label(" ")); //$NON-NLS-1$
    mainUserDetailsPanel.add(spacerPanel);
    mainUserDetailsPanel.setCellHeight(spacerPanel, "10px");  //$NON-NLS-1$
    
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
	
	public VerticalPanel buildUsersListPanel() {
	  DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteUserBtn, DockPanel.EAST);
    VerticalPanel spacer = new VerticalPanel();
    spacer.setWidth("2"); //$NON-NLS-1$
    headerDockPanel.add(spacer, DockPanel.EAST);
	  headerDockPanel.add(addUserBtn, DockPanel.EAST);
    Label label = new Label("Users"); //$NON-NLS-1$
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%"); //$NON-NLS-1$
	  VerticalPanel userListPanel = new VerticalPanel();
    userListPanel.add(headerDockPanel);
    userListPanel.add(usersList);    
    userListPanel.add(new Label(Messages.getString("filter"))); //$NON-NLS-1$
    userListPanel.add(filterTextBox);
    
    userListPanel.setCellHeight(usersList, "100%"); //$NON-NLS-1$
    userListPanel.setCellWidth(usersList, "100%"); //$NON-NLS-1$
    userListPanel.setHeight("100%"); //$NON-NLS-1$
    userListPanel.setWidth("100%"); //$NON-NLS-1$
    usersList.setHeight("100%"); //$NON-NLS-1$
    usersList.setWidth("100%"); //$NON-NLS-1$
    filterTextBox.setWidth( "100%" ); //$NON-NLS-1$
    deleteUserBtn.setEnabled(false);
    
    filterTextBox.addKeyboardListener( this );
    usersList.addChangeListener(this);
    addUserBtn.addClickListener(this);
    deleteUserBtn.addClickListener(this);
    return userListPanel;
	}
	
	public VerticalPanel buildAssignedRolesPanel() {
    DockPanel headerDockPanel = new DockPanel();
    
    VerticalPanel fieldsetPanel = new VerticalPanel();
    
    Label label = new Label(Messages.getString("assignedRoles")); //$NON-NLS-1$
    Label spacer = new Label(""); //$NON-NLS-1$
    
    
    headerDockPanel.add(label, DockPanel.WEST);
    headerDockPanel.setCellWidth(label, "100%");  //$NON-NLS-1$
    headerDockPanel.add(deleteRoleAssignmentBtn, DockPanel.EAST);
    VerticalPanel spacer2 = new VerticalPanel();
    spacer2.setWidth("2"); //$NON-NLS-1$
    headerDockPanel.add(spacer2, DockPanel.EAST);
    headerDockPanel.add(addRoleAssignmentBtn, DockPanel.EAST);
    
    headerDockPanel.add(spacer, DockPanel.WEST);
    headerDockPanel.setCellWidth(spacer, "100%"); //$NON-NLS-1$
    
    
	  DockPanel assignedRolesPanel = new DockPanel();
	  assignedRolesPanel.add(headerDockPanel, DockPanel.NORTH);
	  assignedRolesPanel.add(assignedRolesList, DockPanel.CENTER);
	  assignedRolesPanel.setCellHeight(assignedRolesList, "100%"); //$NON-NLS-1$
	  assignedRolesPanel.setCellWidth(assignedRolesList, "100%"); //$NON-NLS-1$
	  assignedRolesList.setHeight("100%"); //$NON-NLS-1$
	  assignedRolesList.setWidth("100%"); //$NON-NLS-1$
	  
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
	    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
	      public void onSuccess(Boolean result) {
	        usersList.removeObjects(selectedUsers);
	        userSelectionChanged();
	      }

	      public void onFailure(Throwable caught) {
          MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorDeletingUsers")), false, false, true); //$NON-NLS-1$
          messageDialog.center();
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
      
      AsyncCallback<Object> callback = new AsyncCallback<Object>() {
        public void onSuccess(Object result) {
          assignedRolesList.removeObjects(rolesToUnassign);
        }

        public void onFailure(Throwable caught) {
          MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorUnassigningRoles")), false, false, true); //$NON-NLS-1$
          messageDialog.center();
        }
      };
      UserAndRoleMgmtService.instance().setRoles(selectedUsers.get(0), (ProxyPentahoRole[])assignedRoles.toArray(new ProxyPentahoRole[0]), callback);
    }
  }
  
  private void assignedRoleSelectionChanged() {
    if (assignedRolesList.getSelectedObjects().size() > 0){
      deleteRoleAssignmentBtn.setEnabled(true);
    }else{
      deleteRoleAssignmentBtn.setEnabled(false);
    }
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
    
    if (selectedUsers.size() > 0){
      deleteUserBtn.setEnabled(true);
    }else{
      deleteUserBtn.setEnabled(false);
    }
    
    if (selectedUsers.size() == 1){
      addRoleAssignmentBtn.setEnabled(true);
    }else{
      addRoleAssignmentBtn.setEnabled(false);
    }
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
    assignedRoleSelectionChanged();
	}
	
	@SuppressWarnings("unchecked")
	private void updateUserDetails( final Widget sender ) {
	  if (!userDetailsPanel.getPassword().equals(userDetailsPanel.getPasswordConfirmation())) { 
	    MessageDialogBox errorDialog = new MessageDialogBox(Messages.getString("updateUser"), Messages.getString("passwordConfirmationFailed"), false, false, true);  //$NON-NLS-1$//$NON-NLS-2$
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
	        MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorUpdatingUser")), false, false, true); //$NON-NLS-1$
	        messageDialog.center();
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
    if (roleAssignmentsDialog.getRoleAssignmentsModified()) {
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