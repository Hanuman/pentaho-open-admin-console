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
package org.pentaho.pac.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISchedulerServiceAsync {

  public void getJobNames( AsyncCallback<Map<String,Schedule>> callback );
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList, AsyncCallback<Object> callback );
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList, AsyncCallback<Object> callback );

  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, 
      String actionsList, AsyncCallback<Object> callback );
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount,String repeatInterval,
      String actionsList, AsyncCallback<Object> callback );
  
  public void deleteJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
  public void pauseJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
  public void resumeJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
  public void executeJobs( List<Schedule> scheduleList, AsyncCallback<Object> callback );
}
