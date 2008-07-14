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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pentaho.pac.client.ISchedulerServiceAsync;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.IResponseCallback;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.CronParseException;
import org.pentaho.pac.client.scheduler.ScheduleEditorValidator;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;
import org.pentaho.pac.client.scheduler.view.ScheduleCreatorDialog;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor;
import org.pentaho.pac.client.scheduler.view.SchedulerPanel;
import org.pentaho.pac.client.scheduler.view.SchedulerToolbar;
import org.pentaho.pac.client.scheduler.view.SchedulesListCtrl;
import org.pentaho.pac.client.scheduler.view.SolutionRepositoryItemPicker;
import org.pentaho.pac.client.scheduler.view.SolutionRepositoryItemPickerValidator;
import org.pentaho.pac.client.scheduler.view.ScheduleCreatorDialog.TabIndex;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor.ScheduleType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PushButton;


public class SchedulerController {

  private SchedulerPanel schedulerPanel = null; // this is the view
  private SchedulesModel schedulesModel = null;   // this is the model

  private SchedulesListController schedulesListController = null;
  private ScheduleCreatorDialog scheduleCreatorDialog = null;
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private static final int INVALID_SCROLL_POS = -1;
  
  public SchedulerController( SchedulerPanel schedulerPanel ) {
    assert (null != schedulerPanel ) : "schedulerPanel cannot be null.";
    
    this.schedulerPanel = schedulerPanel;
    this.scheduleCreatorDialog = new ScheduleCreatorDialog();
    this.scheduleCreatorDialog.setOnCancelHandler( new ICallback<Object>() {
      public void onHandle(Object o) {
        clearScheduleEditorValidationMsgs();
        scheduleCreatorDialog.hide();
      }
    });
    this.scheduleCreatorDialog.setOnValidateHandler( new IResponseCallback<MessageDialog, Boolean>() {
      public Boolean onHandle( MessageDialog schedDlg ) {
        return isScheduleCreatorDialogValid();
      }
    });
  }
  
  public void init() {
    
    if ( !isInitialized() ) {
      schedulerPanel.init();
      schedulesListController = new SchedulesListController(this.schedulerPanel.getSchedulesListCtrl() );
      loadJobsTable();
      SchedulerToolbar schedulerToolbar = schedulerPanel.getSchedulerToolbar();
      final SchedulerController localThis = this;
      
      schedulerToolbar.setOnCreateListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          localThis.handleCreateSchedule();
        }
      });
      
      schedulerToolbar.setOnUpdateListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          localThis.handleUpdateSchedule();
        }
      });

      schedulerToolbar.setOnDeleteListener( new ICallback<Object>() {
        public void onHandle(Object o) {
          localThis.handleDeleteSchedules();
        }
      });
      
      schedulerToolbar.setOnResumeListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          localThis.handleResumeSchedules();
        }
      });
      
      schedulerToolbar.setOnPauseListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          localThis.handlePauseSchedules();
        }
      });
      
      schedulerToolbar.setOnRefreshListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          loadJobsTable();
        }
      });
      
      // TODO sbarkdull, uh, ya, this needs some work
      schedulerToolbar.setOnToggleResumePauseAllListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          PushButton b = (PushButton)o;
          // TODO sbarkdull
          b.setText( "toggled" );
          
          b.setTitle( "yep" );
        }
      });
      
      schedulerToolbar.setOnFilterListChangeListener( new ICallback<Object>() { 
        public void onHandle(Object o) {
          updateSchedulesTable();
        }
      });
    } // end isInitialized
  }
  
  private boolean isInitialized() {
    return null != schedulesModel;
  }
  
  private void initFilterList() {
    
    Set<String> groupNames = new HashSet<String>();
    List<Schedule> scheduleList = schedulesModel.getScheduleList();
    for ( int ii=0; ii<scheduleList.size(); ++ii ) {
      Schedule s = scheduleList.get( ii );
      String groupName = s.getJobGroup();
      if ( !groupNames.contains( groupName ) ) {
        groupNames.add( groupName );
      }
    }
    schedulerPanel.getSchedulerToolbar().clearFilters();
    Iterator<String> it = groupNames.iterator();
    
    schedulerPanel.getSchedulerToolbar().addFilterItem( SchedulerToolbar.ALL_FILTER );
    while ( it.hasNext() ) {
      String name = it.next();
      schedulerPanel.getSchedulerToolbar().addFilterItem(name );
    }
  }
  
  private List<Schedule> getFilteredSchedulesList() {
    List<Schedule> filteredList = null;
    String filterVal = schedulerPanel.getSchedulerToolbar().getFilterValue();
    if ( SchedulerToolbar.ALL_FILTER.equals( filterVal ) ) {
      filteredList = schedulesModel.getScheduleList();
    } else {
      filteredList = new ArrayList<Schedule>();
      List<Schedule> scheduleList = schedulesModel.getScheduleList();
      for ( int ii=0; ii<scheduleList.size(); ++ii ) {
        Schedule s = scheduleList.get( ii );
        if ( filterVal.equals( s.getJobGroup() ) ) {
          filteredList.add( s );
        }
      }
    }
    return filteredList;
  }
  
  private void updateSchedulesTable() {
    schedulesListController.updateSchedulesTable( getFilteredSchedulesList() );
  }
  
  private void loadJobsTable()
  {
    // TODO sbarkdull does this belong in SchedulesListCtrller?
    SchedulesListCtrl schedListCtrl = schedulerPanel.getSchedulesListCtrl();
    schedListCtrl.setStateToLoading();
    final int currScrollPos = schedListCtrl.getScrollPosition();
    final Map<String,Schedule> schedulesMap = new HashMap<String,Schedule>();
    
    AsyncCallback<Map<String,Schedule>> schedulerServiceCallback = new AsyncCallback<Map<String,Schedule>>() {
      public void onSuccess( Map<String,Schedule> pSchedulesMap ) {
        schedulesMap.putAll( pSchedulesMap );
      }

      public void onFailure(Throwable caught) {
        SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
        schedulesListCtrl.clearStateLoading();
        schedulesListCtrl.setTempMessage( MSGS.noSchedules() );
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    }; // end schedulerServiceCallback
    
    AsyncCallback<Map<String,Schedule>> subscriptionServiceCallback = new AsyncCallback<Map<String,Schedule>>() {
      public void onSuccess( Map<String,Schedule> subscriptionSchedulesMap ) {
        List<Schedule> schedulesList = mergeSchedules( schedulesMap, subscriptionSchedulesMap );
        schedulesModel = new SchedulesModel();
        schedulesModel.add( schedulesList );
        initFilterList();
        schedulerPanel.getSchedulesListCtrl().clearStateLoading();
        updateSchedulesTable();
        if ( INVALID_SCROLL_POS != currScrollPos ) { 
          schedulerPanel.getSchedulesListCtrl().setScrollPosition( currScrollPos );
        }
      }

      public void onFailure(Throwable caught) {
        SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
        schedulesListCtrl.clearStateLoading();
        schedulesListCtrl.setTempMessage( MSGS.noSchedules() );
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    }; // end subscriptionServiceCallback
      
      
    PacServiceFactory.getSchedulerService().getJobNames( schedulerServiceCallback );
    PacServiceFactory.getSubscriptionService().getJobNames( subscriptionServiceCallback );
  }

  /**
   * Merge the two maps into one map. Add all key-values in the schedulerMap to
   * the mergedMap, unless the key is in both the schedulerMap and subscriptionMap.
   * If it is in both maps, add the one from the subscriptionMap.
   * NOTE: all elements in subscriptionMap should be in the schedulerMap, but not the
   * inverse.
   * 
   * @param schedulerMap
   * @param subscriptionMap
   * @return
   */
  private static List<Schedule> mergeSchedules( Map<String,Schedule> schedulerMap, 
      Map<String,Schedule> subscriptionMap ) {
    
    List<Schedule> mergedList = new ArrayList<Schedule>();
    for ( Map.Entry<String,Schedule> me : schedulerMap.entrySet() ) {
      
      Schedule subscriptionSchedule = subscriptionMap.get( me.getKey() );
      mergedList.add( 
          ( null != subscriptionSchedule )
          ? subscriptionSchedule
          : me.getValue() );
    }
    return mergedList;
  }
  
  /**
   * 
   */
  @SuppressWarnings("fallthrough")
  private void updateSchedule() {

    SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
    final List<Schedule> scheduleList = schedulesListCtrl.getSelectedSchedules();
    Schedule oldSchedule = scheduleList.get( 0 );
    
    // TODO, List<Schedule> is probably not what we will get back
    AsyncCallback<List<Schedule>> responseCallback = new AsyncCallback<List<Schedule>>() {
      public void onSuccess( List<Schedule> pSchedulesList ) {
        MessageDialog messageDialog = new MessageDialog( "Kool!", 
            "Success, I guess!" );
        messageDialog.center();
        scheduleCreatorDialog.hide();
        loadJobsTable();
      }

      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    };
    // TODO sbarkdull scheduleCreatorDialog -> scheduleEditorDialog
    ScheduleEditor scheduleEditor = scheduleCreatorDialog.getScheduleEditor();

    ISchedulerServiceAsync schedSvc = oldSchedule.isSubscriptionSchedule()  
      ? PacServiceFactory.getSubscriptionService()
      : PacServiceFactory.getSchedulerService();
    
    String cronStr = scheduleEditor.getCronString();
    Date startDate = scheduleEditor.getStartDate();
    Date endDate = scheduleEditor.getEndDate();
    ScheduleType rt = scheduleEditor.getScheduleType(); 
    switch ( rt ) {
      case RUN_ONCE:
        schedSvc.updateRepeatSchedule(
            oldSchedule.getJobName(),
            oldSchedule.getJobGroup(),
            oldSchedule.getSchedId(),
            scheduleEditor.getName().trim(), 
            scheduleEditor.getGroupName().trim(), 
            scheduleEditor.getDescription().trim(), 
            startDate,
            endDate,
            "0" /*repeat count*/,
            "0" /*repeat time*/, 
            scheduleCreatorDialog.getSolutionRepositoryItemPicker().getActionsAsString().trim(),
            responseCallback
          );
        break;
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        if ( null == cronStr ) {
          String repeatInterval = Integer.toString( TimeUtil.secsToMillisecs( 
                scheduleEditor.getRepeatInSecs() ) );
          schedSvc.updateRepeatSchedule(
              oldSchedule.getJobName(),
              oldSchedule.getJobGroup(),
              oldSchedule.getSchedId(),
              scheduleEditor.getName().trim(), 
              scheduleEditor.getGroupName().trim(), 
              scheduleEditor.getDescription().trim(), 
              startDate,
              endDate,
              null /*repeat count*/,
              repeatInterval.trim(), 
              scheduleCreatorDialog.getSolutionRepositoryItemPicker().getActionsAsString().trim(),
              responseCallback
            );
          break;
        } else {
          // fall through to case CRON
        }
      case CRON:
        schedSvc.updateCronSchedule(
            oldSchedule.getJobName(),
            oldSchedule.getJobGroup(),
            oldSchedule.getSchedId(),
            scheduleEditor.getName().trim(), 
            scheduleEditor.getGroupName().trim(), 
            scheduleEditor.getDescription().trim(), 
            startDate,
            endDate,
            cronStr.trim(), 
            scheduleCreatorDialog.getSolutionRepositoryItemPicker().getActionsAsString().trim(),
            responseCallback
          );
        break;
      default:
        throw new RuntimeException( "Invalid Run Type: " + rt.toString() );
    }
  }
  
  /**
   * NOTE: this method is extremely similar to updateSchedule, when modifying this method,
   * consider modifying updateSchedule in a similar way.
   */
  @SuppressWarnings("fallthrough")
  private void createSchedule() {
    // TODO, List<Schedule> is probably not what we will get back
    AsyncCallback<List<Schedule>> responseCallback = new AsyncCallback<List<Schedule>>() {
      public void onSuccess( List<Schedule> pSchedulesList ) {
        MessageDialog messageDialog = new MessageDialog( "Kool!", 
            "Success, I guess!" );
        messageDialog.center();
        scheduleCreatorDialog.hide();
        loadJobsTable();
      }

      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    };
    // TODO sbarkdull scheduleCreatorDialog -> scheduleEditorDialog
    ScheduleEditor scheduleEditor = scheduleCreatorDialog.getScheduleEditor();

    String cronStr = scheduleEditor.getCronString();
    Date startDate = scheduleEditor.getStartDate();
    Date endDate = scheduleEditor.getEndDate();
    ScheduleType rt = scheduleEditor.getScheduleType();

    // TODO sbarkdull, if we want to support creation of scheduler schedules, we need to supply
 // a UI mechanism like a checkbox to allow user to identify scheduler vs subscription, 
 // and then test the value of the check box instead of the following "true".
    ISchedulerServiceAsync schedSvc = true  
      ? PacServiceFactory.getSubscriptionService()
      : PacServiceFactory.getSchedulerService();
    
    switch ( rt ) {
      case RUN_ONCE:
        schedSvc.createRepeatSchedule(
            scheduleEditor.getName().trim(), 
            scheduleEditor.getGroupName().trim(), 
            scheduleEditor.getDescription().trim(), 
            startDate,
            endDate,
            "0" /*repeat count*/, //$NON-NLS-1$
            "0" /*repeat time*/,  //$NON-NLS-1$
            scheduleCreatorDialog.getSolutionRepositoryItemPicker().getActionsAsString().trim(),
            responseCallback
          );
        break;
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        if ( null == cronStr ) {
          String repeatInterval = Integer.toString( TimeUtil.secsToMillisecs( 
                scheduleEditor.getRepeatInSecs() ) );
          schedSvc.createRepeatSchedule(
              scheduleEditor.getName().trim(), 
              scheduleEditor.getGroupName().trim(), 
              scheduleEditor.getDescription().trim(), 
              startDate,
              endDate,
              null /*repeat count*/,
              repeatInterval.trim(), 
              scheduleCreatorDialog.getSolutionRepositoryItemPicker().getActionsAsString().trim(),
              responseCallback
            );
          break;
        } else {
          // fall through to case CRON
        }
      case CRON:
        schedSvc.createCronSchedule(
            scheduleEditor.getName().trim(), 
            scheduleEditor.getGroupName().trim(), 
            scheduleEditor.getDescription().trim(), 
            startDate,
            endDate,
            cronStr.trim(), 
            scheduleCreatorDialog.getSolutionRepositoryItemPicker().getActionsAsString().trim(),
            responseCallback
          );
        break;
      default:
        throw new RuntimeException( "Invalid Run Type: " + rt.toString() );
    }
  }
  
  private void deleteSelectedSchedules() {
   
    SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
    final List<Schedule> scheduleList = schedulesListCtrl.getSelectedSchedules();
    
    AsyncCallback<Object> callback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        loadJobsTable();
      }
      public void onFailure(Throwable caught) {
        // TODO sbarkdull
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    };
    PacServiceFactory.getSchedulerService().deleteJobs( scheduleList, callback );
    
  }

  private void handleCreateSchedule() {
    final SchedulerController localThis = this;
    
    scheduleCreatorDialog.setTitle( "Schedule Creator" );
    scheduleCreatorDialog.reset( new Date() );
    scheduleCreatorDialog.setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle(MessageDialog d) {
        localThis.createSchedule();
      }
    });
    // TODO sbarkdull, if we decide to create regular schedules, we'll need to do something different here
    scheduleCreatorDialog.getSolutionRepositoryItemPicker().setSingleSelect( false );
    scheduleCreatorDialog.center();
  }
  
  private void handleUpdateSchedule() {
    final SchedulerController localThis = this;

    scheduleCreatorDialog.setTitle( "Schedule Editor" );
    SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
    final List<Schedule> scheduleList = schedulesListCtrl.getSelectedSchedules();
    scheduleCreatorDialog.setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle(MessageDialog d) {
        localThis.updateSchedule();
      }
    });
    // the update button should be enabled/disabled to guarantee that one and only one schedule is selected
    assert scheduleList.size() == 1 : "When clicking update, exactly one schedule should be selected.";
    
    Schedule sched = scheduleList.get( 0 );
    scheduleCreatorDialog.getSolutionRepositoryItemPicker().setSingleSelect( !sched.isSubscriptionSchedule() );
    try {
      initScheduleCreatorDialog( sched );
      scheduleCreatorDialog.center();
    } catch (CronParseException e) {
      final MessageDialog errorDialog = new MessageDialog( "Error",
          "Attempt to initialize the Recurrence Dialog with an invalid CRON string: "
          + sched.getCronString()
          + " Error details: "
          + e.getMessage() );
      errorDialog.setOnOkHandler( new ICallback() {
        public void onHandle(Object o) {
          errorDialog.hide();
          scheduleCreatorDialog.center();
        }
      });
      errorDialog.center();
    }
  }
  
  /**
   * initialize the <code>scheduleEditor</code>'s user interface with 
   * the contents of the <code>sched</code>.
   * 
   * @param scheduleEditor
   * @param sched
   * @throws CronParseException if sched has a non-empty CRON string, and the CRON string is not valid.
   */
  private void initScheduleCreatorDialog( Schedule sched ) throws CronParseException {

    scheduleCreatorDialog.reset( new Date() );
    ScheduleEditor scheduleEditor = scheduleCreatorDialog.getScheduleEditor();
    
    scheduleEditor.setName( sched.getJobName() );
    scheduleEditor.setGroupName( sched.getJobGroup() );
    scheduleEditor.setDescription( sched.getDescription() );
    String cronStr = sched.getCronString();
    
    SolutionRepositoryItemPicker solRepPicker = scheduleCreatorDialog.getSolutionRepositoryItemPicker();
    scheduleCreatorDialog.getSolutionRepositoryItemPicker().setActionsAsList( sched.getActionsList() );
    
    String repeatInMillisecs;
    if ( null != cronStr ) {
      scheduleEditor.setCronString( sched.getCronString() );  // throws CronParseException
    } else if ( null != ( repeatInMillisecs = sched.getRepeatInterval() ) ) {
      int repeatIntervalInSecs = TimeUtil.millsecondsToSecs( Integer.parseInt( repeatInMillisecs ) );
      if ( 0 == repeatIntervalInSecs ) {
        // run once
        scheduleEditor.setScheduleType( ScheduleType.RUN_ONCE );
      } else {
        // run multiple
        scheduleEditor.setRepeatInSecs( repeatIntervalInSecs );
      }
    } else {
      throw new RuntimeException( "Illegal state, must have either a cron string or a repeat time." );
    }

    String strDate = sched.getStartDate();
    if ( null != strDate ) {
      Date startDate = TimeUtil.getDate( strDate );
      scheduleEditor.setStartDate( startDate );
    }
    
    strDate = sched.getEndDate();
    if ( null != strDate ) {
      scheduleEditor.setEndBy();
      Date endDate = TimeUtil.getDate( strDate );
      scheduleEditor.setEndDate(endDate);
    } else {
      scheduleEditor.setNoEndDate();
    }
  }
  
  private void handleDeleteSchedules() {
    final SchedulerController localThis = this;
    final ConfirmDialog confirm = new ConfirmDialog( "Confirm Delete",
        "Are you sure you would like to delete all checked schedules?" );
    confirm.setOnOkHandler( new ICallback<MessageDialog>() {
      public void onHandle( MessageDialog d ) {
        confirm.hide();
        localThis.deleteSelectedSchedules();
      }
    });
    confirm.center();
  }
  
  private void handleResumeSchedules() {
    SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
    final List<Schedule> scheduleList = schedulesListCtrl.getSelectedSchedules();
    
    AsyncCallback<Object> callback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        loadJobsTable();
      }
      public void onFailure(Throwable caught) {
        // TODO sbarkdull
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    };
    PacServiceFactory.getSchedulerService().resumeJobs( scheduleList, callback );
  }
  
  private void handlePauseSchedules() {
    SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
    final List<Schedule> scheduleList = schedulesListCtrl.getSelectedSchedules();
    
    AsyncCallback<Object> callback = new AsyncCallback<Object>() {
      public void onSuccess(Object result) {
        loadJobsTable();
      }
      public void onFailure(Throwable caught) {
        // TODO sbarkdull
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    };
    PacServiceFactory.getSchedulerService().pauseJobs( scheduleList, callback );
  }

  
  /**
   * NOTE: code in this method must stay in sync with isScheduleEditorValid(), i.e. all error msgs
   * that may be cleared in clearScheduleEditorValidationMsgs(), must be set-able here.
   */
  private boolean isScheduleCreatorDialogValid() {

    boolean isValid = true;

    ScheduleEditor schedEd = scheduleCreatorDialog.getScheduleEditor();
    SolutionRepositoryItemPicker solRepPicker = scheduleCreatorDialog.getSolutionRepositoryItemPicker();
    
    ScheduleEditorValidator schedEdValidator = new ScheduleEditorValidator( schedEd );
    SolutionRepositoryItemPickerValidator solRepValidator = new SolutionRepositoryItemPickerValidator( solRepPicker );

    scheduleCreatorDialog.clearTabError();
    schedEdValidator.clear();
    solRepValidator.clear();
    
    /*
     * If a tab's controls have errors, change the tab's appearance so that
     * the tab-label displays in an error-color (red). If the current tab 
     * does not have an error, find the first tab that does have an error,
     * and set it to the current tab.
     */
    TabIndex firstTabWithError = null;
    boolean doesSelectedTabHaveError = false;
    TabIndex selectedIdx = scheduleCreatorDialog.getSelectedTab();
    
    if ( !schedEdValidator.isValid() ) {
      isValid = false ;
      scheduleCreatorDialog.setTabError( TabIndex.SCHEDULE );
      if ( null == firstTabWithError ) {
        firstTabWithError = TabIndex.SCHEDULE;
      }
      if ( TabIndex.SCHEDULE == selectedIdx ) {
        doesSelectedTabHaveError = true;
      }
    }    
    if ( !solRepValidator.isValid() ) {
      isValid = false ;
      scheduleCreatorDialog.setTabError( TabIndex.SCHEDULE_ACTION );
      if ( null == firstTabWithError ) {
        firstTabWithError = TabIndex.SCHEDULE_ACTION;
      }
      if ( TabIndex.SCHEDULE_ACTION == selectedIdx ) {
        doesSelectedTabHaveError = true;
      }
    }
    if ( false == doesSelectedTabHaveError && firstTabWithError != null ) {
      scheduleCreatorDialog.setSelectedTab( firstTabWithError );
    }
    
    return isValid;
  }
  
  /**
   * NOTE: code in this method must stay in sync with isScheduleEditorValid(), i.e. all error msgs
   * that may be set in isScheduleEditorValid(), must be cleared here.
   */
  private void clearScheduleEditorValidationMsgs() {
    
    scheduleCreatorDialog.clearTabError();
    
    ScheduleEditor schedEd = scheduleCreatorDialog.getScheduleEditor();
    SolutionRepositoryItemPicker solRepPicker = scheduleCreatorDialog.getSolutionRepositoryItemPicker();
    
    ScheduleEditorValidator schedEdValidator = new ScheduleEditorValidator( schedEd );
    schedEdValidator.clear();
    
    SolutionRepositoryItemPickerValidator solRepValidator = new SolutionRepositoryItemPickerValidator( solRepPicker );
    solRepValidator.clear();
    
  }
}
