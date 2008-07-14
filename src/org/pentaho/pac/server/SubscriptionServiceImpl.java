package org.pentaho.pac.server;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.pentaho.pac.client.SubscriptionService;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.common.AppConfigProperties;
import org.pentaho.pac.server.scheduler.SubscriptionAdminUIComponentProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubscriptionServiceImpl extends RemoteServiceServlet implements SubscriptionService {

  /**
   * 
   */
  private static final long serialVersionUID = 420L;
  private static SubscriptionAdminUIComponentProxy subscriptionProxy = null;
  static {
    String userName = StringUtils.defaultIfEmpty( AppConfigProperties.getInstance().getProperty("pentaho.platform.userName"), System.getProperty("pentaho.platform.userName") ); //$NON-NLS-1$ //$NON-NLS-2$
    String biServerBaseURL = AppConfigProperties.getInstance().getProperty( "biServerBaseURL" ); //$NON-NLS-1$
    subscriptionProxy = new SubscriptionAdminUIComponentProxy( userName, biServerBaseURL );
  }
  
  /**
   *
   * @throws PacServiceException 
   */
  public Map<String,Schedule> getJobNames() throws PacServiceException {
    Map<String,Schedule> subscriptionMap = subscriptionProxy.getSubscriptionSchedules();
    
    return subscriptionMap;
  }

  /**
   * query string: schedulerAction=createJob&jobName=PentahoSystemVersionCheck&jobGroup=DEFAULT
   * @throws PacServiceException 
   */
  public void createCronSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws PacServiceException {
    subscriptionProxy.createCronSchedule( jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
  }

  public void createRepeatSchedule( String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws PacServiceException {
    subscriptionProxy.createRepeatSchedule( jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatInterval, actionsList );
  
  }
  
  public void updateCronSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String cronString,
      String actionsList ) throws PacServiceException {
    subscriptionProxy.updateCronSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, cronString, actionsList );
    
  }
  
  public void updateRepeatSchedule( String oldJobName, String oldJobGroup, String schedId,
      String jobName, String jobGroup, String description,
      Date startDate, Date endDate,
      String strRepeatCount, String repeatInterval, 
      String actionsList ) throws PacServiceException {
    subscriptionProxy.updateRepeatSchedule( oldJobName, oldJobGroup, schedId, jobName, jobGroup, description,
        startDate, endDate, strRepeatCount, repeatInterval, actionsList );
  }
}
