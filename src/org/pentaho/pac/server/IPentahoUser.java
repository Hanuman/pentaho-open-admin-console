package org.pentaho.pac.server;

import java.util.Set;


public interface IPentahoUser {

  public String getName();
  public Set<IPentahoRole> getRoles();
  public void setPassword(String password);
  public String getPassword();
  public boolean getEnabled();
  public void setEnabled(boolean enabled);
  public String getDescription();
  public void setDescription( String description );
}
