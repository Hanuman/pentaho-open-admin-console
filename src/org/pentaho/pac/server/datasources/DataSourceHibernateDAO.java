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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.HibernateSessionFactory;
import org.pentaho.platform.api.repository.datasource.IDatasource;
import org.pentaho.platform.repository.datasource.Datasource;

public class DataSourceHibernateDAO implements IDataSourceDAO {

  public DataSourceHibernateDAO() {
  }

  public void createDataSource(IDatasource newDataSource) throws DuplicateDataSourceException, DAOException {
    if (getDataSource(newDataSource.getName()) == null) {
      try {
        getSession().save(newDataSource);
      } catch (HibernateException ex) {
        getSession().evict(newDataSource);
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new DuplicateDataSourceException(newDataSource.getName());
    }
  }

  public void deleteDataSource(IDatasource dataSource) throws NonExistingDataSourceException, DAOException {
    IDatasource tmpDataSource = getDataSource(dataSource.getName());
    if (tmpDataSource != null) {
      try {
        getSession().delete(tmpDataSource);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingDataSourceException(dataSource.getName());
    }
  }

  public IDatasource getDataSource(String jndiName) throws DAOException {
    try {
      return(IDatasource) getSession().get(Datasource.class.getName(), jndiName);
    } catch (HibernateException ex) {
      throw new DAOException(ex.getMessage(), ex);
    }
  }
  @SuppressWarnings("unchecked")
  public List<IDatasource> getDataSources() throws DAOException {
    try {
      String nameQuery = "org.pentaho.platform.repository.datasource.Datasource.findAllDatasources"; //$NON-NLS-1$
      Query qry = getSession().getNamedQuery(nameQuery).setCacheable(true);
      return  qry.list();
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void updateDataSource(IDatasource dataSource) throws NonExistingDataSourceException, DAOException {
    try {
      getSession().update(dataSource);
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public Session getSession() {
    return HibernateSessionFactory.getSession();
  }

  public void beginTransaction() throws DAOException {
    try {
      getSession().beginTransaction();
    } catch ( JDBCConnectionException ex )
    {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void commitTransaction() throws DAOException {
    try {
      getSession().getTransaction().commit();
    } catch ( JDBCConnectionException ex )
    {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void rollbackTransaction() throws DAOException {
    try {
      getSession().getTransaction().rollback();
    } catch ( JDBCConnectionException ex )
    {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void closeSession() {
    HibernateSessionFactory.closeSession();
  }
}