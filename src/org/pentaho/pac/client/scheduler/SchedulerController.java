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
package org.pentaho.pac.client.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.RecurrenceEditor.RecurrenceEditorException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PushButton;

public class SchedulerController {

  private SchedulerPanel schedulerPanel = null; // this is the view
  private List<Schedule> schedulesList = null;   // this is the model
  
  private SchedulesListController schedulesListController = null;
  private ScheduleCreatorDialog scheduleCreatorDialog = null;
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private static final int INVALID_SCROLL_POS = -1;
  private static final String DEFAULT_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_GROUP_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_DESCRIPTION = ""; //$NON-NLS-1$
  private static final String DEFAULT_CRONSTRING = ""; //$NON-NLS-1$
  
  public SchedulerController( SchedulerPanel schedulerPanel ) {
    assert (null != schedulerPanel ) : "schedulerPanel cannot be null.";
    
    this.schedulerPanel = schedulerPanel;
    this.scheduleCreatorDialog = new ScheduleCreatorDialog();
  }
  
  public void init() {
    
    if ( !isInitialized() ) {
      schedulerPanel.init();
      schedulesListController = new SchedulesListController(this.schedulerPanel.getSchedulesListCtrl() );
      loadJobsTable();
      SchedulerToolbar schedulerToolbar = schedulerPanel.getSchedulerToolbar();
      final SchedulerController localThis = this;
      
      schedulerToolbar.setOnSelectAllListener( new ICallback() { 
        public void onHandle(Object o) {
          localThis.schedulerPanel.getSchedulesListCtrl().selectAll();
        }
      });
      
      schedulerToolbar.setOnUnselectAllListener( new ICallback() { 
        public void onHandle(Object o) {
          localThis.schedulerPanel.getSchedulesListCtrl().unselectAll();
        }
      });
      
      schedulerToolbar.setOnCreateListener( new ICallback() { 
        public void onHandle(Object o) {
          // initialize w/ default values
          scheduleCreatorDialog.getScheduleEditor().setName( DEFAULT_NAME );
          scheduleCreatorDialog.getScheduleEditor().setGroupName( DEFAULT_GROUP_NAME );
          scheduleCreatorDialog.getScheduleEditor().setDescription( DEFAULT_DESCRIPTION );
          scheduleCreatorDialog.getScheduleEditor().setCronString( DEFAULT_CRONSTRING );
          scheduleCreatorDialog.center();
        }
      });
      
      schedulerToolbar.setOnUpdateListener( new ICallback() { 
        public void onHandle(Object o) {
          // TODO sbarkdull, call setter methods on the dialog
          scheduleCreatorDialog.getScheduleEditor().setName( "x" );
          scheduleCreatorDialog.getScheduleEditor().setGroupName( "x" );
          scheduleCreatorDialog.getScheduleEditor().setDescription( "x" );
          scheduleCreatorDialog.getScheduleEditor().setCronString( "0 13 12 0/3 * ?" );
          scheduleCreatorDialog.center();
        }
      });

      
      ICallback onDeleteCallback = new ICallback() {
        public void onHandle(Object o) {
//          SchedulesListCtrl schedulesListCtrl = schedulerPanel.getSchedulesListCtrl();
//          List selectedIdxs = schedulesListCtrl.getSelectedIndexes();
//          
//          PacServiceFactory.getSchedulerService().deleteJob( String jobName, String jobGroup, AsyncCallback callback )
//          
//          // TODO sbarkdull, still need startdate and end date
//          PacServiceFactory.getSchedulerService().createJob( "my jobName", "my jobGroup", "my description",
//              cronString, "samples", "getting-started", "HelloWorld.xaction",
//              new AsyncCallback<Object>() {
//                public void onSuccess(Object result) {
//                  MessageDialog d = new MessageDialog( "done", "done");
//                  d.center();
//                  
//                  SchedulesListCtrl l = localThis.schedulerPanel.getSchedulesListCtrl();
//                  List selectedIdxs = l.getSelectedIndexes();
//                  for ( int ii=selectedIdxs.size()-1; ii>=0; --ii ) {
//                    int selectedIdx = ((Integer)selectedIdxs.get( ii )).intValue();
//                    l.remove( selectedIdx );
//                  }
//                  
//                  
//                  
//                }  
//                public void onFailure(Throwable caught) {
//                }
//          });  
        }
      };
      schedulerToolbar.setOnDeleteListener( onDeleteCallback );
      
      schedulerToolbar.setOnResumeListener( new ICallback() { 
        public void onHandle(Object o) {
          SchedulesListCtrl l = localThis.schedulerPanel.getSchedulesListCtrl();
          List selectedIdxs = l.getSelectedIndexes();
          for ( int ii=selectedIdxs.size()-1; ii>=0; --ii ) {
            int selectedIdx = ((Integer)selectedIdxs.get( ii )).intValue();
// what?
          }
        }
      });
      
      schedulerToolbar.setOnSuspendListener( new ICallback() { 
        public void onHandle(Object o) {
          SchedulesListCtrl l = localThis.schedulerPanel.getSchedulesListCtrl();
          List selectedIdxs = l.getSelectedIndexes();
          for ( int ii=selectedIdxs.size()-1; ii>=0; --ii ) {
            int selectedIdx = ((Integer)selectedIdxs.get( ii )).intValue();
// what?
          }
        }
      });
      
      schedulerToolbar.setOnRefreshListener( new ICallback() { 
        public void onHandle(Object o) {
        }
      });
      
      schedulerToolbar.setOnToggleResumeSuspendAllListener( new ICallback() { 
        public void onHandle(Object o) {
          PushButton b = (PushButton)o;
          // TODO sbarkdull
          b.setText( "toggled" );
          
          b.setTitle( "yep" );
        }
      });
      
      schedulerToolbar.setOnFilterListChangeListener( new ICallback() { 
        public void onHandle(Object o) {
          updateSchedulesTable();
        }
      });
    }

    final SchedulerController localThis = this;
    this.scheduleCreatorDialog.setOnOkHandler( new ICallback() {
      public void onHandle(Object o) {
        localThis.createSchedule();
      }
    });
  }
  
  private boolean isInitialized() {
    return null != schedulesList;
  }
  
  private void initFilterList() {
    
    Set groupNames = new HashSet();
    for ( int ii=0; ii<schedulesList.size(); ++ii ) {
      Schedule s = (Schedule) schedulesList.get( ii );
      String groupName = s.getJobGroup();
      if ( !groupNames.contains( groupName ) ) {
        groupNames.add( groupName );
      }
    }
    Iterator it = groupNames.iterator();
    while ( it.hasNext() ) {
      String name = (String)it.next();
      schedulerPanel.getSchedulerToolbar().addFilterItem(name );
    }
    
  }
  
  private List getFilteredSchedulesList() {
    List filteredList = null;
    String filterVal = schedulerPanel.getSchedulerToolbar().getFilterValue();
    if ( SchedulerToolbar.ALL_FILTER.equals( filterVal ) ) {
      filteredList = schedulesList;
    } else {
      filteredList = new ArrayList();
      for ( int ii=0; ii<schedulesList.size(); ++ii ) {
        Schedule s = (Schedule)schedulesList.get( ii );
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
    final int currScrollPos = schedulerPanel.getSchedulesListCtrl().getScrollPosition();
    
    PacServiceFactory.getSchedulerService().getJobNames(
        new AsyncCallback<List<Schedule>>() {
          public void onSuccess( List<Schedule> pSchedulesList ) {
            schedulesList = pSchedulesList;
            initFilterList();
            updateSchedulesTable();
            if ( INVALID_SCROLL_POS != currScrollPos ) { 
              schedulerPanel.getSchedulesListCtrl().setScrollPosition( currScrollPos );
            }
          }
    
          public void onFailure(Throwable caught) {
            MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
                caught.getMessage() );
            messageDialog.center();
          }
        }
      );
  }
  
  private void createSchedule() {
    // TODO, List<Schedule> is probably not what we will get back
    AsyncCallback<List<Schedule>> responseCallback = new AsyncCallback<List<Schedule>>() {
      public void onSuccess( List<Schedule> pSchedulesList ) {
        MessageDialog messageDialog = new MessageDialog( "Kool!", 
            "Success, I guess!" );
        messageDialog.center();
        scheduleCreatorDialog.hide();
      }

      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
            caught.getMessage() );
        messageDialog.center();
      }
    };
    
    String cronStr;
    try {
      cronStr = scheduleCreatorDialog.getScheduleEditor().getCronString();
    } catch( RecurrenceEditorException ex ) {
      MessageDialog d = new MessageDialog();
      d.setTitle( "Error" );
      d.setMessage( "Doh! " + ex.getMessage() );
      d.center();
      return;
    }
    if ( null != cronStr ) {
      PacServiceFactory.getSchedulerService().createCronJob(
          scheduleCreatorDialog.getScheduleEditor().getName().trim(), 
          scheduleCreatorDialog.getScheduleEditor().getGroupName().trim(), 
          scheduleCreatorDialog.getScheduleEditor().getDescription().trim(), 
          cronStr.trim(), 
          scheduleCreatorDialog.getSolutionRepositoryItemPicker().getSolution().trim(),
          scheduleCreatorDialog.getSolutionRepositoryItemPicker().getPath().trim(),
          scheduleCreatorDialog.getSolutionRepositoryItemPicker().getAction().trim(),
          responseCallback
        );
    } else {
      String repeatTimeMillisecs;
      try {
        repeatTimeMillisecs = Integer.toString( TimeUtil.secsToMillisecs( 
            Integer.parseInt( scheduleCreatorDialog.getScheduleEditor().getRepeatInSecs() ) ) );
      } catch( RecurrenceEditorException ex ) {
        MessageDialog d = new MessageDialog();
        d.setTitle( "Error" );
        d.setMessage( "Doh! " + ex.getMessage() );
        d.center();
        return;
      }
      PacServiceFactory.getSchedulerService().createRepeatJob(
          scheduleCreatorDialog.getScheduleEditor().getName().trim(), 
          scheduleCreatorDialog.getScheduleEditor().getGroupName().trim(), 
          scheduleCreatorDialog.getScheduleEditor().getDescription().trim(), 
          scheduleCreatorDialog.getScheduleEditor().getStartDateTime().trim(), 
          repeatTimeMillisecs.trim(), 
          scheduleCreatorDialog.getSolutionRepositoryItemPicker().getSolution().trim(),
          scheduleCreatorDialog.getSolutionRepositoryItemPicker().getPath().trim(),
          scheduleCreatorDialog.getSolutionRepositoryItemPicker().getAction().trim(),
          responseCallback
        );
    }
  }
}
