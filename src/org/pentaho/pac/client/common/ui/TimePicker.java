package org.pentaho.pac.client.common.ui;

import org.pentaho.pac.client.common.util.TimeUtil;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

// TODO sbarkdull, this code needs to be locale sensitive

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
   * format of string should be: HH:MM:SS AM/PM
   * @param time
   */
  public void setTime( String time ) {
    throw new RuntimeException( "not implemented" );
  }
  
  public String getTime() {
    throw new RuntimeException( "not implemented" );
  }
  private void initHourLB() {
    hourLB.setVisibleItemCount( 1 );
    for ( int ii=1; ii<=TimeUtil.MAX_HOUR; ++ii ) {
      String strHr = Integer.toString( ii );
      strHr = ( strHr.length() == 1 ) ? "0" + strHr : strHr;  // left pad single digit values with 0
      hourLB.addItem( strHr );
    }
  }
  
  private void initMinuteLB() {
    minuteLB.setVisibleItemCount( 1 );
    for ( int ii=0; ii<TimeUtil.MAX_MINUTE; ++ii ) {
      String strMinute = Integer.toString( ii );
      strMinute = ( strMinute.length() == 1 ) ? "0" + strMinute : strMinute;  // left pad single digit values with 0
      minuteLB.addItem( strMinute );
    }
  }
  
  private void initAmPmLB() {
    timeOfDayLB.setVisibleItemCount( 1 );
    timeOfDayLB.addItem( TimeUtil.TimeOfDay.AM.toString() );
    timeOfDayLB.addItem( TimeUtil.TimeOfDay.PM.toString() );
  }

  public String getHour() {
    return hourLB.getItemText( hourLB.getSelectedIndex() );
  }

  public void setHour(String hour) {
    this.hourLB.setSelectedIndex( Integer.parseInt( hour ) );
  }

  public String getMinute() {
    return minuteLB.getItemText( minuteLB.getSelectedIndex() );
  }

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
