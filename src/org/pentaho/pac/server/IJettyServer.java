package org.pentaho.pac.server;

import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.Context;

public interface IJettyServer {

  public void configureResourceHandlers( Context servletContext, SecurityHandler securityHandler );
  public void configureServlets( Context servletContext );
  public void configureEventListeners( Context servletContext );
  public SecurityHandler configureSecurityHandler();
  public String getResourceBaseName();
  public String[] getWelcomeFiles(); 
}
