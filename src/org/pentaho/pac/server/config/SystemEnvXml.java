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

import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.pentaho.pac.common.config.ISystemEnv;

public class SystemEnvXml extends AbstractDiagnosticsJmxXml implements ISystemEnv {

  public static final String OS_NAME = "os.name"; //$NON-NLS-1$
  public static final String OS_VERSION = "os.version"; //$NON-NLS-1$
  public static final String SUN_OS_PATCH_LEVEL = "sun.os.patch.level"; //$NON-NLS-1$
  public static final String USER_COUNTRY = "user.country"; //$NON-NLS-1$
  public static final String USER_HOME = "user.home"; //$NON-NLS-1$
  public static final String USER_LANGUAGE = "user.language"; //$NON-NLS-1$
  public static final String USER_NAME = "user.name"; //$NON-NLS-1$
  public static final String USER_TIMEZONE = "user.timezone"; //$NON-NLS-1$
  public static final String USER_DIR = "user.dir"; //$NON-NLS-1$
  public static final String JAVA_VERSION = "java.version"; //$NON-NLS-1$
  public static final String JAVA_VENDOR = "java.vendor"; //$NON-NLS-1$
  public static final String JAVA_VM_INFO = "java.vm.info"; //$NON-NLS-1$
  public static final String JAVA_VM_NAME = "java.vm.name"; //$NON-NLS-1$
  public static final String FILE_ENCODING = "file.encoding"; //$NON-NLS-1$
  public static final String JAVA_CLASSPATH = "java.class.path"; //$NON-NLS-1$
  public static final String JAVA_HOME = "java.home"; //$NON-NLS-1$
  public static final String JAVA_LIBRARY_PATH = "java.library.path"; //$NON-NLS-1$ 
  public static final String APP_SERVER_BIND_ADDRESS = "jboss.bind.address"; //$NON-NLS-1$
  public static final String APP_SERVER_HOME_DIR = "jboss.home.dir"; //$NON-NLS-1$
  public static final String APP_SERVER_LIB_URL = "jboss.server.lib.url"; //$NON-NLS-1$
  public static final String PENTAHO_OLAP_XMLDATASOURCES = "pentaho.olap.xmladatasources"; //$NON-NLS-1$
  
  public SystemEnvXml(String jmxXml) throws DocumentException {
    super(jmxXml);
  }
  
  public SystemEnvXml(Document jmxDocument) throws DocumentException {
    super(jmxDocument);
  }
  
  public SystemEnvXml() {
    super();
  }
  
  public String getAppServerBindAddr() {
    return getAttributeValue(APP_SERVER_BIND_ADDRESS);
  }

  public String getAppServerHomeDir() {
    return getAttributeValue(APP_SERVER_HOME_DIR);
  }

  public String getAppServerLibUrl() {
    return getAttributeValue(APP_SERVER_LIB_URL);
  }

  public String getFileEncoding() {
    return getAttributeValue(FILE_ENCODING);
  }

  public String getJavaClassPath() {
    return getAttributeValue(JAVA_CLASSPATH);
  }

  public String getJavaHome() {
    return getAttributeValue(JAVA_HOME);
  }

  public String getJavaLibraryPath() {
    return getAttributeValue(JAVA_LIBRARY_PATH);
  }

  public String getJavaVendor() {
    return getAttributeValue(JAVA_VENDOR);
  }

  public String getJavaVersion() {
    return getAttributeValue(JAVA_VERSION);
  }

  public String getJavaVmInfo() {
    return getAttributeValue(JAVA_VM_INFO);
  }

  public String getJavaVmName() {
    return getAttributeValue(JAVA_VM_NAME);
  }

  public String getOsName() {
    return getAttributeValue(OS_NAME);
  }

  public String getOsVersion() {
    return getAttributeValue(OS_VERSION);
  }

  public String getPentahoOlapXmlDataSources() {
    return getAttributeValue(PENTAHO_OLAP_XMLDATASOURCES);
  }

  public String getSunOsPatchLevel() {
    return getAttributeValue(SUN_OS_PATCH_LEVEL);
  }

  public String getUserCountry() {
    return getAttributeValue(USER_COUNTRY);
  }

  public String getUserDir() {
    return getAttributeValue(USER_DIR);
  }

  public String getUserLanguage() {
    return getAttributeValue(USER_LANGUAGE);
  }

  public String getUserName() {
    return getAttributeValue(USER_NAME);
  }

  public String getUserTimeZone() {
    return getAttributeValue(USER_TIMEZONE);
  }

  public void setAppServerBindAddr(String appServerBindAddr) {
    setAttributeValue(APP_SERVER_BIND_ADDRESS, appServerBindAddr);
  }

  public void setAppServerHomeDir(String appServerHomeDir) {
    setAttributeValue(APP_SERVER_HOME_DIR, appServerHomeDir);
  }

  public void setAppServerLibUrl(String appServerLibUrl) {
    setAttributeValue(APP_SERVER_LIB_URL, appServerLibUrl);
  }

  public void setFileEncoding(String fileEncoding) {
    setAttributeValue(FILE_ENCODING, fileEncoding);
  }

  public void setJavaClassPath(String javaClassPath) {
    setAttributeValue(JAVA_CLASSPATH, javaClassPath);
  }

  public void setJavaHome(String javaHome) {
    setAttributeValue(JAVA_HOME, javaHome);
  }

  public void setJavaLibraryPath(String javaLibraryPath) {
    setAttributeValue(JAVA_LIBRARY_PATH, javaLibraryPath);
  }

  public void setJavaVendor(String javaVendor) {
    setAttributeValue(JAVA_VENDOR, javaVendor);
  }

  public void setJavaVersion(String javaVersion) {
    setAttributeValue(JAVA_VERSION, javaVersion);
  }

  public void setJavaVmInfo(String javaVmInfo) {
    setAttributeValue(JAVA_VM_INFO, javaVmInfo);
    
  }

  public void setJavaVmName(String javaVmName) {
    setAttributeValue(JAVA_VM_NAME, javaVmName);
  }

  public void setOsName(String osName) {
    setAttributeValue(OS_NAME, osName);
  }

  public void setOsVersion(String osVersion) {
    setAttributeValue(OS_VERSION, osVersion);
  }

  public void setPentahoOlapXmlDataSources(String pentahoOlapXmlDataSources) {
    setAttributeValue(PENTAHO_OLAP_XMLDATASOURCES, pentahoOlapXmlDataSources);
  }

  public void setSunOsPatchLevel(String sunOsPatchLevel) {
    setAttributeValue(SUN_OS_PATCH_LEVEL, sunOsPatchLevel);
  }

  public void setUserCountry(String userCountry) {
    setAttributeValue(USER_COUNTRY, userCountry);
  }

  public void setUserDir(String userDir) {
    setAttributeValue(USER_DIR, userDir);
  }

  public void setUserLanguage(String userLanguage) {
    setAttributeValue(USER_LANGUAGE, userLanguage);
  }

  public void setUserName(String userName) {
    setAttributeValue(USER_NAME, userName);
  }

  public void setUserTimeZone(String userTimeZone) {
    setAttributeValue(USER_TIMEZONE, userTimeZone);
  }

  public Properties getEnv() {
    Properties properties = new Properties();
    List<Element> elements = document.selectNodes(ROOT_ELEMENT + "/" + ATTRIBUTE_ELEMENT + "[@" + ID_ATTRIBUTE + "]");   
    for (Element element : elements) {
      String propertyName = element.attributeValue(ID_ATTRIBUTE);
      String propertyValue = getAttributeValue(propertyName);
      properties.put(propertyName, propertyValue);
    }
    return properties;
  }
}
