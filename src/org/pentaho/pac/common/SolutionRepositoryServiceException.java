package org.pentaho.pac.common;

public class SolutionRepositoryServiceException extends CheckedException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 420;

  public SolutionRepositoryServiceException(String msg) {
    super(msg);
  }
  
  public SolutionRepositoryServiceException(Throwable cause) {
    super(cause);
  }
  
  public SolutionRepositoryServiceException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public SolutionRepositoryServiceException() {
    super();
  }
}
