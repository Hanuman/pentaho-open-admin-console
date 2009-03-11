/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
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
