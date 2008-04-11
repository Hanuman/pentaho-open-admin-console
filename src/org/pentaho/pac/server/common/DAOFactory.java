package org.pentaho.pac.server.common;

import org.pentaho.pac.server.IUserRoleDAO;
import org.pentaho.pac.server.UserRoleHibernateDAO;
import org.pentaho.pac.server.datasources.DataSourceHibernateDAO;
import org.pentaho.pac.server.datasources.IDataSourceDAO;
public class DAOFactory {
  public static IUserRoleDAO getUserRoleDAO() {
    return new UserRoleHibernateDAO();
  }
  public static IDataSourceDAO getDataSourceDAO() {
    return new DataSourceHibernateDAO();
  }
}
