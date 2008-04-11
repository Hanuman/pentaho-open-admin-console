package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.IGenericDAO;


public interface IDataSourceDAO extends IGenericDAO {

  public void createDataSource(IPentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException;
  public void deleteDataSource(IPentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException;
  public IPentahoDataSource getDataSource(String jndiName) throws DAOException;
  public List<IPentahoDataSource> getDataSources() throws DAOException;
  public void updateDataSource(IPentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException;

  }
