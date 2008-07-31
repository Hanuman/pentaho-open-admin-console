package org.pentaho.pac.server.datasources;

import java.util.List;

import org.pentaho.pac.common.PentahoSecurityException;
import org.pentaho.pac.common.datasources.DuplicateDataSourceException;
import org.pentaho.pac.common.datasources.NonExistingDataSourceException;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.server.common.DAOException;
import org.pentaho.pac.server.common.DAOFactory;

public class DataSourceMgmtService implements IDataSourceMgmtService {
  IDataSourceDAO dataSourceDAO = null;

  public DataSourceMgmtService() {
    dataSourceDAO = DAOFactory.getDataSourceDAO();
  }

  public void createDataSource(PentahoDataSource newDataSource) throws DuplicateDataSourceException, DAOException, PentahoSecurityException {
    if (hasCreateDataSourcePerm(newDataSource)) {
      dataSourceDAO.createDataSource(newDataSource);
    } else {
      throw new PentahoSecurityException();
    }
  }

  public void deleteDataSource(String jndiName) throws NonExistingDataSourceException, DAOException, PentahoSecurityException {
    PentahoDataSource dataSource = dataSourceDAO.getDataSource(jndiName);
    if (dataSource != null) {
      deleteDataSource(dataSource);
    } else {
      throw new NonExistingDataSourceException(jndiName);
    }
  }

  public void deleteDataSource(PentahoDataSource dataSource) throws NonExistingDataSourceException, DAOException, PentahoSecurityException {
    if (hasDeleteDataSourcePerm(dataSource)) {
      dataSourceDAO.deleteDataSource(dataSource);
    } else {
      throw new PentahoSecurityException();
    }
  }

  public PentahoDataSource getDataSource(String jndiName) throws DAOException {
    return dataSourceDAO.getDataSource(jndiName);
  }

  public List<PentahoDataSource> getDataSources() throws DAOException {
    return dataSourceDAO.getDataSources();
  }

  public void updateDataSource(PentahoDataSource dataSource) throws DAOException, PentahoSecurityException, NonExistingDataSourceException {
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

  protected boolean hasCreateDataSourcePerm(PentahoDataSource dataSource) {
    return true;
  }

  protected boolean hasUpdateDataSourcePerm(PentahoDataSource dataSource) {
    return true;
  }
  protected boolean hasDeleteDataSourcePerm(PentahoDataSource dataSource) {
    return true;
  }
}
