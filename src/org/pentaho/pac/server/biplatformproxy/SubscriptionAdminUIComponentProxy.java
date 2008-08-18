package org.pentaho.pac.server.biplatformproxy;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.SubscriptionXmlSerializer;
import org.pentaho.pac.server.biplatformproxy.xmlserializer.XmlSerializerException;
import org.pentaho.pac.server.common.BiServerTrustedProxy;
import org.pentaho.pac.server.common.ProxyException;
import org.pentaho.pac.server.common.ThreadSafeHttpClient.HttpMethodType;
import org.pentaho.pac.server.common.util.DateUtil;

public class SubscriptionAdminUIComponentProxy {

  private static final String SUBSCRIPTION_SERVICE_NAME = "SubscriptionAdmin"; //$NON-NLS-1$
  private String userName = null;

  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SubscriptionAdminUIComponentProxy( String userName, String biServerBaseURL ) {
    this.userName = userName;
    biServerProxy.setBaseUrl( biServerBaseURL );
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
      throw new SchedulerServiceException( e.getMessage(), e );
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
  
    DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "doAddScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
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
    String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
    params.put( "actionRefs", actionsAr ); //$NON-NLS-1$

    String responseStrXml = executePostMethod( params );
    // TODO sbarkdull

  }
  
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList ) throws SchedulerServiceException {

    DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "doAddScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
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
    String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
    params.put( "actionRefs", actionsAr ); //$NON-NLS-1$

    String responseStrXml = executePostMethod( params );
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList ) throws SchedulerServiceException {

    DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "doEditScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
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
    String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
    params.put( "actionRefs", actionsAr ); //$NON-NLS-1$

    String responseStrXml = executePostMethod( params );
  }
  
  public void updateRepeatSchedule(  String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval,
      String actionsList ) throws SchedulerServiceException {

    DateFormat dateTimeFormatter = DateUtil.getDateTimeFormatter();
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate 
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "doEditScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
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
    String[] actionsAr = actionsList.split( "," ); //$NON-NLS-1$
    params.put( "actionRefs", actionsAr ); //$NON-NLS-1$

    String responseStrXml = executePostMethod( params );
  }
  
  public void deleteJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "doDeleteScheduleContentAndSubscription" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "schedId", s.getSchedId() ); //$NON-NLS-1$

      String responseStrXml = executeGetMethod( params );
    }
  }
  public void pauseJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "doPauseJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobId", s.getJobName() ); //$NON-NLS-1$

      String responseStrXml = executeGetMethod( params );
    }
  }
  
  public void resumeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "doResumeJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobId", s.getJobName() ); //$NON-NLS-1$

      String responseStrXml = executeGetMethod( params );
    }
  }
  
  public void executeJobs( List<Schedule> scheduleList ) throws SchedulerServiceException {
    
    for ( Schedule s : scheduleList ) {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "schedulerAction", "doExecuteJob" ); //$NON-NLS-1$  //$NON-NLS-2$
      params.put( "jobId", s.getJobName() ); //$NON-NLS-1$

      String responseStrXml = executeGetMethod( params );
    }
  }
  
  private String executeGetMethod( Map<String, Object> params ) throws SchedulerServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.GET, userName, params );
    } catch (ProxyException e) {
      throw new SchedulerServiceException( e.getMessage(), e );
    }
    SubscriptionXmlSerializer s = new SubscriptionXmlSerializer();
    s.detectSubscriptionExceptionInXml( strXmlResponse );
    s.detectSubscriptionErrorInXml( strXmlResponse );
    return strXmlResponse;
  }
  
  private String executePostMethod(Map<String, Object> params ) throws SchedulerServiceException {
    String strXmlResponse;
    try {
      strXmlResponse = biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.POST, userName, params );
    } catch (ProxyException e) {
      throw new SchedulerServiceException( e.getMessage(), e );
    }
    SubscriptionXmlSerializer s = new SubscriptionXmlSerializer();
    s.detectSubscriptionExceptionInXml( strXmlResponse );
    s.detectSubscriptionErrorInXml( strXmlResponse );
    return strXmlResponse;
  }
}