package org.pentaho.pac.common;




public class PentahoSecurityException extends CheckedException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PentahoSecurityException(String msg) {
    super(msg);
  }
  
  public PentahoSecurityException(Throwable cause) {
    super(cause);
  }

  public PentahoSecurityException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public PentahoSecurityException() {
    super();
  }
}
