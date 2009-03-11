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
package org.pentaho.pac.server.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.pentaho.pac.common.config.IJavaClasspathConfig;

public class JavaClasspathConfigXml extends AbstractDiagnosticsJmxXml implements IJavaClasspathConfig {
  private static final String SYSTEM_CLASSPATH = "systemClasspath"; //$NON-NLS-1$
  private static final String APP_SERVER_LIB_CLASSPATH = "jbossLibClasspath"; //$NON-NLS-1$
  private static final String WEBAPP_LIB_CLASSPATH = "webappLibClasspath"; //$NON-NLS-1$

  public JavaClasspathConfigXml(String jmxXml) throws DocumentException {
    super(jmxXml);
  }
  
  public JavaClasspathConfigXml(Document jmxDocument) throws DocumentException {
    super(jmxDocument);
  }
  
  public JavaClasspathConfigXml() {
    super();
  }
  
  public String getAppServerLibClasspath() {
    return getAttributeValue(APP_SERVER_LIB_CLASSPATH);
  }

  public String getSystemClasspath() {
    return getAttributeValue(SYSTEM_CLASSPATH);
  }

  public String getWebAppClasspath() {
    return getAttributeValue(WEBAPP_LIB_CLASSPATH);
  }

  public void setAppServerLibClasspath(String appServerLibClasspath) {
    setAttributeValue(APP_SERVER_LIB_CLASSPATH, appServerLibClasspath);
  }

  public void setSystemClasspath(String systemClasspath) {
    setAttributeValue(SYSTEM_CLASSPATH, systemClasspath);
  }

  public void setWebAppClasspath(String webAppClasspath) {
    setAttributeValue(WEBAPP_LIB_CLASSPATH, webAppClasspath);
  }

}
