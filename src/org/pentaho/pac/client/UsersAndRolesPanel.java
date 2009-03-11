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
package org.pentaho.pac.client;

import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.roles.RolesPanel;
import org.pentaho.pac.client.users.UsersPanel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class UsersAndRolesPanel extends DockPanel implements ClickListener {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

	ToggleButton usersButton = new ToggleButton( MSGS.users(), MSGS.users() );
  ToggleButton rolesButton = new ToggleButton( MSGS.roles(), MSGS.roles() );
  
  DeckPanel deckPanel = new DeckPanel();
  UsersPanel usersPanel = new UsersPanel();
  RolesPanel rolesPanel = new RolesPanel();
  
  // TODO sbarkdull, w/java 5 make it an enum
  public static final int USER_PANEL_ID = 1;
  public static final int ROLE_PANEL_ID = 0;
  
  public UsersAndRolesPanel() {
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.setStyleName("deckToolbar"); //$NON-NLS-1$

    horizontalPanel.add(rolesButton);
    horizontalPanel.add(usersButton);
    Label spacer = new Label(""); //$NON-NLS-1$
    horizontalPanel.add(spacer);
    horizontalPanel.setCellWidth(spacer, "100%"); //$NON-NLS-1$
    this.setSpacing(4);
    add(horizontalPanel, DockPanel.NORTH);
    
    setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

    usersButton.setTitle( MSGS.clickEditUsers() );
    rolesButton.setTitle( MSGS.clickEditRoles() );
    
    usersButton.setStylePrimaryName( "usersToggleBtn" ); //$NON-NLS-1$
    rolesButton.setStylePrimaryName( "rolesToggleBtn" ); //$NON-NLS-1$

    deckPanel.add(rolesPanel);
    deckPanel.add(usersPanel);
    add(deckPanel, DockPanel.CENTER);
    
    setCellWidth(deckPanel, "100%"); //$NON-NLS-1$
    setCellHeight(deckPanel, "100%"); //$NON-NLS-1$
    
    deckPanel.setWidth("100%"); //$NON-NLS-1$
    deckPanel.setHeight("100%"); //$NON-NLS-1$
    
    deckPanel.showWidget(0);
    usersButton.setDown(false);
    rolesButton.setDown(true);
    usersButton.addClickListener(this);
    rolesButton.addClickListener(this);
  }

  public void onClick(Widget sender) {
    if (sender == usersButton) {
      if (!usersButton.isDown()) {
        usersButton.setDown(true);
      } else {
        rolesButton.setDown(false);
        usersPanel.refresh();
        deckPanel.showWidget(USER_PANEL_ID);
      }
    } else if (sender == rolesButton) {
      if (!rolesButton.isDown()) {
        rolesButton.setDown(true);
      } else {
        usersButton.setDown(false);
        rolesPanel.refresh();
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
  
  public void refresh() {
    usersPanel.refresh();
    rolesPanel.refresh();
  }

}
