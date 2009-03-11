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
package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.platform.api.repository.datasource.IDatasource;

public interface IDataSourceMgmtService {

  public void createDataSource(IDatasource newDataSource) throws DuplicateDataSourceException, DAOException, PentahoSecurityException;

  public void deleteDataSource(String jndiName) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public void deleteDataSource(IDatasource dataSource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public IDatasource getDataSource(String JndiName) throws DAOException;

  public List<IDatasource> getDataSources() throws DAOException;

  public void updateDataSource(IDatasource datasource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public void beginTransaction() throws DAOException;

  public void commitTransaction() throws DAOException;

  public void rollbackTransaction() throws DAOException;

  public void closeSession();
}
