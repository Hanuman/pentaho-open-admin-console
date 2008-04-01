package org.pentaho.pac.server;

import org.pentaho.pac.client.CheckedException;


public class DAOException extends CheckedException {
  
  public DAOException(String msg) {
    super(msg);
  }
  
  public DAOException(Throwable cause) {
    super(cause);
  }
  
  public DAOException() {
  }
}
