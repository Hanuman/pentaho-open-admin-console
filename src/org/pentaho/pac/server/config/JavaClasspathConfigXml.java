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
