package org.pentaho.pac.client.users;

import java.io.Serializable;


public class ProxyPentahoUser implements Serializable {

  private String name;

  private String password = "";

  private String description = "test description";

  private boolean enabled = true;

  public ProxyPentahoUser() {
  }

  public String getName() {
    return name;
  }
  
  public String getPassword() {
    return password;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public String getDescription() {
    return description;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean equals(Object o) {
    return ((o instanceof ProxyPentahoUser) ? name.equals(((ProxyPentahoUser) o).getName()) : false);
  }

  public int hashCode() {
    return name.hashCode();
  }


}
