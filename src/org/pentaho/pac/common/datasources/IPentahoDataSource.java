package org.pentaho.pac.common.datasources;


public interface IPentahoDataSource extends java.io.Serializable{

  public String getJndiName();
  public int getMaxActConn();
  public String getDriverClass();
  public int getIdleConn();
  public String getUserName();
  public String getPassword();
  public String getUrl();
  public String getQuery();
  public long getWait();
  public void setJndiName(String jndiName);
  public void setMaxActConn(int maxActConn);
  public void setDriverClass(String driverClass);
  public void setIdleConn(int idleConn);
  public void setUserName(String userName);
  public void setPassword(String password);
  public void setUrl(String url);
  public void setQuery(String query);
  public void setWait(long wait);
}
