package org.pentaho.pac.client.common.util;

public class TimeUtil {

  private static final int HOURS_IN_DAY = 24;

  private static final int MINUTES_IN_HOUR = 60;

  private static final int SECONDS_IN_MINUTE = 60;

  private TimeUtil() {
  } // cannot create instance, static class

  public static int daysToSecs(int days) {
    return hoursToSecs(days * HOURS_IN_DAY);
  }

  public static int hoursToSecs(int hours) {
    return minutesToSecs(hours * MINUTES_IN_HOUR);
  }

  public static int minutesToSecs(int minutes) {
    return minutes * SECONDS_IN_MINUTE;
  }

  public static int secsToDays(int secs) {
    return secs / HOURS_IN_DAY / MINUTES_IN_HOUR / SECONDS_IN_MINUTE;
  }

  public static int secsToHours(int secs) {
    return secs / MINUTES_IN_HOUR / SECONDS_IN_MINUTE;
  }

  public static int secsToMinutes(int secs) {
    return secs / SECONDS_IN_MINUTE;
  }

  public static boolean isSecondsWholeDay(int secs) {
    return ( daysToSecs( secsToDays( secs )) ) == secs;
  }

  public static boolean isSecondsWholeHour(int secs) {
    return ( hoursToSecs( secsToHours( secs ) ) ) == secs;
  }

  public static boolean isSecondsWholeMinute(int secs) {
    return ( minutesToSecs( secsToMinutes( secs ) ) ) == secs;
  }
  
  public static void main( String[] args ) {
    assert daysToSecs( 13 ) == 1123200: ""; //$NON-NLS-1$
    assert daysToSecs( 13 ) != 1123201 : ""; //$NON-NLS-1$
    assert daysToSecs( 13 ) != 1123199 : ""; //$NON-NLS-1$
    
    assert hoursToSecs( 13 ) == 46800: ""; //$NON-NLS-1$
    assert hoursToSecs( 13 ) != 46801 : ""; //$NON-NLS-1$
    assert hoursToSecs( 13 ) != 46799 : ""; //$NON-NLS-1$
    
    assert minutesToSecs( 13 ) == 780: ""; //$NON-NLS-1$
    assert minutesToSecs( 13 ) != 781 : ""; //$NON-NLS-1$
    assert minutesToSecs( 13 ) != 779 : ""; //$NON-NLS-1$
    
    assert secsToDays( 1123200 ) == 13 : ""; //$NON-NLS-1$
    assert secsToDays( 1123201 ) != 13 : ""; //$NON-NLS-1$
    assert secsToDays( 1123199 ) != 13 : ""; //$NON-NLS-1$
    
    assert secsToHours( 46800 ) == 13 : ""; //$NON-NLS-1$
    assert secsToHours( 46801 ) != 13 : ""; //$NON-NLS-1$
    assert secsToHours( 46799 ) != 13 : ""; //$NON-NLS-1$
    
    assert secsToMinutes( 780 ) == 13 : ""; //$NON-NLS-1$
    assert secsToMinutes( 781 ) != 13 : ""; //$NON-NLS-1$
    assert secsToMinutes( 779 ) != 13 : ""; //$NON-NLS-1$
    
    assert isSecondsWholeDay( 1123200 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeDay( 1123201 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeDay( 1123199 ) : ""; //$NON-NLS-1$
    
    assert isSecondsWholeHour( 46800 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeHour( 46801 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeHour( 46799 ) : ""; //$NON-NLS-1$
    
    assert isSecondsWholeMinute( 780 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeMinute( 781 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeMinute( 779 ) : ""; //$NON-NLS-1$
    
    System.out.println( "done" ); //$NON-NLS-1$
  }
}
