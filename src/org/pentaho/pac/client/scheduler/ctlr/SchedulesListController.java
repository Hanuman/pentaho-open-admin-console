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
package org.pentaho.pac.client.scheduler.ctlr;

import java.util.List;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.view.SchedulesListCtrl;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SchedulesListController {

  private static int NUM_COLUMNS = SchedulesListCtrl.COLUMN_HEADER_TITLE.length;
  private SchedulesListCtrl schedulesListCtrl = null;
  
  public SchedulesListController( SchedulesListCtrl schedulesListCtrl ) {
    assert (null != schedulesListCtrl ) : "schedulesListCtrl cannot be null.";
    this.schedulesListCtrl = schedulesListCtrl;
  }
  
  public void updateSchedulesTable( List<Schedule> scheduleList )
  {
    schedulesListCtrl.removeAll();
    if ( scheduleList.size() > 0 ) {
      for ( int scheduleIdx=0; scheduleIdx< scheduleList.size(); ++scheduleIdx ) {
        Schedule schedule = scheduleList.get( scheduleIdx );
  
        Widget[] widgets = new Widget[ NUM_COLUMNS ];

        // column 0 
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName( "schedulesTableCellTable" ); //$NON-NLS-1$
        vp.add( new Label( schedule.getJobName()) );
        vp.add( new Label( schedule.getJobGroup() ) );
        widgets[ 0 ] = vp;

        // column 1
        Label l = new Label();
        String txt = StringUtils.defaultString( schedule.getTriggerState(),
            "<span>&nbsp;</span>" ); //$NON-NLS-1$
        l.getElement().setInnerHTML( txt );
        widgets[ 1 ] = l;

        // column 2 
        vp = new VerticalPanel();
        vp.setStyleName( "schedulesTableCellTable" ); //$NON-NLS-1$
        vp.add( new Label( schedule.getPrevFireTime() ) );
        vp.add( new Label( schedule.getNextFireTime() ) );
        // TODO remove next line, only for debug
        vp.add( new Label( schedule.getCronString()) );
        widgets[ 2 ] = vp;

        // column 3
        String labelTxt = schedule.isSubscriptionSchedule()
          ? schedule.getSubscriberCount()
          : "n/a";
        l = new Label( labelTxt );
        widgets[ 3 ] = l;
        
        // column 4
        l = new Label();
        txt = StringUtils.defaultString( schedule.getDescription(),
            "<span>&nbsp;</span>" ); //$NON-NLS-1$
        l.getElement().setInnerHTML( txt );
        widgets[ 4 ] = l;

        schedulesListCtrl.addRow( widgets, schedule );
      }
    } else {
      PacLocalizedMessages msgs = PentahoAdminConsole.getLocalizedMessages();
      schedulesListCtrl.setTempMessage( msgs.noSchedules() );
    }
  }
}
