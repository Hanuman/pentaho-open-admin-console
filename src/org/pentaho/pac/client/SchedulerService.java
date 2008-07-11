package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.PacServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SchedulerService extends RemoteService, ISchedulerService {
  
  public void deleteJob( String jobName, String jobGroup ) throws PacServiceException;
  public void deleteJobs( List<Schedule> scheduleList ) throws PacServiceException;
  public void executeJob( String jobName, String jobGroup ) throws PacServiceException;
  public void executeJobs( List<Schedule> scheduleList ) throws PacServiceException;
  public boolean isSchedulerPaused() throws PacServiceException;
  public void pauseAll() throws PacServiceException;
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException;
  public void pauseJobs( List<Schedule> scheduleList ) throws PacServiceException;
  public void resumeAll() throws PacServiceException;
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException;
  public void resumeJobs( List<Schedule> scheduleList ) throws PacServiceException;
}
