package org.pentaho.pac.common.config;

public interface IHibernateSettings {

  public String getHibernateConfigFile();
  public void setHibernateConfigFile(String hibernateConfigFile);
  public boolean getHibernateManaged();
  public void setHibernateManaged(boolean hibernateManaged);
}
