package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;

public interface ISchedulerService {

  public Map<String,Schedule> getJobNames() throws SchedulerServiceException;
  
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws SchedulerServiceException;
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws SchedulerServiceException;
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList ) throws SchedulerServiceException;
  
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList ) throws SchedulerServiceException;
  
  public void deleteJobs( List<Schedule> scheduleList ) throws SchedulerServiceException;
}
