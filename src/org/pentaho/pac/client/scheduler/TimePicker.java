package org.pentaho.pac.client.scheduler;

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

public class TimePicker extends HorizontalPanel {
  private ListBox hourLB = new ListBox();
  private ListBox minuteLB = new ListBox();
  private ListBox amPmLB = new ListBox();
  
  public static final int MAX_HOUR = 12;
  public static final int MAX_MINUTE = 60;
  public static final String AM = "AM";
  public static final String PM = "PM";
  
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
    add( amPmLB );
  }
  
  /**
   * format of string should be: HH:MM:AM/PM
   * @param time
   */
  public void setTime( String time ) {
    
  }
  
  public String getTime() {
    return "";
  }
  private void initHourLB() {
    hourLB.setVisibleItemCount( 1 );
    for ( int ii=1; ii<=MAX_HOUR; ++ii ) {
      String strHr = Integer.toString( ii );
      hourLB.addItem( strHr );
    }
  }
  
  private void initMinuteLB() {
    minuteLB.setVisibleItemCount( 1 );
    for ( int ii=0; ii<MAX_MINUTE; ++ii ) {
      String strMinute = Integer.toString( ii );
      minuteLB.addItem( strMinute );
    }
  }
  
  private void initAmPmLB() {
    amPmLB.setVisibleItemCount( 1 );
    amPmLB.addItem( AM );
    amPmLB.addItem( PM );
  }
}
