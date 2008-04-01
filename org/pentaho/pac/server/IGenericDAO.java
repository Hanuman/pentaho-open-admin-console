package org.pentaho.pac.server;

/**
 * Data access interface for domain model
  */
/*package private*/ interface IGenericDAO {
	public void beginTransaction() throws DAOException;
	public void commitTransaction() throws DAOException;
	public void rollbackTransaction() throws DAOException;
	public void closeSession();
}