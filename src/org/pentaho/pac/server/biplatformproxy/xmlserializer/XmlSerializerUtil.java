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

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.pentaho.pac.server.i18n.Messages;

public class XmlSerializerUtil {

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
  
  private XmlSerializerUtil() {
    
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
  public static SAXParserFactory getSAXParserFactory() {
    SAXParserFactory threadLocalSAXParserFactory = SAX_FACTORY.get();
    if ( null == threadLocalSAXParserFactory )
    {
      threadLocalSAXParserFactory = SAXParserFactory.newInstance();
      SAX_FACTORY.set( threadLocalSAXParserFactory );
    }
    return threadLocalSAXParserFactory;
  }
  
  public static String triggerInt2Name( String strInt )
  {
    return STATE_STRINGS.get( strInt );
  }
}
