/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.server.scheduler;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.pentaho.pac.client.scheduler.Schedule;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.BiServerTrustedProxy;

public class SchedulerAdminUIComponentProxy {

  private static final String SCHEDULER_SERVICE_NAME = "SchedulerAdmin"; //$NON-NLS-1$
  private static final int NUM_RETRIES = 3; // TODO is used?
  private String userName = null;
  private static DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.LONG,
    DateFormat.MEDIUM,
    Locale.getDefault());

  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SchedulerAdminUIComponentProxy( String userName ) {
    this.userName = userName;
  }

  /**
   * query string: schedulerAction=deleteJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void deleteJob( String jobName, String jobGroup ) throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "deleteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
    
    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  // TODO sbarkdull, this implementation is very slow, needs to be replaced. Replacement
  // requires a server side interface to match this one.
  /**
   * query string: schedulerAction=deleteJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void deleteJobs( List<Schedule> scheduleList ) throws PacServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, String> params = new HashMap<String, String>();
      params.put( "schedulerAction", "deleteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
      params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
      
      String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    }
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJob( String jobName, String jobGroup ) throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "executeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * TODO sbarkdull, needs to be optimized
   * 
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJobs( List<Schedule> scheduleList ) throws PacServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, String> params = new HashMap<String, String>();
      params.put( "schedulerAction", "executeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
      params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
      
      String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    }
  }
  
  /**
   * query string: schedulerAction=getJobNames
   * @throws PacServiceException 
   */
  public List<Schedule> getAllScheduleProperties() throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "getJobNames" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml = biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );

    // TODO sbarkdull, should XmlSerializer be a static class?
    XmlSerializer s = new XmlSerializer();
    List<Schedule> l = s.getAllSchedulePropertiesFromXml( responseStrXml );
    return l;
  }
  
  /**
   * query string: schedulerAction=isSchedulerPaused
   * @throws PacServiceException 
   */
  public boolean isSchedulerPaused() throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "isSchedulerPaused" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    XmlSerializer s = new XmlSerializer();
    boolean isRunning = s.getSchedulerStatusFromXml( responseStrXml );
    
    return isRunning;
  }

  /**
   * query string: schedulerAction=suspendScheduler
   * @throws PacServiceException 
   */
  public void pauseAll() throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "suspendScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "pauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * TODO sbarkdull, needs to be optimized
   * 
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void pauseJobs( List<Schedule> scheduleList ) throws PacServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, String> params = new HashMap<String, String>();
      params.put( "schedulerAction", "pauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
      params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
      
      String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    }
  }

  /**
   * query string: schedulerAction=resumeScheduler
   * @throws PacServiceException 
   */
  public void resumeAll() throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "resumeScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException {
    Map<String, String> params = new HashMap<String, String>();
    params.put( "schedulerAction", "resumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * TODO sbarkdull, needs to be optimized
   * 
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void resumeJobs( List<Schedule> scheduleList ) throws PacServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, String> params = new HashMap<String, String>();
      params.put( "schedulerAction", "resumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
      params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
      
      String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    }
  }
  
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String solutionName, String solutionPath, String actionName ) throws PacServiceException {
    
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = dateTimeFormatter.format( endDate );
    Map<String, String> params = new HashMap<String, String>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "createJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
    params.put( "description", description ); //$NON-NLS-1$
    params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
    params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
    params.put( "cron-string", cronString ); //$NON-NLS-1$
    params.put( "solution", solutionName ); //$NON-NLS-1$
    params.put( "path", solutionPath ); //$NON-NLS-1$
    params.put( "action", actionName ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    
    int ii=0; // TODO clean up
    // TODO sbarkdull, need to return a status
    // TODO sbarkdull, get strings into static finals for all parameters
  }
  
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String repeatTimeMillisecs,
      String solutionName, String solutionPath, String actionName ) throws PacServiceException {
    
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = dateTimeFormatter.format( endDate );
    Map<String, String> params = new HashMap<String, String>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "createJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
    params.put( "description", description ); //$NON-NLS-1$
    params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
    params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
    params.put( "repeat-time-millisecs", repeatTimeMillisecs ); //$NON-NLS-1$
    params.put( "solution", solutionName ); //$NON-NLS-1$
    params.put( "path", solutionPath ); //$NON-NLS-1$
    params.put( "action", actionName ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    
    int ii=0; // TODO clean up
    // TODO sbarkdull, need to return a status
    // TODO sbarkdull, get strings into static finals for all parameters
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String solutionName, String solutionPath, String actionName ) throws PacServiceException {
    
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = dateTimeFormatter.format( endDate );
    Map<String, String> params = new HashMap<String, String>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "updateJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "oldJobName", oldJobName ); //$NON-NLS-1$
    params.put( "oldJobGroup", oldJobGroup ); //$NON-NLS-1$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
    params.put( "description", description ); //$NON-NLS-1$
    params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
    params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
    params.put( "cron-string", cronString ); //$NON-NLS-1$
    params.put( "solution", solutionName ); //$NON-NLS-1$
    params.put( "path", solutionPath ); //$NON-NLS-1$
    params.put( "action", actionName ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    
    int ii=0; // TODO clean up
    // TODO sbarkdull, need to return a status
    // TODO sbarkdull, get strings into static finals for all parameters
  }
  
  public void updateRepeatSchedule(  String oldJobName, String oldJobGroup,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String repeatTimeMillisecs,
      String solutionName, String solutionPath, String actionName ) throws PacServiceException {

    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = dateTimeFormatter.format( endDate );
    Map<String, String> params = new HashMap<String, String>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "updateJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "oldJobName", oldJobName ); //$NON-NLS-1$
    params.put( "oldJobGroup", oldJobGroup ); //$NON-NLS-1$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
    params.put( "description", description ); //$NON-NLS-1$
    params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
    params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
    params.put( "repeat-time-millisecs", repeatTimeMillisecs ); //$NON-NLS-1$
    params.put( "solution", solutionName ); //$NON-NLS-1$
    params.put( "path", solutionPath ); //$NON-NLS-1$
    params.put( "action", actionName ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
    
    int ii=0; // TODO clean up
    // TODO sbarkdull, need to return a status
    // TODO sbarkdull, get strings into static finals for all parameters
  }
}
