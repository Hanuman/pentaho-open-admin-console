package org.pentaho.pac.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ConsoleServlet extends HttpServlet {

	
	  /**
   * 
   */
  private static final long serialVersionUID = -4220122336517696578L;

    protected final void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  doHandle(request, response);
		  }

	  protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  doHandle(request, response);
		  }

  
	  protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
	  }

	  protected abstract void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	  
}
