package org.pentaho.pac.common.users;

import java.io.Serializable;

import org.pentaho.pac.common.CheckedException;

public class DuplicateUserException extends CheckedException implements Serializable {
  
  public DuplicateUserException() {
    super();
  }
  
  public DuplicateUserException(String msg) {
    super( msg );
  }
}
