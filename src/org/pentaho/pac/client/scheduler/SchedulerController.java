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
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PushButton;

public class SchedulerController {

  private SchedulerPanel schedulerPanel = null; // this is the view
  private static final int INVALID_SCROLL_POS = -1;
  private List<Schedule> schedulesList = null;   // this is the model
  private ScheduleCreatorDialog scheduleCreatorDialog = null;
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  public SchedulerController( SchedulerPanel schedulerPanel ) {
    this.schedulerPanel = schedulerPanel;
    this.scheduleCreatorDialog = new ScheduleCreatorDialog();
  }
  
  public void init() {
    
    if ( !isInitialized() ) {
      schedulerPanel.init();
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
          scheduleCreatorDialog.center();
        }
      });
      
      schedulerToolbar.setOnUpdateListener( new ICallback() { 
        public void onHandle(Object o) {
        }
      });
      
      schedulerToolbar.setOnDeleteListener( new ICallback() { 
        public void onHandle(Object o) {
          SchedulesListCtrl l = localThis.schedulerPanel.getSchedulesListCtrl();
          List selectedIdxs = l.getSelectedIndexes();
          for ( int ii=selectedIdxs.size()-1; ii>=0; --ii ) {
            int selectedIdx = ((Integer)selectedIdxs.get( ii )).intValue();
            l.remove( selectedIdx );
          }
        }
      });
      
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
    
    scheduleCreatorDialog.setOnOkHandler(new ICallback() {
      public void onHandle(Object o) {
        ScheduleEditor editor = scheduleCreatorDialog.getScheduleEditor();
        
        editor.getName();
        editor.getGroupName();
        editor.getDescription();
        editor.getCronString();
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
    schedulerPanel.getSchedulesListCtrl()
      .updateSchedulesTable( getFilteredSchedulesList() );
  }
  
  private void loadJobsTable()
  {
    final int currScrollPos = schedulerPanel.getSchedulesListCtrl().getScrollPosition();
    
    PacServiceFactory.getSchedulerService().getJobNames(
        new AsyncCallback<Object>() {
          public void onSuccess( Object oJobList ) {
            schedulesList = (List<Schedule>)oJobList;
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
}
