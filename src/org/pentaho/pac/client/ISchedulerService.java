package org.pentaho.pac.client;

import java.util.Date;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.PacServiceException;

public interface ISchedulerService {

  public Map<String,Schedule> getJobNames() throws PacServiceException;
  
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws PacServiceException;
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs, 
      String actionsList ) throws PacServiceException;
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList ) throws PacServiceException;
  
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs,
      String actionsList ) throws PacServiceException;
}
