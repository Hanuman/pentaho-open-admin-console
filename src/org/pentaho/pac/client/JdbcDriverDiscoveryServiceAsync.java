package org.pentaho.pac.client;

import org.pentaho.pac.common.NameValue;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface JdbcDriverDiscoveryServiceAsync {
  public void initialize(AsyncCallback callback);
  public void getAvailableJdbcDrivers(AsyncCallback<NameValue[]> callback);
  public void getAvailableJdbcDrivers(String location, AsyncCallback<NameValue[]> callback);
  public void getRelativeAvailableJdbcDrivers(String location, AsyncCallback<NameValue[]> callback);

}
