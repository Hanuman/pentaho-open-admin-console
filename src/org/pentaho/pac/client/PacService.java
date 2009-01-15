package org.pentaho.pac.client;



import org.pentaho.pac.common.HibernateConfigException;
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
  public void refreshHibernateConfig() throws HibernateConfigException, HibernateConfigException;
  public boolean createRole(ProxyPentahoRole role) throws HibernateConfigException, DuplicateRoleException, PentahoSecurityException, PacServiceException;
  public boolean deleteRoles(ProxyPentahoRole[] roles) throws HibernateConfigException, NonExistingRoleException, PentahoSecurityException, PacServiceException;
  public boolean updateRole(ProxyPentahoRole role) throws HibernateConfigException, HibernateConfigException, NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  public ProxyPentahoRole[] getRoles() throws HibernateConfigException, PacServiceException;
  public ProxyPentahoRole[] getRoles(ProxyPentahoUser user) throws HibernateConfigException, NonExistingUserException, PacServiceException;
  public void setRoles(ProxyPentahoUser user, ProxyPentahoRole[] assignedRoles) throws HibernateConfigException, NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  
  public boolean createUser(ProxyPentahoUser user) throws HibernateConfigException, DuplicateUserException, PentahoSecurityException, PacServiceException;
  public boolean deleteUsers(ProxyPentahoUser[] users) throws HibernateConfigException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  public boolean updateUser(ProxyPentahoUser user) throws HibernateConfigException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  public ProxyPentahoUser[] getUsers() throws HibernateConfigException, PacServiceException;
  public ProxyPentahoUser[] getUsers(ProxyPentahoRole role) throws HibernateConfigException, NonExistingRoleException, PacServiceException;
  public void setUsers(ProxyPentahoRole role, ProxyPentahoUser[] assignedUsers) throws HibernateConfigException, NonExistingRoleException, NonExistingUserException, PentahoSecurityException, PacServiceException;
  
  public boolean createDataSource(PentahoDataSource dataSource) throws HibernateConfigException, PacServiceException;
  public boolean deleteDataSources(PentahoDataSource[] dataSources) throws HibernateConfigException, PacServiceException;
  public boolean updateDataSource(PentahoDataSource dataSource) throws HibernateConfigException, PacServiceException;
  public PentahoDataSource[] getDataSources() throws HibernateConfigException, PacServiceException;
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
