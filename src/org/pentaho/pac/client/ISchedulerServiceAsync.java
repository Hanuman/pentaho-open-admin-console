package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.PacServiceException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISchedulerServiceAsync {

  public void getJobNames( AsyncCallback<Map<String,Schedule>> callback );
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList, AsyncCallback<Object> callback );
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList, AsyncCallback<Object> callback );

  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, 
      String actionsList, AsyncCallback<Object> callback );
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount,String repeatInterval,
      String actionsList, AsyncCallback<Object> callback );
  
  public void deleteJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
  public void pauseJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
  public void resumeJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
  public void executeJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
}
