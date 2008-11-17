package org.pentaho.pac.common.config;

import java.io.Serializable;

public class HibernateConfig implements IHibernateConfig, Serializable {

  String dbDriver;
  String dbUrl;
  String dialect;
  String password;
  String userId;
  Integer connectionPoolSize;
  public HibernateConfig() { 
  }
  public HibernateConfig(IHibernateConfig hibernateConfig) {
    setDbDriver(hibernateConfig.getDbDriver());
    setDbUrl(hibernateConfig.getDbUrl());
    setDialect(hibernateConfig.getDialect());
    setPassword(hibernateConfig.getPassword());
    setUserId(hibernateConfig.getUserId());
    setConnectionPoolSize(hibernateConfig.getConnectionPoolSize());
  }
  public String getDbDriver() {
    return dbDriver;
  }
  public void setDbDriver(String dbDriver) {
    this.dbDriver = dbDriver;
  }
  public String getDbUrl() {
    return dbUrl;
  }
  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }
  public String getDialect() {
    return dialect;
  }
  public void setDialect(String dialect) {
    this.dialect = dialect;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public Integer getConnectionPoolSize() {
    return connectionPoolSize;
  }
  public void setConnectionPoolSize(Integer connectionPoolSize) {
    this.connectionPoolSize = connectionPoolSize;
  }
  
}
