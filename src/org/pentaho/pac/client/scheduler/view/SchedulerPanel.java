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

package org.pentaho.pac.client.scheduler.view;

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
    schedulerToolbar = new SchedulerToolbar();
    add( schedulerToolbar );
    
    schedulesListCtrl = new SchedulesListCtrl();
    add( schedulesListCtrl );
  }
  
  public SchedulerToolbar getSchedulerToolbar() {
    return schedulerToolbar;
  }
  
  public SchedulesListCtrl getSchedulesListCtrl() {
    return schedulesListCtrl;
  }
}
