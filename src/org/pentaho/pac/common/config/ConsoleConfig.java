package org.pentaho.pac.common.config;

import java.io.Serializable;


public class ConsoleConfig implements IConsoleConfig, Serializable, Cloneable {

  protected String backupDirectory;
  protected String platformUserName;
  protected String webAppPath;
  protected String solutionPath;
  protected Long serverStatusCheckPeriod;
  protected String tempDirectory;
  protected String defaultRoles;
  protected String helpUrl;
  protected String jdbcDriversClassPath;
  protected String homePageUrl;
  protected Integer homePageTimeout;
  
  
  public ConsoleConfig() {
    
  }
  
  public ConsoleConfig(IConsoleConfig config) {
    backupDirectory = config.getBackupDirectory();
    platformUserName = config.getPlatformUserName();
    webAppPath = config.getWebAppPath();
    solutionPath = config.getSolutionPath();
    serverStatusCheckPeriod = config.getServerStatusCheckPeriod();
    tempDirectory = config.getTempDirectory();
    defaultRoles = config.getDefaultRoles();
    helpUrl = config.getHelpUrl();
    jdbcDriversClassPath = config.getJdbcDriversClassPath();
    homePageTimeout = config.getHomePageTimeout();
    homePageUrl = config.getHomePageUrl();
  }
  
  public String getBackupDirectory() {
    return backupDirectory;
  }
  public void setBackupDirectory(String backupDirectory) {
    this.backupDirectory = backupDirectory;
  }
  public String getPlatformUserName() {
    return platformUserName;
  }
  public void setPlatformUserName(String platformUserName) {
    this.platformUserName = platformUserName;
  }
  public String getWebAppPath() {
    return webAppPath;
  }
  public void setWebAppPath(String webAppPath) {
    this.webAppPath = webAppPath;
  }
  public String getSolutionPath() {
    return solutionPath;
  }
  public void setSolutionPath(String solutionPath) {
    this.solutionPath = solutionPath;
  }
  public Integer getHomePageTimeout() {
    return homePageTimeout;
  }

  public void setHomePageTimeout(Integer homePageTimeout) {
    this.homePageTimeout = homePageTimeout;
  }
  public Long getServerStatusCheckPeriod() {
    return serverStatusCheckPeriod;
  }
  public void setServerStatusCheckPeriod(Long serverStatusCheckPeriod) {
    this.serverStatusCheckPeriod = serverStatusCheckPeriod;
  }
  public String getTempDirectory() {
    return tempDirectory;
  }
  public void setTempDirectory(String tempDirectory) {
    this.tempDirectory = tempDirectory;
  }
  public String getDefaultRoles() {
    return defaultRoles;
  }
  public void setDefaultRoles(String defaultRoles) {
    this.defaultRoles = defaultRoles;
  }
  public String getHelpUrl() {
    return helpUrl;
  }
  public void setHelpUrl(String helpUrl) {
    this.helpUrl = helpUrl;
  }
  public String getJdbcDriversClassPath() {
    return jdbcDriversClassPath;
  }
  public void setJdbcDriversClassPath(String jdbcDriversClassPath) {
    this.jdbcDriversClassPath = jdbcDriversClassPath;
  }
  public String getHomePageUrl() {
    return homePageUrl;
  }

  public void setHomePageUrl(String homePageUrl) {
    this.homePageUrl = homePageUrl;
  }

  public Object clone() {
    ConsoleConfig clone = new ConsoleConfig();
    clone.backupDirectory = backupDirectory;
    clone.platformUserName = platformUserName;
    clone.webAppPath = webAppPath;
    clone.solutionPath = solutionPath;
    clone.serverStatusCheckPeriod = serverStatusCheckPeriod;
    clone.tempDirectory = tempDirectory;
    clone.defaultRoles = defaultRoles;
    clone.helpUrl = helpUrl;
    clone.jdbcDriversClassPath = jdbcDriversClassPath;
    clone.homePageTimeout = homePageTimeout;
    clone.homePageUrl = homePageUrl;
    return clone;
  }
}
