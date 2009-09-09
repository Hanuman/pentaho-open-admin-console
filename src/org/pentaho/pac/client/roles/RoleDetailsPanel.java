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

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.common.roles.ProxyPentahoRole;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RoleDetailsPanel extends VerticalPanel {
  TextBox roleNameTextBox = new TextBox();
  TextBox descriptionTextBox = new TextBox();
  
  public RoleDetailsPanel() {
    add(new Label(Messages.getString("roleName"))); //$NON-NLS-1$
    add(roleNameTextBox);
    add(new Label(Messages.getString("description"))); //$NON-NLS-1$
    add(descriptionTextBox);
    roleNameTextBox.setWidth("100%"); //$NON-NLS-1$
    descriptionTextBox.setWidth("100%"); //$NON-NLS-1$
    setSpacing(4);
  }

  public String getRoleName() {
    return roleNameTextBox.getText();
  }

  public void setRoleName(String roleName) {
    this.roleNameTextBox.setText(roleName);
  }

  public String getDescription() {
    return descriptionTextBox.getText();
  }

  public void setDescription(String description) {
    descriptionTextBox.setText(description);
  }

  public TextBox getRoleNameTextBox() {
    return roleNameTextBox;
  }

  public TextBox getDescriptionTextBox() {
    return descriptionTextBox;
  }

  public void setRole(ProxyPentahoRole role) {
    if (role == null) {
      setRoleName(""); //$NON-NLS-1$
      setDescription(""); //$NON-NLS-1$
    } else {
      setRoleName(role.getName());
      setDescription(role.getDescription());
    }
  }
  
  public ProxyPentahoRole getRole() {
    ProxyPentahoRole role = new ProxyPentahoRole();
    role.setName(getRoleName());
    role.setDescription(getDescription());
    return role;
  }
  
  public void setEnabled(boolean enabled) {
    roleNameTextBox.setEnabled(enabled);
    descriptionTextBox.setEnabled(enabled);
    
  }
}
