package org.pentaho.pac.client;

import org.pentaho.pac.client.roles.RolesPanel;
import org.pentaho.pac.client.users.UsersPanel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class UsersAndRolesPanel extends DockPanel implements ClickListener {
  
  ToggleButton usersButton = new ToggleButton("Users");
  ToggleButton rolesButton = new ToggleButton("Roles");
  DeckPanel deckPanel = new DeckPanel();
  UsersPanel usersPanel = new UsersPanel();
  RolesPanel rolesPanel = new RolesPanel();
  boolean rolesInitialized = false;
  
  public static final int USER_PANEL_ID = 0;
  public static final int ROLE_PANEL_ID = 1;
  
  public UsersAndRolesPanel() {
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.add(usersButton);
    horizontalPanel.add(rolesButton);
    add(horizontalPanel, DockPanel.NORTH);
    setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
   
    deckPanel.add(usersPanel);
    deckPanel.add(rolesPanel);
    add(deckPanel, DockPanel.CENTER);
    
    setCellWidth(deckPanel, "100%");
    setCellHeight(deckPanel, "100%");
    
    deckPanel.setWidth("100%");
    deckPanel.setHeight("100%");
    
    deckPanel.showWidget(0);
    usersButton.setDown(true);
    rolesButton.setDown(false);
    usersButton.addClickListener(this);
    rolesButton.addClickListener(this);
    
    usersPanel.refresh();
  }

  public void onClick(Widget sender) {
    if (sender == usersButton) {
      if (!usersButton.isDown()) {
        usersButton.setDown(true);
      } else {
        rolesButton.setDown(false);
        deckPanel.showWidget(USER_PANEL_ID);
        if (!rolesInitialized) {
          rolesPanel.refresh();
          rolesInitialized = true;
        }
      }
    } else if (sender == rolesButton) {
      if (!rolesButton.isDown()) {
        rolesButton.setDown(true);
      } else {
        usersButton.setDown(false);
        deckPanel.showWidget(ROLE_PANEL_ID);
      }
    }    
  }

  public ToggleButton getUsersButton() {
    return usersButton;
  }

  public ToggleButton getRolesButton() {
    return rolesButton;
  }

  public UsersPanel getUsersPanel() {
    return usersPanel;
  }

  public RolesPanel getRolesPanel() {
    return rolesPanel;
  }

}
