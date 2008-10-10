package org.pentaho.pac.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.server.i18n.Messages;

public class StopJettyServer {
  public static final String DEFAULT_CONSOLE_PROPERTIES_FILE_NAME = "resource/config/console.properties"; //$NON-NLS-1$
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
      File file = new File(DEFAULT_CONSOLE_PROPERTIES_FILE_NAME);
      fis = new FileInputStream(file);
    } catch (IOException e1) {
      logger.error(Messages.getString("PacService.OPEN_PROPS_FAILED", DEFAULT_CONSOLE_PROPERTIES_FILE_NAME)); //$NON-NLS-1$
    }
    if (null != fis) {
      properties = new Properties();
      try {
        properties.load(fis);
      } catch (IOException e) {
        logger.error(Messages.getString("PacService.LOAD_PROPS_FAILED", DEFAULT_CONSOLE_PROPERTIES_FILE_NAME)); //$NON-NLS-1$
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
      logger.error(Messages.getString("PacService.UNKNOWN_HOST", host.getHostName())); //$NON-NLS-1$
    }

  }

}
