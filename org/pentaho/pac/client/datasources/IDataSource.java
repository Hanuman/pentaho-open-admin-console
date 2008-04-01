package org.pentaho.pac.client.datasources;

public interface IDataSource {
  public abstract String getJndiName();

  public abstract void setJndiName(String objectName);

  public abstract int getActive();

  public abstract void setActive(int a);

  public abstract String getDriverClass();

  public abstract void setDriverClass(String name);

  public abstract int getIdle();

  public abstract void setIdle(int i);

  public abstract String getPassword();

  public abstract void setPassword(String s);

  public abstract String getUrl();

  public abstract void setUrl(String url);

  public abstract String getUserName();

  public abstract void setUserName(String s);

  public abstract String getValidationQuery();

  public abstract void setValidationQuery(String q);

  public abstract long getWait();

  public abstract void setWait(long w);

  public void setParameter(String name, String prop);

  public String getParameter(String name);

}