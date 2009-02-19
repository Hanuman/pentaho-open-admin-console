package org.pentaho.pac.server.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.HibernateConfigurationService;
import org.pentaho.pac.common.HibernateConfigurationServiceException;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.server.i18n.Messages;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HibernateConfigurationServiceImpl extends RemoteServiceServlet implements HibernateConfigurationService, IConsoleConfigEventListener {

  private static final long serialVersionUID = 420L;

  List<NameValue> hibernateConfigurations = new ArrayList<NameValue>();

  private static final String HIBERNATE = "/system/hibernate"; //$NON-NLS-1$
  private static final Log logger = LogFactory.getLog(HibernateConfigurationServiceImpl.class);

  public HibernateConfigurationServiceImpl() {
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
    try {
      AppConfigProperties.getInstance().refreshConfig();      
    } catch(AppConfigException ace) {
      logger.error(Messages.getErrorString(
          "HibernateConfigurationService.ERROR_0003_UNABLE_TO_INITIALIZE_CONFIGURATION", ace.getLocalizedMessage())); //$NON-NLS-1$            
    }
  }

  public NameValue[] getAvailableHibernateConfigurations() throws HibernateConfigurationServiceException {
    String solutionPath = AppConfigProperties.getInstance().getSolutionPath();
    if (solutionPath != null && solutionPath.length() > 0) {
      return getAvailableHibernateConfigurations(solutionPath);
    } else {
      throw new HibernateConfigurationServiceException(Messages
          .getErrorString("HibernateConfigurationService.ERROR_0001_SOLUTION_PATH_IS_NULL"));//$NON-NLS-1$
    }
  }

  public NameValue[] getAvailableHibernateConfigurations(String solutionPath)
      throws HibernateConfigurationServiceException {
    return getConfigurations(solutionPath);
  }

  private NameValue[] getConfigurations(String solutionPath) throws HibernateConfigurationServiceException {
    if (hibernateConfigurations.size() == 0) {
      if (solutionPath != null && solutionPath.length() > 0) {
        File parent = new File(solutionPath + HIBERNATE);
        FilenameFilter filter = new FilenameFilter() {

          public boolean accept(File dir, String pathname) {
            return pathname.endsWith(".xml") && pathname.contains("hibernate.cfg"); //$NON-NLS-1$//$NON-NLS-2$
          }
        };

        if (parent.exists()) {
          for (File file : parent.listFiles(filter)) {
            String fileName = file.getName();
            if(fileName != null && fileName.length() > 0) {
              String truncatedFileName = fileName.substring(0, fileName.indexOf(".hibernate"));//$NON-NLS-1$            
              hibernateConfigurations.add(new NameValue(truncatedFileName, fileName));
            }
          }
        } else {
          throw new HibernateConfigurationServiceException(Messages
              .getErrorString("HibernateConfigurationService.ERROR_0002_NO_CONFIGURATION_FILES_FOUND_IN_PATH")); //$NON-NLS-1$
        }

      } else {
        throw new HibernateConfigurationServiceException(Messages
            .getErrorString("HibernateConfigurationService.ERROR_0001_SOLUTION_PATH_IS_NULL")); //$NON-NLS-1$
      }
    }
    return hibernateConfigurations.toArray(new NameValue[hibernateConfigurations.size()]);
  }

  public void onConfigChanged() {
    initialize();
  }
}
