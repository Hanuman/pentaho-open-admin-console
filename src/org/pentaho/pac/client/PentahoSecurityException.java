package org.pentaho.pac.client;

import java.io.Serializable;



public class PentahoSecurityException extends CheckedException implements Serializable {
  
  public PentahoSecurityException(String msg) {
    super(msg);
  }
  
  public PentahoSecurityException(Throwable cause) {
    super(cause);
  }
  
  public PentahoSecurityException() {
  }
}
