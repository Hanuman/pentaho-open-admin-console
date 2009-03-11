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
package org.pentaho.pac.client.utils;

public class ExceptionParser {
  public static String DELIMETER = "-"; //$NON-NLS-1$
  public static String DEFAULT_ERROR_HEADER = "Error"; //$NON-NLS-1$
  
  public static String getErrorMessage(String message, String defaultErrorMessage) {
    if(message != null && message.length() > 0) {
      int index = message.indexOf(DELIMETER);
      if(index > 0) {
        return message.substring(index + 1);
      } else {
        return defaultErrorMessage;
      }
    } else {
      return defaultErrorMessage;
    }
    
  }
  
  public static String getErrorHeader(String message) {
    if(message != null && message.length() > 0) {
      int index = message.indexOf(DELIMETER);
      if(index > 0) {
        return message.substring(0, index -1);
      } else {
        return DEFAULT_ERROR_HEADER;
      }
    } else {
      return DEFAULT_ERROR_HEADER;
    }
    
  }
}
