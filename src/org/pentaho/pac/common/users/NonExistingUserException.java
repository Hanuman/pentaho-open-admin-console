package org.pentaho.pac.common.users;

import java.io.Serializable;

import org.pentaho.pac.common.CheckedException;


public class NonExistingUserException extends CheckedException implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  public NonExistingUserException(String msg) {
    super(msg);
  }
  
  public NonExistingUserException(Throwable cause) {
    super(cause);
  }

  public NonExistingUserException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public NonExistingUserException() {
    super();
  }

}
