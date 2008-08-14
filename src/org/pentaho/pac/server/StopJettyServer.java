package org.pentaho.pac.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.server.i18n.Messages;

public class StopJettyServer {
  public static final String DEFAULT_CONSOLE_PROPERTIES_FILE_NAME = "config/console.properties"; //$NON-NLS-1$
  public static final String CONSOLE_HOST_NAME = "console.hostname"; //$NON-NLS-1$
  public static final int DEFAULT_STOP_PORT_NUMBER = 8022;
  public static final String DEFAULT_HOSTNAME = "localhost"; //$NON-NLS-1$
  public static final String STOP_ARG = "-STOP"; //$NON-NLS-1$
  public static final String STOP_PORT = "console.stop.port.number";//$NON-NLS-1$
  private static final Log logger = LogFactory.getLog(StopJettyServer.class);
  public static int stopPort = 0;
  private static String host;

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
      String hostname = properties.getProperty(CONSOLE_HOST_NAME, null);
      String stopPortNumber = properties.getProperty(STOP_PORT, null);

      if (stopPortNumber != null && stopPortNumber.length() > 0) {
        stopPort = Integer.parseInt(stopPortNumber);
      } else {
        stopPort = DEFAULT_STOP_PORT_NUMBER;
      }

      if (hostname != null && hostname.length() > 0) {
        host = hostname;
      } else {
        host = DEFAULT_HOSTNAME;
      }
    } else {
      host = DEFAULT_HOSTNAME;
      stopPort = DEFAULT_STOP_PORT_NUMBER;
    }

    try {
      Socket clientSocket = new Socket(host, stopPort);
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      out.println(STOP_ARG);
      out.flush();
    } catch (Exception e) {
      System.out.println("Unknown host"); //$NON-NLS-1$
    }

  }

}
