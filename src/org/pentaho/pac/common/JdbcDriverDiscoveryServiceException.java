package org.pentaho.pac.common;

import java.io.Serializable;



public class JdbcDriverDiscoveryServiceException extends CheckedException implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 691L;

  public JdbcDriverDiscoveryServiceException(String msg) {
    super(msg);
  }
  
  public JdbcDriverDiscoveryServiceException(Throwable cause) {
    super(cause);
  }
  
  public JdbcDriverDiscoveryServiceException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public JdbcDriverDiscoveryServiceException() {
    super();
  }
}