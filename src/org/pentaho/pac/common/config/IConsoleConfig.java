package org.pentaho.pac.common.config;




public interface IConsoleConfig {
  public String getSolutionPath();
  public void setSolutionPath(String settingsDirectoryPath);
  public String getWebAppPath();
  public void setWebAppPath(String webAppPath);
  public String getBackupDirectory();
  public void setBackupDirectory(String backupDirectory);  
  public String getPlatformUserName();
  public void setPlatformUserName(String username);
  public Long getServerStatusCheckPeriod();
  public void setServerStatusCheckPeriod(Long serverStatusCheckPeriod);
  public Integer getHomePageTimeout();
  public void setHomePageTimeout(Integer serverStatusCheckPeriod);
  public String getHomePageUrl();
  public void setHomePageUrl(String url);
  public String getTempDirectory();
  public void setTempDirectory(String tempDir);  
  public String getHelpUrl();
  public void setHelpUrl(String url);
  public String getJdbcDriversClassPath();
  public void setJdbcDriversClassPath(String classpath);
  public String getDefaultRoles();
  public void setDefaultRoles(String defaultRoles);
}
