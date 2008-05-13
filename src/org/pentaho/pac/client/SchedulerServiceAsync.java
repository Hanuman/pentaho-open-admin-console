package org.pentaho.pac.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SchedulerServiceAsync {
  
  public void deleteJob( String jobName, String jobGroup, AsyncCallback callback );
  public void executeJobNow( String jobName, String jobGroup, AsyncCallback callback );
  public void getJobNames( AsyncCallback callback );
  public void isSchedulerPaused( AsyncCallback callback );
  public void pauseAll( AsyncCallback callback );
  public void pauseJob( String jobName, String jobGroup, AsyncCallback callback );
  public void resumeAll( AsyncCallback callback );
  public void resumeJob( String jobName, String jobGroup, AsyncCallback callback );
}
