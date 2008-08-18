package org.pentaho.pac.client;

import org.pentaho.pac.common.SchedulerServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SchedulerService extends RemoteService, ISchedulerService {
  
  public void deleteJob( String jobName, String jobGroup ) throws SchedulerServiceException;
  public void executeJob( String jobName, String jobGroup ) throws SchedulerServiceException;
  public boolean isSchedulerPaused() throws SchedulerServiceException;
  public void pauseAll() throws SchedulerServiceException;
  public void pauseJob( String jobName, String jobGroup ) throws SchedulerServiceException;
  public void resumeAll() throws SchedulerServiceException;
  public void resumeJob( String jobName, String jobGroup ) throws SchedulerServiceException;
}
