package org.pentaho.pac.server.common;

import org.mortbay.jetty.security.Password;

public class JettyPasswordEncoder {

  public static void main(String[] args) {
    String encodedPassword = Password.obfuscate("password"); //$NON-NLS-1$
    System.out.println("encodedPassword" + encodedPassword); //$NON-NLS-1$
  }
}
