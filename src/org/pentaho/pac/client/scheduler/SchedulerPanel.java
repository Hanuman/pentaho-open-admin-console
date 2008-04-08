package org.pentaho.pac.client.scheduler;

import java.util.List;

import org.pentaho.pac.client.PacServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerPanel extends VerticalPanel implements ClickListener {

  //Button testBtn = new Button("Test");
  private FlexTable jobsTable = null;
  private FlexTable allActionsTable = null;
  private boolean isInitialized = false;
  private static final String USER_INSTRUCTION = "You can use the pages to suspend or resume the scheduler. You can check the status of the scheduler and get a list of the current jobs that are scheduled.";
  
  public SchedulerPanel()
  {
  }
  public void onClick( Widget sender ) {
    int x=0;
  }


  public void refresh() {
    if ( !isInitialized ) {
      PacServiceFactory.getPacService().getJobNames(
          new AsyncCallback() {
            public void onSuccess( Object oJobList ) {
              List/*<Job>*/ jobList = (List/*<Job>*/)oJobList;
              createUI( jobList );
              isInitialized = true;
            }
      
            public void onFailure(Throwable caught) {
              // TODO sbarkdull
  //            messageDialog.setText("Error Deleting Data Source");
  //            messageDialog.setMessage(caught.getMessage());
  //            messageDialog.center();
              int ii=0;
            }
          }
        );
      updateSchedulerPausedStatus();
    } // end if (!isInitialized)
  }
  

  public boolean isInitialized() {
    return isInitialized;
  }
  
  private void createUI( List/*<Job>*/ jobList )
  {
    Label l = new Label( USER_INSTRUCTION );
    add( l );
    
    allActionsTable = createAllActionsTable();
    add( allActionsTable );
    
    jobsTable = createJobsTable( jobList );
    add( jobsTable );
  }
  
  private void addJobListToTable( List/*<Job>*/ jobList, FlexTable jobsTable )
  {
    for ( int jobIdx=0; jobIdx< jobList.size(); ++jobIdx ) {
      Job job = (Job)jobList.get( jobIdx );

      int rowNum = jobIdx+1;
      jobsTable.setHTML( rowNum, 0, job.jobGroup + "<br/>" + job.jobName );
      jobsTable.setHTML( rowNum, 1, job.triggerGroup + "<br/>" + job.triggerName );
      jobsTable.setHTML( rowNum, 2, job.description );
      jobsTable.setHTML( rowNum, 3, job.jobGroup );
      jobsTable.setHTML( rowNum, 4, job.prevFireTime + "<br/>" + job.getNextFireTime() );
      
      VerticalPanel actionPanel = createActionPanel( job, rowNum );
      jobsTable.setWidget( rowNum, 5, actionPanel );
    }
  }
  
  private FlexTable createAllActionsTable()
  {
    FlexTable t = new FlexTable();

    int rowNum = 0;
    Label l = new Label( "Actions: " );
    t.setWidget( rowNum, 0, l );
    t.setText( rowNum, 1, "" );
    
    rowNum++; // 1
    Hyperlink statusHyper = createSchedulerStatusHyperlink();
    t.setWidget( rowNum, 0, statusHyper );
    t.setText( rowNum, 1, "" );

    rowNum++; // 2
    Hyperlink resumeHyper = createResumeAllHyperlink();
    Hyperlink suspendHyper = createSuspendAllHyperlink();
    VerticalPanel p = new VerticalPanel();
    p.add( resumeHyper );
    p.add( suspendHyper );
    t.setWidget( rowNum, 0, p );
    t.setText( rowNum, 1, "" );
    
    return t;
  }
  
  private VerticalPanel createActionPanel( Job job, int rowNum ) {
    VerticalPanel p = new VerticalPanel();

    Hyperlink a = createSuspendHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    a = createDeleteHyperlink( job.getJobName(), job.getJobGroup(), rowNum );
    p.add( a );
    a = createRunNowHyperlink( job.getJobName(), job.getJobGroup() );
    p.add( a );
    return p;
  }
   
  private Hyperlink createSuspendHyperlink( final String jobName, final String jobGroup ) {
    Hyperlink a = new Hyperlink();
    a.setText(  "Suspend" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        PacServiceFactory.getPacService().pauseJob(
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                // TODO sbarkdull
                Object x = o;
                int ii=0;
              }
        
              public void onFailure(Throwable caught) {
                // TODO sbarkdull
                int ii=0;
              }
            }
          );
      }
    } );
    
    return a;
  }
  private Hyperlink createDeleteHyperlink( final String jobName, final String jobGroup,
      final int rowNum ) {
    Hyperlink a = new Hyperlink();
    a.setText(  "Delete" );
    // TODO sbarkdull, yuk
    a.addClickListener( new ClickListener() {
      public void onClick( Widget sender ) {
        PacServiceFactory.getPacService().deleteJob( 
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                // TODO sbarkdull
                jobsTable.removeRow( rowNum );
              }
        
              public void onFailure(Throwable caught) {
                // TODO sbarkdull
                int ii=0;
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
        PacServiceFactory.getPacService().executeJobNow( 
            jobName,
            jobGroup,
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                // TODO sbarkdull
                Object x = o;
                int ii=0;
              }
        
              public void onFailure(Throwable caught) {
                // TODO sbarkdull
                int ii=0;
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
        PacServiceFactory.getPacService().pauseAll(
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                // TODO sbarkdull
                allActionsTable.setText( 2, 1, "All Jobs Suspended" );
              }
        
              public void onFailure(Throwable caught) {
                // TODO sbarkdull
                int ii=0;
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
        PacServiceFactory.getPacService().resumeAll(
            new AsyncCallback() {
              public void onSuccess( Object o ) {
                // TODO sbarkdull
                int ii=0;
                allActionsTable.setText( 2, 1, "All Jobs Resumed" );
              }
        
              public void onFailure(Throwable caught) {
                // TODO sbarkdull
                int ii=0;
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
            allActionsTable.setText( 1, 1, isRunning ? "Running" : "Suspended" );
          }
    
          public void onFailure(Throwable caught) {
            // TODO sbarkdull
            int ii=0;
          }
        }
      );
  }
  
  private FlexTable createJobsTable( List/*<Job>*/ jobList ) {
    
    FlexTable jobsTable = new FlexTable();
    addJobsTableHeader( jobsTable );
    
    addJobListToTable( jobList, jobsTable );
    
    return jobsTable;
  }
  
  private static final String[] COLUMN_HEADER_TITLE = {
    "Job - Group / Name",
    "Trigger - Group / Name",
    "Description",
    "Fire Time - Last / Next",
    "State",
    "Action"
  };
  
  private void addJobsTableHeader( FlexTable t )
  {
    for ( int ii=0; ii<COLUMN_HEADER_TITLE.length; ++ii ) {
      String title = COLUMN_HEADER_TITLE[ii];
      //t.addCell( 0 );
      t.setText(0, ii, title );
    }
  }
}
