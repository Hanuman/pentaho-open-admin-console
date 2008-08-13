/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.client.utils;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * Usage:
 * IPacImageBundle b = (IPacImageBundle)GWT.create(IPacImageBundle.class);
 * 
 * @author Steven Barkdull
 *
 */
public interface IPacImageBundle extends ImageBundle {

  @Resource( "org/pentaho/pac/public/style/images/help.png" )
  public AbstractImagePrototype helpIcon();

  @Resource( "org/pentaho/pac/public/style/images/refresh.png" )
  public AbstractImagePrototype refreshIcon();

  @Resource( "org/pentaho/pac/public/style/images/status_working.png" )
  public AbstractImagePrototype statusWorkingIcon();
  
  @Resource( "org/pentaho/pac/public/style/images/close.png" )
  public AbstractImagePrototype closeIcon();
}
