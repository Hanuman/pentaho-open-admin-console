package org.pentaho.pac.client;

import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PacServiceAsync {
  public void getUserRoleSecurityInfo(AsyncCallback callback);
  
  public void createUser(ProxyPentahoUser user, AsyncCallback callback);
  public void deleteUsers(ProxyPentahoUser[] users, AsyncCallback callback);
  public void updateUser(ProxyPentahoUser user, AsyncCallback callback);
  public void getUsers(AsyncCallback callback);
  public void getUsers(ProxyPentahoRole role, AsyncCallback callback);
  public void setUsers(ProxyPentahoRole role, ProxyPentahoUser[] assignedUsers, AsyncCallback callback);
  
  public void createRole(ProxyPentahoRole role, AsyncCallback callback);
  public void deleteRoles(ProxyPentahoRole[] roles, AsyncCallback callback);
  public void updateRole(ProxyPentahoRole role, AsyncCallback callback);
  public void getRoles(AsyncCallback callback);
  public void getRoles(ProxyPentahoUser user, AsyncCallback callback);
  public void setRoles(ProxyPentahoUser user, ProxyPentahoRole[] assignedRoles, AsyncCallback callback);
  
  public void createDataSource(PentahoDataSource dataSource, AsyncCallback callback);
  public void deleteDataSources(PentahoDataSource[] dataSources, AsyncCallback callback);
  public void updateDataSource(PentahoDataSource dataSource, AsyncCallback callback);
  public void testDataSourceConnection(PentahoDataSource dataSource, AsyncCallback callback);
  public void testDataSourceValidationQuery(PentahoDataSource dataSource, AsyncCallback callback);
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
  public void getHomePageAsHtml(String url, AsyncCallback<String> callback);
  
  public void isBiServerAlive( AsyncCallback<Object> callback );
  public void getBiServerStatusCheckPeriod( AsyncCallback<Integer> callback );
}
