package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.server.common.DAOException;

public interface IDataSourceMgmtService {

  public void createDataSource(IPentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException, PentahoSecurityException;

  public void deleteDataSource(String jndiName) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public void deleteDataSource(IPentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public IPentahoDataSource getDataSource(String JndiName) throws DAOException;

  public List<IPentahoDataSource> getDataSources() throws DAOException;

  public void updateDataSource(IPentahoDataSource datasource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public void beginTransaction() throws DAOException;

  public void commitTransaction() throws DAOException;

  public void rollbackTransaction() throws DAOException;

  public void closeSession();
}
