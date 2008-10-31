package org.pentaho.pac.client;



import org.pentaho.pac.common.PacServiceException;
import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.ServiceInitializationException;
import org.pentaho.pac.common.UserRoleSecurityInfo;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.common.roles.DuplicateRoleException;
import org.pentaho.pac.common.roles.NonExistingRoleException;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.RemoteService;

public interface PacService extends RemoteService {
  public UserRoleSecurityInfo getUserRoleSecurityInfo() throws PacServiceException;
  public void initialze() throws ServiceInitializationException;
  public void updateHibernate() throws PacServiceException;
  public boolean createRole(ProxyPentahoRole role) throws DuplicateRoleException, PentahoSecurityException, PacServiceException;
  public boolean deleteRoles(ProxyPentahoRole[] roles) throws NonExistingRoleException, PentahoSecurityException, PacServiceException;
  public boolean updateRole(ProxyPentahoRole role) throws NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  public ProxyPentahoRole[] getRoles() throws PacServiceException;
  public ProxyPentahoRole[] getRoles(ProxyPentahoUser user) throws NonExistingUserException, PacServiceException;
  public void setRoles(ProxyPentahoUser user, ProxyPentahoRole[] assignedRoles) throws NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  
  public boolean createUser(ProxyPentahoUser user) throws DuplicateUserException, PentahoSecurityException, PacServiceException;
  public boolean deleteUsers(ProxyPentahoUser[] users) throws NonExistingUserException, PentahoSecurityException, PacServiceException;
  public boolean updateUser(ProxyPentahoUser user) throws NonExistingUserException, PentahoSecurityException, PacServiceException;
  public ProxyPentahoUser[] getUsers() throws PacServiceException;
  public ProxyPentahoUser[] getUsers(ProxyPentahoRole role) throws NonExistingRoleException, PacServiceException;
  public void setUsers(ProxyPentahoRole role, ProxyPentahoUser[] assignedUsers) throws NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  
  public boolean createDataSource(PentahoDataSource dataSource) throws PacServiceException;
  public boolean deleteDataSources(PentahoDataSource[] dataSources) throws PacServiceException;
  public boolean updateDataSource(PentahoDataSource dataSource) throws PacServiceException;
  public PentahoDataSource[] getDataSources() throws PacServiceException;
  public boolean testDataSourceConnection(PentahoDataSource dataSource) throws PacServiceException;
  public boolean testDataSourceValidationQuery(PentahoDataSource dataSource) throws PacServiceException;
  
  public String refreshSolutionRepository() throws PacServiceException;
  public String cleanRepository() throws PacServiceException;
  public String clearMondrianSchemaCache() throws PacServiceException;
  public String scheduleRepositoryCleaning() throws PacServiceException;
  public String resetRepository() throws PacServiceException;
  public String refreshSystemSettings() throws PacServiceException;
  public String executeGlobalActions() throws PacServiceException;
  public String refreshReportingMetadata() throws PacServiceException;
  public String getHomePageAsHtml(String url) throws PacServiceException;
  
  public void isBiServerAlive() throws PacServiceException;
  public int getBiServerStatusCheckPeriod();
  public String getBIServerBaseUrl();
  public String getHomepageUrl();
  public String getHelpUrl();
}
