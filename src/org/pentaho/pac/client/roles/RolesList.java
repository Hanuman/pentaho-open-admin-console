package org.pentaho.pac.client.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class RolesList extends ListBox {
  ArrayList roles = new ArrayList();
  String roleNameFilter;
  
  public RolesList(boolean isMultiSelect) {
    super(isMultiSelect);
  }

  public ProxyPentahoRole[] getRoles() {
    return (ProxyPentahoRole[])roles.toArray(new ProxyPentahoRole[0]);
  }

  public void setRoles(ProxyPentahoRole[] roles) {
    
    this.roles.clear();
    this.roles.addAll(Arrays.asList(roles));
    clear();
    applyFilter();
  }
  
  public void addRole(ProxyPentahoRole role) {
    int index = roles.indexOf(role);
    if (index >= 0) {
      roles.set(index, role);
    } else {
      roles.add(role);
      if (doesFilterMatch(role.getName())) {
        addItem(role.getName());
      }
    }
  }
  
  public ProxyPentahoRole[] getSelectedRoles() {
    List selectedRoles = new ArrayList();
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      if (isItemSelected(i)) {
        String roleName = getItemText( i );
        ProxyPentahoRole u = getRoleByName( roleName );
        selectedRoles.add( u );
      }
    }
    return (ProxyPentahoRole[])selectedRoles.toArray(new ProxyPentahoRole[0]);
  }
  
  public void setSelectedRole(ProxyPentahoRole role) {
    setSelectedRoles(new ProxyPentahoRole[]{role});
  }
  
  public void setSelectedRoles(ProxyPentahoRole[] roles) {
    ArrayList roleNames = new ArrayList();
    for (int i = 0; i < roles.length; i++) {
      roleNames.add(roles[i].getName());
    }
    int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      setItemSelected(i, roleNames.contains(getItemText(i)));
    }   
  }
  
  public void removeSelectedRoles() {
    int numRolesDeleted = 0;
    int selectedIndex = -1;
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        ProxyPentahoRole  proxyPentahoRole = getRoleByName(getItemText(i));
        if (proxyPentahoRole != null) {
          roles.remove(i);
        }
        removeItem(i);
        numRolesDeleted++;
        selectedIndex = i;
      }
    }
    if (numRolesDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void removeRoles(ProxyPentahoRole[] rolesToRemove) {
    int numRolesDeleted = 0;
    int selectedIndex = -1;
    roles.removeAll(Arrays.asList(rolesToRemove));
    for (int i = getItemCount() - 1; i >= 0; i--) {
      if (isItemSelected(i)) {
        selectedIndex = i;
      }
      for (int j = 0; j < rolesToRemove.length; j++) {
        if (getItemText(i).equals(rolesToRemove[j].getName())) {
          removeItem(i);
          numRolesDeleted++;
          break;
        }
      }
    }
    if (numRolesDeleted == 1) {
      if (selectedIndex >= getItemCount()) {
        selectedIndex = getItemCount() - 1;
      }
      if (selectedIndex >= 0) {
        setItemSelected(selectedIndex, true);
      }
    }
  }
  
  public void addRole(ProxyPentahoUser role) {
    int index = roles.indexOf(role);
    if (index >= 0) {
      roles.set(index, role);
    } else {
      roles.add(role);
      addItem(role.getName());
    }
  }
  
  public void setRoleNameFilter(String roleNameFilter) {
    if (roleNameFilter != null) {
      this.roleNameFilter = roleNameFilter.trim();
    } else {
      this.roleNameFilter = roleNameFilter;
    }
    applyFilter();
  }

  private void applyFilter() {
    ProxyPentahoRole[] selectedRoles = getSelectedRoles();
    this.clear();
    Iterator roleItr = roles.iterator();
    while ( roleItr.hasNext() )
    {
      ProxyPentahoRole role = (ProxyPentahoRole)roleItr.next();
      boolean doesMatch = doesFilterMatch( role.getName() );
      if ( doesMatch ) {
        this.addItem( role.getName() );
      }
    }
    setSelectedRoles(selectedRoles);
  }
  
  private boolean doesFilterMatch( String filterTarget )
  {
    boolean result = ((roleNameFilter == null) || (roleNameFilter.length() == 0));
    if (!result) {
      int filterLen = roleNameFilter.length();
      result = ( filterLen <= filterTarget.length() )
        && roleNameFilter.toLowerCase().substring( 0, filterLen )
          .equals( filterTarget.toLowerCase().substring( 0, filterLen ) ); 
    }
    return result;
  }

  private ProxyPentahoRole getRoleByName( String name )
  {
    for ( int ii=0; ii<roles.size(); ++ii )
    {
      ProxyPentahoRole role = (ProxyPentahoRole)roles.get( ii );
      if ( role.getName().equals( name ) ) {
        return role;
      }
    }
    return null;
  }
  
}
