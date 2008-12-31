package org.pentaho.pac.common.config;

import java.io.Serializable;

public class JavaClasspathConfig implements Serializable, IJavaClasspathConfig {

  private String appServerLibClasspath;
  private String systemClasspath;
  private String webAppClasspath;
  
  public JavaClasspathConfig() {
    
  }
  
  public JavaClasspathConfig(IJavaClasspathConfig javaClasspathConfig) {
    setSystemClasspath(javaClasspathConfig.getSystemClasspath());
    setWebAppClasspath(javaClasspathConfig.getWebAppClasspath());
    setAppServerLibClasspath(javaClasspathConfig.getAppServerLibClasspath());
  }
  
  public String getAppServerLibClasspath() {
    return appServerLibClasspath;
  }
  public void setAppServerLibClasspath(String appServerLibClasspath) {
    this.appServerLibClasspath = appServerLibClasspath;
  }
  public String getSystemClasspath() {
    return systemClasspath;
  }
  public void setSystemClasspath(String systemClasspath) {
    this.systemClasspath = systemClasspath;
  }
  public String getWebAppClasspath() {
    return webAppClasspath;
  }
  public void setWebAppClasspath(String webAppClasspath) {
    this.webAppClasspath = webAppClasspath;
  }

}
