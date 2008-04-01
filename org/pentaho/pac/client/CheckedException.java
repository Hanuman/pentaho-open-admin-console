package org.pentaho.pac.client;

public class CheckedException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 69L;

  public CheckedException() {
    super();
  }

  public CheckedException(String message) {
    super(message);
  }

  public CheckedException(String message, Throwable cause) {
    super(message, cause);
  }

  public CheckedException(Throwable cause) {
    super(cause);
  }
}
