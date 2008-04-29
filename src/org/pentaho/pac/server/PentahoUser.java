package org.pentaho.pac.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PentahoUser implements IPentahoUser {

  private String name;

  private String password = ""; //$NON-NLS-1$

  private String description = "test description"; //$NON-NLS-1$

  private boolean enabled = true;

  private Set<IPentahoRole> assignedRoles = new HashSet<IPentahoRole>();

  protected PentahoUser() {

  }

  public PentahoUser(String userName) {
    this.name = userName;
  }

  public String getName() {
    return name;
  }
  
  private void setName(String userName) {
    name = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  protected Set<IPentahoRole> getAssignedRoles() {
    return assignedRoles;
  }

  protected void setAssignedRoles(Set<IPentahoRole> roles) {
    this.assignedRoles = roles;
    ;
  }

  public Set<IPentahoRole> getRoles() {
    return Collections.unmodifiableSet(getAssignedRoles());
  }

  public boolean equals(Object o) {
    return ((o instanceof PentahoUser) ? name.equals(((PentahoUser) o).getName()) : false);
  }

  public int hashCode() {
    return name.hashCode();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
