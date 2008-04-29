package org.pentaho.pac.server.common;

import org.pentaho.pac.common.CheckedException;


public class DAOException extends CheckedException {
  

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

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
