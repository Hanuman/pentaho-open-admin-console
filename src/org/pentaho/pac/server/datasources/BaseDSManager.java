package org.pentaho.pac.server.datasources;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;


/**
 * A base class, designed to extended by actual implementations.
 * Provides initial parameter storage/retrieval capabilities.
 * 
 * @author Alex Silva
 *
 */
public abstract class BaseDSManager implements IDataSourceManager {
  private Set<NamedParameter> parms = new HashSet<NamedParameter>();

  /**
   * The JMX MBeanServer we will use to look up management beans.
   */
  protected MBeanServerConnection mserver = null;

  public void addInitParameter(String name, Object value) {
    parms.add(new NamedParameter(name, value));
  }

  public Object getInitParameter(String name) {
    for (NamedParameter parm : parms) {
      if (parm.getName().equals(name))
        return parm.getValue();

    }

    return null;
  }

}
