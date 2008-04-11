package org.pentaho.pac.common.datasources;

import org.pentaho.pac.common.CheckedException;


public class NonExistingDataSourceException extends CheckedException {

  public NonExistingDataSourceException() {
    super();
  }

  public NonExistingDataSourceException(String msg) {
    super( msg );
  }

}
