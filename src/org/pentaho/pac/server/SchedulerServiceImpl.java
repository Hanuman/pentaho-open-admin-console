/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.pentaho.pac.client.SchedulerService;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;
import org.pentaho.pac.server.biplatformproxy.SchedulerAdminUIComponentProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SchedulerServiceImpl extends RemoteServiceServlet implements SchedulerService {

  /**
   * 
   */
  private static final long serialVersionUID = 420L;

  private static SchedulerAdminUIComponentProxy schedulerProxy = null;
  static {
    schedulerProxy = new SchedulerAdminUIComponentProxy();
  }
  
  public SchedulerServiceImpl() {
    super();
  }
  
  /**
   * NOTE: this is where all initialization for the Scheduler Service should occur.
   */
  public void init(ServletConfig config) throws ServletException {
    super.init( config );
  }
  
  public void deleteJob(String jobName, String jobGroup) throws SchedulerServiceException {
    schedulerProxy.deleteJob(jobName, jobGroup);
  }
  
  public void deleteJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    schedulerProxy.deleteJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void executeJob(String jobName, String jobGroup) throws SchedulerServiceException {
    schedulerProxy.executeJob(jobName, jobGroup);
  }
  
  public void executeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    schedulerProxy.executeJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=getJobNames
   * @throws SchedulerServiceException 
   */
  public Map<String,Schedule> getJobNames() throws SchedulerServiceException {
    Map<String,Schedule> schedulerMap = schedulerProxy.getSchedules();
    return schedulerMap;
  }
  
  /**
   * query string: schedulerAction=isSchedulerPaused
   * @throws SchedulerServiceException 
   */
  public boolean isSchedulerPaused() throws SchedulerServiceException {
    return schedulerProxy.isSchedulerPaused();
  }

  /**
   * query string: schedulerAction=pauseAll
   * @throws SchedulerServiceException 
   */
  public void pauseAll() throws SchedulerServiceException {
    schedulerProxy.pauseAll();
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void pauseJob(String jobName, String jobGroup) throws SchedulerServiceException {
    schedulerProxy.pauseJob(jobName, jobGroup);
  }
  
  public void pauseJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    schedulerProxy.pauseJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=resumeAll
   * @throws SchedulerServiceException 
   */
  public void resumeAll() throws SchedulerServiceException {
    schedulerProxy.resumeAll();
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void resumeJob(String jobName, String jobGroup) throws SchedulerServiceException {
    schedulerProxy.resumeJob(jobName, jobGroup);
  }
  
  public void resumeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    schedulerProxy.resumeJobs( scheduleList );
  }

  /**
   * query string: schedulerAction=createJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList) throws SchedulerServiceException {
    schedulerProxy.createCronSchedule( jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
  }

  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws SchedulerServiceException {
    schedulerProxy.createRepeatSchedule( jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatInterval, actionsList );
  
  }

  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws SchedulerServiceException {
    schedulerProxy.updateCronSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
    
  }
  
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws SchedulerServiceException {
    schedulerProxy.updateRepeatSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatInterval, actionsList );
  }
}
