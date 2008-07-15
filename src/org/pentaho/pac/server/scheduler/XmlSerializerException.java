package org.pentaho.pac.server.scheduler;

import org.pentaho.pac.common.CheckedException;

public class XmlSerializerException extends CheckedException {

  private static final long serialVersionUID = 420L;

  public XmlSerializerException() {
    super();
  }

  public XmlSerializerException(String message) {
    super(message);
  }

  public XmlSerializerException(String message, Throwable cause) {
    super(message, cause);
  }

  public XmlSerializerException(Throwable cause) {
    super(cause);
  }    
}