package org.pentaho.pac.server;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.pentaho.platform.util.messages.LocaleHelper;

public class PentahoAdminConsole extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
    Locale effectiveLocale = req.getLocale(); 
    if (!StringUtils.isEmpty(req.getParameter("locale"))) { //$NON-NLS-1$
      effectiveLocale = new Locale(req.getParameter("locale")); //$NON-NLS-1$
      req.getSession().setAttribute("locale_override", req.getParameter("locale")); //$NON-NLS-1$ //$NON-NLS-2$
      LocaleHelper.setLocaleOverride(effectiveLocale);
    }
    
    resp.getWriter().println("document.write('<meta name=\"gwt:property\" content=\"locale=" + effectiveLocale.toString() + "\"/>');"); //$NON-NLS-1$ //$NON-NLS-2$
  }

}
