package org.pentaho.pac.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.SchedulerService;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.AppConfigProperties;
import org.pentaho.pac.server.scheduler.SchedulerAdminUIComponentProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SchedulerServiceImpl extends RemoteServiceServlet implements SchedulerService {

  private static final Log logger = LogFactory.getLog(SchedulerServiceImpl.class);
  
  // TODO sbarkdull, damn it would be nice to inject this with Spring (and some of these other props)
  private static SchedulerAdminUIComponentProxy schedulerProxy = null;
  static {
    String userName = StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getProperty("pentaho.platform.userName"), System.getProperty("pentaho.platform.userName") ); //$NON-NLS-1$ //$NON-NLS-2$
    String biServerBaseURL = AppConfigProperties.getInstance().getProperty( "biServerBaseURL" );
    schedulerProxy = new SchedulerAdminUIComponentProxy( userName, biServerBaseURL );
  }
  
  public SchedulerServiceImpl() {
    super();
  }
  
  /**
   * NOTE: this is where all initialization for the Scheduler Service should occur.
   */
  public void init(ServletConfig config) throws ServletException {
    super.init( config );
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
  public Map<String,Schedule> getJobNames() throws PacServiceException {
    Map<String,Schedule> schedulerMap = schedulerProxy.getSchedules();
    return schedulerMap;
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
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList) throws PacServiceException {
    schedulerProxy.createCronSchedule( jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
  }

  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs, 
      String actionsList ) throws PacServiceException {
    schedulerProxy.createRepeatSchedule( jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatTimeMillisecs, actionsList );
  
  }

  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws PacServiceException {
    schedulerProxy.updateCronSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
    
  }
  
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs, 
      String actionsList ) throws PacServiceException {
    schedulerProxy.updateRepeatSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatTimeMillisecs, actionsList );
  }
}
