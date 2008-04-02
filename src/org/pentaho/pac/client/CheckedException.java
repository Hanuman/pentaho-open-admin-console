package org.pentaho.pac.client;

import java.io.Serializable;

public class CheckedException extends Exception implements Serializable {

  private String msg = null;
  /**
   * 
   */
  private static final long serialVersionUID = 69L;

  public CheckedException() {
    super();
  }

  public CheckedException(String message) {
    super(message);
    msg = message;
  }

  public CheckedException(String message, Throwable cause) {
    super(message, cause);
    msg = message;
  }

  public CheckedException(Throwable cause) {
    super(cause);
  }    
  
  public String getMessage() {
    return msg;
  }
}
