package org.pentaho.pac.server.datasources;


/**
 * A factory which uses class names to instantiate ds manager objects.
 * @author Alex Silva
 *
 */
class ClassNameDSMgrFactory implements IDataSourceManagerFactory {

  /**
   * Retrieves a <code>DataSourceManager</code> implementation based on the className supplied to this method.
   */
  public IDataSourceManager create(String className) throws DataSourceManagementException {
    Class<?> clazz = null;

    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new DataSourceManagementException("Unable to load class " + className);
    }

    if (!IDataSourceManager.class.isAssignableFrom(clazz))
      throw new DataSourceManagementException("Invalid datasource manager class.");

    Class<? extends IDataSourceManager> mgrc = clazz.asSubclass(IDataSourceManager.class);

    try {
      return mgrc.newInstance();
    } catch (Exception e) {
      throw new DataSourceManagementException("Unable to instantiate data source manager", e);
    }

  }

}
