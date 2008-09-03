package org.pentaho.pac.client;

import org.pentaho.pac.common.JdbcDriverDiscoveryServiceException;
import org.pentaho.pac.common.NameValue;

import com.google.gwt.user.client.rpc.RemoteService;

public interface JdbcDriverDiscoveryService extends RemoteService {
  public void initialize();
  public NameValue[] getAvailableJdbcDrivers() throws JdbcDriverDiscoveryServiceException;
  public NameValue[] getAvailableJdbcDrivers(String location) throws JdbcDriverDiscoveryServiceException;
  public NameValue[] getRelativeAvailableJdbcDrivers(String location) throws JdbcDriverDiscoveryServiceException;
}
