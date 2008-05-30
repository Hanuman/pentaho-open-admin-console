package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;

import org.pentaho.pac.client.scheduler.Schedule;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SchedulerServiceAsync {
  
  public void deleteJob( String jobName, String jobGroup, AsyncCallback callback );
  public void deleteJobs( List<Schedule> scheduleList, AsyncCallback callback );
  public void executeJob( String jobName, String jobGroup, AsyncCallback callback );
  public void executeJobs( List<Schedule> scheduleList, AsyncCallback callback );
  public void getJobNames( AsyncCallback callback );
  public void isSchedulerPaused( AsyncCallback callback );
  public void pauseAll( AsyncCallback callback );
  public void pauseJob( String jobName, String jobGroup, AsyncCallback callback );
  public void pauseJobs( List<Schedule> scheduleList, AsyncCallback callback );
  public void resumeAll( AsyncCallback callback );
  public void resumeJob( String jobName, String jobGroup, AsyncCallback callback );
  public void resumeJobs( List<Schedule> scheduleList, AsyncCallback callback );
  public void createCronJob( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String solutionName, String solutionPath, String actionName, AsyncCallback callback );
  public void createRepeatJob( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String repeatTimeMillisecs,
      String solutionName, String solutionPath, String actionName, AsyncCallback callback );
  public void updateCronJob( String oldJobName, String oldJobGroup,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, 
      String solutionName, String solutionPath, String actionName, AsyncCallback callback );
  public void updateRepeatJob( String oldJobName, String oldJobGroup,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String repeatTimeMillisecs,
      String solutionName, String solutionPath, String actionName, AsyncCallback callback );
}
