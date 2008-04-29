package org.pentaho.pac.common.users;

import java.io.Serializable;

import org.pentaho.pac.common.CheckedException;

public class DuplicateUserException extends CheckedException implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DuplicateUserException() {
    super();
  }
  
  public DuplicateUserException(String msg) {
    super( msg );
  }
}
