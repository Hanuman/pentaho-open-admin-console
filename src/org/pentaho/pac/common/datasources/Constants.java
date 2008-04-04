package org.pentaho.pac.common.datasources;

public class Constants {
  public final static String PATH = "ds.path"; //$NON-NLS-1$

  public final static String HOST = "ds.host"; //$NON-NLS-1$

  /**
   * The username used to login to the admin console. <BR/><i>Optional</i>
   * parameter;used when authentication is turned on.
   */
  public static final String JMX_USERNAME = "mgmt.jmx.un"; //$NON-NLS-1$

  /**
   * The password used to login to the admin console. <BR/><i>Optional</i>
   * parameter;used when authentication is turned on.
   */
  public static final String JMX_PASSWORD = "mgmt.jmx.pwd"; //$NON-NLS-1$

  /**
   * The port on which server registered its remote MBean server. <BR/> This
   * is a <b>required</b> parameter.
   */
  public static final String JMX_PORT = "mgmt.jmx.port"; //$NON-NLS-1$

  /**
   * The port on which server registered its remote MBean server. <BR/> This
   * is a <b>required</b> parameter.
   */
  public static final String JMX_HOST = "mgmt.jmx.host"; //$NON-NLS-1$
  
  /**
   * The port on which server registered its remote MBean server. <BR/> This
   * is a <b>required</b> parameter.
   */
  public static final String JMX_AUTH = "mgmt.jmx.auth"; //$NON-NLS-1$

}
