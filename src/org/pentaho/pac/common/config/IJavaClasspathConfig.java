package org.pentaho.pac.common.config;

public interface IJavaClasspathConfig {
  public String getAppServerLibClasspath();
  public String getSystemClasspath();
  public String getWebAppClasspath();
  public void setAppServerLibClasspath(String appServerLibClasspath);
  public void setSystemClasspath(String systemClasspath);
  public void setWebAppClasspath(String webAppClasspath);
}
