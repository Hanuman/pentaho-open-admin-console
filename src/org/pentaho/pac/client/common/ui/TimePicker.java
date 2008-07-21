package org.pentaho.pac.client.common.ui;

import org.pentaho.pac.client.common.EnumException;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.common.util.TimeUtil.TimeOfDay;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

// TODO sbarkdull, this code needs to be locale sensitive

/**
 * Although hours combo displays the range 1...12, the hour 
 * setter/getters take/return the range 0...11. So setting the hour to 0
 * will cause 1 to be selected in the combo.
 */
public class TimePicker extends HorizontalPanel {
  private ListBox hourLB = new ListBox();
  private ListBox minuteLB = new ListBox();
  private ListBox timeOfDayLB = new ListBox();
  
  public TimePicker() {
    
    // hour list
    initHourLB();
    
    // minute list
    initMinuteLB();
    
    // second list
    // nah!
    
    // AM/PM list
    initAmPmLB();

    add( hourLB );
    add( minuteLB );
    add( timeOfDayLB );
  }
  
  /**
   * format of string should be: HH:MM:SS AM/PM, e.g. 7:12:28 PM
   * @param time
   */
  public void setTime( String time ) {
    String[] parts = time.split( ":" ); //$NON-NLS-1$
    String[] lastParts = parts[2].split( "\\s" ); //$NON-NLS-1$
    setHour( parts[0] );
    setMinute( parts[1] );
    //setSeconds( lastParts[0] );
    TimeOfDay td = TimeOfDay.stringToTimeOfDay( lastParts[1] );
    setTimeOfDay( td );
  }
  
  /**
   * format of string should be: HH:MM:SS AM/PM, e.g. 7:12:28 PM
   * @return
   */
  public String getTime() {
    StringBuilder sb = new StringBuilder( getHour() )
      .append( ":" ) //$NON-NLS-1$
      .append( getMinute() )
      .append( ":" ) //$NON-NLS-1$
      .append( "00 " ) //$NON-NLS-1$
      .append( getTimeOfDay().toString() );
    
    return sb.toString();
  }
  private void initHourLB() {
    hourLB.setVisibleItemCount( 1 );
    for ( int ii=1; ii<=TimeUtil.MAX_HOUR; ++ii ) {
      String strHrDisplay = Integer.toString( ii );
      strHrDisplay = ( strHrDisplay.length() == 1 ) ? "0" + strHrDisplay : strHrDisplay;  // left pad single digit values with 0 //$NON-NLS-1$
      String strHrValue = (ii == 12) ? "0" : Integer.toString( ii ); //$NON-NLS-1$
      hourLB.addItem( strHrDisplay, strHrValue );
    }
  }
  
  private void initMinuteLB() {
    minuteLB.setVisibleItemCount( 1 );
    for ( int ii=0; ii<TimeUtil.MAX_MINUTE; ++ii ) {
      String strMinute = Integer.toString( ii );
      strMinute = ( strMinute.length() == 1 ) ? "0" + strMinute : strMinute;  // left pad single digit values with 0 //$NON-NLS-1$
      minuteLB.addItem( strMinute );
    }
  }
  
  private void initAmPmLB() {
    timeOfDayLB.setVisibleItemCount( 1 );
    timeOfDayLB.addItem( TimeUtil.TimeOfDay.AM.toString() );
    timeOfDayLB.addItem( TimeUtil.TimeOfDay.PM.toString() );
  }

  /**
   * Get the value (not the text) of the current selected item
   * 
   * NOTE: hours are 0...11, in spite of the fact that the combo displays 1...12
   * 0...11 maps to 12,1,...11
   * @return
   */
  public String getHour() {
    return hourLB.getValue( hourLB.getSelectedIndex() );
  }


  /**
   * Set the hour you want to see visible in the control (i.e. 1,2,3,..12)
   * 
   * NOTE: hours are 0...11, in spite of the fact that the combo displays 1...12
   * 0...11 maps to 12,1,...11
   * @return
   */
  public void setHour(String hour) {
    this.hourLB.setSelectedIndex( Integer.parseInt( hour )-1 );
  }

  /**
   * Get the value (not the text) of the current selected item
   * @return
   */
  public String getMinute() {
    return minuteLB.getValue( minuteLB.getSelectedIndex() );
  }

  /**
   * Set the minute you want to see visible in the control
   * @param minute
   */
  public void setMinute(String minute) {
    this.minuteLB.setSelectedIndex( Integer.parseInt( minute ) );
  }

  public TimeUtil.TimeOfDay getTimeOfDay() {
    return TimeUtil.TimeOfDay.get( timeOfDayLB.getSelectedIndex() );
  }

  public void setTimeOfDay(TimeUtil.TimeOfDay timeOfDay) {
    this.timeOfDayLB.setSelectedIndex( timeOfDay.value() );
  }
}
