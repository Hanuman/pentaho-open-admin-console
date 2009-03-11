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
package org.pentaho.pac.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.server.i18n.Messages;

public class StopJettyServer {
  public static final String DEFAULT_CONSOLE_PROPERTIES_FILE_NAME = "console.properties"; //$NON-NLS-1$
  public static final String CONSOLE_HOST_NAME = "console.hostname"; //$NON-NLS-1$
  public static final int DEFAULT_STOP_PORT_NUMBER = 8022;
  public static final String DEFAULT_HOSTNAME = "localhost"; //$NON-NLS-1$
  public static final String STOP_ARG = "-STOP"; //$NON-NLS-1$
  public static final String STOP_PORT = "console.stop.port.number";//$NON-NLS-1$
  private static final Log logger = LogFactory.getLog(StopJettyServer.class);
  public static int stopPort = 0;

  public static void main(String[] args) {
    FileInputStream fis = null;
    Properties properties = null;
    try {
      URL url = ClassLoader.getSystemResource(DEFAULT_CONSOLE_PROPERTIES_FILE_NAME);
      URI uri = url.toURI();
      File file = new File(uri);
      fis = new FileInputStream(file);
    } catch (Exception e1) {
      logger.error(Messages.getErrorString("StopJettyServer.ERROR_0001_OPEN_PROPS_FAILED", DEFAULT_CONSOLE_PROPERTIES_FILE_NAME)); //$NON-NLS-1$
    }
    if (null != fis) {
      properties = new Properties();
      try {
        properties.load(fis);
      } catch (IOException e) {
        logger.error(Messages.getErrorString("StopJettyServer.ERROR_0002_LOAD_PROPS_FAILED", DEFAULT_CONSOLE_PROPERTIES_FILE_NAME)); //$NON-NLS-1$
      }
    }
    if (properties != null) {
      String stopPortNumber = properties.getProperty(STOP_PORT, null);

      if (stopPortNumber != null && stopPortNumber.length() > 0) {
        stopPort = Integer.parseInt(stopPortNumber);
      } else {
        stopPort = DEFAULT_STOP_PORT_NUMBER;
      }
    } else {
      stopPort = DEFAULT_STOP_PORT_NUMBER;
    }
    InetAddress host = null;
    String hostName = null;;
    try {
      host = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      hostName = DEFAULT_HOSTNAME;
    }
    
    try {
      Socket clientSocket = null;
      // If you were not able to find the host name we will default to localhost
      if(hostName != null && hostName.length() > 0) {
        clientSocket = new Socket(hostName, stopPort);
      } else {
        clientSocket = new Socket(host, stopPort);
      }
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      out.println(STOP_ARG);
      out.flush();
    } catch (Exception e) {
      logger.error(Messages.getErrorString("StopJettyServer.ERROR_0003_UNKNOWN_HOST", host.getHostName())); //$NON-NLS-1$
    }

  }

}
