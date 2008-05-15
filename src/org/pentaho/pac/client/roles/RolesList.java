package org.pentaho.pac.client.roles;

import org.pentaho.pac.client.common.ui.GenericObjectListBox;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.roles.RoleComparator;

public class RolesList extends GenericObjectListBox<ProxyPentahoRole> {
  
  public RolesList(boolean isMultiSelect) {
    super(isMultiSelect);
    setComparator(new RoleComparator());
  }

  protected String getObjectText(ProxyPentahoRole object) {
    return object.getName();
  }
}
