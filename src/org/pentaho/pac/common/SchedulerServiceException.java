package org.pentaho.pac.common;

public class SchedulerServiceException extends CheckedException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 691L;

  public SchedulerServiceException(String msg) {
    super(msg);
  }
  
  public SchedulerServiceException(Throwable cause) {
    super(cause);
  }
  
  public SchedulerServiceException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public SchedulerServiceException() {
    super();
  }
}
