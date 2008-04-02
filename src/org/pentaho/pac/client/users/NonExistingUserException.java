package org.pentaho.pac.client.users;

import java.io.Serializable;

import org.pentaho.pac.client.CheckedException;


public class NonExistingUserException extends CheckedException implements Serializable {

  public NonExistingUserException() {
    super();
  }
  
  public NonExistingUserException(String msg) {
   super( msg ); 
  }

}
