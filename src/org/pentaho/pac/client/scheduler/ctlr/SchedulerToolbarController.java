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
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.scheduler.view.SchedulerToolbar;
import org.pentaho.pac.client.scheduler.view.SchedulesListCtrl;

public class SchedulerToolbarController {
  
  private SchedulerToolbar schedulerToolbar;
  private SchedulesListCtrl schedulesListCtrl;
  
  public SchedulerToolbarController( SchedulerToolbar schedulerToolbar, SchedulesListCtrl schedulesListCtrl ) {
    this.schedulerToolbar = schedulerToolbar;
    this.schedulesListCtrl = schedulesListCtrl;
  }
  
  public void enableTools() {
    int numSelectedItems = schedulesListCtrl.getNumSelections();
    
    schedulerToolbar.getCreateBtn().setEnabled( true );
    schedulerToolbar.getDeleteBtn().setEnabled( numSelectedItems > 0 );
    schedulerToolbar.getRefreshBtn().setEnabled( true );
    schedulerToolbar.getResumeBtn().setEnabled( numSelectedItems > 0 );
    schedulerToolbar.getSuspendBtn().setEnabled( numSelectedItems > 0);
    schedulerToolbar.getUpdateBtn().setEnabled( 1 == numSelectedItems );
    schedulerToolbar.getToggleResumePauseAllBtn().setEnabled( true );
  }
}
