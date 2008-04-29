package org.pentaho.pac.common.users;

import java.io.Serializable;


public class ProxyPentahoUser implements Serializable, Cloneable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String name;

  private String password = ""; //$NON-NLS-1$

  private String description = ""; //$NON-NLS-1$

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

  public Object clone() {  
    ProxyPentahoUser o = new ProxyPentahoUser();
    o.name = name;
    o.password = password;
    o.description = description;
    o.enabled = enabled;
    return o;
  }

}
