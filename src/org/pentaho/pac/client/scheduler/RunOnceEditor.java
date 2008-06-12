package org.pentaho.pac.client.scheduler;

import java.util.Date;

import org.pentaho.pac.client.common.ui.DatePickerEx;
import org.pentaho.pac.client.common.ui.TimePicker;
import org.pentaho.pac.client.common.ui.widget.ValidationLabel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RunOnceEditor extends VerticalPanel{

  private TimePicker startTimePicker = new TimePicker();
  private DatePickerEx startDatePicker = new DatePickerEx();
  private ValidationLabel startTimeLabel = null;
  private ValidationLabel startDateLabel = null;
  
  public RunOnceEditor() {
    startTimeLabel = new ValidationLabel( "Start Time:" );
    add( startTimeLabel );
    add( startTimePicker );
    startDateLabel = new ValidationLabel( "Start Date:" );
    add( startDateLabel );
    add( startDatePicker );
  }

  public Date getStartDate() {
    return startDatePicker.getSelectedDate();
  }
  
  public void setStartDate( Date d ) {
    startDatePicker.setSelectedDate( d );
  }

  public String getStartTime() {
    return startTimePicker.getTime();
  }
  
  public void setStartTime( String strTime ) {
    startTimePicker.setTime( strTime );
  }
  
  public boolean isValid() {
    startTimeLabel.setErrorMsg( "this is bad dude" );
    
    return false;
  }
}
