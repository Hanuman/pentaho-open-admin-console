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

import org.pentaho.pac.client.SubscriptionService;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;
import org.pentaho.pac.server.biplatformproxy.SubscriptionAdminUIComponentProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubscriptionServiceImpl extends RemoteServiceServlet implements SubscriptionService {

  /**
   * 
   */
  private static final long serialVersionUID = 420L;
  private static SubscriptionAdminUIComponentProxy subscriptionProxy = null;
  static {
    subscriptionProxy = new SubscriptionAdminUIComponentProxy();
  }
  
  /**
   *
   * @throws SchedulerServiceException 
   */
  public Map<String,Schedule> getJobNames() throws SchedulerServiceException {
    Map<String,Schedule> subscriptionMap = subscriptionProxy.getSubscriptionSchedules();
    
    return subscriptionMap;
  }

  /**
   * query string: schedulerAction=createJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws SchedulerServiceException {
    subscriptionProxy.createCronSchedule( jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
  }

  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws SchedulerServiceException {
    subscriptionProxy.createRepeatSchedule( jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatInterval, actionsList );
  
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws SchedulerServiceException {
    subscriptionProxy.updateCronSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
    
  }
  
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws SchedulerServiceException {
    subscriptionProxy.updateRepeatSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatInterval, actionsList );
  }
  
  public void deleteJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    subscriptionProxy.deleteJobs( scheduleList );
  }
  
  public void resumeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    subscriptionProxy.resumeJobs( scheduleList );
  }
  
  public void pauseJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    subscriptionProxy.pauseJobs( scheduleList );
  }
  
  public void executeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    subscriptionProxy.executeJobs( scheduleList );
  }
}
