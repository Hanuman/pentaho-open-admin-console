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

import org.pentaho.pac.client.common.ui.DatePickerEx;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.TimePicker;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.common.util.StringUtils;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleEditor extends FlexTable {

  private TextBox nameTb = new TextBox();
  private TextBox groupNameTb = new TextBox();
  private TextBox descriptionTb = new TextBox();
  
  private TimePicker startTimePicker = new TimePicker();
  private DatePickerEx startDatePicker = new DatePickerEx();

  private String cronStr = null;
  private String repeatInSecs = null;
  
  private static final String DEFAULT_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_GROUP_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_DESCRIPTION = ""; //$NON-NLS-1$
  private static final String DEFAULT_CRONSTRING = null;
  private static final String DEFAULT_REPEAT_IN_SECS = null;
  
  private RecurrenceDialog recurrenceDialog = new RecurrenceDialog();
  private boolean bRecurrenceEditorValid = false;
  
  
//  need to provide on on ok callback for recurrence dialog, set the callback in the scheduleeditor, 
//  on ok, it should set schedule editors cronStr or repeatinSecs value. clients should no longer 
//  reach around the scheudle editor to get the cron string from the recurernce dialog
//  
  public ScheduleEditor() {
    super();

    setCellPadding( 0 );
    setCellSpacing( 0 );
    
    Label l = new Label( "Name:" );
    setWidget( 0 , 0, l );
    setWidget( 0 , 1, nameTb );
    
    l = new Label( "Group:" );
    setWidget( 1 , 0, l );
    setWidget( 1 , 1, groupNameTb );
    
    l = new Label( "Description:" );
    setWidget( 2 , 0, l );
    setWidget( 2 , 1, descriptionTb );
    
    recurrenceDialog.setOnOkHandler( new ICallback<Object>() {
      public void onHandle(Object o) {
        bRecurrenceEditorValid = true;
        cronStr = recurrenceDialog.getRecurrenceEditor().getCronString();
        if ( null == cronStr ) {
          repeatInSecs = recurrenceDialog.getRecurrenceEditor().getRepeatInSecs();
        }
        startTimePicker.setTime( recurrenceDialog.getRecurrenceEditor().getStartTime() );
        startDatePicker.setSelectedDate( recurrenceDialog.getRecurrenceEditor().getStartDate() );
        recurrenceDialog.hide();
      }
    });
    
    recurrenceDialog.setOnRemoveRecurrence( new ICallback<Object>() {
      public void onHandle(Object o) {
        bRecurrenceEditorValid = false;
      }
    });
    
    Button btn = new Button( "Recurrence...", new ClickListener() {
      public void onClick(Widget sender) {
        if ( !StringUtils.isEmpty( cronStr ) ) {
          CronParser cp = new CronParser( cronStr );
          try {
            cp.parse();
            recurrenceDialog.getRecurrenceEditor().inititalizeWithRecurrenceString( cp.getRecurrenceString() );
          } catch (CronParseException e) {
            final MessageDialog errorDialog = new MessageDialog( "Error", "Attempt to initialize the recurrence dialog with an invalid CRON string: " + cronStr );
            errorDialog.setOnOkHandler( new ICallback() {
              public void onHandle(Object o) {
                recurrenceDialog.getRecurrenceEditor().reset();
                recurrenceDialog.center();
                errorDialog.hide();
              }
            });
            errorDialog.center();
            return;
          }
        } else if ( !StringUtils.isEmpty( repeatInSecs ) ) {
          int secs = Integer.parseInt( repeatInSecs );
          recurrenceDialog.getRecurrenceEditor().inititalizeWithRepeatInSecs( secs );
        } else {
          // fine, do nothing, default initialization
        }
        recurrenceDialog.getRecurrenceEditor().setStartTime( startTimePicker.getTime() );
        recurrenceDialog.getRecurrenceEditor().setStartDate( startDatePicker.getSelectedDate() );
        recurrenceDialog.center();
      }
    });
    setWidget( 3 , 1, btn );
    
    l = new Label( "Start Date:" );
    setWidget( 4 , 0, l );
    setWidget( 4 , 1, startDatePicker );
    startDatePicker.setYoungestDate( new Date() );
    
    l = new Label( "Start Time:" );
    setWidget( 5 , 0, l );
    setWidget( 5 , 1, startTimePicker );
  }

  public void reset() {
    setName( DEFAULT_NAME );
    setGroupName( DEFAULT_GROUP_NAME );
    setDescription( DEFAULT_DESCRIPTION );
    setCronString( DEFAULT_CRONSTRING );
    setRepeatInSecs( DEFAULT_REPEAT_IN_SECS );
    recurrenceDialog.getRecurrenceEditor().reset();
  }
  
  public String getName() {
    return nameTb.getText();
  }
  
  public void setName( String name ) {
    nameTb.setText( name );
  }
  
  public String getGroupName() {
    return groupNameTb.getText();
  }
  
  public void setGroupName( String groupName ) {
    groupNameTb.setText( groupName );
  }
  
  public String getDescription() {
    return descriptionTb.getText();
  }
  
  public void setDescription( String description ) {
    descriptionTb.setText( description );
  }
  
  public String getCronString() {
    return bRecurrenceEditorValid 
    ? cronStr
    : null;
  }
  
  public void setCronString( String cronStr ) {
    this.cronStr = cronStr;
    this.repeatInSecs = null;
  }

  public String getRepeatInSecs() {
    return bRecurrenceEditorValid
      ? repeatInSecs
      : null;
  }

  public void setRepeatInSecs(String repeatInSec) {
    this.cronStr = null;
    this.repeatInSecs = repeatInSec;
  }
  
  public void setStartTime( String startTime ) {
    startTimePicker.setTime( startTime );
  }
  
  public String getStartTime() {
    return startTimePicker.getTime();
  }
  
  public void setStartDate( Date startDate ) {
    startDatePicker.setSelectedDate( startDate );
  }
  
  public Date getStartDate() {
    return startDatePicker.getSelectedDate();
  }

  // TODO sbarkdull, this only works if they have set a recurrence, what to do if they havent?
  // recurrence editor needs a state indicating if it's values are active or not.
  public void setEndDate( Date endDate ) {
    recurrenceDialog.getRecurrenceEditor().setEndDate( endDate );
  }
  // TODO sbarkdull, this only works if they have set a recurrence, what to do if they havent?
  public Date getEndDate() {
    return bRecurrenceEditorValid
      ? recurrenceDialog.getRecurrenceEditor().getEndDate()
      : null;
  }

  public void setNoEndDate() {
    recurrenceDialog.getRecurrenceEditor().setNoEndDate();
  }

  public void setEndBy() {
    recurrenceDialog.getRecurrenceEditor().setEndBy();
  }
  
  public boolean isRecurrenceEditorValid() {
    return bRecurrenceEditorValid;
  }
}
