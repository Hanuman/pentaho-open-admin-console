package org.pentaho.pac.server.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.pentaho.pac.server.i18n.Messages;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution.  Follows the Thread Local Session
 * pattern, see {@link http://hibernate.org/42.html }.
 */
public class HibernateSessionFactory {

  public static final String DEFAULT_CONFIG_NAME = "$$DEFAULT_CONFIG"; //$NON-NLS-1$

  public static String DEFAULT_CONFIG_FILE_LOCATION = "system/hibernate/hsql.hibernate.cfg.xml"; //$NON-NLS-1$

  public static String SLASH = "/"; //$NON-NLS-1$

  /** 
     * Location of hibernate.cfg.xml file.
     * Location should be on the classpath as Hibernate uses  
     * #resourceAsStream style lookup for its configuration file. 
     * The default classpath location of the hibernate config file is 
     * in the default package. Use #setConfigFile() to update 
     * the location of the configuration file for the current session.   
     */
  // private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
  // private  static org.hibernate.cfg.AnnotationConfiguration configuration = new AnnotationConfiguration();
  //private static org.hibernate.SessionFactory sessionFactory;
  private static Map<String, HibConfig> configs = new HashMap<String, HibConfig>();

  private static String defaultConfigFile = null;

  private static boolean isHibernateManaged = false;

  private static String factoryJndiName;

  private static Context initialContext;

  private HibernateSessionFactory() {
  }

  public static void initDefaultConfiguration() {
    String hibernateConfigPath = AppConfigProperties.getInstance().getHibernateConfigPath();
    String solutionPath = AppConfigProperties.getInstance().getSolutionPath();
    if (hibernateConfigPath != null && hibernateConfigPath.length() > 0) {
      defaultConfigFile = solutionPath + SLASH + hibernateConfigPath;
      addOrUpdateConfiguration(DEFAULT_CONFIG_NAME, defaultConfigFile);
    } else {
      defaultConfigFile = solutionPath + SLASH + DEFAULT_CONFIG_FILE_LOCATION;
      addOrUpdateConfiguration(DEFAULT_CONFIG_NAME, defaultConfigFile);
    }
  }

  public static void addConfiguration(String name, String configFile) {
    if (!configs.containsKey(name))
      addOrUpdateConfiguration(name, configFile);
  }

  public static void addOrUpdateConfiguration(String name, String configFile) {
    Configuration configuration = new AnnotationConfiguration();

    try {
      File file = new File(configFile);
      if (file != null && file.exists()) {
        configuration.configure(file);
      } else {
        configuration.configure(configFile);
      }

      // If Hibernate is running in a managed environment
      isHibernateManaged = AppConfigProperties.getInstance().isHibernateManaged();
      if (!isHibernateManaged) {
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        configs.put(name, new HibConfig(sessionFactory, configuration, configFile));
      } else {
        factoryJndiName = configuration.getProperty(Environment.SESSION_FACTORY_NAME);
        if (factoryJndiName != null && factoryJndiName.length() > 0) {
          configuration.buildSessionFactory(); // Let hibernate Bind it to JNDI...  
        } else {
          throw new HibernateException(
              Messages
                  .getErrorString(
                      "HibernateSessionFactory.ERROR_0002_MISSING_MANAGED_CONFIGURATION", Environment.SESSION_FACTORY_NAME.toString()));//$NON-NLS-1$
        }
      }
    } catch (Exception e) {
      throw new HibernateException(Messages.getErrorString(
          "HibernateSessionFactory.ERROR_0003_UNABLE_TO_CREATE_SESSION_FACTORY", e.getLocalizedMessage()), e);//$NON-NLS-1$
    }
  }

  /**
     * Returns the ThreadLocal Session instance.  Lazy initialize
     * the <code>SessionFactory</code> if needed.
     *
     *  @return Session
     *  @throws HibernateException
     */
  public static Session getSession() throws HibernateException {
    return getSession(DEFAULT_CONFIG_NAME);
  }

  /**
   * Returns the ThreadLocal Session instance.  Lazy initialize
   * the <code>SessionFactory</code> if needed.
   *
   *  @return Session
   *  @throws HibernateException
   */
  public static Session getSession(String configName) throws HibernateException {
    HibConfig cfg = configs.get(configName);
    if (cfg == null)
      throw new HibernateException(Messages.getErrorString(
          "HibernateSessionFactory.ERROR_0001_UNKNOWN_CONFIGURATION", configName)); //$NON-NLS-1$
    Session session = cfg.threadLocal.get();

    if (session == null || !session.isOpen()) {
      if (cfg.sessionFactory == null) {
        rebuildSessionFactory(cfg);
      }
      session = (cfg.sessionFactory != null) ? cfg.sessionFactory.openSession() : null;
      cfg.threadLocal.set(session);
    }

    return session;
  }

  /**
     *  Rebuild hibernate session factory
     *
     */
  public static void rebuildSessionFactory(HibConfig cfg) {
    try {
      cfg.configuration.configure(cfg.configFile);
      cfg.sessionFactory = cfg.configuration.buildSessionFactory();
    } catch (Exception e) {
      throw new HibernateException(Messages.getErrorString(
          "HibernateSessionFactory.ERROR_0004_UNABLE_TO_REBUILD_SESSION_FACTORY", e.getLocalizedMessage()));//$NON-NLS-1$
    }
  }

  /**
     *  Close the single hibernate session instance.
     *
     *  @throws HibernateException
     */
  public static void closeSession() throws HibernateException {
    closeSession(DEFAULT_CONFIG_NAME);
  }

  public static void closeSession(String configName) {
    HibConfig cfg = configs.get(configName);
    if (cfg == null)
      throw new HibernateException(Messages.getErrorString(
          "HibernateSessionFactory.ERROR_0001_UNKNOWN_CONFIGURATION", configName)); //$NON-NLS-1$    	
    Session session = (Session) cfg.threadLocal.get();
    cfg.threadLocal.set(null);

    if (session != null) {
      session.close();
    }
  }

  /**
     *  return session factory
     *
     */
  public static org.hibernate.SessionFactory getSessionFactory() {
    return getSessionFactory(defaultConfigFile);
  }

  public static org.hibernate.SessionFactory getSessionFactory(String configName) {
    isHibernateManaged = AppConfigProperties.getInstance().isHibernateManaged();
    return getSessionFactory(configName, isHibernateManaged);
  }

  private static org.hibernate.SessionFactory getSessionFactory(String configName, boolean isHibernateManaged) {
    // If Hibernate is managed than get the session factory from Jndi otherwise return the session factory from the map
    if (!isHibernateManaged) {
      HibConfig cfg = configs.get(configName);
      if (cfg == null)
        throw new HibernateException(Messages.getErrorString(
            "HibernateSessionFactory.ERROR_0001_UNKNOWN_CONFIGURATION", configName)); //$NON-NLS-1$
      return cfg.sessionFactory;
    } else {
      SessionFactory sf = null;
      try {
        if (initialContext == null) {
          initialContext = new InitialContext();
        }
        String jndiName = factoryJndiName;
        try {
          sf = (SessionFactory) initialContext.lookup(jndiName);
        } catch (Exception ignored) {
        }
        if (sf == null) {
          try {
            sf = (SessionFactory) initialContext.lookup("java:" + jndiName); //$NON-NLS-1$
          } catch (Exception ignored) {
            ignored.printStackTrace();
          }
        }
      } catch (NamingException ignored) {
      }
      return sf;

    }
  }

  /**
     *  return hibernate configuration
     *
     */
  public static Configuration getConfiguration() {
    return getConfiguration(defaultConfigFile);
  }

  /**
     *  return hibernate configuration
     *
     */
  public static Configuration getConfiguration(String configName) {
    HibConfig cfg = configs.get(configName);
    if (cfg == null)
      throw new HibernateException(Messages.getErrorString(
          "HibernateSessionFactory.ERROR_0001_UNKNOWN_CONFIGURATION", configName)); //$NON-NLS-1$

    return cfg.configuration;
  }

  private static class HibConfig {
    HibConfig(SessionFactory sessionFactory, Configuration configuration, String configFile) {
      this.sessionFactory = sessionFactory;
      this.configuration = configuration;
      this.configFile = configFile;
    }

    SessionFactory sessionFactory;

    Configuration configuration;

    ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

    String configFile;

  }

}