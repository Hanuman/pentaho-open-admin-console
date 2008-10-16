package org.pentaho.pac.common.roles;

import org.pentaho.pac.common.CheckedException;


public class NonExistingRoleException extends CheckedException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public NonExistingRoleException(String msg) {
    super(msg);
  }
  
  public NonExistingRoleException(Throwable cause) {
    super(cause);
  }

  public NonExistingRoleException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public NonExistingRoleException() {
    super();
  }

}
