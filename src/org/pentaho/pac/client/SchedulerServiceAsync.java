package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;

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
