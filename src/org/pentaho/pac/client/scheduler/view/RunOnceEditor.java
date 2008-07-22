package org.pentaho.pac.client.scheduler.view;

import java.util.Date;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.DatePickerEx;
import org.pentaho.pac.client.common.ui.TimePicker;
import org.pentaho.pac.client.common.ui.widget.ErrorLabel;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RunOnceEditor extends VerticalPanel{

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private TimePicker startTimePicker = new TimePicker();
  private DatePickerEx startDatePicker = new DatePickerEx();
  private Label startTimeLabel = null;
  private ErrorLabel startDateLabel = null;
  private static final String DEFAULT_START_HOUR = "12"; //$NON-NLS-1$
  private static final String DEFAULT_START_MINUTE = "00"; //$NON-NLS-1$
  private static final TimeUtil.TimeOfDay DEFAULT_TIME_OF_DAY = TimeUtil.TimeOfDay.AM;
  
  public RunOnceEditor() {
    startTimeLabel = new Label( MSGS.startTimeColon() );
    add( startTimeLabel );
    add( startTimePicker );
    startDateLabel = new ErrorLabel( new Label( MSGS.startDate() ) );
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
  
  public void reset( Date d ) {
    startTimePicker.setHour( DEFAULT_START_HOUR );
    startTimePicker.setMinute( DEFAULT_START_MINUTE );
    startTimePicker.setTimeOfDay( DEFAULT_TIME_OF_DAY );
    startDatePicker.setSelectedDate( d );
    startDatePicker.setYoungestDate( d );
  }
  
  public void setStartDateError( String errorMsg ) {
    startDateLabel.setErrorMsg( errorMsg );
  }
}
