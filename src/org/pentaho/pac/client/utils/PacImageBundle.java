package org.pentaho.pac.client.utils;

import com.google.gwt.core.client.GWT;

public class PacImageBundle {
  private static IPacImageBundle bundle = (IPacImageBundle)GWT.create(IPacImageBundle.class);
  
  public static IPacImageBundle getBundle() {
    return bundle;
  }
}
