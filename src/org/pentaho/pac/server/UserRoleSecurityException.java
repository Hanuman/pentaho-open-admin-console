package org.pentaho.pac.server;

import org.pentaho.pac.client.CheckedException;


public class UserRoleSecurityException extends CheckedException {
  
  public UserRoleSecurityException(String msg) {
    super(msg);
  }
  
  public UserRoleSecurityException(Throwable cause) {
    super(cause);
  }
  
  public UserRoleSecurityException() {
  }
}
