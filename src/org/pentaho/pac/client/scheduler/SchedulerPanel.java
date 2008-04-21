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

package org.pentaho.pac.client.scheduler;

import java.util.List;

import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.ICallbackHandler;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerPanel extends VerticalPanel {

  private ScrollPanel jobsTableScrollPanel = null;
  private FlexTable jobsTable = null;
  private FlexTable allActionsTable = null;
  private Label userInstructionLabel = null;
  private boolean isInitialized = false;
  private String resumeSuspendState = "&nbsp;"; //$NON-NLS-1$
  private static final int INVALID_SCROLL_POS = -1;
  private int currScrollPos = INVALID_SCROLL_POS;
  private Label loading = null;
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  private static final String USER_INSTRUCTION = MSGS.schedulerUserInstruction();

  private static final String[] COLUMN_HEADER_TITLE = {
    MSGS.jobGroupName(),
    MSGS.triggerGroupName(),
    MSGS.description(),
    MSGS.fireTimeLastNext(),
    MSGS.state(),
    MSGS.action()
  };
  
  public SchedulerPanel()
  {
    this.setStyleName( "schedulerPanel" ); //$NON-NLS-1$
  }

  public void refresh() {
    if ( !isInitialized ) {
      
      if ( !hasUIBeenCreated() ) {
        loading = new Label( MSGS.loading() );
        add( loading );
      } else {
        currScrollPos = jobsTableScrollPanel.getScrollPosition();
      }

      PacServiceFactory.getPacService().getJobNames(
          new AsyncCallback() {
            public void onSuccess( Object oJobList ) {
              List/*<Job>*/ jobList = (List/*<Job>*/)oJobList;
              createUI( jobList );
              allActionsTable.setHTML( 2, 1, resumeSuspendState );
              isInitialized = true;
              updateSchedulerPausedStatus();
              if ( INVALID_SCROLL_POS != currScrollPos ) { 
                jobsTableScrollPanel.setScrollPosition( currScrollPos );
                currScrollPos = INVALID_SCROLL_POS;
              }
            }
      
            public void onFailure(Throwable caught) {
              MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
                  caught.getMessage() );
              messageDialog.center();
              remove( loading );
              isInitialized = false;
            }
          }
        );
    } // end if (!isInitialized)
  }

  public void forceRefresh() {
    isInitialized = false;
    refresh();
  }

  public boolean isInitialized() {
    return isInitialized;
  }
  
  private boolean hasUIBeenCreated()
  {
    return null != userInstructionLabel;
  }
  
  private void createUI( List/*<Job>*/ jobList )
  {
    if ( hasUIBeenCreated() ) {
      remove( userInstructionLabel );
      remove( allActionsTable );
      remove( jobsTableScrollPanel );
    } else {
      remove( loading );
    }

    userInstructionLabel = new Label( USER_INSTRUCTION );
    userInstructionLabel.setStyleName( "schedulerInstructionLabel" ); //$NON-NLS-1$
    add( userInstructionLabel );
    
    allActionsTable = createAllActionsTable();
    add( allActionsTable );

    createJobsTable( jobList );
    jobsTableScrollPanel = new ScrollPanel( jobsTable );
    // TODO sbarkdull, move the 230px to style sheet
    jobsTableScrollPanel.setHeight( "230px" ); //$NON-NLS-1$
    add( jobsTableScrollPanel );
  }
  
  private void addJobListToTable( List/*<Job>*/ jobList, FlexTable table )
  {
    if ( jobList.size() > 0 ) {
      for ( int jobIdx=0; jobIdx< jobList.size(); ++jobIdx ) {
        Job job = (Job)jobList.get( jobIdx );
  
        int rowNum = jobIdx+1;
        table.setHTML( rowNum, 0, job.jobGroup + "<br/>" + job.jobName ); //$NON-NLS-1$
        table.setHTML( rowNum, 1, job.triggerGroup + "<br/>" + job.triggerName ); //$NON-NLS-1$
        table.setHTML( rowNum, 2, StringUtils.defaultIfEmpty( job.description, "&nbsp" ) ); //$NON-NLS-1$
        table.setHTML( rowNum, 3, job.prevFireTime + "<br/>" + job.getNextFireTime() ); //$NON-NLS-1$
        table.setHTML( rowNum, 4, StringUtils.defaultIfEmpty( job.triggerState, "&nbsp" ) ); //$NON-NLS-1$
        
        VerticalPanel actionPanel = createActionPanel( job, job.triggerState );
        table.setWidget( rowNum, 5, actionPanel );
      }
    } else {
      showNoScheduledJobsRow();
    }
  }
  
  private void showNoScheduledJobsRow()
  {
    if ( jobsTable.getRowCount() <= 1 ) {
      jobsTable.setHTML( 1, 0, MSGS.noScheduledJobs() );
      jobsTable.setHTML( 1, 1, "&nbsp;" ); //$NON-NLS-1$
      jobsTable.setHTML( 1, 2, "&nbsp;" ); //$NON-NLS-1$
      jobsTable.setHTML( 1, 3, "&nbsp;" ); //$NON-NLS-1$
      jobsTable.setHTML( 1, 4, "&nbsp;" ); //$NON-NLS-1$
      jobsTable.setHTML( 1, 5, "&nbsp;" ); //$NON-NLS-1$
    }
  }
  private FlexTable createAllActionsTable()
  {
    FlexTable t = new FlexTable();
    t.setStyleName( "allActionTable"); //$NON-NLS-1$
    t.setCellPadding( 0 );
    t.setCellSpacing( 0 );

    int rowNum = 0;
    Label l = new Label( MSGS.actions() );
    t.setWidget( rowNum, 0, l );
    t.setHTML( rowNum, 1, MSGS.status() );
    t.getRowFormatter().setStyleName( 0, "allActionTableHeader" ); //$NON-NLS-1$
    
    rowNum++; // 1
    Hyperlink statusHyper = createSchedulerStatusHyperlink();
    t.setWidget( rowNum, 0, statusHyper );
    //see setSchedulerStatusMsg
    t.setHTML( rowNum, 1, "&nbsp;" ); //$NON-NLS-1$

    rowNum++; // 2
    Hyperlink resumeHyper = createResumeAllHyperlink();
    Hyperlink suspendHyper = createSuspendAllHyperlink();
    VerticalPanel p = new VerticalPanel();
    p.setStyleName( "resumeSuspendPanel" ); //$NON-NLS-1$
    p.setSpacing(0);
    
    p.add( resumeHyper );
    p.add( suspendHyper );
    t.setWidget( rowNum, 0, p );
    t.setHTML( rowNum, 1, resumeSuspendState );
    
    return t;
  }
  
  private VerticalPanel createActionPanel( Job job, String triggerState ) {
    VerticalPanel p = new VerticalPanel();
    p.setStyleName( "actionCellPanel" ); //$NON-NLS-1$
    p.setSpacing(0);
    
    Hyperlink a = MSGS.stateSuspended().equals( triggerState )
      ? createResumeHyperlink( job.getJobName(), job.getJobGroup() )
      : createSuspendHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    a = createDeleteHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    a = createRunNowHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    return p;
  }
   
  private Hyperlink createSuspendHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.suspend() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( final Widget sender ) {
        ((Hyperlink)sender).setText( MSGS.working() );
        PacServiceFactory.getPacService().pauseJob(
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog( MSGS.error(), 
                    caught.getMessage() );
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }
   
  private Hyperlink createResumeHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.resume() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        ((Hyperlink)sender).setText( MSGS.working() );
        PacServiceFactory.getPacService().resumeJob(
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog(MSGS.error(), 
                    caught.getMessage() );
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }
  private Hyperlink createDeleteHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.delete() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( final Widget sender ) {
        
        final ConfirmDialog confirmDeleteDlg = new ConfirmDialog( "Delete Job", "Are you sure you want to delete this job?");
        confirmDeleteDlg.center();
        confirmDeleteDlg.setOnOkHandler( new ICallbackHandler() {
          public void onHandle(Object o) {
            ((Hyperlink)sender).setText( MSGS.working() );
            PacServiceFactory.getPacService().deleteJob( 
                jobName,
                jobGroup,
                new AsyncCallback() {
                  public void onSuccess( Object o ) {
                    confirmDeleteDlg.hide();
                    forceRefresh();
                  }
            
                  public void onFailure(Throwable caught) {
                    MessageDialog messageDialog = new MessageDialog(MSGS.error(), 
                        caught.getMessage() );
                    messageDialog.center();
                  }
                }
              );
          }
        });
      }
    } );
    
    return a;
  }
  private Hyperlink createRunNowHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.runNow() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        ((Hyperlink)sender).setText( MSGS.working() );
        PacServiceFactory.getPacService().executeJobNow( 
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog(MSGS.error(), 
                    caught.getMessage() );
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }

  
  private Hyperlink createSuspendAllHyperlink() {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.suspendAllJobs() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        allActionsTable.setHTML( 2, 1, MSGS.working() );
        PacServiceFactory.getPacService().pauseAll(
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                resumeSuspendState = MSGS.allJobsSuspended();
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog(MSGS.error(), 
                    caught.getMessage() );
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }
  private Hyperlink createResumeAllHyperlink() {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.resumeAllJobs() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        allActionsTable.setHTML( 2, 1, MSGS.working() );
        PacServiceFactory.getPacService().resumeAll(
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                resumeSuspendState = MSGS.allJobsResumed();
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog(MSGS.error(), 
                    caught.getMessage() );
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }
  
  private Hyperlink createSchedulerStatusHyperlink() {
    Hyperlink a = new Hyperlink();
    a.setText( MSGS.schedulerStatus() );
    
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        setSchedulerStatusMsg( MSGS.working() );
        updateSchedulerPausedStatus();
      }
    } );
    
    return a;
  }
  
  private void updateSchedulerPausedStatus()
  {
    PacServiceFactory.getPacService().isSchedulerPaused(
        new AsyncCallback() {
          public void onSuccess( Object oIsRunning ) {
            boolean isRunning = ((Boolean)oIsRunning).booleanValue();
            setSchedulerStatusMsg( isRunning ? MSGS.running() : MSGS.notRunning() );
          }
    
          public void onFailure(Throwable caught) {
            MessageDialog messageDialog = new MessageDialog(MSGS.error(), 
                caught.getMessage() );
            messageDialog.center();
          }
        }
      );
  }
  
  private void setSchedulerStatusMsg( String statusMsg )
  {
    allActionsTable.setHTML( 1, 1, statusMsg );
  }
  
  private void createJobsTable( List/*<Job>*/ jobList ) {
    
    jobsTable = new FlexTable();
    jobsTable.setStyleName( "jobsTable" ); //$NON-NLS-1$
    jobsTable.setCellPadding( 0 );
    jobsTable.setCellSpacing( 0 );
    addJobsTableHeader();
    
    addJobListToTable( jobList, jobsTable );
  }
  
  private void addJobsTableHeader()
  {
    for ( int ii=0; ii<COLUMN_HEADER_TITLE.length; ++ii ) {
      String title = COLUMN_HEADER_TITLE[ii];
      //jobsTable.addCell( 0 );
      jobsTable.setText(0, ii, title );
    }
    jobsTable.getRowFormatter().setStyleName( 0, "jobsTableHeader" ); //$NON-NLS-1$
  }
}
