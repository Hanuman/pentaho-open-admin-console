package org.pentaho.pac.server;

/*package private*/ class UserRoleDAOFactory {
  static IUserRoleDAO getDAO() {
    return new UserRoleHibernateDAO();
  }
}
