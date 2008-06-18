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
import java.util.HashMap;
import java.util.Map;

import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class ScheduleCreatorDialog extends ConfirmDialog {

  public enum TabIndex {
    SCHEDULE( 0, "Schedule" ),
    SCHEDULE_ACTION( 1, "Scheduled Action" );
    
    private TabIndex( int value, String name ) {
      this.value = value;
      this.name = name;
    }
    private int value;
    private String name;
    
    private static TabIndex[] tabIndexAr = {
      SCHEDULE, 
      SCHEDULE_ACTION 
    };

    public static TabIndex get(int idx) {
      return tabIndexAr[idx];
    }
    
    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }
  }; // end enum
  
  private ScheduleEditor scheduleEditor = new ScheduleEditor();
  private SolutionRepositoryItemPicker solRepItemPicker = new SolutionRepositoryItemPicker();
  private Label scheduleTabLabel = new Label( TabIndex.SCHEDULE.toString() );
  private Label scheduleActionTabLabel = new Label( TabIndex.SCHEDULE_ACTION.toString() );
  private Map<TabIndex, Label> tabMap = new HashMap<TabIndex, Label>();
  private TabPanel tabPanel = new TabPanel();
  
  public ScheduleCreatorDialog() {
    super();

    setTitle( "Schedule Creator" );
    setClientSize( "475px", "450px" );
    
    tabPanel.setStylePrimaryName( "schedulerTabPanel" );
    tabPanel.add( scheduleEditor, scheduleTabLabel );
    tabPanel.add( solRepItemPicker, scheduleActionTabLabel );
    scheduleTabLabel.setStyleName( "tabLabel" );
    scheduleActionTabLabel.setStyleName( "tabLabel" );
    tabMap.put( TabIndex.SCHEDULE, scheduleTabLabel );
    tabMap.put( TabIndex.SCHEDULE_ACTION, scheduleActionTabLabel );

    tabPanel.setWidth( "100%" );
    tabPanel.setHeight( "100%" );
    
//  scheduleEditor.setWidth( "100%" );
//  scheduleEditor.setHeight( "100%" );
//    solRepItemPicker.setWidth( "100%" );
//    solRepItemPicker.setHeight( "100%" );
    
    tabPanel.selectTab( TabIndex.SCHEDULE.value() );
    
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
    
    tabPanel.selectTab( TabIndex.SCHEDULE.value() );
  }
  
  public void setSelectedTab( TabIndex tabKey ) {
    tabPanel.selectTab( tabKey.value() );
  }
  
  public TabIndex getSelectedTab() {
    return TabIndex.get( tabPanel.getTabBar().getSelectedTab() );
  }
  
  public void setTabError( TabIndex tabKey ) {
    tabMap.get(tabKey).setStylePrimaryName( "tabLabelError" ); //$NON-NLS-1$
  }
  
  public void clearTabError() {
    for ( Map.Entry<TabIndex, Label> me : tabMap.entrySet() ) {
      me.getValue().setStylePrimaryName( "tabLabel" ); //$NON-NLS-1$
    }
  }
}
