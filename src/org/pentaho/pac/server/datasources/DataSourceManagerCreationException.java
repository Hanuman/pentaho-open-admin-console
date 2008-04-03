/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Mar 28 2008 
 * @author Steven Barkdull
 * 
 */
package org.pentaho.pac.server.datasources;

import org.pentaho.pac.client.CheckedException;



/**
 * Throw instance of this class when client code is unable to connect to
 * the service that provides the data source persistence mechanism
 * (e.g. a Tomcat server).
 * 
 * @author Steven Barkdull
 *
 */
public class DataSourceManagerCreationException extends CheckedException
{
  private static final long serialVersionUID = 69L;

  public DataSourceManagerCreationException()
  {
    super();
  }

  public DataSourceManagerCreationException(String message)
  {
    super(message);
  }

  public DataSourceManagerCreationException(Throwable cause)
  {
    super(cause);
  }

  public DataSourceManagerCreationException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
