package org.pentaho.pac.common;

import java.io.Serializable;

public class HibernateConfigException extends PacServiceException implements Serializable {

  public HibernateConfigException(String msg) {
    super(msg);
  }
  
  public HibernateConfigException(Throwable cause) {
    super(cause);
  }
  
  public HibernateConfigException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public HibernateConfigException() {
    super();
  }
}
