package org.pentaho.pac.server.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.messages.Messages;

public class AppConfigProperties {

  private static final Log logger = LogFactory.getLog(AppConfigProperties.class);
  private static final String PROPERTIES_FILE_NAME = "pac.properties"; //$NON-NLS-1$
  private static final String PROPERTIES_PATH = "./config"; //$NON-NLS-1$
  private static Properties properties = null;

  static {
    File propFile = new File(  PROPERTIES_PATH + "/" + PROPERTIES_FILE_NAME ); //$NON-NLS-1$
    InputStream s = null;
    try {
      s = new FileInputStream(propFile);
    } catch (FileNotFoundException e1) {
      logger.error( Messages.getString( "PacService.OPEN_PROPS_FAILED", PROPERTIES_FILE_NAME ) ); //$NON-NLS-1$
    }
    if ( null != s ) {
      properties = new Properties();
      try {
        properties.load( s );
      } catch (IOException e) {
        logger.error( Messages.getString( "PacService.LOAD_PROPS_FAILED", PROPERTIES_FILE_NAME ) ); //$NON-NLS-1$
      }
    }
  }

  public static Properties getProperties()
  {
    return properties;
  }
}
