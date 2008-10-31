package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.IGenericDAO;
import org.pentaho.platform.api.repository.datasource.IDatasource;


public interface IDataSourceDAO extends IGenericDAO {

  public void createDataSource(IDatasource newDataSource) throws DuplicateDataSourceException, DAOException;
  public void deleteDataSource(IDatasource dataSource) throws NonExistingDataSourceException, DAOException;
  public IDatasource getDataSource(String jndiName) throws DAOException;
  public List<IDatasource> getDataSources() throws DAOException;
  public void updateDataSource(IDatasource dataSource) throws NonExistingDataSourceException, DAOException;

  }
