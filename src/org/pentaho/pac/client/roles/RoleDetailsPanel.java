package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.common.roles.ProxyPentahoRole;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RoleDetailsPanel extends VerticalPanel {
  TextBox roleNameTextBox = new TextBox();
  TextBox descriptionTextBox = new TextBox();
  
  public RoleDetailsPanel() {
    add(new Label(PentahoAdminConsole.getLocalizedMessages().roleName()));
    add(roleNameTextBox);
    add(new Label(PentahoAdminConsole.getLocalizedMessages().description()));
    add(descriptionTextBox);
    roleNameTextBox.setWidth("100%"); //$NON-NLS-1$
    descriptionTextBox.setWidth("100%"); //$NON-NLS-1$
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
