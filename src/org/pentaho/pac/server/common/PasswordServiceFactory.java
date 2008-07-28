/*
 * Copyright 2008 Pentaho Corporation.  All rights reserved. 
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
 * Created April 7th, 2008 
 * @author Will Gorman (wgorman@pentaho.com)
 */
package org.pentaho.pac.server.common;

import org.pentaho.platform.api.util.IPasswordService;

public class PasswordServiceFactory {
  
  private static final String DEFAULT_IMPL = "org.pentaho.platform.util.Base64PasswordService";  //$NON-NLS-1$
  private static IPasswordService currentService;

  static {
    init(DEFAULT_IMPL);
  }
  
  public synchronized static void init(String classname) {
    try {
      currentService = (IPasswordService)Class.forName(classname).newInstance();
    } catch (Exception e) {
      // wrap this as a runtime exception.  This type of error is configuration related
      throw new RuntimeException(e);
    }
  }
  
  /**
   * returns the current implementation of IPasswordService
   *
   * @return datasource service
   * 
   * @throws RuntimeException if class cannot be instantiated
   */
  public static IPasswordService getPasswordService() {
    return currentService;
  }
}
