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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * Usage:
 * PacImageBundle b = (PacImageBundle)GWT.create(PacImageBundle.class);
 * @deprecated
 * @author Steven Barkdull
 *
 */
public interface PacImageBundle extends ImageBundle {
  
  /**
  * @gwt.resource org/pentaho/pac/public/style/images/minus_disabled.png
  */
 public AbstractImagePrototype minusDisabledIcon(); 
  /**
  * @gwt.resource org/pentaho/pac/public/style/images/minus.png
  */
 public AbstractImagePrototype minusIcon(); 
  /**
  * @gwt.resource org/pentaho/pac/public/style/images/plus.png
  */
 public AbstractImagePrototype plusIcon();
 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/users_over.png
  */
 public AbstractImagePrototype usersOverIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/users_selected.png
  */
 public AbstractImagePrototype usersSelectedIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/users_off.png
  */
 public AbstractImagePrototype usersOffIcon(); 
 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/roles_over.png
  */
 public AbstractImagePrototype rolesOverIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/roles_selected.png
  */
 public AbstractImagePrototype rolesSelectedIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/roles_off.png
  */
 public AbstractImagePrototype rolesOffIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/serverOn.gif
  */
 public AbstractImagePrototype serverOnIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/serverOff.gif
  */
 public AbstractImagePrototype serverOffIcon(); 
 
 
}
