package org.pentaho.pac.server.common;

import org.pentaho.pac.common.CheckedException;

public class ProxyException extends CheckedException {

  private static final long serialVersionUID = 420L;

  public ProxyException() {
    super();
  }

  public ProxyException(String message) {
    super(message);
  }

  public ProxyException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProxyException(Throwable cause) {
    super(cause);
  }    
}
