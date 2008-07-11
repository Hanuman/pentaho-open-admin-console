package org.pentaho.pac.client;

import java.util.Date;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISchedulerServiceAsync {

  public void getJobNames( AsyncCallback<Map<String,Schedule>> callback );
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList, AsyncCallback callback );
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs,
      String actionsList, AsyncCallback callback );

  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, 
      String actionsList, AsyncCallback callback );
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount,String repeatTimeMillisecs,
      String actionsList, AsyncCallback callback );
}
