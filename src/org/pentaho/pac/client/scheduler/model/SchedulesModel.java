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
package org.pentaho.pac.client.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.gwt.widgets.client.utils.TimeUtil;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class SchedulesModel {

  //private DoubleKeyMap<String, String, Schedule> scheduleMap = new DoubleKeyMap<String, String, Schedule>();
  private Map<String, Schedule> scheduleMap = new HashMap<String, Schedule>();
  
  public SchedulesModel() {
  }
  
  // TODO, only partially implemented
  private boolean isValidSchedule( Schedule schedule ) {
    try {
      if ( null != schedule.getEndDate() ) {
        TimeUtil.getDate( schedule.getEndDate() );
      }
      if ( null != schedule.getStartDate() ) {
        TimeUtil.getDate( schedule.getStartDate() );
      }
      return true;
    } catch ( IllegalArgumentException e ) {
      return false;
    }
  }
  
  public void add( List<Schedule> l ) {
    for ( Schedule schedule : l ) {
      assert isValidSchedule( schedule ) : "Loading an invalid schedule: " + schedule.toString();
      add( schedule.getJobName(), schedule );
    }
  }
  
  // TODO sbarkdull, need to do something in case client tries to add some key-pair more than once, exception?
  public void add( String name, Schedule schedule ) {
    scheduleMap.put( name, schedule );
  }
  
  public void remove( String name ) {
    scheduleMap.remove( name );
  }
  
  public void remove( List<Schedule> l ) {
    for ( Schedule s : l ) {
      remove( s.getJobName() );
    }
  }
  
  public Schedule get( String name ) {
    return scheduleMap.get( name );
  }
  
  public List<Schedule> getScheduleList() {
    return new ArrayList<Schedule>( scheduleMap.values() );
  }
}
