/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.server.common;

import static org.pentaho.pac.server.common.HibernateSessionFactory.DEFAULT_CONFIG_NAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.pentaho.pac.server.datasources.DataSourceHibernateDAO;
import org.pentaho.pac.server.datasources.IDataSourceDAO;
import org.pentaho.pac.server.i18n.Messages;
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
  private static final Log logger = LogFactory.getLog(DAOFactory.class);
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
    try {
      userRoleDao.init();  
    } catch(Exception e) {
      logger.warn(Messages.getString("DAOFactory.WARN_0001_UNABLE_TO_INITIALIZE_USER_ROLE_DAO"), e); //$NON-NLS-1$
    }
    
    return txnDao;
  }

  public static IDataSourceDAO getDataSourceDAO() {
    return new DataSourceHibernateDAO();
  }
}
