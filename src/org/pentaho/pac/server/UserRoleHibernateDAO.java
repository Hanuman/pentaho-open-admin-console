package org.pentaho.pac.server;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.pentaho.pac.common.users.DuplicateUserException;
import org.pentaho.pac.common.users.NonExistingUserException;

/*package private*/ class UserRoleHibernateDAO implements IUserRoleDAO {

  public UserRoleHibernateDAO() {
  }
  
  public void createUser(IPentahoUser newUser) throws DuplicateUserException, DAOException {
    if (getUser(newUser.getName()) == null) {
      try {
        getSession().save(newUser);
      } catch (HibernateException ex) {
        getSession().evict(newUser);
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new DuplicateUserException(newUser.getName());
    }
  }

  public void deleteUser(IPentahoUser user) throws NonExistingUserException, DAOException {
    IPentahoUser tmpUser = getUser(user.getName());
    if (tmpUser != null) {
      try {
      	// the underlying code is modifying the tmpRole.getUsers() list, so make a
      	// temporary list to prevent concurrent modification exception.
        IPentahoRole[] roleArray = tmpUser.getRoles().toArray( new PentahoRole[ tmpUser.getRoles().size()]);
        
        for ( IPentahoRole role : roleArray ) {
          role.removeUser(tmpUser);
        }
        getSession().delete(tmpUser);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingUserException(user.getName());
    }
  }

  public IPentahoUser getUser(String name) throws DAOException {
    try {
      return (IPentahoUser) getSession().get(PentahoUser.class.getName(), name);
    } catch (HibernateException ex) {
      throw new DAOException(ex.getMessage(), ex);
    }
  }

  public List<IPentahoUser> getUsers() throws DAOException {
    try {
      String queryString = "from PentahoUser";
      Query queryObject = getSession().createQuery(queryString);
      return queryObject.list();
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void updateUser(IPentahoUser user) throws NonExistingUserException, DAOException {
    if (getUser(user.getName()) != null) {
      try {
        getSession().update(user);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingUserException(user.getName());
    }
  }

  public void createRole(IPentahoRole newRole) throws DuplicateRoleException, DAOException {
    if (getRole(newRole.getName()) == null) {
      try {
        getSession().save(newRole);
      } catch (HibernateException ex) {
        getSession().evict(newRole);
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new DuplicateRoleException(newRole.getName());
    }
  }

  public void deleteRole(IPentahoRole role) throws NonExistingRoleException, DAOException {
    IPentahoRole tmpRole = getRole(role.getName());
    if (tmpRole != null) {
      try {
      	// the underlying code is modifying the tmpRole.getUsers() list, so make a
      	// temporary list to prevent concurrent modification exception.
        IPentahoUser[] userArray = tmpRole.getUsers().toArray( new PentahoUser[ tmpRole.getUsers().size()]);
        
        for (IPentahoUser user : userArray ) {
          tmpRole.removeUser(user);
        }
        getSession().delete(tmpRole);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingRoleException (role.getName());
    }
  }

  public IPentahoRole getRole(String name) throws DAOException {
    try {
      return (IPentahoRole) getSession().get(PentahoRole.class.getName(), name);
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public List<IPentahoRole> getRoles() throws DAOException {
    try {
      String queryString = "from PentahoRole";
      Query queryObject = getSession().createQuery(queryString);
      return queryObject.list();
    } catch (HibernateException ex) {
      throw new DAOException( ex.getMessage(), ex );
    }
  }

  public void updateRole(IPentahoRole role) throws NonExistingRoleException, DAOException {
    if (getRole(role.getName()) != null) {
      try {
        getSession().update(role);
      } catch (HibernateException ex) {
        throw new DAOException( ex.getMessage(), ex );
      }
    } else {
      throw new NonExistingRoleException(role.getName());
    }
  }
  
  public Session getSession() {
    return HibernateSessionFactory.getSession();
  }
  
  public void beginTransaction() throws DAOException {
    try {
      getSession().beginTransaction(); 
    } catch ( JDBCConnectionException ex )
    {
      throw new DAOException( ex.getMessage(), ex );
    }
  }
  
  public void commitTransaction() throws DAOException {
    try {
      getSession().getTransaction().commit();
    } catch ( JDBCConnectionException ex )
    {
      throw new DAOException( ex.getMessage(), ex );
    }
  }
  
  public void rollbackTransaction() throws DAOException {
    try {
      getSession().getTransaction().rollback();
    } catch ( JDBCConnectionException ex )
    {
      throw new DAOException( ex.getMessage(), ex );
    }
  }
  
  public void closeSession() {
    HibernateSessionFactory.closeSession();
  }
}