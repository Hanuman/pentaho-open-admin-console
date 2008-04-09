package org.pentaho.pac.client.scheduler;

import java.util.List;

import org.pentaho.pac.client.MessageDialog;
import org.pentaho.pac.client.PacServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerPanel extends VerticalPanel implements ClickListener {

  private ScrollPanel jobsTableScrollPanel = null;
  private FlexTable jobsTable = null;
  private FlexTable allActionsTable = null;
  private Label userInstructionLabel = null;
  private boolean isInitialized = false;
  private String resumeSuspendState = "&nbsp;";
  
  private static final String USER_INSTRUCTION = "You can use the pages to suspend or resume the scheduler. You can check the status of the scheduler and get a list of the current jobs that are scheduled.";

  private static final String[] COLUMN_HEADER_TITLE = {
    "Job - Group / Name",
    "Trigger - Group / Name",
    "Description",
    "Fire Time - Last / Next",
    "State",
    "Action"
  };
  
  public SchedulerPanel()
  {
    this.setStylePrimaryName( "schedulerPanel" );
  }
  
  public void onClick( Widget sender ) {
  }


  public void refresh() {
    if ( !isInitialized ) {
      PacServiceFactory.getPacService().getJobNames(
          new AsyncCallback() {
            public void onSuccess( Object oJobList ) {
              List/*<Job>*/ jobList = (List/*<Job>*/)oJobList;
              createUI( jobList );
              allActionsTable.setHTML( 2, 1, resumeSuspendState );
              isInitialized = true;
            }
      
            public void onFailure(Throwable caught) {
              MessageDialog messageDialog = new MessageDialog("", 
                  caught.getMessage(), new int[]{MessageDialog.OK_BTN});
              messageDialog.center();
            }
          }
        );
      updateSchedulerPausedStatus();
    } // end if (!isInitialized)
  }

  public void forceRefresh() {
    isInitialized = false;
    refresh();
  }

  public boolean isInitialized() {
    return isInitialized;
  }
  
  private void createUI( List/*<Job>*/ jobList )
  {
    if ( null != userInstructionLabel ) {
      remove( userInstructionLabel );
      remove( allActionsTable );
      remove( jobsTableScrollPanel );
    }
    userInstructionLabel = new Label( USER_INSTRUCTION );
    userInstructionLabel.setStylePrimaryName( "schedulerInstructionLabel" );
    add( userInstructionLabel );
    
    allActionsTable = createAllActionsTable();
    add( allActionsTable );

    jobsTable = createJobsTable( jobList );
    jobsTableScrollPanel = new ScrollPanel( jobsTable );
    jobsTableScrollPanel.setHeight( "450px" );
    add( jobsTableScrollPanel );
  }
  
  private void addJobListToTable( List/*<Job>*/ jobList, FlexTable jobsTable )
  {
    for ( int jobIdx=0; jobIdx< jobList.size(); ++jobIdx ) {
      Job job = (Job)jobList.get( jobIdx );

      int rowNum = jobIdx+1;
      jobsTable.setHTML( rowNum, 0, job.jobGroup + "<br/>" + job.jobName );
      jobsTable.setHTML( rowNum, 1, job.triggerGroup + "<br/>" + job.triggerName );
      jobsTable.setHTML( rowNum, 2, defaultIfEmpty( job.description ) );
      jobsTable.setHTML( rowNum, 3, job.prevFireTime + "<br/>" + job.getNextFireTime() );
      jobsTable.setHTML( rowNum, 4, job.triggerState );
      
      VerticalPanel actionPanel = createActionPanel( job, rowNum, job.triggerState );
      jobsTable.setWidget( rowNum, 5, actionPanel );
    }
  }
  
//TODO move to a client side utils class
  /**
   * mimics StringUtils.defaultIfEmpty()
   * @param str
   * @return
   */
  private static String defaultIfEmpty( String str )
  {
    return ( null == str || "".equals( str ) ) ? "&nbsp;" : str;
  }
  
  private FlexTable createAllActionsTable()
  {
    FlexTable t = new FlexTable();
    t.setStylePrimaryName( "allActionTable");
    t.setCellPadding( 0 );
    t.setCellSpacing( 0 );
    
    int rowNum = 0;
    Label l = new Label( "Actions: " );
    t.setWidget( rowNum, 0, l );
    t.setHTML( rowNum, 1, "&nbsp;" );
    t.getRowFormatter().setStylePrimaryName( 0, "allActionTableHeader" );
    
    rowNum++; // 1
    Hyperlink statusHyper = createSchedulerStatusHyperlink();
    t.setWidget( rowNum, 0, statusHyper );
    t.setHTML( rowNum, 1, "&nbsp;" );

    rowNum++; // 2
    Hyperlink resumeHyper = createResumeAllHyperlink();
    Hyperlink suspendHyper = createSuspendAllHyperlink();
    VerticalPanel p = new VerticalPanel();
    p.setStylePrimaryName( "resumeSuspendPanel" );
    p.setSpacing(0);
    
    p.add( resumeHyper );
    p.add( suspendHyper );
    t.setWidget( rowNum, 0, p );
    t.setHTML( rowNum, 1, resumeSuspendState );
    
    return t;
  }
  
  private VerticalPanel createActionPanel( Job job, int rowNum, String triggerState ) {
    VerticalPanel p = new VerticalPanel();
    p.setStylePrimaryName( "actionCellPanel" );
    p.setSpacing(0);
    
    Hyperlink a = "Suspended".equals( triggerState )
      ? createResumeHyperlink( job.getJobName(), job.getJobGroup() )
      : createSuspendHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    a = createDeleteHyperlink( job.getJobName(), job.getJobGroup(), rowNum );
    p.add( a );
    a = createRunNowHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    return p;
  }
   
  private Hyperlink createSuspendHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText( "Suspend" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( final Widget sender ) {
        ((Hyperlink)sender).setText( "Working..." );
        PacServiceFactory.getPacService().pauseJob(
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog("", 
                    caught.getMessage(), new int[]{MessageDialog.OK_BTN});
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
    a.setText(  "Resume" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        ((Hyperlink)sender).setText( "Working..." );
        PacServiceFactory.getPacService().resumeJob(
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                // TODO sbarkdull
                MessageDialog messageDialog = new MessageDialog("", 
                    caught.getMessage(), new int[]{MessageDialog.OK_BTN});
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }
  private Hyperlink createDeleteHyperlink( final String jobName, final String jobGroup, int rowNum ) {
    Hyperlink a = new Hyperlink();
    a.setText( "Delete" );
    // TODO sbarkdull, yuk
    final int localRowNum = rowNum;
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        ((Hyperlink)sender).setText( "Working..." );
        PacServiceFactory.getPacService().deleteJob( 
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
                jobsTable.removeRow( localRowNum );
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog("", 
                    caught.getMessage(), new int[]{MessageDialog.OK_BTN});
                messageDialog.center();
              }
            }
          );
      }
    } );
    
    return a;
  }
  private Hyperlink createRunNowHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText(  "Run Now" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        ((Hyperlink)sender).setText( "Working..." );
        PacServiceFactory.getPacService().executeJobNow( 
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog("", 
                    caught.getMessage(), new int[]{MessageDialog.OK_BTN});
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
    a.setText( "Suspend All Jobs" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        allActionsTable.setHTML( 2, 1, "Working..." );
        PacServiceFactory.getPacService().pauseAll(
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                resumeSuspendState = "All Jobs Suspended";
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog("", 
                    caught.getMessage(), new int[]{MessageDialog.OK_BTN});
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
    a.setText( "Resume All Jobs" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        allActionsTable.setHTML( 2, 1, "Working..." );
        PacServiceFactory.getPacService().resumeAll(
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                resumeSuspendState = "All Jobs Resumed";
                forceRefresh();
              }
        
              public void onFailure(Throwable caught) {
                MessageDialog messageDialog = new MessageDialog("", 
                    caught.getMessage(), new int[]{MessageDialog.OK_BTN});
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
    a.setText( "Scheduler Status" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        allActionsTable.setHTML( 1, 1, "Working..." );
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
            allActionsTable.setHTML( 1, 1, isRunning ? "Running" : "Suspended" );
          }
    
          public void onFailure(Throwable caught) {
            MessageDialog messageDialog = new MessageDialog("", 
                caught.getMessage(), new int[]{MessageDialog.OK_BTN});
            messageDialog.center();
          }
        }
      );
  }
  
  private FlexTable createJobsTable( List/*<Job>*/ jobList ) {
    
    FlexTable jobsTable = new FlexTable();
    jobsTable.setStylePrimaryName( "jobsTable" );
    jobsTable.setCellPadding( 0 );
    jobsTable.setCellSpacing( 0 );
    addJobsTableHeader( jobsTable );
    
    addJobListToTable( jobList, jobsTable );
    
    return jobsTable;
  }
  
  private void addJobsTableHeader( FlexTable t )
  {
    for ( int ii=0; ii<COLUMN_HEADER_TITLE.length; ++ii ) {
      String title = COLUMN_HEADER_TITLE[ii];
      //t.addCell( 0 );
      t.setText(0, ii, title );
    }
    t.getRowFormatter().setStylePrimaryName( 0, "jobsTableHeader" );
  }
}
