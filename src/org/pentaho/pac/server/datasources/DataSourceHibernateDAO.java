package org.pentaho.pac.server.datasources;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.HibernateSessionFactory;

public class DataSourceHibernateDAO implements IDataSourceDAO {

  public DataSourceHibernateDAO() {
  }

  public void createDataSource(PentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException {
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

  public void deleteDataSource(PentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException {
    PentahoDataSource tmpDataSource = getDataSource(dataSource.getName());
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

  public PentahoDataSource getDataSource(String jndiName) throws DAOException {
    try {
      PentahoDataSource pentahoDataSource = (PentahoDataSource) getSession().get(PentahoDataSource.class.getName(), jndiName);
      return pentahoDataSource;
    } catch (HibernateException ex) {
      throw new DAOException(ex.getMessage(), ex);
    }
  }

  public List<PentahoDataSource> getDataSources() throws DAOException {
    try {
      String queryString = "from PentahoDataSource";  //$NON-NLS-1$
      Query queryObject = getSession().createQuery(queryString);
      List<PentahoDataSource> pentahoDataSourceList = queryObject.list();
      return pentahoDataSourceList;
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void updateDataSource(PentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException {
    if (getDataSource(dataSource.getName()) != null) {
      try {
        getSession().update(dataSource);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingDataSourceException(dataSource.getName());
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