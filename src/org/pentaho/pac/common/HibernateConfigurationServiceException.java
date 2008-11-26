package org.pentaho.pac.common;

import java.io.Serializable;

public class HibernateConfigurationServiceException extends CheckedException implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 691L;

  public HibernateConfigurationServiceException(String msg) {
    super(msg);
  }
  
  public HibernateConfigurationServiceException(Throwable cause) {
    super(cause);
  }
  
  public HibernateConfigurationServiceException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public HibernateConfigurationServiceException() {
    super();
  }
}