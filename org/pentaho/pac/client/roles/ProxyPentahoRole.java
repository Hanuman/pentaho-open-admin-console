package org.pentaho.pac.client.roles;

import java.io.Serializable;


public class ProxyPentahoRole implements Serializable{

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
}
