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
package org.pentaho.pac.client.scheduler.ctlr;

import java.util.List;

import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.view.SchedulesListCtrl;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SchedulesListController {

  private SchedulesListCtrl schedulesListCtrl = null;
  
  public SchedulesListController( SchedulesListCtrl schedulesListCtrl ) {
    assert (null != schedulesListCtrl ) : "schedulesListCtrl cannot be null."; //$NON-NLS-1$
    this.schedulesListCtrl = schedulesListCtrl;
  }
  
  public void updateSchedulesTable( List<Schedule> scheduleList )
  {
    schedulesListCtrl.removeAll();
    if ( scheduleList.size() > 0 ) {
      for ( int scheduleIdx=0; scheduleIdx< scheduleList.size(); ++scheduleIdx ) {
        Schedule schedule = scheduleList.get( scheduleIdx );
  
        Widget[] widgets = new Widget[ schedulesListCtrl.getNumColumns()-1 ];

        // column 0
        Label l = new Label( schedule.getJobName());
        widgets[ 0 ] = l;
        
        // column 1
        l = new Label( schedule.getJobGroup() );
        widgets[ 1 ] = l;

        // column 2
        l = new Label();
        String txt = StringUtils.defaultString( schedule.getTriggerState(),
            "<span>&nbsp;</span>" ); //$NON-NLS-1$
        l.getElement().setInnerHTML( txt );
        widgets[ 2 ] = l;

        // column 3 
        l = new Label( schedule.getNextFireTime() );
        widgets[ 3 ] = l;
        
        // column 4
        l = new Label( schedule.getPrevFireTime() );
        widgets[ 4 ] = l;

        // column 5
        String labelTxt = schedule.isSubscriptionSchedule()
          ? Messages.getString("publicLabel") + "[" + schedule.getSubscriberCount() + "]" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          : Messages.getString("notApplicable"); //$NON-NLS-1$
        l = new Label( labelTxt );
        widgets[ 5 ] = l;

        schedulesListCtrl.addRow( widgets, schedule );
      }
    } else {
      schedulesListCtrl.setTempMessage( Messages.getString("noSchedules") ); //$NON-NLS-1$
    }
  }
}
