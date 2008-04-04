package org.pentaho.pac.common.users;

import java.io.Serializable;

import org.pentaho.pac.common.CheckedException;


public class NonExistingUserException extends CheckedException implements Serializable {

  public NonExistingUserException() {
    super();
  }
  
  public NonExistingUserException(String msg) {
   super( msg ); 
  }

}
