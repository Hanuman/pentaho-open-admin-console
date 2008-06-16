/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler;

import java.util.Date;

import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;

import com.google.gwt.user.client.ui.TabPanel;

public class ScheduleCreatorDialog extends ConfirmDialog {

  private ScheduleEditor scheduleEditor = new ScheduleEditor();
  private SolutionRepositoryItemPicker solRepItemPicker = new SolutionRepositoryItemPicker();
  private TabPanel tabPanel = new TabPanel();
  enum TabIndex {
    SCHEDULE,
    SCHEDULE_ACTION;
  }; // end enum
  
  public ScheduleCreatorDialog() {
    super();

    setTitle( "Schedule Creator" );
    setClientSize( "430px", "300px" );
    
    tabPanel.add( scheduleEditor, "Schedule" );
    tabPanel.add( solRepItemPicker, "Scheduled Action" );

    tabPanel.setWidth( "100%" );
    tabPanel.setHeight( "100%" );
    
//  scheduleEditor.setWidth( "100%" );
//  scheduleEditor.setHeight( "100%" );
//    solRepItemPicker.setWidth( "100%" );
//    solRepItemPicker.setHeight( "100%" );
    
    tabPanel.selectTab( TabIndex.SCHEDULE.ordinal() );
    
    addWidgetToClientArea( tabPanel );
  }

  public ScheduleEditor getScheduleEditor() {
    return scheduleEditor;
  }

  public SolutionRepositoryItemPicker getSolutionRepositoryItemPicker() {
    return solRepItemPicker;
  }
  
  public void reset( Date d ) {
    scheduleEditor.reset( d );
    solRepItemPicker.reset();
    
    tabPanel.selectTab( TabIndex.SCHEDULE.ordinal() );
  }
}
