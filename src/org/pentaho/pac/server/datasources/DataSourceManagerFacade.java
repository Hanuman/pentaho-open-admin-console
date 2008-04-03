package org.pentaho.pac.server.datasources;


public class DataSourceManagerFacade {
  private static final IDataSourceManagerFactory dsMgrFactory = new ClassNameDSMgrFactory();

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
  public IDataSourceManager getDSManager(Type type, NamedParameter... parms) throws DataSourceManagerCreationException {
    switch (type) {
      case TOMCAT:
      case JBOSS:
        IDataSourceManager mgr = dsMgrFactory.create(type.impl.getName());

        for (NamedParameter parm : parms) {
          mgr.addInitParameter(parm.getName(), parm.getValue());
        }
        mgr.init();

        return mgr;

      case OTHER:
      default:
        throw new DataSourceManagerCreationException("Unrecognized data source type: " + type );

    }
  }

  public IDataSourceManager getDSManager(String className, NamedParameter... parms)
      throws DataSourceManagerCreationException {
    IDataSourceManager mgr = dsMgrFactory.create(className);

    for (NamedParameter parm : parms) {
      mgr.addInitParameter(parm.getName(), parm.getValue());
    }

    mgr.init();

    return mgr;

  }

}

