package org.pentaho.pac.server.common;

/**
 * Data access interface for domain model
  */
public interface IGenericDAO {
	public void beginTransaction() throws DAOException;
	public void commitTransaction() throws DAOException;
	public void rollbackTransaction() throws DAOException;
	public void closeSession();
}