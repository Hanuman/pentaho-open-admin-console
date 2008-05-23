package org.pentaho.pac.client;

import java.util.List;

import org.pentaho.pac.client.scheduler.Schedule;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.UserRoleSecurityInfo;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.roles.DuplicateRoleException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SchedulerService extends RemoteService {
  
  public void deleteJob( String jobName, String jobGroup ) throws PacServiceException;
  public void executeJobNow( String jobName, String jobGroup ) throws PacServiceException;
  public List<Schedule> getJobNames() throws PacServiceException;
  public boolean isSchedulerPaused() throws PacServiceException;
  public void pauseAll() throws PacServiceException;
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException;
  public void resumeAll() throws PacServiceException;
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException;
  public void createCronJob( String jobName, String jobGroup, String description,
      String cronString, String solutionName, String solutionPath, String actionName ) throws PacServiceException;
  public void createRepeatJob( String jobName, String jobGroup, String description,
      String startTime, String repeatTimeMillisecs, 
      String solutionName, String solutionPath, String actionName ) throws PacServiceException;
}
