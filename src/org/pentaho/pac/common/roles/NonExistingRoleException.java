package org.pentaho.pac.common.roles;

import org.pentaho.pac.common.CheckedException;


public class NonExistingRoleException extends CheckedException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public NonExistingRoleException() {
    super();
  }
  
  public NonExistingRoleException(String msg) {
    super( msg );
  }

}
