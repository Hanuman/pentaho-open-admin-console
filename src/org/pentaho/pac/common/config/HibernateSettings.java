package org.pentaho.pac.common.config;

import java.io.Serializable;

public class HibernateSettings implements IHibernateSettings, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  String hibernateConfigFile;
  boolean hibernateManaged;
  public HibernateSettings() {
    
  }
  public HibernateSettings(IHibernateSettings hibernateSettings) {
    setHibernateConfigFile(hibernateSettings.getHibernateConfigFile());
    setHibernateManaged(hibernateSettings.getHibernateManaged());
  }
  
  public String getHibernateConfigFile() {
    return hibernateConfigFile;
  }
  public void setHibernateConfigFile(String hibernateConfigFile) {
    this.hibernateConfigFile = hibernateConfigFile;
  }
  public boolean getHibernateManaged() {
    return hibernateManaged;
  }
  public void setHibernateManaged(boolean hibernateManaged) {
    this.hibernateManaged = hibernateManaged;
  }
  
}
