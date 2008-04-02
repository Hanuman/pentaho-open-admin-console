package org.pentaho.pac.server.datasources;

import org.pentaho.pac.client.datasources.DataSourceManagementException;



public class DataSourceManagerFacade {
  private static final IDataSourceManagerFactory fact = new ClassNameDSMgrFactory();

  public enum Type {
    TOMCAT(TomcatDSManager.class), JBOSS(JBossDSManager.class), OTHER(null);

    Type(Class<? extends IDataSourceManager> impl) {
      this.impl = impl;
    }

    private Class<? extends IDataSourceManager> impl;

  }

  /**
   * Havent implemented caching yet... Will do that later.
   * @param type
   * @param parms
   * @return
   * @throws DataSourceManagementException
   */
  public IDataSourceManager getDSManager(Type type, NamedParameter... parms) throws DataSourceManagementException {
    switch (type) {
      case TOMCAT:
      case JBOSS:
        IDataSourceManager mgr = fact.create(type.impl.getName());

        for (NamedParameter parm : parms) {
          mgr.addInitParameter(parm.getName(), parm.getValue());
        }
        mgr.init();

        return mgr;

      case OTHER:
      default:
        throw new DataSourceManagementException("'OTHER' is not supported with this method!");

    }
  }

  public IDataSourceManager getDSManager(String className, NamedParameter... parms)
      throws DataSourceManagementException {
    IDataSourceManager mgr = fact.create(className);

    for (NamedParameter parm : parms) {
      mgr.addInitParameter(parm.getName(), parm.getValue());
    }

    mgr.init();

    return mgr;

  }

}
