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
package org.pentaho.pac.server.biplatformproxy;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.pentaho.pac.server.i18n.Messages;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.SchedulerServiceException;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.SubscriptionXmlSerializer;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.XmlSerializerException;
import org.pentaho.pac.server.common.AppConfigProperties;
import org.pentaho.pac.server.common.BiServerTrustedProxy;
import org.pentaho.pac.server.common.ProxyException;
import org.pentaho.pac.server.common.ThreadSafeHttpClient.HttpMethodType;
import org.pentaho.pac.server.common.util.DateUtil;

public class SubscriptionAdminUIComponentProxy {

  private static final String SUBSCRIPTION_SERVICE_NAME = "SubscriptionAdmin"; //$NON-NLS-1$

  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SubscriptionAdminUIComponentProxy() {
  }

  /**
   * @throws SchedulerServiceException 
   */
  public Map<String,Schedule> getSubscriptionSchedules() throws SchedulerServiceException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "listSchedules" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml = executeGetMethod( params );
    SubscriptionXmlSerializer s = new SubscriptionXmlSerializer();
    Map<String, Schedule> m;
    try {
      m = s.getSubscriptionSchedulesFromXml( responseStrXml );
    } catch (XmlSerializerException e) {
      throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0015_PUBLIC_UNABLE_TO_RETRIEVE_SHCEDULE_LIST",e.getMessage()), e); //$NON-NLS-1$
    }
    return m;
  }
  
  /**
   * Service requires these params:
   * title
   * schedRef
   * desc
   * cron
   * group
   * 
   * @param jobName
   * @param jobGroup
   * @param description
   * @param startDate
   * @param endDate
   * @param cronString
   * @param solutionName
   * @param solutionPath
   * @param actionName
   * @throws SchedulerServiceException
   */
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws SchedulerServiceException {
    try {
      DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
      String strStartDate = dateTimeFormatter.format( startDate );
      String strEndDate = null != endDate
        ? dateTimeFormatter.format( endDate )
        : null;
      Map<String, Object> params = new HashMap<String, Object>();
      // If the action sequence list is null then we will only create a schedule without
      // a content otherwise schedule with content will be created
      if(actionsList != null && actionsList.length() > 0) {
        params.put( "schedulerAction", "doAddScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
          String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
          params.put( "actionRefs", actionsAr ); //$NON-NLS-1$
      } else {
        params.put( "schedulerAction", "doAddScheduleWithoutContent" ); //$NON-NLS-1$  //$NON-NLS-2$  
      }
  
      
      // TODO sbarkdull, this may not be right (using jobName for title)
      params.put( "title", jobName ); //$NON-NLS-1$
      params.put( "schedRef", jobName ); //$NON-NLS-1$
      params.put( "desc", description ); //$NON-NLS-1$
      params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
      if ( null != strEndDate ) {
        params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
      }
      params.put( "cron", cronString ); //$NON-NLS-1$
      params.put( "group", jobGroup ); //$NON-NLS-1$
  
      executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0011_PUBLIC_UNABLE_TO_CREATE_CRON_SCHEDULE",jobName), sse); //$NON-NLS-1$
    }    
  }
  
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList ) throws SchedulerServiceException {
    try {
      DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
      String strStartDate = dateTimeFormatter.format( startDate );
      String strEndDate = null != endDate
        ? dateTimeFormatter.format( endDate )
        : null;
      Map<String, Object> params = new HashMap<String, Object>();
      // If the action sequence list is null then we will only create a schedule without
      // a content otherwise schedule with content will be created
      if(actionsList != null && actionsList.length() > 0) {
        params.put( "schedulerAction", "doAddScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
          String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
          params.put( "actionRefs", actionsAr ); //$NON-NLS-1$
      } else {
        params.put( "schedulerAction", "doAddScheduleWithoutContent" ); //$NON-NLS-1$  //$NON-NLS-2$  
      }
      
      // TODO sbarkdull, this may not be right (using jobName for title)
      params.put( "title", jobName ); //$NON-NLS-1$
      params.put( "schedRef", jobName ); //$NON-NLS-1$
      params.put( "desc", description ); //$NON-NLS-1$
      params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
      if ( null != strEndDate ) {
        params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
      }
      if ( null != strRepeatCount ) {
        params.put( "repeat-count", strRepeatCount ); //$NON-NLS-1$
      }
      assert Integer.parseInt( repeatInterval ) >= 0 : "Invalid repeat interval"; //$NON-NLS-1$
      params.put( "repeat-time-millisecs", repeatInterval ); //$NON-NLS-1$
      params.put( "group", jobGroup ); //$NON-NLS-1$
  
      executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0013_PUBLIC_UNABLE_TO_CREATE_REPEATING_SCHEDULE",jobName), sse); //$NON-NLS-1$
    }
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList ) throws SchedulerServiceException {
    try {
      DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
      String strStartDate = dateTimeFormatter.format( startDate );
      String strEndDate = null != endDate
        ? dateTimeFormatter.format( endDate )
        : null;
      Map<String, Object> params = new HashMap<String, Object>();
      // If the action sequence list is null then we will only create a schedule without
      // a content otherwise schedule with content will be created
      if(actionsList != null && actionsList.length() > 0) {
        params.put( "schedulerAction", "doEditScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
          String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
          params.put( "actionRefs", actionsAr ); //$NON-NLS-1$
      } else {
        params.put( "schedulerAction", "doEditScheduleWithoutContent" ); //$NON-NLS-1$  //$NON-NLS-2$ 
      }
      
      params.put( "schedId", schedId ); //$NON-NLS-1$
      params.put( "schedRef", jobName ); //$NON-NLS-1$
      params.put( "group", jobGroup ); //$NON-NLS-1$
      params.put( "desc", description ); //$NON-NLS-1$
      // TODO sbarkdull, this may not be right (using jobName for title)
      params.put( "title", jobName ); //$NON-NLS-1$
      params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
      if ( null != strEndDate ) {
        params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
      }
      params.put( "cron", cronString ); //$NON-NLS-1$S
  
      executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0012_PUBLIC_UNABLE_TO_UPDATE_CRON_SCHEDULE", oldJobName,jobName), sse); //$NON-NLS-1$
    }
  }
  
  public void updateRepeatSchedule(  String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList ) throws SchedulerServiceException {
    try {
      DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
      String strStartDate = dateTimeFormatter.format( startDate );
      String strEndDate = null != endDate 
        ? dateTimeFormatter.format( endDate )
        : null;
      Map<String, Object> params = new HashMap<String, Object>();
      // TODO sbarkdull, some of these params may not be used, clean up
      // If the action sequence list is null then we will only create a schedule without
      // a content otherwise schedule with content will be created
      if(actionsList != null && actionsList.length() > 0) {
        params.put( "schedulerAction", "doEditScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
          String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
          params.put( "actionRefs", actionsAr ); //$NON-NLS-1$
      } else {
        params.put( "schedulerAction", "doEditScheduleWithoutContent" ); //$NON-NLS-1$  //$NON-NLS-2$ 
      }
      params.put( "schedId", schedId ); //$NON-NLS-1$
      params.put( "schedRef", jobName ); //$NON-NLS-1$
      params.put( "group", jobGroup ); //$NON-NLS-1$
      params.put( "desc", description ); //$NON-NLS-1$
      // TODO sbarkdull, this may not be right (using jobName for title)
      params.put( "title", jobName ); //$NON-NLS-1$
      params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
      if ( null != strEndDate ) {
        params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
      }
      if ( null != strRepeatCount ) {
        params.put( "repeat-count", strRepeatCount ); //$NON-NLS-1$
      }
      assert Integer.parseInt( repeatInterval ) >= 0 : "Invalid repeat interval"; //$NON-NLS-1$
      params.put( "repeat-time-millisecs", repeatInterval ); //$NON-NLS-1$
  
      executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0014_PUBLIC_UNABLE_TO_UPDATE_REPEATING_SCHEDULE", oldJobName,jobName), sse); //$NON-NLS-1$
    }
  }
  
  public void deleteJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "doDeleteScheduleContentAndSubscription" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "schedId", s.getSchedId() ); //$NON-NLS-1$
  
        executeGetMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0001_PUBLIC_UNABLE_TO_DELETE_JOB", s.getSchedId()), sse); //$NON-NLS-1$
      }      
    }
  }
  public void pauseJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "doPauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobId", s.getJobName() ); //$NON-NLS-1$

      executeGetMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0003_PUBLIC_UNABLE_TO_PAUSE_JOB", s.getJobName()), sse); //$NON-NLS-1$        
    }
    }
  }
  
  public void resumeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "doResumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobId", s.getJobName() ); //$NON-NLS-1$
  
        executeGetMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0004_PUBLIC_UNABLE_TO_RESUME_JOB", s.getJobName()), sse); //$NON-NLS-1$        
      }
    }
  }
  
  public void executeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "doExecuteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobId", s.getJobName() ); //$NON-NLS-1$
  
        executeGetMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SubscriptionAdminUIComponentProxy.ERROR_0002_PUBLIC_UNABLE_TO_EXECUTE_JOB", s.getJobName()), sse); //$NON-NLS-1$        
      }
    }
  }
  
  private String executeGetMethod( Map<String, Object> params ) throws SchedulerServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SUBSCRIPTION_SERVICE_NAME, HttpMethodType.GET, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SchedulerServiceException(ExceptionParser.getErrorMessage(e.getMessage(), e.getMessage()), e );
    }
    SubscriptionXmlSerializer s = new SubscriptionXmlSerializer();
    s.detectSubscriptionExceptionInXml( strXmlResponse );
    s.detectSubscriptionErrorInXml( strXmlResponse );
    return strXmlResponse;
  }
  
  private String executePostMethod(Map<String, Object> params ) throws SchedulerServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SUBSCRIPTION_SERVICE_NAME, HttpMethodType.POST, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SchedulerServiceException(ExceptionParser.getErrorMessage(e.getMessage(), e.getMessage()), e );
    }
    SubscriptionXmlSerializer s = new SubscriptionXmlSerializer();
    s.detectSubscriptionExceptionInXml( strXmlResponse );
    s.detectSubscriptionErrorInXml( strXmlResponse );
    return strXmlResponse;
  }
}