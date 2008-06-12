package org.pentaho.pac.server;

import java.util.Date;
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

  private static String userName = null;
  private static final Log logger = LogFactory.getLog(SchedulerServiceImpl.class);

  // TODO sbarkdull, damn it would be nice to inject this with Spring (and some of these other props)
  private static SchedulerAdminUIComponentProxy schedulerProxy = null;
  static {
    initFromConfiguration();
    schedulerProxy = new SchedulerAdminUIComponentProxy( getUserName() );
  }
  
  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SchedulerServiceImpl() {
  }

  private static void initFromConfiguration()
  {
    Properties p = AppConfigProperties.getProperties();
    userName = StringUtils.defaultIfEmpty( p.getProperty("pentaho.platform.userName"), System.getProperty("pentaho.platform.userName") ); //$NON-NLS-1$ //$NON-NLS-2$
  }

  private static String getUserName() {
    return userName;
  }
  
  public void deleteJob(String jobName, String jobGroup) throws PacServiceException {
    schedulerProxy.deleteJob(jobName, jobGroup);
  }
  
  public void deleteJobs( List<Schedule> scheduleList ) throws PacServiceException {
    schedulerProxy.deleteJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJob(String jobName, String jobGroup) throws PacServiceException {
    schedulerProxy.executeJob(jobName, jobGroup);
  }
  
  public void executeJobs( List<Schedule> scheduleList ) throws PacServiceException {
    schedulerProxy.executeJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=getJobNames
   * @throws PacServiceException 
   */
  public List<Schedule> getJobNames() throws PacServiceException {
    List<Schedule> l = schedulerProxy.getAllScheduleProperties();
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
  
  public void pauseJobs( List<Schedule> scheduleList ) throws PacServiceException {
    schedulerProxy.pauseJobs( scheduleList );
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
  
  public void resumeJobs( List<Schedule> scheduleList ) throws PacServiceException {
    schedulerProxy.resumeJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=createJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void createCronJob( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String solutionName, String solutionPath, String actionName) throws PacServiceException {
    schedulerProxy.createCronSchedule( jobName, jobGroup, description,
        startDate, endDate, cronString, solutionName, solutionPath, actionName );
  }

  public void createRepeatJob( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs, 
      String solutionName, String solutionPath, String actionName ) throws PacServiceException {
    schedulerProxy.createRepeatSchedule( jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatTimeMillisecs, solutionName, solutionPath, actionName );
  
  }

  public void updateCronJob( String oldJobName, String oldJobGroup,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String solutionName, String solutionPath, String actionName ) throws PacServiceException {
    schedulerProxy.updateCronSchedule( oldJobName, oldJobGroup, jobName, jobGroup, description,
        startDate, endDate, cronString, solutionName, solutionPath, actionName );
    
  }
  
  public void updateRepeatJob( String oldJobName, String oldJobGroup,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs, 
      String solutionName, String solutionPath, String actionName ) throws PacServiceException {
    schedulerProxy.updateRepeatSchedule( oldJobName, oldJobGroup, jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatTimeMillisecs, solutionName, solutionPath, actionName );
  }
}
