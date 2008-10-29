package org.pentaho.pac.client.utils;

public class ExceptionParser {
  public static String DELIMETER = "-"; //$NON-NLS-1$

  
  public static String getErrorMessage(String message) {
    if(message != null && message.length() > 0) {
      int index = message.indexOf(DELIMETER);
      if(index > 0) {
        return message.substring(index + 1);
      } else {
        return null;
      }
    } else {
      return null;
    }
    
  }
  
  public static String getErrorHeader(String message) {
    if(message != null && message.length() > 0) {
      int index = message.indexOf(DELIMETER);
      if(index > 0) {
        return message.substring(0, index -1);
      } else {
        return null;
      }
    } else {
      return null;
    }
    
  }
}
