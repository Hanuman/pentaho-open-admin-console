package org.pentaho.pac.server.datasources;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.HibernateSessionFactory;

public class DataSourceHibernateDAO implements IDataSourceDAO {

  public DataSourceHibernateDAO() {
  }

  public void createDataSource(IPentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException {
    if (getDataSource(newDataSource.getJndiName()) == null) {
      try {
        getSession().save(newDataSource);
      } catch (HibernateException ex) {
        getSession().evict(newDataSource);
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new DuplicateDataSourceException(newDataSource.getJndiName());
    }
  }

  public void deleteDataSource(IPentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException {
    IPentahoDataSource tmpDataSource = getDataSource(dataSource.getJndiName());
    if (tmpDataSource != null) {
      try {
        getSession().delete(tmpDataSource);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingDataSourceException(dataSource.getJndiName());
    }
  }

  public IPentahoDataSource getDataSource(String jndiName) throws DAOException {
    try {
      return (IPentahoDataSource) getSession().get(PentahoDataSource.class.getName(), jndiName);
    } catch (HibernateException ex) {
      throw new DAOException(ex.getMessage(), ex);
    }
  }

  public List<IPentahoDataSource> getDataSources() throws DAOException {
    try {
      String queryString = "from PentahoDataSource";
      Query queryObject = getSession().createQuery(queryString);
      return queryObject.list();
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void updateDataSource(IPentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException {
    if (getDataSource(dataSource.getJndiName()) != null) {
      try {
        getSession().update(dataSource);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingDataSourceException(dataSource.getJndiName());
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