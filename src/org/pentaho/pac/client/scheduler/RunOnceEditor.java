package org.pentaho.pac.client.scheduler;

import java.util.Date;

import org.pentaho.pac.client.common.ui.DatePickerEx;
import org.pentaho.pac.client.common.ui.TimePicker;

import com.google.gwt.user.client.ui.VerticalPanel;

public class RunOnceEditor extends VerticalPanel{

  private TimePicker startTimePicker = new TimePicker();
  private DatePickerEx startDatePicker = new DatePickerEx();
  
  public RunOnceEditor() {
    add( startTimePicker );
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
}
