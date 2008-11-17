package org.pentaho.pac.common.config;

public interface IHibernateConfig {
  public String getDbDriver();
  public void setDbDriver(String driver);
  public String getDbUrl();
  public void setDbUrl( String url);
  public String getDialect();
  public void setDialect(String dialect);
  public String getUserId();
  public void setUserId(String userId);
  public String getPassword();
  public void setPassword(String password);
  public Integer getConnectionPoolSize();
  public void setConnectionPoolSize(Integer poolSize);
}
