package org.pentaho.pac.common.users;

import java.io.Serializable;

import org.pentaho.pac.common.CheckedException;

public class DuplicateUserException extends CheckedException implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  public DuplicateUserException(String msg) {
    super(msg);
  }
  
  public DuplicateUserException(Throwable cause) {
    super(cause);
  }

  public DuplicateUserException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public DuplicateUserException() {
    super();
  }
}
