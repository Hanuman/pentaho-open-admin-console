package org.pentaho.pac.client.datasources;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A base data source implementation, nothing but a POJO.
 * 
 * @author Alex Silva
 *
 */
public class SimpleDataSource implements IDataSource, Serializable {
  private HashMap parms = new HashMap();

  private String url;

  private String jndiName;

  private String driverClass;

  private String userName;

  private String password;

  private int active;

  private int idle;

  private long wait;

  private String validationQuery;

  private String type;

  public SimpleDataSource() {
    parms.put(Constants.HOST, "localhost"); //$NON-NLS-1$
  }

  public void setJndiName(String objectName) {
    this.jndiName = objectName;
  }

  public int getActive() {
    return active;
  }

  public void setActive(int active) {
    this.active = active;
  }

  public String getDriverClass() {
    return driverClass;
  }

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

  public int getIdle() {
    return idle;
  }

  public void setIdle(int idle) {
    this.idle = idle;
  }

  public String getJndiName() {
    return jndiName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getValidationQuery() {
    return validationQuery;
  }

  public void setValidationQuery(String validationQuery) {
    this.validationQuery = validationQuery;
  }

  public long getWait() {
    return wait;
  }

  public void setWait(long wait) {
    this.wait = wait;
  }

  public void setParameter(String name, String value) {
    parms.put(name, value);
  }

  public String getParameter(String name) {
    return parms.get(name).toString();
  }

}
