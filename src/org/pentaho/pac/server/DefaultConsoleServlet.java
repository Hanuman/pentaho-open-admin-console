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
      response.getWriter().write(Messages.getString("CONSOLE.STOPPING")); //$NON-NLS-1$
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
