/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * Created  
 * @author Steven Barkdull
 */

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
