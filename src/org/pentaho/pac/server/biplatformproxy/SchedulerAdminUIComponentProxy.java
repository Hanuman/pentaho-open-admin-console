/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 *
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.server.biplatformproxy;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.server.i18n.Messages;
import org.apache.commons.lang.StringUtils;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.utils.ExceptionParser;
import org.pentaho.pac.common.SchedulerServiceException;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.SchedulerXmlSerializer;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.XmlSerializerException;
import org.pentaho.pac.server.common.AppConfigProperties;
import org.pentaho.pac.server.common.BiServerTrustedProxy;
import org.pentaho.pac.server.common.ProxyException;
import org.pentaho.pac.server.common.ThreadSafeHttpClient.HttpMethodType;
import org.pentaho.pac.server.common.util.DateUtil;

public class SchedulerAdminUIComponentProxy {

  private static final String SCHEDULER_SERVICE_NAME = "SchedulerAdmin"; //$NON-NLS-1$
  // matches a string that contains no commas, and exactly three substrings each separated by a "/"
  private static String RE_MATCH_ACTION_REF = "^[^,/]+/[^,/]+/[^,/]+$"; //$NON-NLS-1$

  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SchedulerAdminUIComponentProxy() {
  }

  /**
   * query string: schedulerAction=deleteJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void deleteJob( String jobName, String jobGroup ) throws SchedulerServiceException {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "deleteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", jobName ); //$NON-NLS-1$
      params.put( "jobGroup", jobGroup ); //$NON-NLS-1$

      executeGetMethod( params );      
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0001_PRIVATE_UNABLE_TO_DELETE_JOB", jobName), sse); //$NON-NLS-1$
    }
  }

  // TODO sbarkdull, this implementation is very slow, needs to be replaced. Replacement
  // requires a server side interface to match this one.
  /**
   * query string: schedulerAction=deleteJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void deleteJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "deleteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
        params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$

        executeGetMethod( params );        
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0001_PRIVATE_UNABLE_TO_DELETE_JOB", s.getJobName()), sse); //$NON-NLS-1$        
      }
    }
  }

  /**
   * query string: schedulerAction=executeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void executeJob( String jobName, String jobGroup ) throws SchedulerServiceException {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "executeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", jobName ); //$NON-NLS-1$
      params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
  
      executeGetMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0002_PRIVATE_UNABLE_TO_EXECUTE_JOB", jobName), sse); //$NON-NLS-1$        
    }

  }

  /**
   * TODO sbarkdull, needs to be optimized
   * 
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void executeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "executeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
        params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
  
        executeGetMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0002_PRIVATE_UNABLE_TO_EXECUTE_JOB", s.getJobName()), sse); //$NON-NLS-1$        
      }      
    }
  }
  
  /**
   * query string: schedulerAction=getJobNames
   * @throws SchedulerServiceException 
   */
  public Map<String,Schedule> getSchedules() throws SchedulerServiceException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "getJobNames" ); //$NON-NLS-1$  //$NON-NLS-2$

    String responseStrXml = executeGetMethod( params );

    SchedulerXmlSerializer s = new SchedulerXmlSerializer();
    Map<String, Schedule> m;
    try {
      m = s.getSchedulesFromXml( responseStrXml );
    } catch (XmlSerializerException e) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0005_PRIVATE_UNABLE_TO_RETRIEVE_JOB", e.getMessage()), e); //$NON-NLS-1$      
    }
    return m;
  }
  
  /**
   * query string: schedulerAction=isSchedulerPaused
   * @throws SchedulerServiceException 
   */
  public boolean isSchedulerPaused() throws SchedulerServiceException {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "isSchedulerPaused" ); //$NON-NLS-1$  //$NON-NLS-2$
  
      String responseStrXml = executeGetMethod( params );
      SchedulerXmlSerializer s = new SchedulerXmlSerializer();
      boolean isRunning = s.getSchedulerStatusFromXml( responseStrXml );
      
      return isRunning;
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0006_PRIVATE_UNABLE_TO_CHECK_IS_PAUSED", sse.getMessage()), sse); //$NON-NLS-1$        
    }      
  }

  /**
   * query string: schedulerAction=suspendScheduler
   * @throws SchedulerServiceException 
   */
  public void pauseAll() throws SchedulerServiceException {
    try {    
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "suspendScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$
  
      executeGetMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0009_PRIVATE_UNABLE_TO_PAUSE_SHCEDULER", sse.getMessage()), sse); //$NON-NLS-1$        
    } 
  }

  /**
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void pauseJob( String jobName, String jobGroup ) throws SchedulerServiceException {
    try {    
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "pauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", jobName ); //$NON-NLS-1$
      params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
  
      executeGetMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0003_PRIVATE_UNABLE_TO_PAUSE_JOB", jobName), sse); //$NON-NLS-1$        
    } 
  }

  /**
   * TODO sbarkdull, needs to be optimized
   * 
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void pauseJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {    
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "pauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
        params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
  
        executeGetMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0003_PRIVATE_UNABLE_TO_PAUSE_JOB", s.getJobName()), sse); //$NON-NLS-1$        
      }       
    }
  }

  /**
   * query string: schedulerAction=resumeScheduler
   * @throws SchedulerServiceException 
   */
  public void resumeAll() throws SchedulerServiceException {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "resumeScheduler" ); //$NON-NLS-1$  //$NON-NLS-2$
  
      executeGetMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0010_PRIVATE_UNABLE_TO_RESUME_SCHEDULER", sse.getMessage()), sse); //$NON-NLS-1$        
    } 
  }

  /**
   * query string: schedulerAction=resumeJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void resumeJob( String jobName, String jobGroup ) throws SchedulerServiceException {
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "resumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobName", jobName ); //$NON-NLS-1$
      params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
  
      executeGetMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0004_PRIVATE_UNABLE_TO_RESUME_JOB", jobName), sse); //$NON-NLS-1$        
    }     
  }

  /**
   * TODO sbarkdull, needs to be optimized
   * 
   * query string: schedulerAction=pauseJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws SchedulerServiceException 
   */
  public void resumeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "schedulerAction", "resumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobName", s.getJobName() ); //$NON-NLS-1$
        params.put( "jobGroup", s.getJobGroup() ); //$NON-NLS-1$
  
        executeGetMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0004_PRIVATE_UNABLE_TO_RESUME_JOB", s.getJobName()), sse); //$NON-NLS-1$        
      }           
    }
  }
  
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionRef ) throws SchedulerServiceException {
      try {
        if(actionRef != null && actionRef.length() > 0) {
          assert actionRef.matches( RE_MATCH_ACTION_REF ) : "Invalid actionRef " + actionRef; //$NON-NLS-1$ 
        }
        DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
        String strStartDate = dateTimeFormatter.format( startDate );
        String strEndDate = null != endDate
          ? dateTimeFormatter.format( endDate )
          : null;
        Map<String, Object> params = new HashMap<String, Object>();
        // TODO sbarkdull, some of these params may not be used, clean up
        params.put( "schedulerAction", "createJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobName", jobName ); //$NON-NLS-1$
        params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
        params.put( "description", description ); //$NON-NLS-1$
        params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
        if ( null != strEndDate ) {
          params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
        }
        params.put( "cron-string", cronString ); //$NON-NLS-1$
        if(actionRef != null && actionRef.length() > 0) {
          params.put( "actionRef", actionRef ); //$NON-NLS-1$ 
        }
    
        executePostMethod( params );
      } catch(SchedulerServiceException sse) {
        throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0011_PRIVATE_UNABLE_TO_CREATE_CRON_SCHEDULE", jobName), sse); //$NON-NLS-1$
      }
  }
  
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionRef ) throws SchedulerServiceException {
    try {
      if(actionRef != null && actionRef.length() > 0) {
        assert actionRef.matches( RE_MATCH_ACTION_REF ) : "Invalid actionRef " + actionRef; //$NON-NLS-1$ 
      }
    
      DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
        String strStartDate = dateTimeFormatter.format( startDate );
        String strEndDate = null != endDate
          ? dateTimeFormatter.format( endDate )
          : null;
        Map<String, Object> params = new HashMap<String, Object>();
        // TODO sbarkdull, some of these params may not be used, clean up
        params.put( "schedulerAction", "createJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "jobName", jobName ); //$NON-NLS-1$
        params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
        params.put( "description", description ); //$NON-NLS-1$
        params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
        if ( null != strEndDate ) {
          params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
        }
        if ( null != strRepeatCount ) {
          params.put( "repeat-count", strRepeatCount ); //$NON-NLS-1$
        }
        params.put( "repeat-time-millisecs", repeatInterval ); //$NON-NLS-1$
        if(actionRef != null && actionRef.length() > 0) {
          params.put( "actionRef", actionRef ); //$NON-NLS-1$ 
        }
        executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0013_PRIVATE_UNABLE_TO_CREATE_REPEATING_SCHEDULE", jobName), sse); //$NON-NLS-1$
    }    
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionRef ) throws SchedulerServiceException {
    try {
  
      assert actionRef.matches( RE_MATCH_ACTION_REF ) : "Invalid actionRef " + actionRef; //$NON-NLS-1$
      
      DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
      String strStartDate = dateTimeFormatter.format( startDate );
      String strEndDate = null != endDate
        ? dateTimeFormatter.format( endDate )
        : null;
      Map<String, Object> params = new HashMap<String, Object>();
      // TODO sbarkdull, some of these params may not be used, clean up
      params.put( "schedulerAction", "updateJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "oldJobName", oldJobName ); //$NON-NLS-1$
      params.put( "oldJobGroup", oldJobGroup ); //$NON-NLS-1$
      params.put( "jobName", jobName ); //$NON-NLS-1$
      params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
      params.put( "description", description ); //$NON-NLS-1$
      params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
      if ( null != strEndDate ) {
        params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
      }
      params.put( "cron-string", cronString ); //$NON-NLS-1$
      if(actionRef != null && actionRef.length() > 0) {
        params.put( "actionRef", actionRef ); //$NON-NLS-1$ 
      }
      executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0012_PRIVATE_UNABLE_TO_UPDATE_CRON_SCHEDULE", jobName), sse); //$NON-NLS-1$
    }      
  }
  
  public void updateRepeatSchedule(  String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionRef ) throws SchedulerServiceException {
    try {
        assert actionRef.matches( RE_MATCH_ACTION_REF ) : "Invalid actionRef " + actionRef; //$NON-NLS-1$
        
        DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
        String strStartDate = dateTimeFormatter.format( startDate );
        String strEndDate = null != endDate
          ? dateTimeFormatter.format( endDate )
          : null;
        Map<String, Object> params = new HashMap<String, Object>();
        // TODO sbarkdull, some of these params may not be used, clean up
        params.put( "schedulerAction", "updateJob" ); //$NON-NLS-1$  //$NON-NLS-2$
        params.put( "oldJobName", oldJobName ); //$NON-NLS-1$
        params.put( "oldJobGroup", oldJobGroup ); //$NON-NLS-1$
        params.put( "jobName", jobName ); //$NON-NLS-1$
        params.put( "jobGroup", jobGroup ); //$NON-NLS-1$
        params.put( "description", description ); //$NON-NLS-1$
        params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
        if ( null != strEndDate ) {
          params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
        }
        if ( null != strRepeatCount ) {
          params.put( "repeat-count", strRepeatCount ); //$NON-NLS-1$
        }
        params.put( "repeat-time-millisecs", repeatInterval ); //$NON-NLS-1$
        if(actionRef != null && actionRef.length() > 0) {
          params.put( "actionRef", actionRef ); //$NON-NLS-1$ 
        }
        executePostMethod( params );
    } catch(SchedulerServiceException sse) {
      throw new SchedulerServiceException(Messages.getErrorString("SchedulerAdminUIComponentProxy.ERROR_0014_PRIVATE_UNABLE_TO_UPDATE_REPEATING_SCHEDULE", jobName), sse); //$NON-NLS-1$
    }     
  }
  
  // TODO sbarkdull, need to look at xml response from server, and throw exceptions if necessary,
  // see how the SubscriptionAdminUIComponentProxy does it.
  private String executeGetMethod( Map<String, Object> params ) throws SchedulerServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SCHEDULER_SERVICE_NAME, HttpMethodType.GET, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SchedulerServiceException(ExceptionParser.getErrorMessage(e.getMessage(), e.getMessage()), e );
    }
    SchedulerXmlSerializer s = new SchedulerXmlSerializer();
    s.detectSchedulerExceptionInXml( strXmlResponse );
    return strXmlResponse;
  }
  
  private String executePostMethod(Map<String, Object> params ) throws SchedulerServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod(AppConfigProperties.getInstance().getBiServerBaseUrl(), SCHEDULER_SERVICE_NAME, HttpMethodType.POST, StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getPlatformUsername(), System.getProperty(AppConfigProperties.KEY_PLATFORM_USERNAME) ), params );
    } catch (ProxyException e) {
      throw new SchedulerServiceException(ExceptionParser.getErrorMessage(e.getMessage(), e.getMessage()), e );
    }
    SchedulerXmlSerializer s = new SchedulerXmlSerializer();
    s.detectSchedulerExceptionInXml( strXmlResponse );
    return strXmlResponse;
  }
}
