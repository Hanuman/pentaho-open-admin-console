/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler.view;

import java.util.LinkedList;
import java.util.List;

import org.pentaho.pac.client.common.ui.TableListCtrl;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.scheduler.model.Schedule;

public class SchedulesListCtrl extends TableListCtrl<Schedule> {

  // requirement: Name, Group Name, Schedule State, Next Fire, Previous Fire, Subscriber Count 
  private static final String[] COLUMN_HEADER_TITLE = {
    Messages.getString("scheduleName"), //$NON-NLS-1$
    Messages.getString("scheduleGroupName"), //$NON-NLS-1$
    Messages.getString("state"), //$NON-NLS-1$
    Messages.getString("nextFireTime"), //$NON-NLS-1$
    Messages.getString("lastFireTime"), //$NON-NLS-1$
    Messages.getString("subscriberCount") //$NON-NLS-1$
  };
  
  public SchedulesListCtrl()
  {
    super( COLUMN_HEADER_TITLE );
    
    setStyleName( "schedulerListCtrl" ); //$NON-NLS-1$
    setTableStyleName( "schedulesTable" ); //$NON-NLS-1$
    setTableHeaderStyleName( "schedulesTableHeader" ); //$NON-NLS-1$
  }
  
  public List<Schedule> getSelectedSchedules() {
    List<Integer> selectedIdxs = getSelectedIndexes();
    final List<Schedule> scheduleList = new LinkedList<Schedule>();
    
    for ( int rowNum : selectedIdxs ) {
      Schedule schedule = getRowData( rowNum );
      scheduleList.add( schedule );
    }
    return scheduleList;
  }
}
