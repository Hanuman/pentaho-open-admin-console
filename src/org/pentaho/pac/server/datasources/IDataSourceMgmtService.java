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
