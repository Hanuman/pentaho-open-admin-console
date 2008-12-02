package org.pentaho.pac.server.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.pentaho.pac.server.common.OpenAdminConsoleSettings;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DtdEntityResolver implements EntityResolver {

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
   *      java.lang.String)
   */
  public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
    int idx = systemId.lastIndexOf('/');
    String KEY_SOLUTION_PATH = "solution-path"; //$NON-NLS-1$
    OpenAdminConsoleSettings settings = new OpenAdminConsoleSettings(); 
    String dtdName = systemId.substring(idx + 1);
    String solutionPath = null;
    try {
      solutionPath = settings.getSystemSetting(KEY_SOLUTION_PATH, null);
      File f = new File(solutionPath + "/system/dtd/" + dtdName); //$NON-NLS-1$
      if (f.exists() && f.isFile()) {
        return new InputSource(new FileInputStream(f));
      }
    } catch (IOException e) {

    }
    return null;
  }

}
