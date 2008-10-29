package org.pentaho.pac.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SchedulerServiceAsync extends ISchedulerServiceAsync {

  public void deleteJob( String jobName, String jobGroup, AsyncCallback<Object> callback );
  public void executeJob( String jobName, String jobGroup, AsyncCallback<Object> callback );
  public void isSchedulerPaused( AsyncCallback<Boolean> callback );
  public void pauseAll( AsyncCallback<Object> callback );
  public void pauseJob( String jobName, String jobGroup, AsyncCallback<Object> callback );
  public void resumeAll( AsyncCallback<Object> callback );
  public void resumeJob( String jobName, String jobGroup, AsyncCallback<Object> callback );
}
