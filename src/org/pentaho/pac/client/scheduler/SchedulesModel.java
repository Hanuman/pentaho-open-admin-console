package org.pentaho.pac.client.scheduler;

import java.util.List;

import org.pentaho.pac.client.common.util.DoubleKeyMap;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class SchedulesModel {

  private DoubleKeyMap<String, String, Schedule> scheduleMap = new DoubleKeyMap<String, String, Schedule>();
  
  public SchedulesModel() {
  }
  
  public void add( List<Schedule> l ) {
    for ( Schedule schedule : l ) {
      add( schedule.getJobName(), schedule.getJobGroup(), schedule );
    }
  }
  
  // TODO sbarkdull, need to do something in case client tries to add some key-pair more than once, exception?
  public void add( String name, String groupName, Schedule schedule ) {
    scheduleMap.put( groupName, name, schedule );
  }
  
  public void remove( String name, String groupName ) {
    scheduleMap.remove( groupName, name );
  }
  
  public void remove( List<Schedule> l ) {
    for ( Schedule s : l ) {
      remove( s.getJobName(), s.getJobGroup() );
    }
  }
  
  public Schedule get( String name, String groupName ) {
    return scheduleMap.get( groupName, name );
  }
  
  public List<Schedule> getScheduleList() {
    return scheduleMap.getList();
  }
}
