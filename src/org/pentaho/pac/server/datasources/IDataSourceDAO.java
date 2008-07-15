package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.IPentahoDataSource;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.IGenericDAO;


public interface IDataSourceDAO extends IGenericDAO {

  public void createDataSource(PentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException;
  public void deleteDataSource(PentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException;
  public PentahoDataSource getDataSource(String jndiName) throws DAOException;
  public List<PentahoDataSource> getDataSources() throws DAOException;
  public void updateDataSource(PentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException;

  }
