package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SchedulerServiceAsync extends ISchedulerServiceAsync {

  public void deleteJob( String jobName, String jobGroup, AsyncCallback callback );
  public void executeJob( String jobName, String jobGroup, AsyncCallback callback );
  public void isSchedulerPaused( AsyncCallback callback );
  public void pauseAll( AsyncCallback callback );
  public void pauseJob( String jobName, String jobGroup, AsyncCallback callback );
  public void resumeAll( AsyncCallback callback );
  public void resumeJob( String jobName, String jobGroup, AsyncCallback callback );
}
