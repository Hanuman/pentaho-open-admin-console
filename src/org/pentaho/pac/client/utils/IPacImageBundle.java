/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
  
  @Resource( "org/pentaho/pac/public/style/images/serverAlive.png" )
  public AbstractImagePrototype serverAliveIcon();
  
  @Resource( "org/pentaho/pac/public/style/images/serverDead.png" )
  public AbstractImagePrototype serverDeadIcon();
    
}
