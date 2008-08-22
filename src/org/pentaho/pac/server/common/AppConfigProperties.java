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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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

  public static final String KEY_PASSWORD_SERVICE_CLASS = "password-service-class"; //$NON-NLS-1$

  public static final String KEY_WAR_PATH = "war-path"; //$NON-NLS-1$

  public static final String KEY_SOLUTION_PATH = "solution-path"; //$NON-NLS-1$

  public static final String KEY_HIBERNATE_CONFIG_PATH = "hibernate-config-path"; //$NON-NLS-1$

  public static final String KEY_HOMEPAGE_TIMEOUT = "homepage-timeout"; //$NON-NLS-1$

  public static final String KEY_BISERVER_STATUS_CHECK_PERIOD = "biserver-status-check-period"; //$NON-NLS-1$

  public static final String KEY_BISERVER_BASE_URL = "biserver-base-url"; //$NON-NLS-1$

  public static final String KEY_BISERVER_CONTEXT_PATH = "biserver-context-path"; //$NON-NLS-1$

  public static final String KEY_PLATFORM_USERNAME = "platform-username"; //$NON-NLS-1$

  public static final String DEFAULT_PROPERTIES_FILE_NAME = "console.xml"; //$NON-NLS-1$

  public static final String VALUE_PASSWORD_SERVICE_CLASS = "org.pentaho.platform.util.Base64PasswordService"; //$NON-NLS-1$

  // ~ Instance fields =================================================================================================

  private ISystemSettings settings = new OpenAdminConsoleSettings();

  private static AppConfigProperties instance = new AppConfigProperties();

  private boolean initialized = false;

  // ~ Constructors ====================================================================================================

  protected AppConfigProperties() {

  }

  // ~ Methods =========================================================================================================

  public static synchronized AppConfigProperties getInstance() {
    if (!instance.initialized) {
      init();
    }
    return instance;
  }

  /**
   * Can be called externally or called automatically by the getInstance method. 
   */
  public static synchronized void init() {
    initPasswordService();
    instance.initialized = true;
  }

  /**
   * Optionally called before the first call to getInstance. 
   */
  public static synchronized void init(ISystemSettings settings) {
    instance.settings = settings;
    initPasswordService();
    instance.initialized = true;
  }

  protected static void initPasswordService() {
    String passwordServiceClassName = instance.settings.getSystemSetting(KEY_PASSWORD_SERVICE_CLASS,
        VALUE_PASSWORD_SERVICE_CLASS);
    PasswordServiceFactory.init(passwordServiceClassName);
  }

  public String getPlatformUsername() {
    return settings.getSystemSetting(KEY_PLATFORM_USERNAME, null);
  }

  public String getBiServerContextPath() {
    return settings.getSystemSetting(KEY_BISERVER_CONTEXT_PATH, null);
  }

  public String getBiServerBaseUrl() {
    return settings.getSystemSetting(KEY_BISERVER_BASE_URL, null);
  }

  public String getBiServerStatusCheckPeriod() {
    return settings.getSystemSetting(KEY_BISERVER_STATUS_CHECK_PERIOD, null);
  }

  public String getHomepageTimeout() {
    return settings.getSystemSetting(KEY_HOMEPAGE_TIMEOUT, null);
  }

  public String getHibernateConfigPath() {
    return settings.getSystemSetting(KEY_HIBERNATE_CONFIG_PATH, null);
  }

  public String getSolutionPath() {
    return settings.getSystemSetting(KEY_SOLUTION_PATH, null);
  }

  public String getWarPath() {
    return settings.getSystemSetting(KEY_WAR_PATH, null);
  }

  public String getPasswordServiceClass() {
    return settings.getSystemSetting(KEY_PASSWORD_SERVICE_CLASS, null);
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
      return "resource/config/" + path; //$NON-NLS-1$
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

}
