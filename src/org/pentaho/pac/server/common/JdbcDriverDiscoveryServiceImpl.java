package org.pentaho.pac.server.common;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.JdbcDriverDiscoveryService;
import org.pentaho.pac.common.JdbcDriverDiscoveryServiceException;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.server.i18n.Messages;
import org.pentaho.pac.server.util.ResolverUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class JdbcDriverDiscoveryServiceImpl extends RemoteServiceServlet implements JdbcDriverDiscoveryService, IConsoleConfigEventListener {

  /**
   * 
   */
  private static final long serialVersionUID = 420L;

  private static final long interval = 300000; // every five mins

  private final HashMap<String,CacheInfo> cache = new HashMap<String,CacheInfo>();
  private static final String DEFAULT_JDBC_PATH_2 = "./lib"; //$NON-NLS-1$
  private static final String DEFAULT_JDBC_PATH_1 = "./lib-ext/jdbc";//$NON-NLS-1$
  private static final Log logger = LogFactory.getLog(JdbcDriverDiscoveryServiceImpl.class);
  private static String jdbcDriverPath;

  public JdbcDriverDiscoveryServiceImpl() {
  }

  public void init() throws ServletException {
    super.init();
    ServletContext context = this.getServletContext();
    ConsoleConfigEventMgr mgr = (ConsoleConfigEventMgr) context.getAttribute(ConsoleConfigEventMgr.CONSOLE_CONFIG_EVENT_MGR);
    if(mgr != null) {
      mgr.addConfigListener(this);  
    }
  }

  public void initialize() {
    initFromConfiguration();
  }
  private void initFromConfiguration() {
    AppConfigProperties appCfg = AppConfigProperties.getInstance();
    try {
      appCfg.refreshConfig();  
    } catch(AppConfigException ace) {
      logger.error(Messages.getErrorString(
          "JdbcDriverDiscoveryService.ERROR_0003_UNABLE_TO_INITIALIZE_CONFIGURATION", ace.getLocalizedMessage())); //$NON-NLS-1$      
    }
    
    jdbcDriverPath = StringUtils.defaultIfEmpty(appCfg.getJdbcDriverPath(), System.getProperty("jdbc.drivers.path")); //$NON-NLS-1$ 
    if(!isExist(jdbcDriverPath)) {
      jdbcDriverPath = DEFAULT_JDBC_PATH_1;
    } 
    if(!isExist(jdbcDriverPath)){
      jdbcDriverPath = DEFAULT_JDBC_PATH_2;
    }
    cache.clear();
  }

  public NameValue[] getAvailableJdbcDrivers() throws JdbcDriverDiscoveryServiceException {
    if (jdbcDriverPath != null && jdbcDriverPath.length() > 0) {
      return getAvailableJdbcDrivers(jdbcDriverPath);
    } else {
      throw new JdbcDriverDiscoveryServiceException(Messages
          .getErrorString("JdbcDriverDiscoveryService.ERROR_0002_NO_DRIVERS_FOUND_IN_JDBC_DRIVER_PATH"));//$NON-NLS-1$
    }

  }

  public NameValue[] getAvailableJdbcDrivers(String location) throws JdbcDriverDiscoveryServiceException {
    try {
      return getDrivers(location,false); 
    } catch(JdbcDriverDiscoveryServiceException jdse) {
      jdbcDriverPath = DEFAULT_JDBC_PATH_2;
      return getDrivers(location,false);
    }
  }
  
  private NameValue[] getDrivers(String location,boolean relative) throws JdbcDriverDiscoveryServiceException {
    CacheInfo ci = cache.get(location);
    try {
     long timeMil = System.currentTimeMillis();
          
      if (ci!=null){
        long inte =  timeMil - ci.lastCall;
        if (inte < interval) {
          return ci.cachedObj;
        }
      }
     
      if (location != null && location.length() > 0) {
        File parent = relative?new File(getServletContext().getRealPath(location)):new File(location);
        FilenameFilter libs = new FilenameFilter() {

          public boolean accept(File dir, String pathname) {
            return pathname.endsWith("jar") || pathname.endsWith("zip"); //$NON-NLS-1$//$NON-NLS-2$
          }

        };

        ResolverUtil<Driver> resolver = new ResolverUtil<Driver>();

        if (parent.exists()) {
          for (File lib : parent.listFiles(libs)) {
            try {
              resolver.loadImplementationsInJar("", lib.toURI().toURL(), //$NON-NLS-1$
                  new ResolverUtil.IsA(Driver.class));
            } catch (MalformedURLException e) {
              e.printStackTrace();
              continue;
            }
          }

          List<NameValue> drivers = new ArrayList<NameValue>();
          for (Class<? extends Driver> cd : resolver.getClasses())
            drivers.add(new NameValue(cd.getName(), cd.getName()));

          if(drivers.size() > 0) {
            if(ci == null) {
              ci = new CacheInfo();
              ci.lastCall = timeMil;
              cache.put(location, ci);
            }
            ci.cachedObj = drivers.toArray(new NameValue[drivers.size()]);  
          } else {
            throw new JdbcDriverDiscoveryServiceException(Messages
                .getErrorString("JdbcDriverDiscoveryService.ERROR_0002_NO_DRIVERS_FOUND_IN_JDBC_DRIVER_PATH")); //$NON-NLS-1$
          }
          
        } else {
          throw new JdbcDriverDiscoveryServiceException(Messages
              .getErrorString("JdbcDriverDiscoveryService.ERROR_0002_NO_DRIVERS_FOUND_IN_JDBC_DRIVER_PATH")); //$NON-NLS-1$ 
        }
      } else {
        throw new JdbcDriverDiscoveryServiceException(Messages
            .getErrorString("JdbcDriverDiscoveryService.ERROR_0001_NO_JDBC_DRIVER_PATH_SPECIFIED")); //$NON-NLS-1$
      }
    } catch (Exception e) {
      throw new JdbcDriverDiscoveryServiceException(Messages
          .getErrorString("JdbcDriverDiscoveryService.ERROR_0001_NO_JDBC_DRIVER_PATH_SPECIFIED"), e); //$NON-NLS-1$
    }
    return ci.cachedObj;
  }
  
  public NameValue[] getRelativeAvailableJdbcDrivers(String location) throws JdbcDriverDiscoveryServiceException {
    return getDrivers(location,true);
  }
  
  private class CacheInfo
  {
    private long lastCall;
    private NameValue[] cachedObj;
  }
  
  private boolean isExist(String location) {
    File file = new File(location);
    return (file != null && file.isDirectory());     
  }

  @Override
  public void onConfigChanged() {
    initialize();
  }
}
