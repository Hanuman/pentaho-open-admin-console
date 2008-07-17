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

import com.google.gwt.user.client.ui.ButtonBase;

public class SchedulerToolbarController {
  
  private SchedulerToolbar schedulerToolbar;
  private SchedulesListCtrl schedulesListCtrl;
  
  public SchedulerToolbarController( SchedulerToolbar schedulerToolbar, SchedulesListCtrl schedulesListCtrl ) {
    this.schedulerToolbar = schedulerToolbar;
    this.schedulesListCtrl = schedulesListCtrl;
  }
  
  public void enableTools() {
    int numSelectedItems = schedulesListCtrl.getNumSelections();

    enableWidget( schedulerToolbar.getCreateBtn(), true );
    enableWidget( schedulerToolbar.getUpdateBtn(), 1 == numSelectedItems );
    enableWidget( schedulerToolbar.getDeleteBtn(), numSelectedItems > 0 );
    enableWidget( schedulerToolbar.getSuspendBtn(),  numSelectedItems > 0 );
    enableWidget( schedulerToolbar.getResumeBtn(), numSelectedItems > 0 );
    enableWidget( schedulerToolbar.getRunNowBtn(), numSelectedItems > 0 );
    enableWidget( schedulerToolbar.getSuspendSchedulerBtn(), false );
    enableWidget( schedulerToolbar.getResumeSchedulerBtn(), false );
    enableWidget( schedulerToolbar.getRefreshBtn(), true );
  }
  
  private static void enableWidget( ButtonBase btn, boolean isEnabled ) {
    btn.setEnabled( isEnabled );
    if ( isEnabled ) {
      btn.removeStyleDependentName( "disabled" );
    } else {
      btn.addStyleDependentName( "disabled" );
    }
  }
}
