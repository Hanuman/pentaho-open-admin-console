package org.pentaho.pac.client.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

public class SchedulesListCtrl extends ScrollPanel {

  private FlexTable schedulesTable = null;
  private static final int HEADER_ROW = 0;
  private static final int FIRST_ROW = HEADER_ROW+1;
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private static final String[] COLUMN_HEADER_TITLE = {
    "", //$NON-NLS-1$
    MSGS.jobGroupName(),
    MSGS.triggerGroupName(),
    MSGS.description(),
    MSGS.fireTimeLastNext(),
    MSGS.state()
  };
  
  public SchedulesListCtrl()
  {
    super();
    
    // TODO sbarkdull, get it in style sheet
    setHeight( "230px" ); //$NON-NLS-1$
    schedulesTable = createSchedulesTable();
    add( schedulesTable );
  }
  
  private FlexTable createSchedulesTable() {
    
    FlexTable table = new FlexTable();
    table.setStyleName( "schedulesTable" ); //$NON-NLS-1$
    table.setCellPadding( 0 );
    table.setCellSpacing( 0 );
    addSchedulesTableHeader( table );
    
    return table;
  }
  
  private void addSchedulesTableHeader( FlexTable table )
  {
    for ( int ii=0; ii<COLUMN_HEADER_TITLE.length; ++ii ) {
      String title = COLUMN_HEADER_TITLE[ii];
      table.setText( HEADER_ROW, ii, title );
    }
    table.getRowFormatter().setStyleName( 0, "schedulesTableHeader" ); //$NON-NLS-1$
  }
  
  public void updateSchedulesTable( List<Schedule> scheduleList )
  {
    removeAll();
    if ( scheduleList.size() > 0 ) {
      for ( int scheduleIdx=0; scheduleIdx< scheduleList.size(); ++scheduleIdx ) {
        Schedule schedule = scheduleList.get( scheduleIdx );
  
        int rowNum = scheduleIdx+FIRST_ROW;
        schedulesTable.setWidget( rowNum, 0, new CheckBox() );
        // TODO sbarkdull, remove cron string
        schedulesTable.setHTML( rowNum, 1, schedule.getJobGroup() + "<br/>" + schedule.getJobName() + "<br/>" + schedule.getCronString() ); //$NON-NLS-1$ //$NON-NLS-2$
        schedulesTable.setHTML( rowNum, 2, schedule.getTriggerGroup() + "<br/>" + schedule.getTriggerName() ); //$NON-NLS-1$
        schedulesTable.setHTML( rowNum, 3, StringUtils.defaultIfEmpty( schedule.getDescription(), "&nbsp" ) ); //$NON-NLS-1$
        schedulesTable.setHTML( rowNum, 4, schedule.getPrevFireTime() + "<br/>" + schedule.getNextFireTime() ); //$NON-NLS-1$
        schedulesTable.setHTML( rowNum, 5, StringUtils.defaultIfEmpty( schedule.getTriggerState(), "&nbsp" ) ); //$NON-NLS-1$
      }
    } else {
      showNoScheduledSchedulesRow();
    }
  }
  
  private void showNoScheduledSchedulesRow()
  {
    schedulesTable.setHTML( FIRST_ROW, 0, MSGS.noSchedules() );
    schedulesTable.setHTML( FIRST_ROW, 1, "&nbsp;" ); //$NON-NLS-1$
    schedulesTable.setHTML( FIRST_ROW, 2, "&nbsp;" ); //$NON-NLS-1$
    schedulesTable.setHTML( FIRST_ROW, 3, "&nbsp;" ); //$NON-NLS-1$
    schedulesTable.setHTML( FIRST_ROW, 4, "&nbsp;" ); //$NON-NLS-1$
    schedulesTable.setHTML( FIRST_ROW, 5, "&nbsp;" ); //$NON-NLS-1$
  }
  
  public List<Integer> getSelectedIndexes()
  {
    List<Integer> idxs = new ArrayList<Integer>();

    for ( int ii=FIRST_ROW; ii<schedulesTable.getRowCount(); ++ii ) {
      CheckBox cb = (CheckBox)schedulesTable.getWidget( ii, 0 );
      if ( cb.isChecked() ) {
        idxs.add( new Integer( ii-FIRST_ROW ) );
      }
    }
    
    return idxs;
  }
  
  /**
   * Removes all non-header items from the list
   */
  public void removeAll() {
    // don't delete row 0, it's the header
    for ( int ii=schedulesTable.getRowCount()-1; ii>=FIRST_ROW; --ii ) {
      schedulesTable.removeRow( ii );
    }
    showNoScheduledSchedulesRow();
  }
  
  public void remove( int idx ) {
    schedulesTable.removeRow( idx+FIRST_ROW );
    if ( getNumItems() <= 0 ) {
      showNoScheduledSchedulesRow();
    }
  }
  
  public void selectAll() {
    for ( int ii=FIRST_ROW; ii<schedulesTable.getRowCount(); ++ii ) {
      CheckBox cb = (CheckBox)schedulesTable.getWidget( ii, 0 );
      cb.setChecked( true );
    }
  }
  
  public void unselectAll() {
    for ( int ii=FIRST_ROW; ii<schedulesTable.getRowCount(); ++ii ) {
      CheckBox cb = (CheckBox)schedulesTable.getWidget( ii, 0 );
      cb.setChecked( false );
    }
  }
  
  public void select( int idx ) {
    CheckBox cb = (CheckBox)schedulesTable.getWidget( idx+FIRST_ROW, 0 );
    cb.setChecked( true );
  }

  public void add( Schedule schedule ) {
    // not yet impl
  }

  public void updateItem( int idx, Schedule schedule ) {
    // not yet impl
  }
  
  public int getNumItems() {
    return schedulesTable.getRowCount() - FIRST_ROW;
  }
}
