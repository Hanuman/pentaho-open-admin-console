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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.common.SchedulerServiceException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SchedulerXmlSerializer {

  private static final Log logger = LogFactory.getLog(SchedulerXmlSerializer.class);

  public SchedulerXmlSerializer() {
    
  }

  public Map<String,Schedule> getSchedulesFromXml( String strXml ) throws XmlSerializerException
  {
    JobsParserHandler h = null;
    try {
      h = parseJobNamesXml( strXml );
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
    return h.schedules;
  }
  
  private JobsParserHandler parseJobNamesXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
      SAXParser parser = XmlSerializerUtil.getSAXParserFactory().newSAXParser();
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
        currentSchedule.setTriggerState( XmlSerializerUtil.triggerInt2Name( val ) );
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
          currentSchedule.setRepeatInterval( val );
        }
        assert currentSchedule.getJobName() != null : "Error, job name cannot be null."; //$NON-NLS-1$
        schedules.put( currentSchedule.getJobName(), currentSchedule );
      } else if ( qName.equals( "description" ) ) { //$NON-NLS-1$
      } else {
      }
    }
  }
  
  public boolean getSchedulerStatusFromXml( String strXml ) {
    return strXml.contains( "Running" ); //$NON-NLS-1$
  }
  
  public void detectSchedulerExceptionInXml( String strXml ) throws SchedulerServiceException {

    SchedulerExceptionParserHandler exceptionHandler;
    try {
      exceptionHandler = parseSchedulerExceptionXml( strXml );
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
  
  /**
   * 
   * @param strXml
   * @return
   * @throws SAXException
   * @throws IOException
   * @throws ParserConfigurationException
   */
  private SchedulerExceptionParserHandler parseSchedulerExceptionXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
      SAXParser parser = XmlSerializerUtil.getSAXParserFactory().newSAXParser();
      SchedulerExceptionParserHandler h = new SchedulerExceptionParserHandler();
      // TODO sbarkdull, need to set encoding
//      String encoding = CleanXmlHelper.getEncoding( strXml );
//      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
      InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8" ) ); //$NON-NLS-1$
     
      parser.parse( is, h );
      return h;
  }

  /**
   * <?xml version="1.0" encoding="UTF-8"?>
   * <schedulerResults>
   *   <error
   *     msg="Failed to execute job ff. Job with that name does not exist in scheduler. ff" />
   * </schedulerResults>
   * @author Steven Barkdull
   *
   */
  private static class SchedulerExceptionParserHandler extends DefaultHandler {

    public String exceptionMessage = null;
    private boolean isSchedulerResults = false;
    
    public SchedulerExceptionParserHandler()
    {
    }
  
    public void characters( char[] ch, int startIdx, int length )
    {
      // no-op
    }
    
    public void endElement(String uri, String localName, String qName ) throws SAXException
    {
      if ( qName.equals( "schedulerResults" ) ) { //$NON-NLS-1$
        isSchedulerResults = false;
      }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "schedulerResults" ) ) { //$NON-NLS-1$
        isSchedulerResults = true;
      } else if ( qName.equals( "error" ) ) { //$NON-NLS-1$
        if ( isSchedulerResults ) {
          exceptionMessage = attributes.getValue( "msg" );//$NON-NLS-1$
        }
      }
    }
  }
}