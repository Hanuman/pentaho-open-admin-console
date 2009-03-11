/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
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
import org.pentaho.pac.client.PentahoAdminConsole;

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
      assert isValidSchedule( schedule ) : PentahoAdminConsole.MSGS.loadingInvalidSchedule(schedule.toString());
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
