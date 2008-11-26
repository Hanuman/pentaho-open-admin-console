package org.pentaho.pac.client;

import org.pentaho.pac.common.HibernateConfigurationServiceException;
import org.pentaho.pac.common.NameValue;

import com.google.gwt.user.client.rpc.RemoteService;

public interface HibernateConfigurationService extends RemoteService {
  public void initialize();
  public NameValue[] getAvailableHibernateConfigurations() throws HibernateConfigurationServiceException;
  public NameValue[] getAvailableHibernateConfigurations(String location) throws HibernateConfigurationServiceException;
}
