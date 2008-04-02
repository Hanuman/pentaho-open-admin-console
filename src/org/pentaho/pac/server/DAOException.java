package org.pentaho.pac.server;

import org.pentaho.pac.client.CheckedException;


public class DAOException extends CheckedException {
  

  public DAOException() {
    super();
  }

  public DAOException(String message) {
    super(message);
  }

  public DAOException(String message, Throwable cause) {
    super(message, cause);
  }

  public DAOException(Throwable cause) {
    super(cause);
  }    
}
