package org.pentaho.pac.server.common;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.pentaho.pac.client.JdbcDriverDiscoveryService;
import org.pentaho.pac.common.JdbcDriverDiscoveryServiceException;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.server.i18n.Messages;
import org.pentaho.pac.server.util.ResolverUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class JdbcDriverDiscoveryServiceImpl extends RemoteServiceServlet implements JdbcDriverDiscoveryService {

  /**
   * 
   */
  private static final long serialVersionUID = 420L;

  private static final long interval = 300000; // every five mins

  private final HashMap<String,CacheInfo> cache = new HashMap<String,CacheInfo>();

  private String jdbcDriverPath;

  public JdbcDriverDiscoveryServiceImpl() {
  }

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    initialize();
  }
  public void initialize() {
    initFromConfiguration();
  }
  private void initFromConfiguration() {
    AppConfigProperties appCfg = AppConfigProperties.getInstance();
    jdbcDriverPath = StringUtils.defaultIfEmpty(appCfg.getJdbcDriverPath(), System.getProperty("jdbc.drivers.path")); //$NON-NLS-1$ //$NON-NLS-2$
    cache.clear();
  }

  public NameValue[] getAvailableJdbcDrivers() throws JdbcDriverDiscoveryServiceException {
    if (jdbcDriverPath != null && jdbcDriverPath.length() > 0) {
      return getAvailableJdbcDrivers(jdbcDriverPath);
    } else {
      throw new JdbcDriverDiscoveryServiceException(Messages
          .getString("JdbcDriverDiscoveryService.NO_JDBC_DRIVER_PATH_SPECIFIED"));
    }

  }

  public NameValue[] getAvailableJdbcDrivers(String location) throws JdbcDriverDiscoveryServiceException {
	  return getDrivers(location,false);
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
      else {
    	 ci = new CacheInfo();
    	 ci.lastCall = timeMil;
    	 cache.put(location, ci);
      }
      
      if (location != null && location.length() > 0) {
    	ci.lastCall = timeMil;
        File parent = relative?new File(getServletContext().getRealPath(location)):new File(location);
        System.out.println("THE PATH IS " + parent.getPath());
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

          ci.cachedObj = drivers.toArray(new NameValue[drivers.size()]);
        } else {
          ci.cachedObj = new NameValue[0];
        }
      } else {
        throw new JdbcDriverDiscoveryServiceException(Messages
            .getString("JdbcDriverDiscoveryService.NO_JDBC_DRIVER_PATH_SPECIFIED"));
      }
    } catch (Exception e) {
      throw new JdbcDriverDiscoveryServiceException(Messages
          .getString("JdbcDriverDiscoveryService.NO_JDBC_DRIVER_PATH_SPECIFIED"), e);
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
}
