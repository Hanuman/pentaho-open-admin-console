package org.pentaho.pac.server;

import org.pentaho.pac.client.CheckedException;


public class NonExistingUserException extends CheckedException {

  public NonExistingUserException() {
    super();
  }
  
  public NonExistingUserException(String msg) {
   super( msg ); 
  }

}
