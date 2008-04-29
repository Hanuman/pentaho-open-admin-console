package org.pentaho.pac.common.datasources;

import org.pentaho.pac.common.CheckedException;


public class NonExistingDataSourceException extends CheckedException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public NonExistingDataSourceException() {
    super();
  }

  public NonExistingDataSourceException(String msg) {
    super( msg );
  }

}
