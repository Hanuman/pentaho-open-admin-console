package org.pentaho.pac.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.pentaho.pac.server.i18n.Messages;

public class JettyServer implements Halter {
  protected Server server;

  private int portNumber;

  private String hostname;

  public static final String DEFAULT_CONSOLE_PROPERTIES_FILE_NAME = "resource/config/console.properties"; //$NON-NLS-1$

  public static final String CONSOLE_PASSWORD_FILE_NAME = "resource/config/console.pwd"; //$NON-NLS-1$

  public static final String CONSOLE_PORT_NUMBER = "console.start.port.number"; //$NON-NLS-1$

  public static final String CONSOLE_HOST_NAME = "console.hostname"; //$NON-NLS-1$

  public static final int DEFAULT_PORT_NUMBER = 8099;
  public static final int DEFAULT_STOP_PORT_NUMBER = 8011;

  public static final String DEFAULT_HOSTNAME = "localhost"; //$NON-NLS-1$

  private static final Log logger = LogFactory.getLog(JettyServer.class);
  
  public static final String STOP_ARG = "-STOP"; //$NON-NLS-1$
  public static final String STOP_PORT = "console.stop.port.number";//$NON-NLS-1$
  public int stopPort = 0;
  private boolean running = false;

  public static JettyServer jettyServer;

  public JettyServer() {
    readConfiguration();
    server = new Server();
    setupServer();
    startServer();
    stopHandler(this, stopPort);
  }

  public JettyServer(String hostname, int port, int stop) {
    this.portNumber = port;
    this.hostname = hostname;
    stopPort = stop;
    server = new Server();
    setupServer();
    startServer();
    stopHandler(this, stopPort);
  }

  public static JettyServer getInstance() {
    return jettyServer;
  }

  public boolean isRunning() {
    return running;
  }

  public void stop() {

    Halter halter = new Halter(this);
    // Create the thread supplying it with the runnable object
    Thread thread = new Thread(halter);

    // Start the thread
    thread.start();
  }

  public void haltNow() {
    try {
      server.stop();
      running = false;
    } catch (Exception e) {
      logger.error("error starting server", e); //$NON-NLS-1$
    }
  }

  private void startServer() {
    SocketConnector connector = new SocketConnector();
    connector.setPort(portNumber);
    connector.setHost(hostname);
    connector.setName("Pentaho Console HTTP listener for [" + hostname + ":" + portNumber + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    logger.info("starting " + connector.getName()); //$NON-NLS-1$
    server.setConnectors(new Connector[] { connector });
    server.setStopAtShutdown(true);
    logger.info("Console Starting"); //$NON-NLS-1$

    try {
      server.start();
    } catch (BindException e) {
      haltNow();
    } catch (RuntimeException e) {
      // let runtime exceptions leak, developer should handle them before release.
      logger.error("Error starting server", e); //$NON-NLS-1$
      throw e;
    } catch (Exception e) {
      logger.error("Error starting server", e); //$NON-NLS-1$
    }
  }

  protected void setupServer() {
    server = new Server();

    ContextHandlerCollection contexts = new ContextHandlerCollection();

    // configure security if necessary
    File passwdFile = new File(CONSOLE_PASSWORD_FILE_NAME); //$NON-NLS-1$
    SecurityHandler securityHandler = null;
    if (passwdFile.exists()) {
      try {
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[] { "admin" });
        constraint.setAuthenticate(true);
        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*"); //$NON-NLS-1$

        securityHandler = new SecurityHandler();
        securityHandler.setUserRealm(new HashUserRealm("Pentaho", CONSOLE_PASSWORD_FILE_NAME)); //$NON-NLS-1$ //$NON-NLS-2$
        securityHandler.setConstraintMappings(new ConstraintMapping[] { constraintMapping });
      } catch (IOException e) {
        logger.error("error securing server", e); //$NON-NLS-1$
      }
    }

    // Start execution
    Context startExecution = new Context(contexts, "/", Context.SESSIONS); //$NON-NLS-1$
    startExecution.setResourceBase("www/org.pentaho.pac.PentahoAdminConsole"); //$NON-NLS-1$
    startExecution.setWelcomeFiles(new String[] { "PentahoAdminConsole.html" }); //$NON-NLS-1$

    // add servlets
    ServletHolder defaultServlet = new ServletHolder(new DefaultConsoleServlet("/", this)); //$NON-NLS-1$
    startExecution.addServlet(defaultServlet, "/*"); //$NON-NLS-1$
    startExecution.addServlet(defaultServlet, "/halt"); //$NON-NLS-1$

    ServletHolder pacsvc = new ServletHolder(new org.pentaho.pac.server.PacServiceImpl());
    startExecution.addServlet(pacsvc, "/pacsvc"); //$NON-NLS-1$

    ServletHolder schedulersvc = new ServletHolder(new org.pentaho.pac.server.SchedulerServiceImpl());
    startExecution.addServlet(schedulersvc, "/schedulersvc"); //$NON-NLS-1$

    ServletHolder subscriptionsvc = new ServletHolder(new org.pentaho.pac.server.SubscriptionServiceImpl());
    startExecution.addServlet(subscriptionsvc, "/subscriptionsvc"); //$NON-NLS-1$

    ServletHolder solutionrepositorysvc = new ServletHolder(new org.pentaho.pac.server.SolutionRepositoryServiceImpl());
    startExecution.addServlet(solutionrepositorysvc, "/solutionrepositorysvc"); //$NON-NLS-1$

    ServletHolder jdbcdriverdiscoveryservice = new ServletHolder(new org.pentaho.pac.server.common.JdbcDriverDiscoveryServiceImpl());
    startExecution.addServlet(jdbcdriverdiscoveryservice, "/jdbcdriverdiscoverysvc"); //$NON-NLS-1$

    // TODO sbarkdull, can this be deleted?
    // sample
    Handler hello = new HomeHandler();

    // resource handler
    ResourceHandler resources = new ResourceHandler();
    resources.setResourceBase("www/org.pentaho.pac.PentahoAdminConsole"); //$NON-NLS-1$
    resources.setWelcomeFiles(new String[] { "PentahoAdminConsole.html" }); //$NON-NLS-1$

    if (securityHandler != null) {
      server.setHandlers(new Handler[] { securityHandler, resources, startExecution });
    } else {
      server.setHandlers(new Handler[] { resources, startExecution });
    }
  }

  public int getPortNumber() {

    return portNumber;
  }

  public void setPortNumber(int portNumber) {

    this.portNumber = portNumber;
  }

  public String getHostname() {

    return hostname;
  }

  public void setHostname(String hostname) {

    this.hostname = hostname;
  }

  public static void main(String[] args) {
    jettyServer = new JettyServer();
  }

  // TODO sbarkdull, can this be deleted?
  public static class HomeHandler extends AbstractHandler {
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
        throws IOException, ServletException {
      Request base_request = (request instanceof Request) ? (Request) request : HttpConnection.getCurrentConnection()
          .getRequest();
      base_request.setHandled(true);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("text/html"); //$NON-NLS-1$
      response.getWriter().println("<h1>Hello OneContext</h1>"); //$NON-NLS-1$

    }
  }

  private class Halter implements Runnable {
    // This method is called when the thread runs

    private JettyServer jettyServer;

    public Halter(JettyServer jettyServer) {
      this.jettyServer = jettyServer;
    }

    public void run() {
      System.out.println(Messages.getString("CONSOLE.WAITING_TO_HALT")); //$NON-NLS-1$
      try {
        Thread.sleep(3000);
      } catch (Exception e) {
        // ignore this
      }
      System.out.println(Messages.getString("CONSOLE.HALTING")); //$NON-NLS-1$
      jettyServer.haltNow();
    }
  }
  
  public void readConfiguration() {
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
      String port = properties.getProperty(CONSOLE_PORT_NUMBER, null);
      String hostname = properties.getProperty(CONSOLE_HOST_NAME, null);
      String stopPortNumber = properties.getProperty(STOP_PORT, null);

      if (port != null && port.length() > 0) {
        this.portNumber = Integer.parseInt(port);
      } else {
        this.portNumber = DEFAULT_PORT_NUMBER;
      }

      if (stopPortNumber != null && stopPortNumber.length() > 0) {
        stopPort = Integer.parseInt(stopPortNumber);
      } else {
        stopPort = DEFAULT_STOP_PORT_NUMBER;
      }

      
      if (hostname != null && hostname.length() > 0) {
        this.hostname = hostname;
      } else {
        this.hostname = DEFAULT_HOSTNAME;
      }
    } else {
      this.hostname = DEFAULT_HOSTNAME;
      this.portNumber = DEFAULT_PORT_NUMBER;
      stopPort = DEFAULT_STOP_PORT_NUMBER;
    }
  }
  public void stopHandler(JettyServer jServer, int stopPort) {
    ServerSocket server = null;
    try {
      server = new ServerSocket(stopPort);
    } catch (IOException ioe) {
      System.out.println("IO Error: " + ioe.getLocalizedMessage());//$NON-NLS-1$
    }
    try {
      Socket s = server.accept();
      Thread t = new Thread(new RequestHandler(jServer, s));
      t.start();
    } catch (Exception e) {
      System.out.println("IO Error: " + e.getLocalizedMessage());//$NON-NLS-1$
    }
  }
  private class RequestHandler implements Runnable {
    // This method is called when the thread runs

    private JettyServer jettyServer;

    private Socket socket;

    public RequestHandler(JettyServer jettyServer, Socket socket) {
      this.jettyServer = jettyServer;
      this.socket = socket;
    }

    public void run() {
      try {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          if (inputLine != null && inputLine.length() > 0) {
            inputLine = inputLine.trim();
            if (inputLine.equalsIgnoreCase(STOP_ARG)) {
              System.out.println("Waiting to halt console"); //$NON-NLS-1$
              try {
                Thread.sleep(3000);
              } catch (Exception e) {
                // ignore this
              }
              System.out.println("Console is halting"); //$NON-NLS-1$
              jettyServer.haltNow();
            }
          }
        }
      } catch (IOException ioe) {
        System.out.println("IO Error: " + ioe.getLocalizedMessage());//$NON-NLS-1$
      }
    }
  }
}

