/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.client;

import org.pentaho.pac.client.common.PentahoAsyncService;
import org.pentaho.pac.common.UserRoleSecurityInfo;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PacServiceAsync extends PentahoAsyncService<Object>{
  public void initialze(AsyncCallback<Object> callback);
  public void isValidConfiguration(AsyncCallback<Boolean> callback);
  public void refreshHibernateConfig(AsyncCallback<Object> callback);
  public void getUserRoleSecurityInfo(AsyncCallback<UserRoleSecurityInfo> callback);
  public void createUser(ProxyPentahoUser user, AsyncCallback<Boolean> callback);
  public void deleteUsers(ProxyPentahoUser[] users, AsyncCallback<Boolean> callback);
  public void updateUser(ProxyPentahoUser user, AsyncCallback<Boolean> callback);
  public void getUsers(AsyncCallback<ProxyPentahoUser[]> callback);
  public void getUsers(ProxyPentahoRole role, AsyncCallback<ProxyPentahoUser[]> callback);
  public void setUsers(ProxyPentahoRole role, ProxyPentahoUser[] assignedUsers, AsyncCallback<Object> callback);
  
  public void createRole(ProxyPentahoRole role, AsyncCallback<Boolean> callback);
  public void deleteRoles(ProxyPentahoRole[] roles, AsyncCallback<Boolean> callback);
  public void updateRole(ProxyPentahoRole role, AsyncCallback<Boolean> callback);
  public void getRoles(AsyncCallback<ProxyPentahoRole[]> callback);
  public void getRoles(ProxyPentahoUser user, AsyncCallback<ProxyPentahoRole[]> callback);
  public void setRoles(ProxyPentahoUser user, ProxyPentahoRole[] assignedRoles, AsyncCallback<Object> callback);
  
  public void createDataSource(PentahoDataSource dataSource, AsyncCallback<Boolean> callback);
  public void deleteDataSources(PentahoDataSource[] dataSources, AsyncCallback<Boolean> callback);
  public void updateDataSource(PentahoDataSource dataSource, AsyncCallback<Boolean> callback);
  public void testDataSourceConnection(PentahoDataSource dataSource, AsyncCallback<Boolean> callback);
  public void testDataSourceValidationQuery(PentahoDataSource dataSource, AsyncCallback<Boolean> callback);
  public void getDataSources(AsyncCallback<PentahoDataSource[]> callback);
  
  public void refreshSolutionRepository(AsyncCallback<String> callback);
  public void cleanRepository(AsyncCallback<String> callback);
  public void clearMondrianSchemaCache(AsyncCallback<String> callback);
  public void scheduleRepositoryCleaning(AsyncCallback<String> callback);
  public void resetRepository(AsyncCallback<String> callback);
  public void refreshSystemSettings(AsyncCallback<String> callback);
  public void executeGlobalActions(AsyncCallback<String> callback);
  public void refreshReportingMetadata(AsyncCallback<String> callback);
  public void getHomePageAsHtml(String url, AsyncCallback<String> callback);
  
  public void isBiServerAlive( AsyncCallback<Object> callback );
  public void getBiServerStatusCheckPeriod( AsyncCallback<Integer> callback );

  public void getBIServerBaseUrl(AsyncCallback<String> callback);
  public void getHomepageUrl(AsyncCallback<String> callback);  
  public void getHelpUrl(AsyncCallback<String> callback);
  
}
