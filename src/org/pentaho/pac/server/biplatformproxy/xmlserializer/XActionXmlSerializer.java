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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XActionXmlSerializer {

  private static final Log logger = LogFactory.getLog(XActionXmlSerializer.class);

  public XActionXmlSerializer() {
    
  }
  
  public String getXActionResponseStatusFromXml( String strXml ) throws XmlSerializerException {
    XActionResponseParserHandler h = null;
    try {
      h = parseXActionResponseXml( strXml );
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
    return h.getErrorMsg();
  }
  
  private XActionResponseParserHandler parseXActionResponseXml( String strXml ) throws SAXException, IOException, ParserConfigurationException
  {
    SAXParser parser = XmlSerializerUtil.getSAXParserFactory().newSAXParser();
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
  
}
