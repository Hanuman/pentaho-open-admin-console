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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.pac.client.common.EnumException;
import org.pentaho.pac.client.common.ui.SimpleGroupBox;
import org.pentaho.pac.client.common.ui.widget.ValidationLabel;
import org.pentaho.pac.client.common.util.TimeUtil;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleEditor extends FlexTable {
  public enum RunType {
    RUN_ONCE(0, "Run Once"), 
    RECURRENCE(1, "Simple"), 
    CRON(2, "Advanced");

    RunType(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static RunType[] runTypes = {
      RUN_ONCE, 
      RECURRENCE, 
      CRON 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static RunType get(int idx) {
      return runTypes[idx];
    }

    public static int length() {
      return runTypes.length;
    }
    
    public static RunType stringToRunType( String strRunType ) throws EnumException {
      for (RunType v : EnumSet.range(RunType.RUN_ONCE, RunType.CRON)) {
        if ( v.toString().equals( strRunType ) ) {
          return v;
        }
      }
      throw new EnumException( "Invalid String for run type value: " + strRunType );
    }
  } /* end enum */

  private RunOnceEditor runOnceEditor = null;
  private RecurrenceEditor recurrenceEditor = null;
  private CronEditor cronEditor = null;
  private Map<RunType, Panel> runTypeEditorMap = new HashMap<RunType, Panel>();
  
  private TextBox nameTb = new TextBox();
  private TextBox groupNameTb = new TextBox();
  private TextBox descriptionTb = new TextBox();
  private ListBox recurrenceCombo = null;
  private SimpleGroupBox recurrenceGB = null;
  
  // validation labels
  private ValidationLabel nameLabel;
  private ValidationLabel groupNameLabel;

//  private String cronStr = null;
//  private String repeatInSecs = null;
  
  private static final String DEFAULT_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_GROUP_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_DESCRIPTION = ""; //$NON-NLS-1$
  
 // TODO, do it, or remvoe comment 
//  need to provide on on ok callback for recurrence dialog, set the callback in the scheduleeditor, 
//  on ok, it should set schedule editors cronStr or repeatinSecs value. clients should no longer 
//  reach around the scheudle editor to get the cron string from the recurernce dialog
//  
  public ScheduleEditor() {
    super();

    setCellPadding( 0 );
    setCellSpacing( 0 );
    
    int rowNum = 0;
    nameLabel = new ValidationLabel( "Name:" );
    setWidget( rowNum , 0, nameLabel );
    setWidget( rowNum , 1, nameTb );
    
    rowNum++;
    groupNameLabel = new ValidationLabel( "Group:" );
    setWidget( rowNum, 0, groupNameLabel );
    setWidget( rowNum, 1, groupNameTb );

    rowNum++;
    Label l = new Label( "Description:" );
    setWidget( rowNum, 0, l );
    setWidget( rowNum, 1, descriptionTb );

    rowNum++;
    recurrenceCombo = createRecurrenceCombo();
    l = new Label( "Recurrence:" );
    setWidget( rowNum, 0, l );
    setWidget( rowNum, 1, recurrenceCombo );

    rowNum++;
    recurrenceGB = new SimpleGroupBox( RunType.RUN_ONCE.toString() + " Editor" );
    VerticalPanel vp = new VerticalPanel();
    recurrenceGB.add( vp );
    this.getFlexCellFormatter().setColSpan( 4, 0, 3 );
    setWidget( rowNum, 0, recurrenceGB );

    runOnceEditor = new RunOnceEditor();
    vp.add( runOnceEditor );
    runTypeEditorMap.put( RunType.RUN_ONCE, runOnceEditor );
    runOnceEditor.setVisible( true );
    
    recurrenceEditor = new RecurrenceEditor();
    vp.add( recurrenceEditor );
    runTypeEditorMap.put( RunType.RECURRENCE, recurrenceEditor );
    recurrenceEditor.setVisible( false );
    
    cronEditor = new CronEditor();
    vp.add( cronEditor );
    runTypeEditorMap.put( RunType.CRON, cronEditor );
    cronEditor.setVisible( false );
  }

  public void reset() {
    setName( DEFAULT_NAME );
    setGroupName( DEFAULT_GROUP_NAME );
    setDescription( DEFAULT_DESCRIPTION );
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
  
//  runOnceEditor
//  recurrenceEditor
//  cronEditor
  
  public String getCronString() {
    switch ( getRunType() ) {
      case RUN_ONCE:
        return null;
      case RECURRENCE:
        return recurrenceEditor.getCronString();
      case CRON:
        return cronEditor.getCronString();
      default:
        throw new RuntimeException( "Invalid Run Type: " + getRunType().toString() );
    }
  }
  
  /**
   * 
   * @param cronStr
   * @throws CronParseException if cronStr is not a valid CRON string.
   */
  public void setCronString( String cronStr ) throws CronParseException {

    CronParser cp = new CronParser( cronStr );
    String recurrenceStr = null;
    recurrenceStr = cp.parseToRecurrenceString(); // throws CronParseException

    if ( null != recurrenceStr ) {
      recurrenceEditor.inititalizeWithRecurrenceString( recurrenceStr );
      setRunType( RunType.RECURRENCE );
    } else {
      // its a cron string that cannot be parsed into a recurrence string, switch to cron string editor.
      setRunType( RunType.CRON );
    }
    
    cronEditor.setCronString( cronStr );
  }

  
  /**
   * 
   * @return null if the selected schedule does not support repeat-in-seconds, otherwise
   * return the number of seconds between schedule execution.
   * @throws RuntimeException if the temporal value is invalid. This
   * condition occurs as a result of programmer error.
   */
  public Integer getRepeatInSecs() throws RuntimeException {
    return recurrenceEditor.getRepeatInSecs();
  }

  public void setRepeatInSecs( Integer repeatInSecs ) {
    recurrenceEditor.inititalizeWithRepeatInSecs( repeatInSecs );
    setRunType( RunType.RECURRENCE );
  }
  
  private ListBox createRecurrenceCombo() {
    final ScheduleEditor localThis = this;
    ListBox lb = new ListBox();
    lb.setVisibleItemCount( 1 );
    lb.setStyleName("recurrenceCombo"); //$NON-NLS-1$
    lb.addChangeListener( new ChangeListener() {
      public void onChange(Widget sender) {
        localThis.handleRecurrenceChange();
      }
    });
    
    lb.addItem( RunType.RUN_ONCE.toString() );
    lb.setItemSelected( 0, true );
    lb.addItem( RunType.RECURRENCE.toString() );
    lb.addItem( RunType.CRON.toString() );

    return lb;
  }
  
  public RunType getRunType() {
    String selectedValue = recurrenceCombo.getValue( recurrenceCombo.getSelectedIndex() );
    return RunType.stringToRunType( selectedValue );
  }
  
  public void setRunType( RunType runType ) {
    recurrenceCombo.setSelectedIndex( runType.value() );
    selectRunTypeEditor( runType );
  }
  
  public void setStartTime( String startTime ) {
    runOnceEditor.setStartTime( startTime );
    recurrenceEditor.setStartTime( startTime );
  }
  
  public String getStartTime() {
    switch ( getRunType() ) {
      case RUN_ONCE:
        return runOnceEditor.getStartTime();
      case RECURRENCE:
        return recurrenceEditor.getStartTime();
      case CRON:
        return cronEditor.getStartTime();
      default:
        throw new RuntimeException( "Invalid Run Type: " + getRunType().toString() );
    }
  }

  public void setStartDate( Date startDate ) {
    runOnceEditor.setStartDate( startDate );
    recurrenceEditor.setStartDate( startDate );
    cronEditor.setStartDate( startDate );
  }
  
  public Date getStartDate() {
    switch ( getRunType() ) {
      case RUN_ONCE:
        Date startDate = runOnceEditor.getStartDate();
        String startTime  = runOnceEditor.getStartTime();
        Date startDateTime = TimeUtil.getDateTime( startTime, startDate );
        return startDateTime;
      case RECURRENCE:
        return recurrenceEditor.getStartDate();
      case CRON:
        return cronEditor.getStartDate();
      default:
        throw new RuntimeException( "Invalid Run Type: " + getRunType().toString() );
    }
  }

  public void setEndDate( Date endDate ) {
    recurrenceEditor.setEndDate( endDate );
    cronEditor.setEndDate( endDate );
  }

  public Date getEndDate() {
    switch ( getRunType() ) {
      case RUN_ONCE:
        return null;
      case RECURRENCE:
        return recurrenceEditor.getEndDate();
      case CRON:
        return cronEditor.getEndDate();
      default:
        throw new RuntimeException( "Invalid Run Type: " + getRunType().toString() );
    }
  }

  public void setNoEndDate() {
    recurrenceEditor.setNoEndDate();
    cronEditor.setNoEndDate();
  }

  public void setEndBy() {
    recurrenceEditor.setEndBy();
    cronEditor.setEndBy();
  }

  private void handleRecurrenceChange() throws EnumException {
    RunType rVal = getRunType();
    selectRunTypeEditor( rVal );
  }


  private void selectRunTypeEditor(RunType selectedRunType) {
    for ( Map.Entry<RunType, Panel> me : runTypeEditorMap.entrySet() ) {
      boolean bShow = me.getKey().equals( selectedRunType );
      me.getValue().setVisible( bShow );
    }
    recurrenceGB.setCaption( selectedRunType.toString() + " Editor"  );
  }
  
  /**
   * @param errorMsg String null or "" to clear the error msg, else
   * set the error msg to <param>errorMsg</param>.
   */
  public void setNameError( String errorMsg ) {
    nameLabel.setErrorMsg( errorMsg );
  }
  
  public void setGroupNameError( String errorMsg ) {
    groupNameLabel.setErrorMsg( errorMsg );
  }
}
