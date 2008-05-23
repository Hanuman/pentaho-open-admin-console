package org.pentaho.pac.client.common.util;

import java.util.EnumSet;

import org.pentaho.pac.client.common.EnumException;

public class TimeUtil {

  public static final int HOURS_IN_DAY = 24;

  public static final int MINUTES_IN_HOUR = 60;

  public static final int SECONDS_IN_MINUTE = 60;

  public static final int MILLISECS_IN_SECONDS = 1000;

  public static final int MIN_HOUR = 0;
  public static final int MAX_HOUR = HOURS_IN_DAY/2;
  
  public static final int MAX_MINUTE = 60;

  public enum TimeOfDay {
    AM(0, "AM"),
    PM(1, "PM");

    TimeOfDay(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static TimeOfDay[] timeOfDay = { 
      AM, PM 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static TimeOfDay get(int idx) {
      return timeOfDay[idx];
    }

    public static int length() {
      return timeOfDay.length;
    }    
    
    public static TimeOfDay stringToTimeOfDay( String timeOfDay ) throws EnumException {
      for (TimeOfDay v : EnumSet.range(TimeOfDay.AM, TimeOfDay.PM)) {
        if ( v.toString().equals( timeOfDay ) ) {
          return v;
        }
      }
      throw new EnumException( "Invalid String for time of day value: " + timeOfDay );
    }
  } // end enum TimeOfDay
  
  public enum DayOfWeek {
    SUN(0, "Sunday"),
    MON(1, "Monday"),
    TUES(2, "Tuesday"),
    WED(3, "Wednesday"),
    THUR(4, "Thursday"),
    FRI(5, "Friday"), 
    SAT(6, "Saturday");

    DayOfWeek(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static DayOfWeek[] week = { 
      SUN, MON, TUES, WED, THUR, FRI, SAT 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static DayOfWeek get(int idx) {
      return week[idx];
    }

    public static int length() {
      return week.length;
    }
  } /* end enum */

  public enum MonthOfYear {
    JAN(0, "January"), 
    FEB(1, "February"), 
    MAR(2, "March"), 
    APR(3, "April"), 
    MAY(4, "May"), 
    JUN(5, "June"), 
    JUL(6, "July"), 
    AUG(7, "August"), 
    SEPT(8, "September"), 
    OCT(9, "October"), 
    NOV(10, "November"), 
    DEC(11, "December");

    MonthOfYear(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static MonthOfYear[] year = { 
      JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEPT, OCT, NOV, DEC 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static MonthOfYear get(int idx) {
      return year[idx];
    }

    public static int length() {
      return year.length;
    }
  } /* end enum */

  public enum WeekOfMonth {
    FIRST( 0, "first" ),
    SECOND( 1, "second" ),
    THIRD( 2, "third" ),
    FOURTH( 3, "fourth" ),
    LAST( 4, "last" );
    
    WeekOfMonth( int value, String name ) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static WeekOfMonth[] week = { 
      FIRST, SECOND, THIRD, FOURTH, LAST 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static WeekOfMonth get(int idx) {
      return week[idx];
    }

    public static int length() {
      return week.length;
    }    
    
    public static WeekOfMonth stringToWeekOfMonth( String weekOfMonth ) throws EnumException {
      for (WeekOfMonth v : EnumSet.range(WeekOfMonth.FIRST, WeekOfMonth.LAST)) {
        if ( v.toString().equals( weekOfMonth ) ) {
          return v;
        }
      }
      throw new EnumException( "Invalid String for week of month: " + weekOfMonth );
    }
  } // end enum WeekOfMonth
  
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

  public static int secsToMillisecs(int secs) {
    return secs * MILLISECS_IN_SECONDS;
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
  
  /**
   * sample output: May 21, 2008 8:29:21 PM
   * This is consistent with the formatter constructed like this:
   * DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale.getDefault());
   */
  public static String getDateTimeString( String month, String dayInMonth, String year, 
      String hour, String minute, String second, TimeOfDay timeOfDay ) {
    return new StringBuilder()
      .append( getDateString( month, dayInMonth, year ) )
      .append( " " ) //$NON-NLS-1$
      .append( hour )
      .append( ":" ) //$NON-NLS-1$
      .append( minute )
      .append( ":" ) //$NON-NLS-1$
      .append( second )
      .append( " " ) //$NON-NLS-1$
      .append( timeOfDay.toString() ).toString();
  }
  
  /**
   * sample output: May 21, 2008
   * This is consistent with the formatter constructed like this:
   * DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
   */
  public static String getDateString( String month, String dayInMonth, String year ) {
    return new StringBuilder()
      .append( month )
      .append( " " ) //$NON-NLS-1$
      .append( dayInMonth )
      .append( ", " )
      .append( year ).toString();
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
