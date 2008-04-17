package org.pentaho.pac.client.roles;

import java.util.Arrays;

import org.pentaho.pac.client.common.ui.ObjectListBox;
import org.pentaho.pac.common.roles.ProxyPentahoRole;

public class RolesList extends ObjectListBox {
  
  public RolesList(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  public ProxyPentahoRole[] getRoles() {
    return (ProxyPentahoRole[])getObjects().toArray(new ProxyPentahoRole[0]);
  }

  public void setRoles(ProxyPentahoRole[] roles) {
    setObjects(Arrays.asList(roles));
  }
  
  public void addRole(ProxyPentahoRole role) {
    addObject(role);
  }
  
  public ProxyPentahoRole[] getSelectedRoles() {
    return (ProxyPentahoRole[])getSelectedObjects().toArray(new ProxyPentahoRole[0]);
  }
  
  public void setSelectedRole(ProxyPentahoRole role) {
    setSelectedObject(role);
  }
  
  public void setSelectedRoles(ProxyPentahoRole[] roles) {
    setSelectedObjects(Arrays.asList(roles));
  }
  
  public void removeSelectedRoles() {
    removeSelectedObjects();
  }
  
  public void removeRoles(ProxyPentahoRole[] rolesToRemove) {
    removeObjects(Arrays.asList(rolesToRemove));
  }
  
  protected String getObjectText(Object object) {
    return object instanceof ProxyPentahoRole ? ((ProxyPentahoRole)object).getName() : super.getObjectText(object);
  }
}
