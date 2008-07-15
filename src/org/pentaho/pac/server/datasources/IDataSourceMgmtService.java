package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.server.common.DAOException;

public interface IDataSourceMgmtService {

  public void createDataSource(PentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException, PentahoSecurityException;

  public void deleteDataSource(String jndiName) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public void deleteDataSource(PentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public PentahoDataSource getDataSource(String JndiName) throws DAOException;

  public List<PentahoDataSource> getDataSources() throws DAOException;

  public void updateDataSource(PentahoDataSource datasource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException;

  public void beginTransaction() throws DAOException;

  public void commitTransaction() throws DAOException;

  public void rollbackTransaction() throws DAOException;

  public void closeSession();
}
