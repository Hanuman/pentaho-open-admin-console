package org.pentaho.pac.server.common;

import org.pentaho.pac.common.CheckedException;


public class AppConfigException extends CheckedException {
  

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public AppConfigException() {
    super();
  }

  public AppConfigException(String message) {
    super(message);
  }

  public AppConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public AppConfigException(Throwable cause) {
    super(cause);
  }    
}
