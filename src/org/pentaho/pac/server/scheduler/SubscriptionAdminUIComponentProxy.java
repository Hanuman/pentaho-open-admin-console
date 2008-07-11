package org.pentaho.pac.server.scheduler;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.pentaho.pac.client.scheduler.Schedule;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.BiServerTrustedProxy;
import org.pentaho.pac.server.common.ThreadSafeHttpClient.HttpMethodType;

public class SubscriptionAdminUIComponentProxy {

  private static final String SUBSCRIPTION_SERVICE_NAME = "SubscriptionAdmin"; //$NON-NLS-1$
  private String userName = null;
  // TODO sbarkdull clean up
  private static DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.LONG,
    DateFormat.MEDIUM,
    Locale.getDefault());

  private static BiServerTrustedProxy biServerProxy;
  static {
    biServerProxy = BiServerTrustedProxy.getInstance();
  }
  
  public SubscriptionAdminUIComponentProxy( String userName, String biServerBaseURL ) {
    this.userName = userName;
    biServerProxy.setBaseUrl( biServerBaseURL );
  }

  /**
   * @throws PacServiceException 
   */
  public Map<String,Schedule> getSubscriptionSchedules() throws PacServiceException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "listSchedules" ); //$NON-NLS-1$  //$NON-NLS-2$
    
    String responseStrXml= biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.GET, userName, params );
    XmlSerializer s = new XmlSerializer();
    Map<String,Schedule> m = s.getSubscriptionSchedulesFromXml( responseStrXml );
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
   * @throws PacServiceException
   */
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws PacServiceException {
  
    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "schedulerAction", "doAddScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
    // TODO sbarkdull, this ain't right
    params.put( "title", "I don't know where to get the title" ); //$NON-NLS-1$
    params.put( "schedRef", jobName ); //$NON-NLS-1$
    params.put( "desc", description ); //$NON-NLS-1$
    params.put( "cron", cronString ); //$NON-NLS-1$
    params.put( "group", jobGroup ); //$NON-NLS-1$
    // TODO sbarkdull, what if solutionPath is empty? how do we get multiples in here?
    // TODO clean up String[] actionRefs = { solutionName + "/" + solutionPath + "/" + actionName };
    //params.put( "actionRefs", solutionName + "/" + solutionPath + "/" + actionName ); //$NON-NLS-1$
    params.put( "actionRefs", actionsList ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.POST, userName, params );
    
    // TODO sbarkdull
    /*
    params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
    if ( null != strEndDate ) {
      params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
    }
    params.put( "cron-string", cronString ); //$NON-NLS-1$
    
    params.put( "solution", solutionName ); //$NON-NLS-1$
    params.put( "path", solutionPath ); //$NON-NLS-1$
    params.put( "action", actionName ); //$NON-NLS-1$
    */
  }
  
  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs,
      String actionsList ) throws PacServiceException {

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
    params.put( "repeat-time-millisecs", repeatTimeMillisecs ); //$NON-NLS-1$
    params.put( "actionRefs", actionsList ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.POST, userName, params );
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString, String actionsList ) throws PacServiceException {

    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "doEditScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "oldJobName", oldJobName ); //$NON-NLS-1$
    params.put( "oldJobGroup", oldJobGroup ); //$NON-NLS-1$
    params.put( "schedId", schedId ); //$NON-NLS-1$
    params.put( "schedRef", jobName ); //$NON-NLS-1$
    params.put( "group", jobGroup ); //$NON-NLS-1$
    params.put( "desc", description ); //$NON-NLS-1$
    params.put( "title", "unknown title"); //$NON-NLS-1$
    params.put( "start-date-time", strStartDate ); //$NON-NLS-1$
    if ( null != strEndDate ) {
      params.put( "end-date-time", strEndDate ); //$NON-NLS-1$
    }
    params.put( "cron", cronString ); //$NON-NLS-1$S
    String[] actionsAr = actionsList.split( "," );
    params.put( "actionRefs", actionsAr ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.POST, userName, params );
  }
  
  public void updateRepeatSchedule(  String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatTimeMillisecs,
      String actionsList ) throws PacServiceException {

    String strStartDate = dateTimeFormatter.format( startDate );
    String strEndDate = null != endDate
      ? dateTimeFormatter.format( endDate )
      : null;
    Map<String, Object> params = new HashMap<String, Object>();
    // TODO sbarkdull, some of these params may not be used, clean up
    params.put( "schedulerAction", "doEditScheduleAndContent" ); //$NON-NLS-1$  //$NON-NLS-2$
    params.put( "oldJobName", oldJobName ); //$NON-NLS-1$
    params.put( "oldJobGroup", oldJobGroup ); //$NON-NLS-1$
    params.put( "schedId", schedId ); //$NON-NLS-1$
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
    params.put( "repeat-time-millisecs", repeatTimeMillisecs ); //$NON-NLS-1$
    String[] actionsAr = actionsList.split( "," );
    params.put( "actionRefs", actionsAr ); //$NON-NLS-1$

    String responseStrXml=  biServerProxy.execRemoteMethod( SUBSCRIPTION_SERVICE_NAME, HttpMethodType.POST, userName, params );
  }
}