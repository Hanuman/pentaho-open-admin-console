package org.pentaho.pac.client.scheduler;

import java.util.List;

import org.pentaho.pac.client.PacServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerPanel extends VerticalPanel implements ClickListener {

  //Button testBtn = new Button("Test");
  private FlexTable jobsTable = null;
  private boolean isInitialized = false;
  
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
              addJobsTable( jobList );
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
    } // end if (!isInitialized)
  }
  

  public boolean isInitialized() {
    return isInitialized;
  }
  
  private void addJobsTable( List/*<Job>*/ jobList )
  {
    jobsTable = createJobsTable( jobList );
    add( jobsTable );
    addJobListToTable( jobList, jobsTable );
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
  
  private VerticalPanel createActionPanel( final Job job, int rowNum ) {
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
  
  private FlexTable createJobsTable( List/*<Job>*/ jobList ) {
    
    FlexTable t = new FlexTable();
    addJobsTableHeader( t );
    
    return t;
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
