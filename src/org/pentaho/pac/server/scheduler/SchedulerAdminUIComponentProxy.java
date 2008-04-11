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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.Job;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.BiServerTrustedProxy;

public class SchedulerAdminUIComponentProxy {

  private static final String SCHEDULER_SERVICE_NAME = "SchedulerAdmin"; //$NON-NLS-1$
  private static final int NUM_RETRIES = 3; // TODO is used?
  private String userName = null;

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
    Map params = new HashMap();
    params.put( "schedulerAction", "deleteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
    
    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void executeJobNow( String jobName, String jobGroup ) throws PacServiceException {
    Map params = new HashMap();
    params.put( "schedulerAction", "executeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=getJobNames
   * @throws PacServiceException 
   */
  public List<Job> getJobNames() throws PacServiceException {
    Map params = new HashMap();
    params.put( "schedulerAction", "getJobNames" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml = biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );

    // TODO sbarkdull, should XmlSerializer be a static class?
    XmlSerializer s = new XmlSerializer();
    List<Job> l = s.getJobNamesFromXml( responseStrXml );
    return l;
  }
  
  /**
   * query string: schedulerAction=isSchedulerPaused
   * @throws PacServiceException 
   */
  public boolean isSchedulerPaused() throws PacServiceException {
    Map params = new HashMap();
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
    Map params = new HashMap();
    params.put( "schedulerAction", "suspendScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException {
    Map params = new HashMap();
    params.put( "schedulerAction", "pauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=resumeScheduler
   * @throws PacServiceException 
   */
  public void resumeAll() throws PacServiceException {
    Map params = new HashMap();
    params.put( "schedulerAction", "resumeScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml= biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName, params );
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException {
    Map params = new HashMap();
    params.put( "schedulerAction", "resumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "jobName", jobName ); //$NON-NLS-1$
    params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SCHEDULER_SERVICE_NAME, userName  , params );
  }

}
