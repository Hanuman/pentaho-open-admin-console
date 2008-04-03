package org.pentaho.pac.client.users;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacService;
import org.pentaho.pac.client.PacServiceAsync;
import org.pentaho.pac.client.PentahoSecurityException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UsersPanel extends DockPanel implements ClickListener, ChangeListener, PopupListener, KeyboardListener {

  MessageDialog messageDialog = new MessageDialog("", new int[]{MessageDialog.OK_BTN});
  UsersList usersList = new UsersList();
  ListBox assignedRolesList = new ListBox(true);
  ProxyPentahoUser[] users = null;
  UserDetailsPanel userDetailsPanel = new UserDetailsPanel();
  Button updateUserBtn = new Button("Update");
  PacServiceAsync pacService;
  Button addUserBtn = new Button("+");
  Button deleteUserBtn = new Button("-");
  TextBox filterTextBox = new TextBox();
  NewUserDialogBox newUserDialogBox = new NewUserDialogBox();
  MessageDialog confirmUserDeleteDialog = new MessageDialog("Delete Users", "Are your sure you want to delete the selected users.", new int[] {MessageDialog.OK_BTN, MessageDialog.CANCEL_BTN});
  
	public UsersPanel() {
	  DockPanel userListPanel = buildUsersListPanel();
	  
	  DockPanel userDetailsDockPanel = buildUserDetailsDockPanel();
    add(userListPanel, DockPanel.WEST);
    add(userDetailsDockPanel, DockPanel.CENTER);
    
    setSpacing(10);
    
    setCellWidth(userListPanel, "30%");
    setCellWidth(userDetailsDockPanel, "70%");
    setCellHeight(userListPanel, "100%");
    setCellHeight(userDetailsDockPanel, "100%");
    userListPanel.setWidth("100%");
    userListPanel.setHeight("100%");
    userDetailsDockPanel.setWidth("100%");
    userDetailsDockPanel.setHeight("100%");
    
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
    
	  dockPanel.setCellWidth(userDetailsPanel, "100%");
	  dockPanel.setCellHeight(assignedRolesPanel, "100%");
	  dockPanel.setCellWidth(assignedRolesPanel, "100%");
	  
	  userDetailsPanel.setWidth("100%");
    assignedRolesPanel.setWidth("100%");
    assignedRolesPanel.setHeight("100%");
	  
    updateUserBtn.addClickListener(this);
	  return dockPanel;
	}
	
	public DockPanel buildUsersListPanel() {
	  DockPanel headerDockPanel = new DockPanel();
    headerDockPanel.add(deleteUserBtn, DockPanel.EAST);
	  headerDockPanel.add(addUserBtn, DockPanel.EAST);
    Label label = new Label("Users");
	  headerDockPanel.add(label, DockPanel.WEST);
	  headerDockPanel.setCellWidth(label, "100%");
    DockPanel userListPanel = new DockPanel();
    userListPanel.add(headerDockPanel, DockPanel.NORTH);
    userListPanel.add(usersList, DockPanel.CENTER);
    
    userListPanel.add(filterTextBox, DockPanel.SOUTH  );
    userListPanel.add(new Label("User List Filter:"), DockPanel.SOUTH );
    filterTextBox.addKeyboardListener( this );
    
    userListPanel.setCellHeight(usersList, "100%");
    userListPanel.setCellWidth(usersList, "100%");
    userListPanel.setHeight("100%");
    userListPanel.setWidth("100%");
    usersList.setHeight("100%");
    usersList.setWidth("100%");
    addUserBtn.setWidth("20px");
    deleteUserBtn.setWidth("20px");
    addUserBtn.setHeight("20px");
    deleteUserBtn.setHeight("20px");
    filterTextBox.setWidth( "100%" );
    deleteUserBtn.setEnabled(false);
    
    usersList.addChangeListener(this);
    addUserBtn.addClickListener(this);
    deleteUserBtn.addClickListener(this);
    return userListPanel;
	}
	
	public DockPanel buildAssignedRolesPanel() {
	  DockPanel assignedRolesPanel = new DockPanel();
	  assignedRolesPanel.add(new Label("Assigned Roles"), DockPanel.NORTH);
	  assignedRolesPanel.add(assignedRolesList, DockPanel.CENTER);
	  assignedRolesPanel.setCellHeight(assignedRolesList, "100%");
	  assignedRolesPanel.setCellWidth(assignedRolesList, "100%");
	  assignedRolesList.setHeight("100%");
	  assignedRolesList.setWidth("100%");
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
	        messageDialog.setText("Delete Users");
          if (caught instanceof PentahoSecurityException) {
            messageDialog.setMessage("Insufficient privileges.");
          } else if (caught instanceof NonExistingUserException) {
            messageDialog.setMessage("User does not exist: " + caught.getMessage());
          } else {
            messageDialog.setMessage(caught.getMessage());
          }
          messageDialog.center();
	      }
	    };
	    getPacService().deleteUsers(selectedUsers, callback);
	  }
	}
	
	private void userSelectionChanged() {
    ProxyPentahoUser[] selectedUsers = usersList.getSelectedUsers();
    if (selectedUsers.length == 1) {
      userDetailsPanel.setUser(selectedUsers[0]);
   } else {
      userDetailsPanel.setUser(null);
    }
    userDetailsPanel.setEnabled(selectedUsers.length == 1);
    updateUserBtn.setEnabled(selectedUsers.length == 1);
    deleteUserBtn.setEnabled(selectedUsers.length > 0);
    
    userDetailsPanel.getUserNameTextBox().setEnabled(false);
	}
	
	private void updateUserDetails( final Widget sender ) {
	  if (!userDetailsPanel.getPassword().equals(userDetailsPanel.getPasswordConfirmation())) { 
	    messageDialog.setText("Update User");
      messageDialog.setMessage("Password does not match password confirmation.");
      messageDialog.center();
	  } else {
      ((Button)sender).setEnabled( false );
	    final ProxyPentahoUser user = userDetailsPanel.getUser();
	    final int index = usersList.getSelectedIndex();
	    
	    AsyncCallback callback = new AsyncCallback() {
	      public void onSuccess(Object result) {
	        usersList.setUser(index, user);
	        ((Button)sender).setEnabled( true );
	      }

	      public void onFailure(Throwable caught) {
          messageDialog.setText("Update User");
          if (caught instanceof PentahoSecurityException) {
            messageDialog.setMessage("Insufficient privileges.");
          } else if (caught instanceof NonExistingUserException) {
            messageDialog.setMessage("User does not exist: " + caught.getMessage());
          } else {
            messageDialog.setMessage(caught.getMessage());
          }
          messageDialog.center();
          ((Button)sender).setEnabled( true );
	      }
	    }; // end AsyncCallback
	    getPacService().updateUser(user, callback);
	  }
	}
	
	public boolean validate() {return true;}

	public void onChange(Widget sender) {
	  userSelectionChanged();
	}
	
	public void refresh() {
	  usersList.refresh();
	  userSelectionChanged();
	}
	
	private PacServiceAsync getPacService() {
	  if (pacService == null) {
	    pacService = (PacServiceAsync) GWT.create(PacService.class);
	    ServiceDefTarget endpoint = (ServiceDefTarget) pacService;
	    String moduleRelativeURL = GWT.getModuleBaseURL() + "pacsvc";
	    endpoint.setServiceEntryPoint(moduleRelativeURL);
	  }
	  return pacService;

	}

  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if ((sender == newUserDialogBox) && newUserDialogBox.isUserCreated()) {
      ProxyPentahoUser newUser = newUserDialogBox.getUser();
      if (usersList.addUser(newUser)) {
        usersList.setSelectedUser(newUser);
        userSelectionChanged();
      }
    } else if ((sender == confirmUserDeleteDialog) && (confirmUserDeleteDialog.getButtonPressed() == MessageDialog.OK_BTN)) {
      deleteSelectedUsers();
    }
  }

  public void onKeyDown(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
  }

  public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    if ( filterTextBox == sender ) {
      usersList.filterList( filterTextBox.getText() );
      userSelectionChanged();
    }
  }
  
  public boolean isInitialized() {
    return usersList.isInitialized();
  }
  
  public void clearUsersCache() {
    usersList.clearUsersCache();
  }
}