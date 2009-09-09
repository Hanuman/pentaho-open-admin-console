/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.TableListCtrl;
import org.pentaho.pac.client.common.ui.dialog.BasicDialog;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;
import org.pentaho.pac.client.scheduler.view.ActionSequencePickerDialog;
import org.pentaho.pac.client.scheduler.view.ScheduleCreatorDialog;
import org.pentaho.pac.client.scheduler.view.SchedulerPanel;
import org.pentaho.pac.client.scheduler.view.SchedulerToolbar;
import org.pentaho.pac.client.scheduler.view.SchedulesListCtrl;
import org.pentaho.pac.client.scheduler.view.SolutionRepositoryActionSequenceListEditor;


public class SchedulerController {

  private SchedulerPanel schedulerPanel = null; // this is the view
  private ScheduleCreatorDialog scheduleCreatorDialog = null;

  // sub-controllers
  private SchedulesListController schedulesListController = null;
  private SolutionRepositoryActionSequenceListEditorController solRepActionSequenceEditorController = null;
  private SchedulerToolbarController schedulerToolbarController = null;
  
  private boolean isInitialized = false;
  
  public SchedulerController( SchedulerPanel schedulerPanel ) {
    assert (null != schedulerPanel ) : "schedulerPanel cannot be null."; //$NON-NLS-1$
    
    this.schedulerPanel = schedulerPanel;
  }
  
  // TODO sbarkdull, refactor creation and init of controllers
  public void init() {
    
    if ( !isInitialized ) {
      schedulerPanel.init();
      
      // init dialog
      this.scheduleCreatorDialog = new ScheduleCreatorDialog();
      this.scheduleCreatorDialog.setPanelSize("475px", "465px"); //$NON-NLS-1$ //$NON-NLS-2$
      this.scheduleCreatorDialog.setOnCancelHandler( new ICallback<BasicDialog>() {
        public void onHandle(BasicDialog dlg) {
          clearScheduleEditorValidationMsgs();
          scheduleCreatorDialog.hide();
        }
      });
      scheduleCreatorDialog.getScheduleEditor().setSubscriptionSchedule( true );
      
      // init list control
      SchedulesListCtrl listCtrl = schedulerPanel.getSchedulesListCtrl();
      schedulesListController = new SchedulesListController( listCtrl );
      listCtrl.setOnSelectHandler( new ICallback<TableListCtrl<Schedule>>() {
        public void onHandle(TableListCtrl<Schedule> pListCtrl) {
          schedulerToolbarController.enableTools();
        }
      });
      // TODO sbarkdull, schedulesListController.init( listCtrl, schedulesModel );
      
      listCtrl.setOnSelectAllHandler( new ICallback<TableListCtrl<Schedule>>() {
        public void onHandle(TableListCtrl<Schedule> pListCtrl) {
          schedulerToolbarController.enableTools();
        }
      });
      
      // init item picker
      SolutionRepositoryActionSequenceListEditor picker = scheduleCreatorDialog.getSolutionRepositoryActionSequenceEditor();
      ActionSequencePickerDialog actionSequencePickerDialog = new ActionSequencePickerDialog( Messages.getString("selectTitleBarLabel") ); //$NON-NLS-1$
      solRepActionSequenceEditorController = new SolutionRepositoryActionSequenceListEditorController( picker,
          actionSequencePickerDialog );
      
      // init toolbar
      SchedulerToolbar schedulerToolbar = schedulerPanel.getSchedulerToolbar();
      schedulerToolbarController = new SchedulerToolbarController( scheduleCreatorDialog, schedulerToolbar, listCtrl );
      schedulerToolbarController.init( schedulesListController, solRepActionSequenceEditorController );
      
      isInitialized = true;
    } // end isInitialized
  }
  
  /**
   * NOTE: code in this method must stay in sync with isScheduleEditorValid(), i.e. all error msgs
   * that may be set in isScheduleEditorValid(), must be cleared here.
   */
  private void clearScheduleEditorValidationMsgs() {
    
    scheduleCreatorDialog.clearTabError();
    
    ScheduleEditor schedEd = scheduleCreatorDialog.getScheduleEditor();
    SolutionRepositoryActionSequenceListEditor solRepPicker = scheduleCreatorDialog.getSolutionRepositoryActionSequenceEditor();
    
    SchedulesModel schedulesModel = schedulerToolbarController.getSchedulesModel();
    ScheduleEditorValidator schedEdValidator = new ScheduleEditorValidator( 
        schedEd, schedulesModel );
    schedEdValidator.clear();
    
    SolutionRepositoryActionSequenceListEditorValidator solRepValidator = new SolutionRepositoryActionSequenceListEditorValidator( solRepPicker,
        scheduleCreatorDialog.getScheduleEditor().isSubscriptionSchedule() );
    solRepValidator.clear();
    
  }
}
