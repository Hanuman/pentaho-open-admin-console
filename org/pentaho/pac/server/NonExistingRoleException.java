package org.pentaho.pac.server;

import org.pentaho.pac.client.CheckedException;


public class NonExistingRoleException extends CheckedException {
  
  public NonExistingRoleException() {
    super();
  }
  
  public NonExistingRoleException(String msg) {
    super( msg );
  }

}
