package org.pentaho.pac.server;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.pentaho.platform.util.messages.LocaleHelper;

/**
 * The BrowserLocaleServlet exists to generate a meta tag for the javascript on web client.
 * The web client's javascript does not have a native way of detecting what locale the browser prefers.
 * 
 * @author cboyden
 *
 */
public class BrowserLocaleServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    // Read in the requested locale as sent by the browser
    Locale effectiveLocale = req.getLocale(); 
    
    // Check for a ?locale=xx_XX override in the url
    if (!StringUtils.isEmpty(req.getParameter("locale"))) { //$NON-NLS-1$
      effectiveLocale = new Locale(req.getParameter("locale")); //$NON-NLS-1$
      req.getSession().setAttribute("locale_override", req.getParameter("locale")); //$NON-NLS-1$ //$NON-NLS-2$
      LocaleHelper.setLocaleOverride(effectiveLocale);
    }
    
    // Write the javacsript to the client for generating the meta tag within the html->head element
    resp.getWriter().println("document.write('<meta name=\"gwt:property\" content=\"locale=" + effectiveLocale.toString() + "\"/>');"); //$NON-NLS-1$ //$NON-NLS-2$
  }

}
