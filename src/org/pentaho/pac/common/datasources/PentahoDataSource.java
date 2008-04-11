package org.pentaho.pac.common.datasources;


public class PentahoDataSource implements IPentahoDataSource {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String jndiName;
  private int maxActConn;
  private String driverClass;
  private int idleConn;
  private String userName;
  private String password;
  private String url;
  private String query;
  private long wait;

  public PentahoDataSource() {

  }

  public PentahoDataSource(String jndiName) {
    this.jndiName = jndiName;
  }

  public String getJndiName() {
    return this.jndiName;
  }

  public void setJndiName(String jndiName) {
    this.jndiName = jndiName;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getQuery() {
    return this.query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getMaxActConn() {
    return this.maxActConn;
  }

  public void setMaxActConn(int maxActConn) {
    this.maxActConn = maxActConn;
  }


  public long getWait() {
    return this.wait;
  }

  public void setWait(long wait) {
    this.wait = wait;
  }

  public boolean equals(Object o) {
    return ((o instanceof PentahoDataSource) ? this.jndiName.equals(((PentahoDataSource) o).getJndiName()) : false);
  }

  public int hashCode() {
    return jndiName.hashCode();
  }

  public int getIdleConn() {
    return this.idleConn;
  }

  public void setIdleConn(int idleConn) {
    this.idleConn = idleConn;
  }

  public String getDriverClass() {
    return this.driverClass;
  }

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

}
