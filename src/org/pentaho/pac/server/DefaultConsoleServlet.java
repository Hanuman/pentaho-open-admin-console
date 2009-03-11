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
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pentaho.pac.server.i18n.Messages;
import org.pentaho.platform.util.messages.LocaleHelper;

public class DefaultConsoleServlet extends ConsoleServlet {

  private static final long serialVersionUID = -6461343946361097451L;

  private Halter halter;

  public DefaultConsoleServlet(String baseUrl, Halter halter) {
    this.halter = halter;
  }

  @Override
  protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {

    // allow set attribute on consoleconfig even if console validation fails
    if ("/halt".equals(request.getServletPath())) { //$NON-NLS-1$
      response.getWriter().write(Messages.getString("JettyServer.STOPPING")); //$NON-NLS-1$
      halter.stop();
      return;
    }

    super.doHandle(request, response);

  }

  protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    Locale locale = request.getLocale();
    LocaleHelper.setLocale(locale);
  }

}
