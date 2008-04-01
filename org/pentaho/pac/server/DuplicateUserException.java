package org.pentaho.pac.server;

public class DuplicateUserException extends DAOException {
  
  public DuplicateUserException() {
    super();
  }
  
  public DuplicateUserException(String msg) {
    super( msg );
  }
}
