package org.pentaho.pac.common;

import java.io.Serializable;

public class CheckedException extends Exception implements Serializable {

  /*
   * kludge to work with GWT's exception serialization
   */
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
    msg = cause.getMessage();
  }    
  
  public String getMessage() {
    return msg;
  }
}
