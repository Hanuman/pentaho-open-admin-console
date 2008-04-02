package org.pentaho.pac.server.datasources;

import org.pentaho.pac.client.datasources.DataSourceManagementException;

/**
 * Factory interface to encapsulate object creation from client.
 * @author Alex Silva
 *
 */
interface IDataSourceManagerFactory {

  /**
   * Returns a <Code>DataSourceManager</code> object based on the hint provided.
   * @param hint what to use to instantiate the proper data source manager.
   * @return a valid <Code>DataSourceManager</code> object.
   * @throws DataSourceManagementException If the object cannot be instantiated.
   */
  IDataSourceManager create(String hint) throws DataSourceManagementException;
}
