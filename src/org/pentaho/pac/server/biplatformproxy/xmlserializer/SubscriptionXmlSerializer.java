package org.pentaho.pac.server.biplatformproxy.xmlserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SubscriptionXmlSerializer {

  private static final Log logger = LogFactory.getLog(SubscriptionXmlSerializer.class);
  private static final ThreadLocal<SAXParserFactory> SAX_FACTORY = new ThreadLocal<SAXParserFactory>();

  public SubscriptionXmlSerializer() {
    
  }
  
  public Map<String,Schedule> getSubscriptionSchedulesFromXml( String strXml ) throws XmlSerializerException, SchedulerServiceException
  {
    SubscriptionScheduleParserHandler subscriptionSchedHandler = null;
    try {
      detectSubscriptionExceptionInXml( strXml );
      subscriptionSchedHandler = parseSubscriptionScheduleJobsXml( strXml );
    } catch (SAXException e) {
      logger.error( e.getMessage() );
      throw new XmlSerializerException( e.getMessage() );
    } catch (IOException e) {
      logger.error( e.getMessage() );
      throw new XmlSerializerException( e.getMessage() );
    } catch (ParserConfigurationException e) {
      logger.error( e.getMessage() );
      throw new XmlSerializerException( e.getMessage() );
    }
    return subscriptionSchedHandler.schedules;
  }
  
  public void detectSubscriptionExceptionInXml( String strXml ) throws SchedulerServiceException {

    SubscriptionExceptionParserHandler exceptionHandler;
    try {
      exceptionHandler = parseSubscriptionExceptionXml( strXml );
    } catch (SAXException e) {
      logger.error( e.getMessage() );
      throw new SchedulerServiceException( e.getMessage() );
    } catch (IOException e) {
      logger.error( e.getMessage() );
      throw new SchedulerServiceException( e.getMessage() );
    } catch (ParserConfigurationException e) {
      logger.error( e.getMessage() );
      throw new SchedulerServiceException( e.getMessage() );
    }
    if ( null != exceptionHandler.exceptionMessage ) {
      throw new SchedulerServiceException( exceptionHandler.exceptionMessage );
    }
  }
  
  public void detectSubscriptionErrorInXml( String strXml ) throws SchedulerServiceException {

    SubscriptionErrorParserHandler errorHandler;
    try {
      errorHandler = parseSubscriptionErrorXml( strXml );
    } catch (SAXException e) {
      logger.error( e.getMessage() );
      throw new SchedulerServiceException( e.getMessage() );
    } catch (IOException e) {
      logger.error( e.getMessage() );
      throw new SchedulerServiceException( e.getMessage() );
    } catch (ParserConfigurationException e) {
      logger.error( e.getMessage() );
      throw new SchedulerServiceException( e.getMessage() );
    }
    if ( null != errorHandler.errorMessage ) {
      throw new SchedulerServiceException( errorHandler.errorMessage );
    }
  }

  /**
   * Get a SAX Parser Factory. This method implements a thread-relative
   * singleton.
   * 
   * NOTE: Need sax parser factory per thread for thread safety.
   * See: http://java.sun.com/j2se/1.5.0/docs/api/javax/xml/parsers/SAXParserFactory.html
   * @return
   */
  private static SAXParserFactory getSAXParserFactory() {
    SAXParserFactory threadLocalSAXParserFactory = SAX_FACTORY.get();
    if ( null == threadLocalSAXParserFactory )
    {
      threadLocalSAXParserFactory = SAXParserFactory.newInstance();
      SAX_FACTORY.set( threadLocalSAXParserFactory );
    }
    return threadLocalSAXParserFactory;
  }
  
  private SubscriptionScheduleParserHandler parseSubscriptionScheduleJobsXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
      SAXParser parser = getSAXParserFactory().newSAXParser();
      SubscriptionScheduleParserHandler h = new SubscriptionScheduleParserHandler();
      // TODO sbarkdull, need to set encoding
//      String encoding = CleanXmlHelper.getEncoding( strXml );
//      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
      InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8" ) ); //$NON-NLS-1$
     
      parser.parse( is, h );
      return h;
  }
/*
  <subscriptionAdmin>
  <listSchedules>
    <scheduledJobs>
      <job subscriberCount="0" triggerState="0">
        <schedId>eb7ef4b4-4867-11dd-8266-ff19a6feec31</schedId>
        <schedRef>snb name 2</schedRef>
        <title>snb title 2</title>
        <desc>snb description 2</desc>
        <group>snb group 2</group>
        <cron>0 0 12 3 * ?</cron>
        <content>
          <action solution="samples" path="getting-started" action="HelloWorld.xaction" />
        </content>
        <nextFireTime>Thu Jul 03 12:00:00 EDT 2008</nextFireTime>
        <prevFireTime>Never</prevFireTime>
        <jobId>snb name 2</jobId>
      </job>
    </scheduledJobs>
    <unScheduledJobs>
      <job subscriberCount="0" triggerState="-1">
        <schedId>da95463f-4867-11dd-8266-ff19a6feec31</schedId>
        <schedRef>snb name</schedRef>
        <title>snb title</title>
        <desc>snb description</desc>
        <group>snb group</group>
        <cron>0 0 12 3 * *</cron>
      </job>
    </unScheduledJobs>
    <message result="WARNING">There are subscriptions that are not scheduled to run.</message>
  </listSchedules>
  <message result="INFO">There is no subscription content defined.</message>
  <schedulerStatus state="0" />
  <returnURL>&amp;schedulerAction=listSchedules&amp;_TRUST_USER_=joe</returnURL>
</subscriptionAdmin>
*/
  private static class SubscriptionScheduleParserHandler extends DefaultHandler {

    public String currentText = null;
    public Map<String,Schedule> schedules = new HashMap<String,Schedule>();
    private Schedule currentSchedule = null;
    private boolean isInSubscriptionAdmin = false;
    private boolean isInListSchedules = false;
    private boolean isInScheduledJobs = false;
    private boolean isInContent = false;
    private List<String> actionList = null;
    
    public SubscriptionScheduleParserHandler()
    {
    }
  
    public void characters( char[] ch, int startIdx, int length )
    {
      currentText = String.valueOf( ch, startIdx, length );
    }
    
    public void endElement(String uri, String localName, String qName ) throws SAXException
    {
      if ( qName.equals( "subscriptionAdmin" ) ) { //$NON-NLS-1$
        isInSubscriptionAdmin = false;
      } else if ( qName.equals( "listSchedules" ) ) { //$NON-NLS-1$
        isInListSchedules = false;
      } else if ( qName.equals( "scheduledJobs" ) ) { //$NON-NLS-1$
        isInScheduledJobs = false;
      } else {
        if ( isInSubscriptionAdmin && isInListSchedules && isInScheduledJobs ) {
          if ( qName.equals( "job" ) ) { //$NON-NLS-1$
            assert currentSchedule.getJobName() != null : "Error, job name cannot be null."; //$NON-NLS-1$
            schedules.put( currentSchedule.getJobName(), currentSchedule );
            currentSchedule = null;
          } else if ( qName.equals( "schedId" ) ) { //$NON-NLS-1$
            currentSchedule.setSchedId( currentText );
          } else if ( qName.equals( "schedRef" ) ) { //$NON-NLS-1$
            currentSchedule.setJobName( currentText );
            currentSchedule.setTriggerName( currentText );
          } else if ( qName.equals( "title" ) ) { //$NON-NLS-1$
            currentSchedule.setTitle( currentText );
          } else if ( qName.equals( "desc" ) ) { //$NON-NLS-1$
            currentSchedule.setDescription( currentText );
          } else if ( qName.equals( "group" ) ) { //$NON-NLS-1$
            currentSchedule.setJobGroup( currentText );
          } else if ( qName.equals( "cron" ) ) { //$NON-NLS-1$
            currentSchedule.setCronString( currentText );
          } else if ( qName.equals( "repeat-count" ) ) { //$NON-NLS-1$
            currentSchedule.setRepeatCount( currentText );
          } else if ( qName.equals( "repeat-time-millisecs" ) ) { //$NON-NLS-1$
            currentSchedule.setRepeatInterval( currentText );
          } else if ( qName.equals( "nextFireTime" ) ) { //$NON-NLS-1$
            currentSchedule.setNextFireTime( currentText );
          } else if ( qName.equals( "prevFireTime" ) ) { //$NON-NLS-1$
            currentSchedule.setPrevFireTime( currentText );
          } else if ( qName.equals( "jobId" ) ) { //$NON-NLS-1$
            currentSchedule.setJobName( currentText );
          } else if ( qName.equals( "start-date" ) ) { //$NON-NLS-1$
            currentSchedule.setStartDate( currentText );
          } else if ( qName.equals( "end-date" ) ) { //$NON-NLS-1$
            currentSchedule.setEndDate( currentText );  
          } else if ( qName.equals( "content" ) ) { //$NON-NLS-1$
            isInContent = false;
            currentSchedule.setActionsList( actionList );
            actionList = null;
          }
        }
      }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "subscriptionAdmin" ) ) { //$NON-NLS-1$
        isInSubscriptionAdmin = true;
      } else if ( qName.equals( "listSchedules" ) ) { //$NON-NLS-1$
        isInListSchedules = true;
      } else if ( qName.equals( "scheduledJobs" ) ) { //$NON-NLS-1$
        isInScheduledJobs = true;
      } else {
        if ( isInSubscriptionAdmin && isInListSchedules && isInScheduledJobs ) {
          if ( qName.equals( "job" ) ) { //$NON-NLS-1$
            currentSchedule = new Schedule();

            String val = attributes.getValue( "subscriberCount" ); //$NON-NLS-1$
            currentSchedule.setSubscriberCount( val );
            val = attributes.getValue( "triggerState" ); //$NON-NLS-1$
            currentSchedule.setTriggerState( XmlSerializerUtil.triggerInt2Name( val ) );
          } else if ( qName.equals( "content" ) ) { //$NON-NLS-1$
            isInContent = true;
            actionList = new ArrayList<String>();
          } else if ( qName.equals( "action" ) ) { //$NON-NLS-1$
            if ( isInContent ) {
              String solution = attributes.getValue( "solution" ); //$NON-NLS-1$
              String path = attributes.getValue( "path" ); //$NON-NLS-1$
              String action = attributes.getValue( "action" ); //$NON-NLS-1$
              actionList.add( makePath( solution, path, action ));
            }
          }
        }
      }
    }
    private static String makePath( String solution, String path, String action ) {
      return solution + "/" + path + "/" + action;  //$NON-NLS-1$//$NON-NLS-2$
    }
  }
  
  private SubscriptionExceptionParserHandler parseSubscriptionExceptionXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
      SAXParser parser = getSAXParserFactory().newSAXParser();
      SubscriptionExceptionParserHandler h = new SubscriptionExceptionParserHandler();
      // TODO sbarkdull, need to set encoding
//      String encoding = CleanXmlHelper.getEncoding( strXml );
//      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
      InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8" ) ); //$NON-NLS-1$
     
      parser.parse( is, h );
      return h;
  }

  /**
   * <exception>
   *   <message result="ERROR">
   *     Error, these parameters are missing: actionRefs
   *   </message>
   * </exception>
   *  
   * @author Steven Barkdull
   *
   */
  private static class SubscriptionExceptionParserHandler extends DefaultHandler {

    private String currentText = null;
    public String exceptionMessage = null;
    private boolean isException = false;
    private boolean isMessage = false;
    
    public SubscriptionExceptionParserHandler()
    {
    }
  
    public void characters( char[] ch, int startIdx, int length )
    {
      currentText = String.valueOf( ch, startIdx, length );
    }
    
    public void endElement(String uri, String localName, String qName ) throws SAXException
    {
      if ( qName.equals( "exception" ) ) { //$NON-NLS-1$
        isException = false;
      } else if ( qName.equals( "message" ) ) { //$NON-NLS-1$
        if ( isException && isMessage ) {
          exceptionMessage = currentText;
        }
        isMessage = false;
      }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "exception" ) ) { //$NON-NLS-1$
        isException = true;
      } else if ( qName.equals( "message" ) ) { //$NON-NLS-1$
        isMessage = true;
      }
    }
  }
  
  private SubscriptionErrorParserHandler parseSubscriptionErrorXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
      SAXParser parser = getSAXParserFactory().newSAXParser();
      SubscriptionErrorParserHandler h = new SubscriptionErrorParserHandler();
      // TODO sbarkdull, need to set encoding
//      String encoding = CleanXmlHelper.getEncoding( strXml );
//      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
      InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8" ) ); //$NON-NLS-1$
     
      parser.parse( is, h );
      return h;
  }
  
  /**
   * <?xml version="1.0" encoding="UTF-8"?>
   * <commandResult result="ERROR">
   *   <message result="ERROR">
   *     Unable to complete request:
   *     org.hibernate.exception.ConstraintViolationException: could not
   *     delete:
   *     [org.pentaho.platform.repository.subscription.SubscribeContent#34c6b532-5cd5-11dd-9b0d-53348ed62413]
   *   </message>
   * </commandResult>
   * @author Steven Barkdull
   *
   */
  private static class SubscriptionErrorParserHandler extends DefaultHandler {

    private String currentText = null;
    public String errorMessage = null;
    private boolean isCommandResult = false;
    private boolean isErrorMessage = false;
    private boolean isMessage = false;
    
    public SubscriptionErrorParserHandler()
    {
    }
  
    public void characters( char[] ch, int startIdx, int length )
    {
      currentText = String.valueOf( ch, startIdx, length );
    }
    
    public void endElement(String uri, String localName, String qName ) throws SAXException
    {
      if ( qName.equals( "commandResult" ) ) { //$NON-NLS-1$
        isCommandResult = false;
      } else if ( qName.equals( "message" ) ) { //$NON-NLS-1$
        if ( isCommandResult && isMessage && isErrorMessage ) {
          errorMessage = currentText;
          isErrorMessage = false;
        }
        isMessage = false;
      }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "commandResult" ) ) { //$NON-NLS-1$
        isCommandResult = true;
      } else if ( qName.equals( "message" ) ) { //$NON-NLS-1$
        isMessage = true;
        // result="ERROR"
        isErrorMessage = attributes.getValue( "result" ).equals( "ERROR" ); //$NON-NLS-1$  //$NON-NLS-2$
      }
    }
  }

}