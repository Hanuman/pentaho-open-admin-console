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

package org.pentaho.pac.client.scheduler.view;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SchedulerPanel extends VerticalPanel {

  private SchedulerToolbar schedulerToolbar = null;
  private SchedulesListCtrl schedulesListCtrl = null;
  private boolean isInitialized = false;
  //private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

  public SchedulerPanel()
  {
    this.setStyleName( "schedulerPanel" ); //$NON-NLS-1$
  }
  
  public void init() {
    if ( !isInitialized ) {
      createUI();
      isInitialized = true;
    }
  }
  
  private void createUI()
  {
    setVerticalAlignment( HasVerticalAlignment.ALIGN_TOP );
    setHorizontalAlignment( HasHorizontalAlignment.ALIGN_LEFT );

    schedulerToolbar = new SchedulerToolbar();
    add( schedulerToolbar );
    
    schedulesListCtrl = new SchedulesListCtrl();
    add( schedulesListCtrl );

//    setCellHeight(schedulerToolbar, "10px" );
//    setCellHeight(schedulesListCtrl, "100%" );
  }
  
  public SchedulerToolbar getSchedulerToolbar() {
    return schedulerToolbar;
  }
  
  public SchedulesListCtrl getSchedulesListCtrl() {
    return schedulesListCtrl;
  }
}
