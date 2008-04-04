package org.pentaho.pac.server.datasources;

import java.util.Collection;

import org.pentaho.pac.common.datasources.IDataSource;

/**
 * Interface that encapsulates the steps/operations required to communicate to an underlying server and perform datasource 
 * related operations.
 * Clients are allowed to use implementations of this interface as long-lived (stored in an object context somewhere) 
 * or method/instance level objects.
 * 
 * @author Alex Silva
 *
 */
public interface IDataSourceManager {
  /**
   * Permanently adds a datasource to the server environment.
   * @param ds - The datasource to be added
   * @throws DataSourceManagementException If the datasource name is not unique; if a communication error occurs, etc.
   */
  public void addDataSource(IDataSource ds) throws DataSourceManagementException;

  /**
   * Permanently deletes a datasource from the server environment.
   * @param ds - The datasource to be deleted
   * @throws DataSourceManagementException If the datasource cannot be found; if a communication error occurs, etc.
   */
  public void deleteDataSource(IDataSource ds) throws DataSourceManagementException;

  /**
   * Permanently deletes a datasource from the server environment.
   * @param ds - The <b>fully</b> qualified datasource name, which IS SERVER DEPENDENT.
   * @throws DataSourceManagementException If the datasource cannot be found; if a communication error occurs, etc.
   */
  public void deleteDataSource(String name) throws DataSourceManagementException;

  /**
   * Updates an existing datasource with the new parameters.
   * @param ds - The datasource object containing the new parameters, identified by its jndi name.
   * @throws DataSourceManagementException If the datasource cannot be found; if a communication error occurs, etc.
   */
  public void updateDataSource(IDataSource ds) throws DataSourceManagementException;

  /**
   * @return a list of data sources configured on the server. The collection contains strings with the JNDI name for all the data sources configured on the server.
   * @throws DataSourceManagementException
   */
  public Collection<String> getDataSources() throws DataSourceManagementException;
  
  /**
   * 
   * @param name The JNDI name
   * @return The data source - null if not found
   * @throws DataSourceManagementException if a communication error occurs, etc.
   */
  public IDataSource getDataSource(String name) throws DataSourceManagementException;

  /**
   * Performs any server-specific operation to permanently modify the server environment, such as
   * config file modications, etc.
   * @throws DataSourceManagementException If something goes wrong
   */
  public void commitChanges() throws DataSourceManagementException;

  /**
   * Initializes this manager implementation.
   * It is expected that this method will be invoked before any of the operations above are performed.
   * @throws DataSourceManagementException
   */
  void init() throws DataSourceManagerCreationException;

  /**
   * Destroy this manager implementation, releasing any temporary resources allocated.
   * @throws DataSourceManagementException
   */
  void destroy() throws DataSourceManagementException;

  /**
   * Adds specific parameters that are required for this implementation to work.
   * Can be things such as port number, host name, etc.
   * @param name The name of the parameter
   * @param value
   */
  void addInitParameter(String name, Object value);

  /**
   * 
   * @param name The name of the parameter
   * @return The actual value; null if not found.
   */
  Object getInitParameter(String s);
}
