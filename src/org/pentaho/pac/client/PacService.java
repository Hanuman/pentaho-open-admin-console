package org.pentaho.pac.client;



import java.util.List;

import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.UserRoleSecurityInfo;
import org.pentaho.pac.common.datasources.IDataSource;
import org.pentaho.pac.common.roles.DuplicateRoleException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.RemoteService;

public interface PacService extends RemoteService {
  public UserRoleSecurityInfo getUserRoleSecurityInfo() throws PacServiceException;
  
  public boolean createRole(ProxyPentahoRole role) throws DuplicateRoleException, PentahoSecurityException, PacServiceException;
  public boolean deleteRoles(ProxyPentahoRole[] roles) throws NonExistingRoleException, PentahoSecurityException, PacServiceException;
  public boolean updateRole(ProxyPentahoRole role) throws NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  public ProxyPentahoRole[] getRoles() throws PacServiceException;
  public ProxyPentahoRole[] getRoles(ProxyPentahoUser user) throws NonExistingUserException, PacServiceException;
  
  public boolean createUser(ProxyPentahoUser user) throws DuplicateUserException, PentahoSecurityException, PacServiceException;
  public boolean deleteUsers(ProxyPentahoUser[] users) throws NonExistingUserException, PentahoSecurityException, PacServiceException;
  public boolean updateUser(ProxyPentahoUser user) throws NonExistingUserException, PentahoSecurityException, PacServiceException;
  public ProxyPentahoUser[] getUsers() throws PacServiceException;
  public ProxyPentahoUser[] getUsers(ProxyPentahoRole role) throws NonExistingRoleException, PacServiceException;
  
  public boolean createDataSource(IDataSource dataSource) throws PacServiceException;
  public boolean deleteDataSources(IDataSource[] dataSources) throws PacServiceException;
  public boolean updateDataSource(IDataSource dataSource) throws PacServiceException;
  public IDataSource[] getDataSources() throws PacServiceException;
  public boolean testDataSourceConnection(IDataSource dataSource) throws PacServiceException;
  public boolean testDataSourceValidationQuery(IDataSource dataSource) throws PacServiceException;
  
  public String refreshSolutionRepository() throws PacServiceException;
  public String cleanRepository() throws PacServiceException;
  public String clearMondrianDataCache() throws PacServiceException;
  public String clearMondrianSchemaCache() throws PacServiceException;
  public String scheduleRepositoryCleaning() throws PacServiceException;
  public String resetRepository() throws PacServiceException;
  public String refreshSystemSettings() throws PacServiceException;
  public String executeGlobalActions() throws PacServiceException;
  public String refreshReportingMetadata() throws PacServiceException;
  public String getHomePage(String url) throws PacServiceException;
  
  public void deleteJob( String jobName, String jobGroup ) throws PacServiceException;
  public void executeJobNow( String jobName, String jobGroup ) throws PacServiceException;
  /**
   * @gwt.typeArgs <org.pentaho.pac.client.scheduler.Job>
   */
  public List/*<Job>*/ getJobNames() throws PacServiceException;
  public boolean isSchedulerPaused() throws PacServiceException;
  public void pauseAll() throws PacServiceException;
  public void pauseJob( String jobName, String jobGroup ) throws PacServiceException;
  public void resumeAll() throws PacServiceException;
  public void resumeJob( String jobName, String jobGroup ) throws PacServiceException;
}
