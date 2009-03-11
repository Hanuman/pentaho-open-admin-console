/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 *
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.server.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.server.i18n.Messages;

/**
 * By default, this class will initialize itself from a file on the class path called console.properties.
 * A client of this module can override the default initialization by simply calling 
 * setProperties() with a Properties object that provides an alternate initialization.
 * To understand what the required parameters are, see console.properties.
 * 
 * @author Ramaiz Mansoor
 *
 */
public class ConsoleProperties {

  public static final String CONSOLE_PORT_NUMBER = "console.start.port.number"; //$NON-NLS-1$
  public static final String CONSOLE_HOST_NAME = "console.hostname"; //$NON-NLS-1$
  public static final String SSLENABLED = "console.ssl.enabled"; //$NON-NLS-1$
  public static final String CONSOLE_SSL_PORT_NUMBER = "console.ssl.port.number"; //$NON-NLS-1$
  public static final String KEY_ALIAS = "keyAlias"; //$NON-NLS-1$
  public static final String KEY_PASSWORD = "keyPassword"; //$NON-NLS-1$
  public static final String KEYSTORE = "keyStore"; //$NON-NLS-1$
  public static final String KEYSTORE_PASSWORD = "keyStorePassword"; //$NON-NLS-1$
  public static final String TRUSTSTORE = "trustStore"; //$NON-NLS-1$
  public static final String TRUSTSTORE_PASSWORD = "trustStorePassword"; //$NON-NLS-1$  
  public static final String WANT_CLIENT_AUTH = "wantClientAuth"; //$NON-NLS-1$
  public static final String NEED_CLIENT_AUTH = "needClientAuth"; //$NON-NLS-1$
  public static final String CONSOLE_SECURITY_REALM_NAME = "console.security.realm.name"; //$NON-NLS-1$
  public static final String CONSOLE_SECURITY_LOGIN_MODULE_NAME = "console.security.login.module.name"; //$NON-NLS-1$
  public static final String CONSOLE_SECURITY_ENABLED = "console.security.enabled"; //$NON-NLS-1$
  public static final String CONSOLE_SECURITY_AUTH_CONFIG_PATH = "console.security.auth.config.path"; //$NON-NLS-1$
  public static final String CONSOLE_SECURITY_ROLES_ALLOWED = "console.security.roles.allowed"; //$NON-NLS-1$
  public static final String DEFAULT_CONSOLE_PROPERTIES_FILE_NAME = "console.properties"; //$NON-NLS-1$
  public static final String STOP_ARG = "-STOP"; //$NON-NLS-1$
  public static final String STOP_PORT = "console.stop.port.number";//$NON-NLS-1$
  public static final String CONSOLE_SECURITY_ROLE_DELIMITER = "console.security.roles.delimiter"; //$NON-NLS-1$
  public static final String CONSOLE_SECURITY_CALLBACK_HANDLER = "console.security.callback.handler"; //$NON-NLS-1$
  
  private static final Log logger = LogFactory.getLog(ConsoleProperties.class);
  private Properties properties = null;
  private static ConsoleProperties instance = new ConsoleProperties();
  
  protected ConsoleProperties() {
    init( DEFAULT_CONSOLE_PROPERTIES_FILE_NAME );
  }
  
  public static ConsoleProperties getInstance() {
    return instance;
  }
  
  public void init( String pathToConfigResource ) {
    FileInputStream fis = null;
    try {
      URL url = ClassLoader.getSystemResource(pathToConfigResource);
      URI uri = url.toURI();
      File file = new File(uri);
      fis = new FileInputStream(file);
    } catch (Exception e1) {
      logger.error(Messages.getErrorString("ConsoleProperties.ERROR_0001_OPEN_PROPS_FAILED", pathToConfigResource)); //$NON-NLS-1$      
    }
    if (null != fis) {
      properties = new Properties();
      try {
        properties.load(fis);
      } catch (IOException e) {
        logger.error(Messages.getErrorString("ConsoleProperties.ERROR_0002_LOAD_PROPS_FAILED", DEFAULT_CONSOLE_PROPERTIES_FILE_NAME)); //$NON-NLS-1$
      }
    }
  }
  
  public void init( Properties p ) {
    properties = p;
  }
 
  
  public void setProperties( Properties p )
  {
    properties = p;
  }

  public String getProperty( String key )
  {
    return (String)properties.get( key );
  }
}
