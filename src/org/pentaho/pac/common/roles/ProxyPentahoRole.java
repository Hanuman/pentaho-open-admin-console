package org.pentaho.pac.common.roles;

import java.io.Serializable;

import org.pentaho.pac.common.users.ProxyPentahoUser;


public class ProxyPentahoRole implements Serializable, Cloneable{

  String name;
  String description;

  public ProxyPentahoRole() {
  }
  
  public ProxyPentahoRole(String roleName) {
    this.name = roleName;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean equals(Object o) {
    return ((o instanceof ProxyPentahoRole) ? name.equals(((ProxyPentahoRole) o).getName()) : false);
  }
  
  public int hashCode() {
    return name.hashCode();
  }

  public Object clone() {
    ProxyPentahoRole o = new ProxyPentahoRole();
    o.name = name;
    o.description = description;
    return o;
  }
  
}
