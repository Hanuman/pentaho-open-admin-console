package org.pentaho.pac.client;

import org.pentaho.pac.common.NameValue;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface HibernateConfigurationServiceAsync {
  public void initialize(AsyncCallback<Object> callback);
  public void getAvailableHibernateConfigurations(AsyncCallback<NameValue[]> callback);
  public void getAvailableHibernateConfigurations(String location, AsyncCallback<NameValue[]> callback);
}
