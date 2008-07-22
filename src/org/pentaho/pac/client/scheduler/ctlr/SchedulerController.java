/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.TableListCtrl;
import org.pentaho.pac.client.common.ui.dialog.BasicDialog;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;
import org.pentaho.pac.client.scheduler.view.ScheduleCreatorDialog;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor;
import org.pentaho.pac.client.scheduler.view.SchedulerPanel;
import org.pentaho.pac.client.scheduler.view.SchedulerToolbar;
import org.pentaho.pac.client.scheduler.view.SchedulesListCtrl;
import org.pentaho.pac.client.scheduler.view.SolutionRepositoryItemPicker;


public class SchedulerController {

  private SchedulerPanel schedulerPanel = null; // this is the view
  private SchedulesModel schedulesModel = null;   // this is the model

  private SchedulesListController schedulesListController = null;
  private SchedulerToolbarController schedulerToolbarController = null;
  private ScheduleCreatorDialog scheduleCreatorDialog = null;
  
  public SchedulerController( SchedulerPanel schedulerPanel ) {
    assert (null != schedulerPanel ) : "schedulerPanel cannot be null."; //$NON-NLS-1$
    
    this.schedulerPanel = schedulerPanel;
    this.scheduleCreatorDialog = new ScheduleCreatorDialog();
    this.scheduleCreatorDialog.setOnCancelHandler( new ICallback<BasicDialog>() {
      public void onHandle(BasicDialog dlg) {
        clearScheduleEditorValidationMsgs();
        scheduleCreatorDialog.hide();
      }
    });
  }
  
  public void init() {
    
    if ( !isInitialized() ) {
      schedulerPanel.init();
      SchedulerToolbar schedulerToolbar = schedulerPanel.getSchedulerToolbar();
      SchedulesListCtrl listCtrl = schedulerPanel.getSchedulesListCtrl();
      schedulesListController = new SchedulesListController( listCtrl );
      schedulerToolbarController = new SchedulerToolbarController( schedulerToolbar, listCtrl );
      schedulerToolbarController.init( schedulesModel, scheduleCreatorDialog, schedulesListController );
      
      listCtrl.setOnSelectHandler( new ICallback<TableListCtrl<Schedule>>() {
        public void onHandle(TableListCtrl<Schedule> pListCtrl) {
          schedulerToolbarController.enableTools();
        }
      });
      
      listCtrl.setOnSelectAllHandler( new ICallback<TableListCtrl<Schedule>>() {
        public void onHandle(TableListCtrl<Schedule> pListCtrl) {
          schedulerToolbarController.enableTools();
        }
      });
    } // end isInitialized
  }
  
  private boolean isInitialized() {
    return null != schedulesModel;
  }
  
  /**
   * NOTE: code in this method must stay in sync with isScheduleEditorValid(), i.e. all error msgs
   * that may be set in isScheduleEditorValid(), must be cleared here.
   */
  private void clearScheduleEditorValidationMsgs() {
    
    scheduleCreatorDialog.clearTabError();
    
    ScheduleEditor schedEd = scheduleCreatorDialog.getScheduleEditor();
    SolutionRepositoryItemPicker solRepPicker = scheduleCreatorDialog.getSolutionRepositoryItemPicker();
    
    ScheduleEditorValidator schedEdValidator = new ScheduleEditorValidator( schedEd, schedulesModel );
    schedEdValidator.clear();
    
    SolutionRepositoryItemPickerValidator solRepValidator = new SolutionRepositoryItemPickerValidator( solRepPicker );
    solRepValidator.clear();
    
  }
}
