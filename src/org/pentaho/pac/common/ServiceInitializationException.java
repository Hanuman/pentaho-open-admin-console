package org.pentaho.pac.common;

import org.pentaho.pac.common.CheckedException;



public class ServiceInitializationException extends CheckedException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 691L;

  public ServiceInitializationException(String msg) {
    super(msg);
  }
  
  public ServiceInitializationException(Throwable cause) {
    super(cause);
  }
  
  public ServiceInitializationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public ServiceInitializationException() {
    super();
  }
}