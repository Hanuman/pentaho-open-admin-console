package org.pentaho.pac.common.roles;

import org.pentaho.pac.common.CheckedException;

public class DuplicateRoleException extends CheckedException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DuplicateRoleException(String msg) {
    super(msg);
  }
  
  public DuplicateRoleException(Throwable cause) {
    super(cause);
  }

  public DuplicateRoleException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public DuplicateRoleException() {
    super();
  }
}
