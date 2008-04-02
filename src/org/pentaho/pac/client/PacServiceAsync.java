package org.pentaho.pac.client;

import org.pentaho.pac.client.datasources.IDataSource;
import org.pentaho.pac.client.roles.ProxyPentahoRole;
import org.pentaho.pac.client.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PacServiceAsync {
  public void createUser(ProxyPentahoUser user, AsyncCallback callback);
  public void deleteUsers(ProxyPentahoUser[] users, AsyncCallback callback);
  public void updateUser(ProxyPentahoUser user, AsyncCallback callback);
  public void getUsers(AsyncCallback callback);
  
  public void createRole(ProxyPentahoRole role, AsyncCallback callback);
  public void deleteRoles(ProxyPentahoRole[] roles, AsyncCallback callback);
  public void updateRole(ProxyPentahoRole role, AsyncCallback callback);
  public void getRoles(AsyncCallback callback);
  
  public void createDataSource(IDataSource dataSource, AsyncCallback callback);
  public void deleteDataSources(IDataSource[] dataSources, AsyncCallback callback);
  public void updateDataSource(IDataSource dataSource, AsyncCallback callback);
  public void testDataSourceConnection(IDataSource dataSource, AsyncCallback callback);
  public void testDataSourceValidationQuery(IDataSource dataSource, AsyncCallback callback);
  public void getDataSources(AsyncCallback callback);
  
  public void refreshSolutionRepository(AsyncCallback callback);
  public void cleanRepository(AsyncCallback callback);
  public void clearMondrianDataCache(AsyncCallback callback);
  public void clearMondrianSchemaCache(AsyncCallback callback);
  public void scheduleRepositoryCleaning(AsyncCallback callback);
  public void resetRepository(AsyncCallback callback);
  public void refreshSystemSettings(AsyncCallback callback);
  public void executeGlobalActions(AsyncCallback callback);
  public void refreshReportingMetadata(AsyncCallback callback);
  public void getHomePage(String url, AsyncCallback callback);
}
