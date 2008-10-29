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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.pentaho.pac.server.config.HibernateSettingsXml;
import org.pentaho.pac.server.config.PentahoObjectsConfig;
import org.pentaho.pac.server.config.WebXml;
import org.pentaho.pac.server.i18n.Messages;
import org.pentaho.platform.api.engine.ISystemSettings;
import org.pentaho.platform.engine.core.system.SystemSettings;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

/**
 * By default, this class will initialize itself from <code>resource/config/console.xml</code> (relative to the current
 * working directory). 
 * 
 * @author Steven Barkdull
 * @author mlowery
 *
 */
public class AppConfigProperties {

  // ~ Static fields/initializers ====================================================================================== 

  private static final Log logger = LogFactory.getLog(AppConfigProperties.class);

  public static final String WEB_XML_PATH = "/WEB-INF/web.xml"; //$NON-NLS-1$
  public static final String HIBERNATE_MANAGED_XML_PATH = "/system/hibernate/hibernate-settings.xml"; //$NON-NLS-1$
  public static final String HIBERNATE_CONFIG_PATH = "hibernateConfigPath"; //$NON-NLS-1$
  public static final String PENTAHO_OBJECTS_SPRING_XML = "/system/pentahoObjects.spring.xml" ; //$NON-NLS-1$
  public static final String SLASH = "/" ; //$NON-NLS-1$
  public static final String COLON = ":" ; //$NON-NLS-1$
  public static final String COMMA = "," ; //$NON-NLS-1$
  public static final String XPATH_TO_CONTEXT_PARAM = "web-app/context-param"; //$NON-NLS-1$
  public static final String XPATH_TO_HIBERNATE_CFG_FILE = "settings/config-file"; //$NON-NLS-1$
  public static final String XPATH_TO_PARAM_NAME = "param-name"; //$NON-NLS-1$
  public static final String JDBC_DRIVER_PATH = "./jdbc"; //$NON-NLS-1$
 
  public static final String KEY_WAR_PATH = "war-path"; //$NON-NLS-1$
 
  public static final String KEY_PASSWORD_SERVICE_CLASS = "password-service-class"; //$NON-NLS-1$
 
  public static final String KEY_SOLUTION_PATH = "solution-path"; //$NON-NLS-1$
  
  public static final String KEY_DEFAULT_ROLES = "default-roles"; //$NON-NLS-1$

  public static final String KEY_HIBERNATE_CONFIG_PATH = "hibernate-config-path"; //$NON-NLS-1$

  public static final String KEY_HOMEPAGE_TIMEOUT = "homepage-timeout"; //$NON-NLS-1$

  public static final String KEY_BISERVER_STATUS_CHECK_PERIOD = "biserver-status-check-period"; //$NON-NLS-1$

  public static final String KEY_BISERVER_BASE_URL = "biserver-base-url"; //$NON-NLS-1$

  public static final String KEY_BISERVER_CONTEXT_PATH = "biserver-context-path"; //$NON-NLS-1$

  public static final String KEY_PLATFORM_USERNAME = "platform-username"; //$NON-NLS-1$
  
  public static final String KEY_JDBC_DRIVERS_PATH = "jdbc-drivers-path"; //$NON-NLS-1$
  
  public static final String KEY_HELP_URL = "help-url"; //$NON-NLS-1$
  
  public static final String KEY_HOMEPAGE_URL = "homepage-url"; //$NON-NLS-1$
  
  public static final String DEFAULT_PROPERTIES_FILE_NAME = "console.xml"; //$NON-NLS-1$

  public static final String DEFAULT_VALUE_PASSWORD_SERVICE_CLASS = "org.pentaho.platform.util.Base64PasswordService"; //$NON-NLS-1$
  
  public static final String DEFAULT_BISERVER_BASE_URL = "http://localhost:8080/pentaho"; //$NON-NLS-1$
  
  public static final String DEFAULT_BISERVER_CONTEXT_PATH = "/pentaho"; //$NON-NLS-1$
  
  public static final String DEFAULT_PLATFORM_USERNAME = "joe"; //$NON-NLS-1$
  
  public static final String DEFAULT_BISERVER_STATUS_CHECK_PERIOD = "30000"; //$NON-NLS-1$
  
  public static final String DEFAULT_HOMEPAGE_TIMEOUT = "15000"; //$NON-NLS-1$
  
  public static final String DEFAULT_HIBERNATE_CONFIG_PATH = "hsql.hibernate.cfg.xml"; //$NON-NLS-1$
  
  public static final String DEFAULT_HELP_URL = "http://wiki.pentaho.com/display/PentahoDoc/The+Pentaho+Administration+Console"; //$NON-NLS-1$
  
  public static final String DEFAULT_HOMEPAGE_URL = "http://www.pentaho.com/console_home"; //$NON-NLS-1$
    
  public static final String DEFAULT_SOLUTION_PATH = "./../pentaho-solutions"; //$NON-NLS-1$ 
  
  public static final String DEFAULT_WAR_PATH = "./../tomcat/webapps/pentaho"; //$NON-NLS-1$
  
  public static final String DEFAULT_JDBC_DRIVER_PATH = "./../tomcat/webapps/pentaho/WEB-INF/lib"; //$NON-NLS-1$
  
  private static String baseUrl;
  private static String passwordServiceClass;
  private static String hibernateConfigPath;
  private static boolean hibernateManaged;
  private static String biserverStatusCheckPeriod;
  private static String platformUsername;
  private static String biserverContextPath;
  private static String homepageTimeout;
  private static String helpUrl;
  private static String solutionPath;
  private static String warPath;
  private static List<String> defaultRoles = new ArrayList<String>();
  private static String defaultRolesString;
  private static String homepageUrl;

  
  // ~ Instance fields =================================================================================================
  private static AppConfigProperties instance = new AppConfigProperties();

  // ~ Constructors ====================================================================================================

  protected AppConfigProperties() {
  }

  // ~ Methods =========================================================================================================

  public static synchronized AppConfigProperties getInstance() {
    return instance;
  }

  public String getPlatformUsername() {
    return platformUsername;
  }

  public String getBiServerContextPath() {
    return biserverContextPath;
  }

  public String getBiServerBaseUrl(){
    return baseUrl;
  }

  public String getBiServerStatusCheckPeriod() {
    return biserverStatusCheckPeriod;
  }

  /**
   * Returns a comma-separated list of roles to apply to newly created users.
   */
  public String getDefaultRolesString() {
    return defaultRolesString;
  }
  
  /**
   * Convenience wrapper around getDefaultRolesString that parses the default roles string into individual roles.
   */
  public List<String> getDefaultRoles() {
    return defaultRoles;
  }
 
  public String getHomepageUrl() {
    return homepageUrl;
  }
  
  public String getHomepageTimeout() {
    return homepageTimeout;
  }

  public String getHibernateConfigPath(){
    return hibernateConfigPath;
  }
  public boolean isHibernateManaged(){
    return hibernateManaged;
  }
  public String getSolutionPath() {
    return solutionPath;
  }

  public String getWarPath() {
    return warPath;
  }

  public String getPasswordServiceClass(){
    return passwordServiceClass;
  }

  public String getJdbcDriverPath() {
    return JDBC_DRIVER_PATH;
  }
  
  public String getHelpUrl(){
    return helpUrl;
  }
  
  /**
   * Reuse of {@link SystemSettings} where settings are read from <code>resource/config/console.xml</code>.
   * 
   * @author mlowery
   */
  private class OpenAdminConsoleSettings extends SystemSettings {

    private static final long serialVersionUID = -5912709145466266140L;

    public OpenAdminConsoleSettings() {
      super();
    }

    /**
     * Need to override since default implementation points to solution path (e.g. pentaho-solutions).
     */
    @Override
    protected String getAbsolutePath(String path) {
      try {
        File file = new File(ClassLoader.getSystemResource(path).toURI());
        return file.getAbsolutePath();
      } catch(Exception e) {
        logger.info( Messages.getErrorString("AppConfigProperties.ERROR_0009_UNABLE_TO_GET_ABSOLUTE_PATH", path, e.getLocalizedMessage())); //$NON-NLS-1$
        return "resource/config/" + path; //$NON-NLS-1$  
      }
    }

    /**
     * Need to override since default implementation points to pentaho.xml.
     */
    @Override
    public String getSystemSetting(final String settingName, final String defaultValue) {
      return getSystemSetting(DEFAULT_PROPERTIES_FILE_NAME, settingName, defaultValue);
    }

    /**
     * Need to override to stop caching behavior. Caching is disabled to allow this object to see changes to
     * the console.xml file that are made by other objects.
     */
    @Override
    public Document getSystemSettingsDocument(final String actionPath) {
      
      File f = new File(getAbsolutePath(actionPath));
      if (!f.exists()) {
        return null;
      }
      Document systemSettingsDocument = null;

      try {
        systemSettingsDocument = XmlDom4JHelper.getDocFromFile(f, null);
      } catch (DocumentException e) {
        logger.error(e);
      } catch (IOException e) {
        logger.error(e);
      }

      return systemSettingsDocument;
    }

  }
  
  
  public void initialize() throws AppConfigException{
    // War path
    ISystemSettings settings = new OpenAdminConsoleSettings();
    warPath = settings.getSystemSetting(KEY_WAR_PATH, null);
    if(!validateWarPath(warPath)) {
      throw new AppConfigException(Messages.getErrorString("AppConfigProperties.ERROR_0007_WAR_PATH_NOT_CONFIGURED", warPath)); //$NON-NLS-1$ 
    }
    // Soluiton path
    solutionPath = settings.getSystemSetting(KEY_SOLUTION_PATH, null);

    if(!validateSolutionPath(solutionPath)) {
      throw new AppConfigException(Messages.getErrorString("AppConfigProperties.ERROR_0008_SOLUTION_PATH_NOT_CONFIGURED", solutionPath));  //$NON-NLS-1$
    }
    
    // Help url
    helpUrl = settings.getSystemSetting(KEY_HELP_URL, DEFAULT_HELP_URL);
    // Homepage timeout    
    homepageTimeout = settings.getSystemSetting(KEY_HOMEPAGE_TIMEOUT, DEFAULT_HOMEPAGE_TIMEOUT);
    // default roles as string
    defaultRolesString = settings.getSystemSetting(KEY_DEFAULT_ROLES, null);
    // BI server status check period
    biserverStatusCheckPeriod = settings.getSystemSetting(KEY_BISERVER_STATUS_CHECK_PERIOD, DEFAULT_BISERVER_STATUS_CHECK_PERIOD);
    // Platform username
    platformUsername = settings.getSystemSetting(KEY_PLATFORM_USERNAME, DEFAULT_PLATFORM_USERNAME);
    
    // Help url
    homepageUrl = settings.getSystemSetting(KEY_HOMEPAGE_URL, DEFAULT_HOMEPAGE_URL);
    

    // Initializing DefaultRoles
    
    if (defaultRolesString == null || defaultRolesString.length() == 0) {
      defaultRoles = Collections.emptyList();
    } else {
      StringTokenizer tokenizer = new StringTokenizer(defaultRolesString, COMMA);
      while (tokenizer.hasMoreTokens()) {
        defaultRoles.add(tokenizer.nextToken());
      }
    }

    try {
      PentahoObjectsConfig pentahoObjectsConfig = new PentahoObjectsConfig(new File(solutionPath + PENTAHO_OBJECTS_SPRING_XML));
      passwordServiceClass = pentahoObjectsConfig.getPasswordService();
      if (StringUtils.isEmpty(passwordServiceClass)) {
        passwordServiceClass = DEFAULT_VALUE_PASSWORD_SERVICE_CLASS;
      }
      PasswordServiceFactory.init(passwordServiceClass);

    } catch(Exception e) {
      throw new AppConfigException(Messages.getErrorString("AppConfigProperties.ERROR_0004_UNABLE_TO_READ_FILE", solutionPath + PENTAHO_OBJECTS_SPRING_XML), e); //$NON-NLS-1$
    }
    
    // Initializing isHibernateManaged & HibernateConfigPath
    try {
      HibernateSettingsXml hibernateSettingXml = new HibernateSettingsXml(new File(solutionPath + HIBERNATE_MANAGED_XML_PATH));
      String hibernateConfigFile = hibernateSettingXml.getHibernateConfigFile();
      if(hibernateConfigFile != null && hibernateConfigFile.length() > 0) {
        hibernateConfigPath = hibernateConfigFile.substring(hibernateConfigFile.lastIndexOf(SLASH)+1, hibernateConfigFile.length());
        if (StringUtils.isEmpty(hibernateConfigPath) ) {
          hibernateConfigPath = DEFAULT_HIBERNATE_CONFIG_PATH;
         }
      }
      String isHibernateManaged = hibernateSettingXml.getHibernateManaged();
      if(isHibernateManaged != null && isHibernateManaged.length() > 0) {
        hibernateManaged = Boolean.parseBoolean(isHibernateManaged);  
      } 

    } catch(Exception e) {
      throw new AppConfigException(Messages.getErrorString("AppConfigProperties.ERROR_0004_UNABLE_TO_READ_FILE", solutionPath + HIBERNATE_MANAGED_XML_PATH), e); //$NON-NLS-1$
    } 

    // Initializing Base URL
    try {
      WebXml webXml = new WebXml(new File(warPath + WEB_XML_PATH));
      baseUrl = webXml.getBaseUrl();
      if (!(baseUrl != null && baseUrl.length() > 0)) {
        baseUrl = DEFAULT_BISERVER_BASE_URL;
      }
    } catch(Exception e) {
      throw new AppConfigException(Messages.getErrorString("AppConfigProperties.ERROR_0004_UNABLE_TO_READ_FILE",warPath + WEB_XML_PATH), e); //$NON-NLS-1$
    }
    // Initializing BiServer Context Path
    int start = baseUrl.lastIndexOf(COLON);
    int middle = baseUrl.indexOf(SLASH, start);

    // Initializing BiServer context path
    biserverContextPath = baseUrl.substring(middle, baseUrl.length()-1);
    if (!(biserverContextPath != null && biserverContextPath.length() > 0)) {
      biserverContextPath = DEFAULT_BISERVER_CONTEXT_PATH;
    }
  }
  
  private boolean validateWarPath(String pentahoWarPath) {
    try {
      if(pentahoWarPath != null && pentahoWarPath.length() > 0) {
        File file = new File(pentahoWarPath + WEB_XML_PATH);
        if(file.exists()) {
          return true;
        } else {
          pentahoWarPath = DEFAULT_WAR_PATH;
          File webXmlFile = new File(pentahoWarPath + WEB_XML_PATH);
          if(webXmlFile.exists()) {
            warPath = DEFAULT_WAR_PATH;
            logger.info( Messages.getString("AppConfigProperties.USING_DEFAULT_WAR_PATH", warPath)); //$NON-NLS-1$
            return true;
          } else {
            return false;
          }
        }
      } else {
        pentahoWarPath = DEFAULT_WAR_PATH;
        File webXmlFile = new File(pentahoWarPath + WEB_XML_PATH);
        if(webXmlFile.exists()) {
          warPath = DEFAULT_WAR_PATH;
          logger.info( Messages.getString("AppConfigProperties.USING_DEFAULT_WAR_PATH", warPath)); //$NON-NLS-1$          
          return true;
        } else {
          return false;
        }
      }
    } catch(Exception e) {
      logger.error( Messages.getErrorString("AppConfigProperties.ERROR_0007_WAR_PATH_NOT_CONFIGURED", warPath)); //$NON-NLS-1$
      return false;
    }
  }

  private boolean validateSolutionPath(String pentahoSolutionPath) {
    try {
      if(pentahoSolutionPath != null && pentahoSolutionPath.length() > 0) {
        File file = new File(pentahoSolutionPath + HIBERNATE_MANAGED_XML_PATH);
        if(file.exists()) {
          return true;
        } else {
          pentahoSolutionPath = DEFAULT_SOLUTION_PATH;
          File webXmlFile = new File(pentahoSolutionPath + HIBERNATE_MANAGED_XML_PATH);
          if(webXmlFile.exists()) {
            solutionPath = DEFAULT_SOLUTION_PATH;
            logger.info( Messages.getString("AppConfigProperties.USING_DEFAULT_SOLUTION_PATH", solutionPath)); //$NON-NLS-1$
            return true;
          } else {
            return false;
          }
        }
      } else {
        pentahoSolutionPath = DEFAULT_SOLUTION_PATH;
        File webXmlFile = new File(pentahoSolutionPath + HIBERNATE_MANAGED_XML_PATH);
        if(webXmlFile.exists()) {
          solutionPath = DEFAULT_SOLUTION_PATH;
          logger.info( Messages.getString("AppConfigProperties.USING_DEFAULT_SOLUTION_PATH", solutionPath)); //$NON-NLS-1$
          return true;
        } else {
          return false;
        }
      }
    } catch(Exception e) {
      logger.error( Messages.getErrorString("AppConfigProperties.ERROR_0008_SOLUTION_PATH_NOT_CONFIGURED", solutionPath)); //$NON-NLS-1$
      return false;
    }
  }
}
