package org.pentaho.pac.server.common;

import static org.pentaho.pac.server.common.HibernateSessionFactory.DEFAULT_CONFIG_NAME;

import org.hibernate.SessionFactory;
import org.pentaho.pac.server.datasources.DataSourceHibernateDAO;
import org.pentaho.pac.server.datasources.IDataSourceDAO;
import org.pentaho.platform.engine.security.userroledao.IUserRoleDao;
import org.pentaho.platform.engine.security.userroledao.hibernate.HibernateUserRoleDao;
import org.pentaho.platform.engine.security.userroledao.hibernate.UserRoleDaoTransactionDecorator;
import org.pentaho.platform.engine.security.userroledao.hibernate.sample.SampleUsersAndRolesInitHandler;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * TODO mlowery This is kind of a useless factory, since the classes it returns are hard-coded.
 */
public class DAOFactory {
  public static IUserRoleDao getUserRoleDAO() {
    SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory(DEFAULT_CONFIG_NAME);
    HibernateUserRoleDao userRoleDao = new HibernateUserRoleDao();
    userRoleDao.setSessionFactory(sessionFactory);

    UserRoleDaoTransactionDecorator txnDao = new UserRoleDaoTransactionDecorator();
    txnDao.setUserRoleDao(userRoleDao);
    TransactionTemplate txnTemplate = new TransactionTemplate();
    txnTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    txnTemplate.setTransactionManager(new HibernateTransactionManager(sessionFactory));
    txnDao.setTransactionTemplate(txnTemplate);

    SampleUsersAndRolesInitHandler initHandler = new SampleUsersAndRolesInitHandler();
    initHandler.setSessionFactory(sessionFactory);
    initHandler.setUserRoleDao(txnDao);

    userRoleDao.setInitHandler(initHandler);
    userRoleDao.init();
    return txnDao;
  }

  public static IDataSourceDAO getDataSourceDAO() {
    return new DataSourceHibernateDAO();
  }
}
