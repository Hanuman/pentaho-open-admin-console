package org.pentaho.pac.client.users;

import java.io.Serializable;

import org.pentaho.pac.client.CheckedException;

public class DuplicateUserException extends CheckedException implements Serializable {
  
  public DuplicateUserException() {
    super();
  }
  
  public DuplicateUserException(String msg) {
    super( msg );
  }
}
