package org.pentaho.pac.server;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.SchedulerService;
import org.pentaho.pac.client.scheduler.Schedule;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.AppConfigProperties;
import org.pentaho.pac.server.common.BiServerTrustedProxy;
import org.pentaho.pac.server.scheduler.SchedulerAdminUIComponentProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SchedulerServiceImpl extends RemoteServiceServlet implements SchedulerService  {

  private String userName = null;
  private static final Log logger = LogFactory.getLog(SchedulerServiceImpl.class);

  // TODO sbarkdull, damn it would be nice to inject this with Spring (and some of these other props)
  private static SchedulerAdminUIComponentProxy schedulerProxy = null;
  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SchedulerServiceImpl() {
    initFromConfiguration();
    schedulerProxy = new SchedulerAdminUIComponentProxy( getUserName() );
  }

  private void initFromConfiguration()
  {
    Properties p = AppConfigProperties.getProperties();
//    appConfigProperties = p;
//    jmxHostName = StringUtils.defaultIfEmpty( p.getProperty("jmxHostName"), System.getProperty("jmxHostName") ); //$NON-NLS-1$ //$NON-NLS-2$
//    jmxPortNumber = StringUtils.defaultIfEmpty( p.getProperty("jmxPortNumber"), System.getProperty("jmxPortNumber") ); //$NON-NLS-1$ //$NON-NLS-2$
    userName = StringUtils.defaultIfEmpty( p.getProperty("pentaho.platform.userName"), System.getProperty("pentaho.platform.userName") ); //$NON-NLS-1$ //$NON-NLS-2$
//    pciContextPath = StringUtils.defaultIfEmpty( p.getProperty("pciContextPath"), System.getProperty("pciContextPath") ); //$NON-NLS-1$ //$NON-NLS-2$
//    biServerBaseURL = StringUtils.defaultIfEmpty( p.getProperty("biServerBaseURL"), System.getProperty("biServerBaseURL") ); //$NON-NLS-1$ //$NON-NLS-2$biServerBaseURL = StringUtils.defaultIfEmpty( p.getProperty("biServerBaseURL"), System.getProperty("biServerBaseURL") );
//    String strBiServerStatusCheckPeriod = StringUtils.defaultIfEmpty( p.getProperty("consoleToolBar.biServerStatusCheckPeriod"), System.getProperty("consoleToolBar.biServerStatusCheckPeriod") ); //$NON-NLS-1$ //$NON-NLS-2$
//    try {
//      biServerStatusCheckPeriod = Integer.parseInt( strBiServerStatusCheckPeriod );
//    } catch( NumberFormatException e ) {
//      logger.error( Messages.getString( "PacService.THREAD_SCHEDULING_FAILED" ), e ); //$NON-NLS-1$
//    }
  }

  private String getUserName() {
    return userName;
  }
  
  public void deleteJob(String jobName, String jobGroup) throws PacServiceException {
    schedulerProxy.deleteJob(jobName, jobGroup);
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJobNow(String jobName, String jobGroup) throws PacServiceException {
    schedulerProxy.executeJobNow(jobName, jobGroup);
  }

  /**
   * query string: schedulerAction=getJobNames
   * @throws PacServiceException 
   */
  public List<Schedule> getJobNames() throws PacServiceException {
    List<Schedule> l = schedulerProxy.getScheduleNames();
    return l;
  }

  /**
   * query string: schedulerAction=isSchedulerPaused
   * @throws PacServiceException 
   */
  public boolean isSchedulerPaused() throws PacServiceException {
    return schedulerProxy.isSchedulerPaused();
  }

  /**
   * query string: schedulerAction=pauseAll
   * @throws PacServiceException 
   */
  public void pauseAll() throws PacServiceException {
    schedulerProxy.pauseAll();
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void pauseJob(String jobName, String jobGroup) throws PacServiceException {
    schedulerProxy.pauseJob(jobName, jobGroup);
  }

  /**
   * query string: schedulerAction=resumeAll
   * @throws PacServiceException 
   */
  public void resumeAll() throws PacServiceException {
    schedulerProxy.resumeAll();
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void resumeJob(String jobName, String jobGroup) throws PacServiceException {
    schedulerProxy.resumeJob(jobName, jobGroup);
  }
}
