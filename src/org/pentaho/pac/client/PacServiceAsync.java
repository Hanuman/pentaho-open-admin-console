package org.pentaho.pac.client;

import org.pentaho.pac.common.datasources.IDataSource;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PacServiceAsync {
  public void createUser(ProxyPentahoUser user, AsyncCallback callback);
  public void deleteUsers(ProxyPentahoUser[] users, AsyncCallback callback);
  public void updateUser(ProxyPentahoUser user, AsyncCallback callback);
  public void getUsers(AsyncCallback callback);
  public void getUsers(ProxyPentahoRole role, AsyncCallback callback);
  
  public void createRole(ProxyPentahoRole role, AsyncCallback callback);
  public void deleteRoles(ProxyPentahoRole[] roles, AsyncCallback callback);
  public void updateRole(ProxyPentahoRole role, AsyncCallback callback);
  public void getRoles(AsyncCallback callback);
  public void getRoles(ProxyPentahoUser user, AsyncCallback callback);
  
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
  
  public void deleteJob( String jobName, String jobGroup, AsyncCallback callback );
  public void executeJobNow( String jobName, String jobGroup, AsyncCallback callback );
  public void getJobNames( AsyncCallback callback );
  public void isSchedulerPaused( AsyncCallback callback );
  public void pauseAll( AsyncCallback callback );
  public void pauseJob( String jobName, String jobGroup, AsyncCallback callback );
  public void resumeAll( AsyncCallback callback );
  public void resumeJob( String jobName, String jobGroup, AsyncCallback callback );
}
