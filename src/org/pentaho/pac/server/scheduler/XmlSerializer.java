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
import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.server.i18n.Messages;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSerializer {

  private static final Log logger = LogFactory.getLog(XmlSerializer.class);
  private static final ThreadLocal<SAXParserFactory> SAX_FACTORY = new ThreadLocal<SAXParserFactory>();

  /**
   * NOTE: see messages.properties in pentaho project for valid strings.
   * Locate the keys in messages.properties by looking for:
   * UI.USER_TRIGGER_STATE_<the state>
   */
  private static final Map<String,String> STATE_STRINGS = new HashMap<String,String>();
  static {
    STATE_STRINGS.put( "0", Messages.getString( "XmlSerializer.stateNormal" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    STATE_STRINGS.put( "1", Messages.getString( "XmlSerializer.stateSuspended" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    STATE_STRINGS.put( "2", Messages.getString( "XmlSerializer.stateComplete" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    STATE_STRINGS.put( "3", Messages.getString( "XmlSerializer.stateError" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    STATE_STRINGS.put( "4", Messages.getString( "XmlSerializer.stateBlocked" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    STATE_STRINGS.put( "5", Messages.getString( "XmlSerializer.stateNone" ) ); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public Map<String,Schedule> getSchedulesFromXml( String strXml ) throws PacServiceException
  {
    JobsParserHandler h = null;
    try {
      h = parseJobNamesXml( strXml );
    } catch (SAXException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    } catch (IOException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    } catch (ParserConfigurationException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    }
    return h.schedules;
  }

  public Map<String,Schedule> getSubscriptionSchedulesFromXml( String strXml ) throws PacServiceException
  {
    SubscriptionScheduleParserHandler h = null;
    try {
      h = parseSubscriptionScheduleJobsXml( strXml );
    } catch (SAXException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    } catch (IOException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    } catch (ParserConfigurationException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    }
    return h.schedules;
  }
  
  private JobsParserHandler parseJobNamesXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
      SAXParser parser = getSAXParserFactory().newSAXParser();
      JobsParserHandler h = new JobsParserHandler();
      // TODO sbarkdull, need to set encoding
//      String encoding = CleanXmlHelper.getEncoding( strXml );
//      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
      InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8" ) ); //$NON-NLS-1$
     
      parser.parse( is, h );
      return h;
  }

  private static class JobsParserHandler extends DefaultHandler {

    public String currentText = null;
    public boolean isGetJobNames = false;
    public Map<String,Schedule> schedules = new HashMap<String,Schedule>();
    private Schedule currentSchedule = null;
    
    public JobsParserHandler()
    {
    }
  
    public void characters( char[] ch, int startIdx, int length )
    {
      currentText = String.valueOf( ch, startIdx, length );
    }
    
    public void endElement(String uri, String localName, String qName ) throws SAXException
    {
      if ( qName.equals( "job" ) ) { //$NON-NLS-1$
      } else if ( qName.equals( "description" ) ) { //$NON-NLS-1$
        currentSchedule.setDescription( currentText );
      } else {
      }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "getJobNames" ) ) { //$NON-NLS-1$
        isGetJobNames = true;
      } else if ( qName.equals( "job" ) ) { //$NON-NLS-1$

        currentSchedule = new Schedule();
        String val = attributes.getValue( "triggerName" ); //$NON-NLS-1$
        currentSchedule.setTriggerName( val );
        val = attributes.getValue( "triggerGroup" ); //$NON-NLS-1$
        currentSchedule.setTriggerGroup( val );
        val = attributes.getValue( "triggerState" ); //$NON-NLS-1$
        currentSchedule.setTriggerState( triggerInt2Name( val ) );
        val = attributes.getValue( "nextFireTime" ); //$NON-NLS-1$
        currentSchedule.setNextFireTime( val );
        val = attributes.getValue( "prevFireTime" ); //$NON-NLS-1$
        currentSchedule.setPrevFireTime( val );
        val = attributes.getValue( "jobName" ); //$NON-NLS-1$
        currentSchedule.setJobName( val );
        val = attributes.getValue( "jobGroup" ); //$NON-NLS-1$
        currentSchedule.setJobGroup( val );
        val = attributes.getValue( "start-date-time" ); //$NON-NLS-1$
        currentSchedule.setStartDate( val );
        val = attributes.getValue( "end-date-time" ); //$NON-NLS-1$
        currentSchedule.setEndDate( val );
        // actionsList will only have ONE action
        val = attributes.getValue( "actionRefs" ); //$NON-NLS-1$
        List<String> l = new ArrayList<String>();
        l.add( val );
        currentSchedule.setActionsList( l );
        
        val = attributes.getValue( "cron-string" ); //$NON-NLS-1$
        if ( null != val ) {
          currentSchedule.setCronString( val );
        }
        val = attributes.getValue( "repeat-time-millisecs" ); //$NON-NLS-1$
        if ( null != val ) {
          currentSchedule.setRepeatTimeInMillisecs( val );
        }
        assert currentSchedule.getJobName() != null : "Error, job name cannot be null."; //$NON-NLS-1$
        schedules.put( currentSchedule.getJobName(), currentSchedule );
      } else if ( qName.equals( "description" ) ) { //$NON-NLS-1$
      } else {
      }
    }
  }
  
  private static String triggerInt2Name( String strInt )
  {
    return STATE_STRINGS.get( strInt );
  }
  
  // TODO sbarkdull, threading?
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
  
  public boolean getSchedulerStatusFromXml( String strXml ) {
    return strXml.contains( "Running" ); //$NON-NLS-1$
  }
  
  public String getXActionResponseStatusFromXml( String strXml ) throws PacServiceException {
    XActionResponseParserHandler h = null;
    try {
      h = parseXActionResponseXml( strXml );
    } catch (SAXException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    } catch (IOException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    } catch (ParserConfigurationException e) {
      logger.error( e.getMessage() );
      throw new PacServiceException( e.getMessage() );
    }
    return h.getErrorMsg();
  }
  
  private XActionResponseParserHandler parseXActionResponseXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
    SAXParser parser = getSAXParserFactory().newSAXParser();
    XActionResponseParserHandler h = new XActionResponseParserHandler();
    // TODO sbarkdull, need to set encoding
  //      String encoding = CleanXmlHelper.getEncoding( strXml );
  //      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
    InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8") ); //$NON-NLS-1$
   
    parser.parse( is, h );
    return h;
  }

  /**
   * Sample error document:
   * <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" SOAP-ENV:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
   *   <SOAP-ENV:Body>
   *     <SOAP-ENV:Fault>
   *       <SOAP-ENV:faultcode>
   *         <SOAP-ENV:Subcode>
   *           <SOAP-ENV:Value><![CDATA[Error: SolutionEngine.ERROR_0007 - Action sequence execution failed (org.pentaho.core.solution.SolutionEngine)]]></SOAP-ENV:Value>
   *         </SOAP-ENV:Subcode>
   *       </SOAP-ENV:faultcode>
   *       <SOAP-ENV:faultactor>SOAP-ENV:Server</SOAP-ENV:faultactor>
   *       <SOAP-ENV:faultstring>
   *         <SOAP-ENV:Text xml:lang="en_US"><![CDATA[Error: SolutionEngine.ERROR_0007 - Action sequence execution failed (org.pentaho.core.solution.SolutionEngine)]]></SOAP-ENV:Text>
   *       </SOAP-ENV:faultstring>
   *       <SOAP-ENV:Detail>
   *         <message name="trace"><![CDATA[Debug: Starting execute of admin/xx/clear_mondrian_data_cache.xactionxxxx (org.pentaho.core.solution.SolutionEngine)]]></message>
   *         <message name="trace"><![CDATA[Debug: Getting runtime context and data (org.pentaho.core.solution.SolutionEngine)]]></message>
   *         <message name="trace"><![CDATA[Debug: Loading action sequence definition file (org.pentaho.core.solution.SolutionEngine)]]></message>
   *         <message name="trace"><![CDATA[Error: SolutionEngine.ERROR_0007 - Action sequence execution failed (org.pentaho.core.solution.SolutionEngine)]]></message>
   *       </SOAP-ENV:Detail>
   *     </SOAP-ENV:Fault>
   *   </SOAP-ENV:Body>
   * </SOAP-ENV:Envelope>
   * 
   * @author Steven Barkdull
   *
   */
  private static class XActionResponseParserHandler extends DefaultHandler {

    public String currentText = null;
    private boolean foundFaultStr = false;
    private String errorMsg = null;
    
    public XActionResponseParserHandler()
    {
    }
  
    /**
     * if null, no error detected
     * @return
     */
    public String getErrorMsg()
    {
      return errorMsg;
    }
    
    public void characters( char[] ch, int startIdx, int length )
    {
      currentText = String.valueOf( ch, startIdx, length );
    }
    
    public void endElement(String uri, String localName, String qName ) throws SAXException
    {
      if ( qName.equals( "SOAP-ENV:Text" ) ) { //$NON-NLS-1$
        if ( foundFaultStr ) {
          errorMsg = currentText;
        }
      } else if ( qName.equals( "SOAP-ENV:faultstring" ) ) { //$NON-NLS-1$
        foundFaultStr = false;
      }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "SOAP-ENV:faultstring" ) ) { //$NON-NLS-1$
        foundFaultStr = true;
      }
    }
  }
  
  /**
   * Complete hack, we can only get back html from the service, so lets look
   * at the html and try to find the error string.
   * 
   * @param strXml
   * @return
   */
  public String getPublishStatusFromXml( String strXml )
  {
    String errorMsg = null;
    int startIdx = strXml.indexOf( "PentahoSystem.ERROR" ); //$NON-NLS-1$
    int endIdx = strXml.indexOf( "\n", startIdx ); //$NON-NLS-1$
    if ( -1 != startIdx ) {
      errorMsg = strXml.substring( startIdx, endIdx-1 );
    }
    return errorMsg;
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
            currentSchedule.setSchedRef( currentText );
          } else if ( qName.equals( "title" ) ) { //$NON-NLS-1$
            currentSchedule.setTitle( currentText );
          } else if ( qName.equals( "desc" ) ) { //$NON-NLS-1$
            currentSchedule.setDescription( currentText );
          } else if ( qName.equals( "group" ) ) { //$NON-NLS-1$
            currentSchedule.setJobGroup( currentText );
          } else if ( qName.equals( "cron" ) ) { //$NON-NLS-1$
            currentSchedule.setCronString( currentText );
          } else if ( qName.equals( "nextFireTime" ) ) { //$NON-NLS-1$
            currentSchedule.setNextFireTime( currentText );
          } else if ( qName.equals( "prevFireTime" ) ) { //$NON-NLS-1$
            currentSchedule.setPrevFireTime( currentText );
          } else if ( qName.equals( "jobId" ) ) { //$NON-NLS-1$
            currentSchedule.setJobName( currentText );
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
            currentSchedule.setTriggerState( triggerInt2Name( val ) );
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
}
