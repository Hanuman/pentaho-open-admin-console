package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.DAOFactory;
import org.pentaho.platform.api.repository.datasource.IDatasource;

public class DataSourceMgmtService implements IDataSourceMgmtService {
  IDataSourceDAO dataSourceDAO = null;

  public DataSourceMgmtService() {
    dataSourceDAO = DAOFactory.getDataSourceDAO();
  }

  public void createDataSource(IDatasource newDataSource) throws DuplicateDataSourceException, DAOException, PentahoSecurityException {
    if (hasCreateDataSourcePerm(newDataSource)) {
      dataSourceDAO.createDataSource(newDataSource);
    } else {
      throw new PentahoSecurityException();
    }
  }

  public void deleteDataSource(String jndiName) throws NonExistingDataSourceException, DAOException, PentahoSecurityException {
    IDatasource dataSource = dataSourceDAO.getDataSource(jndiName);
    if (dataSource != null) {
      deleteDataSource(dataSource);
    } else {
      throw new NonExistingDataSourceException(jndiName);
    }
  }

  public void deleteDataSource(IDatasource dataSource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException {
    if (hasDeleteDataSourcePerm(dataSource)) {
      dataSourceDAO.deleteDataSource(dataSource);
    } else {
      throw new PentahoSecurityException();
    }
  }

  public IDatasource getDataSource(String jndiName) throws DAOException {
    return dataSourceDAO.getDataSource(jndiName);
  }

  public List<IDatasource> getDataSources() throws DAOException {
    return dataSourceDAO.getDataSources();
  }

  public void updateDataSource(IDatasource dataSource) throws DAOException, PentahoSecurityException, NonExistingDataSourceException {
    if (hasUpdateDataSourcePerm(dataSource)) {
      dataSourceDAO.updateDataSource(dataSource);
    } else {
      throw new PentahoSecurityException();
    }
  }
  public void beginTransaction()  throws DAOException {
    dataSourceDAO.beginTransaction();
  }

  public void commitTransaction()  throws DAOException {
    dataSourceDAO.commitTransaction();
  }

  public void rollbackTransaction()  throws DAOException {
    dataSourceDAO.rollbackTransaction();
  }

  public void closeSession() {
    dataSourceDAO.closeSession();
  }

  protected boolean hasCreateDataSourcePerm(IDatasource dataSource) {
    return true;
  }

  protected boolean hasUpdateDataSourcePerm(IDatasource dataSource) {
    return true;
  }
  protected boolean hasDeleteDataSourcePerm(IDatasource dataSource) {
    return true;
  }
}
