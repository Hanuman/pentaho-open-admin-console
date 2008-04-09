package org.pentaho.pac.common.roles;

import org.pentaho.pac.common.CheckedException;


public class NonExistingRoleException extends CheckedException {
  
  public NonExistingRoleException() {
    super();
  }
  
  public NonExistingRoleException(String msg) {
    super( msg );
  }

}
