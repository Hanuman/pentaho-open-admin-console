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

import org.pentaho.pac.client.scheduler.Job;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSerializer {
  
  private static final ThreadLocal<SAXParserFactory> SAX_FACTORY = new ThreadLocal<SAXParserFactory>();

  public List<Job> getJobNamesFromXml( String strXml )
  {
    JobsParserHandler h = null;
    // TODO sbarkdull, lets do something better
    try {
      h = parseJobNamesXml( strXml );
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    return h.jobList;
  }
  
  private JobsParserHandler parseJobNamesXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {

      SAXParser parser = getSAXParserFactory().newSAXParser();
      JobsParserHandler h = new JobsParserHandler();
      // TODO sbarkdull, need to set encoding
//      String encoding = CleanXmlHelper.getEncoding( strXml );
//      InputStream is = new ByteArrayInputStream( strXml.getBytes( encoding ) );
      InputStream is = new ByteArrayInputStream( strXml.getBytes( "UTF-8") );
     
      parser.parse( is, h );
      return h;
  }

  
  class JobsParserHandler extends DefaultHandler {

    public String currentText = null;
    public boolean isGetJobNames = false;
    public List<Job> jobList = new ArrayList<Job>();
    private Job currentJob = null;
    
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
        currentJob.description = currentText;
      } else {
        // TODO sbarkdull, error
      }
    }
    
    /**
     * <role name="xx" description="xx">
     *  <users>
     *    <user name="xx"/>
     *    <user name="yy"/>
     *  </users>
     * </role>
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if ( qName.equals( "getJobNames" ) ) { //$NON-NLS-1$
        isGetJobNames = true;
      } else if ( qName.equals( "job" ) ) { //$NON-NLS-1$

        currentJob = new Job();
        jobList.add( currentJob );
        String val = attributes.getValue( "triggerName" );
        currentJob.triggerName = val;
        val = attributes.getValue( "triggerGroup" );
        currentJob.triggerGroup = val;
        val = attributes.getValue( "triggerState" );
        currentJob.triggerState = triggerInt2Name( val );
        val = attributes.getValue( "nextFireTime" );
        currentJob.nextFireTime = val;
        val = attributes.getValue( "prevFireTime" );
        currentJob.prevFireTime = val;
        val = attributes.getValue( "jobName" );
        currentJob.jobName = val;
        val = attributes.getValue( "jobGroup" );
        currentJob.jobGroup = val;
      } else if ( qName.equals( "description" ) ) { //$NON-NLS-1$
      } else {
        // TODO sbarkdull, error
      }
    }
  }

  /**
   * NOTE: see messages.properties in pentaho project for valid strings.
   * Locate the keys in messages.properties by looking for:
   * UI.USER_TRIGGER_STATE_<the state>
   */
  private static final Map STATE_STRINGS = new HashMap();
  static {
    STATE_STRINGS.put( "0", "Normal" );
    STATE_STRINGS.put( "1", "Suspended" );
    STATE_STRINGS.put( "2", "Complete" );
    STATE_STRINGS.put( "3", "Error" );
    STATE_STRINGS.put( "4", "Blocked" );
    STATE_STRINGS.put( "5", "None" );
  }
  
  private static String triggerInt2Name( String strInt )
  {
    return (String)STATE_STRINGS.get( strInt );
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
    return strXml.contains( "Running" );
  }
}
