package org.pentaho.pac.common.datasources;

import org.pentaho.pac.common.CheckedException;

public class DataSourceManagementException extends CheckedException
{

	private static final long serialVersionUID = 1L;

	public DataSourceManagementException()
	{
	  super();
	}

	public DataSourceManagementException(String message)
	{
		super(message);
	}

	public DataSourceManagementException(Throwable cause)
	{
		super(cause);
	}

	public DataSourceManagementException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
