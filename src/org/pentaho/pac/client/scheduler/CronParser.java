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

import java.util.EnumSet;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class CronParser {

  private String cronStr;
  private RecurrenceType recurrenceType = RecurrenceType.Invalid;
  private int startSecond = -1;
  private int startMinute = -1;
  private int startHour = -1;
  
  // for use by internal algorithms
  private String dayOfMonthToke = null; // from cronString[3]
  private String monthToke = null;    // from cronString[4]
  private String dayOfWeekToke = null;  // from cronString[5]

  public final static int REQUIRED_NUM_TOKENS = 6;
  public final static String ALL = "*"; //$NON-NLS-1$
  public final static String DONT_CARE = "?"; //$NON-NLS-1$
  public final static String N_TH = "#"; //$NON-NLS-1$
  public final static String LAST = "L"; //$NON-NLS-1$
  
  enum CronField {
    SECONDS(0),
    MINUTES(1),
    HOURS(2),
    DAY_OF_MONTH(3),
    MONTH(4),
    DAY_OF_WEEK(5),
    YEAR(6);
    
    CronField( int value ) {
      this.value = value;
    }
    private final int value;
    public int value() { return value; }
  }
  enum RecurrenceType {
    Invalid,
    EveryNthDayOfMonth,
    EveryWeekday,
    WeeklyOn,
    DayNOfMonth,
    NthDayNameOfMonth,
    LastDayNameOfMonth,
    EveryMonthNameN,
    NthDayNameOfMonthName,
    LastDayNameOfMonthName;
    
    public static RecurrenceType stringToScheduleType( String str ) {
      for (RecurrenceType d : EnumSet.range(RecurrenceType.EveryNthDayOfMonth, RecurrenceType.LastDayNameOfMonthName)) {
        if ( d.toString().equals( str ) ) {
          return d;
        }
      }
      return Invalid;
    }
  };
  
  public CronParser( String cronStr ) {
    this.cronStr = cronStr.trim();
  }
  
  private static boolean isNumBetween( int low, int num, int high ) {
    return num >= low && num <= high;
  }
  
  private static boolean isDayOfMonth( int num ) {
    return isNumBetween( 1, num, 31 );
  }
  
  private static boolean isDayOfWeek( int num ) {
    return isNumBetween( 1, num, 7 );
  }
  
  private static boolean isWeekOfMonth( int num ) {
    return isNumBetween( 1, num, 4 );
  }
  
  private static boolean isMonthOfYear( int num ) {
    return isNumBetween( 1, num, 12 );
  }
  
  private static boolean isSecond( int num ) {
    return isNumBetween( 0, num, 59 );
  }
  
  private static boolean isMinute( int num ) {
    return isNumBetween( 0, num, 59 );
  }
  
  private static boolean isHour( int num ) {
    return isNumBetween( 0, num, 23 );
  }
  
  private static boolean isMultipleOfN( int testValue, int n ) {
    assert n != 0 : "isMultipleOfN(), n cannot be zero.";
    return ( ( testValue / n ) * n ) == testValue;
  }

  // NOTE: Integer.MAX_VALUE = 2147483647
  private static final String IS_NUM_RE = "\\d{1,10}"; //$NON-NLS-1$
  // NOTE: does not properly handle negative integers
  private static boolean isInt( String sample ) {
    return sample.matches( IS_NUM_RE );
  }

  public String getCronString() {
    return cronStr;
  }
  
  public int getStartSecond() {
    return startSecond;
  }
  public int getStartMinute() {
    return startMinute;
  }
  public int getStartHour() {
    return startHour;
  }
  
  private static final String EVERY_N_DAYS_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+0/\\d{1,2}\\s+\\*\\s+\\?$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH 0/N * ?    fires at hour HH and minute MM, starting on the 1st, every N days.
   * @param cronStr
   * @return
   */
  private boolean isEveryNthDayOfMonthToken() {

    return cronStr.matches( EVERY_N_DAYS_OF_MONTH_RE );
  }
  
  private static final String EVERY_WEEK_DAY_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\*\\s+2\\-6$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH ? * 2-6
   * @param cronStr
   * @return
   */
  private boolean isEveryWeekdayToken() {
    return cronStr.matches( EVERY_WEEK_DAY_RE );
  }
  
  private static final String WEEKLY_ON_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\*\\s+\\d(,\\d){0,6}$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH ? * DAY-LIST, where DAY-LIST is like 1,4,5
   * @param cronStr
   * @return
   */
  private boolean isWeeklyOnToken() {
    return cronStr.matches( WEEKLY_ON_RE );
  }
  
  private static final String DAY_N_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\*\\s+\\?$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH DAY * ?    Fires at HH:MM on the DAY (i.e. 1-31) of each month
   * @param dayOfMonthToke
   * @param monthToke
   * @param dayOfWeekToke
   * @return
   */
  private boolean isDayNOfMonthToken() {
    boolean bReMatch = cronStr.matches( DAY_N_OF_MONTH_RE );
    return bReMatch;
  }

  /**
   * Abstract cron string: 0 MM HH ? * N#DAY
   * (N ε {1,2,3,4} DAY ε {1,2,3,4,5,6,7})
   */
  private static final String NTH_DAY_NAME_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s\\*\\s+[1-4]{1}#[1-7]{1}$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH ? * N#DAY
   * @param dayOfMonthToke
   * @param monthToke
   * @param dayOfWeekToke
   * @return
   */
  private boolean isNthDayNameOfMonthToken() {
    boolean bReMatch = cronStr.matches( NTH_DAY_NAME_OF_MONTH_RE );
    return bReMatch;
  }

  private static final String LAST_DAY_NAME_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\*\\s+[1-7]{1}L$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH ? * DAYL
   * (DAY ε {1,2,3,4,5,6,7}, where these integers map to days of the week)
   * 
   * @param dayOfMonthToke
   * @param monthToke
   * @param dayOfWeekToke
   * @return
   */
  private boolean isLastDayNameOfMonthToken() {
    boolean bReMatch = cronStr.matches( LAST_DAY_NAME_OF_MONTH_RE );
    return bReMatch;
  }
  
  private static final String EVERY_MONTH_NAME_N_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH N MONTH ? (N ε {1-31} MONTH ε {1-12})
   * @param cronStr
   * @return
   */
  private boolean isEveryMonthNameNToken() {
    boolean bReMatch = cronStr.matches( EVERY_MONTH_NAME_N_RE );
    return bReMatch;
  }
  
  private static final String N_DAY_NAME_OF_MONTH_NAME_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\d{1,2}\\s+\\d{1}#\\d{1}$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH ? MONTH N#DAY (MONTH ε {1-12}, N ε {1,2,3,4} DAY ε {1,2,3,4,5,6,7})
   * @param cronStr
   * @return
   */
  private boolean isNthDayNameOfMonthNameToken() {
    boolean bReMatch = cronStr.matches( N_DAY_NAME_OF_MONTH_NAME_RE );
    return bReMatch;
  }
  
  private static final String LAST_DAY_NAME_OF_MONTH_NAME_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\d{1,2}\\s+\\d{1}L$"; //$NON-NLS-1$
  /**
   * Abstract cron string: 0 MM HH ? MONTH DAYL (MONTH ε {1-12}, DAY ε {1,2,3,4,5,6,7})
   * @param cronStr
   * @return
   */
  private boolean isLastDayNameOfMonthNameToken() {
    boolean bReMatch = cronStr.matches( LAST_DAY_NAME_OF_MONTH_NAME_RE );
    return bReMatch;
  }
  
  public RecurrenceType getSchedType() {
    return recurrenceType;
  }
  
  private RecurrenceType getScheduleType() throws CronParseException {
    if ( isEveryNthDayOfMonthToken() ) {
      return RecurrenceType.EveryNthDayOfMonth;
    } else if ( isEveryWeekdayToken() ) {
      return RecurrenceType.EveryWeekday;
    } else if ( isWeeklyOnToken() ) {
      return RecurrenceType.WeeklyOn;
    } else if ( isDayNOfMonthToken() ) {
      return RecurrenceType.DayNOfMonth;
    } else if ( isNthDayNameOfMonthToken() ) {
      return RecurrenceType.NthDayNameOfMonth;
    } else if ( isLastDayNameOfMonthToken() ) {
      return RecurrenceType.LastDayNameOfMonth;
    } else if ( isEveryMonthNameNToken() ) {
      return RecurrenceType.EveryMonthNameN;
    } else if ( isNthDayNameOfMonthNameToken() ) {
      return RecurrenceType.NthDayNameOfMonthName;
    } else if ( isLastDayNameOfMonthNameToken() ) {
      return RecurrenceType.LastDayNameOfMonthName;
    } else {
      throw new CronParseException( "Failed to determine the schedule type." );
    }
  }
  
  public void parse() throws CronParseException {
    
    String[] tokens = cronStr.split( "\\s" ); // split on white space
    if ( REQUIRED_NUM_TOKENS != tokens.length ) {
      throw new CronParseException( "Invalid number of tokens" );
    }
    
    if ( isInt( tokens[CronField.SECONDS.value] ) ) {
      int num = Integer.parseInt( tokens[CronField.SECONDS.value] );
      if ( !isSecond( num ) ) {
        throw new CronParseException( "Seconds token must be an integer between 0 and 59, but it is: " + tokens[0] );
      } else {
        startSecond = num;
      }
    }
    
    if ( isInt( tokens[CronField.MINUTES.value] ) ) {
      int num = Integer.parseInt( tokens[CronField.MINUTES.value] );
      if ( !isMinute( num ) ) {
        throw new CronParseException( "Minute token must be an integer between 0 and 59, but it is: " + tokens[0] );
      } else {
        startMinute = num;
      }
    }
    
    if ( isInt( tokens[CronField.HOURS.value] ) ) {
      int num = Integer.parseInt( tokens[CronField.HOURS.value] );
      if ( !isHour( num ) ) {
        throw new CronParseException( "Hours token must be an integer between 0 and 23, but it is: " + tokens[0] );
      } else {
        startHour = num;
      }
    }

    dayOfMonthToke = tokens[CronField.DAY_OF_MONTH.value];
    monthToke = tokens[CronField.MONTH.value];
    dayOfWeekToke = tokens[CronField.DAY_OF_WEEK.value];
    
    recurrenceType = getScheduleType();
  }
  
  public boolean isDayOfMonthValid() {
    switch( this.recurrenceType ) {
    case EveryNthDayOfMonth:
      // fall through
    case DayNOfMonth:
      // fall through
    case EveryMonthNameN:
      return true;
    default:
      return false;
    }
  }
  
  public int getDayOfMonth() throws CronParseException {
    String strVal;
    switch( this.recurrenceType ) {
    case EveryNthDayOfMonth:
      // token 3 is 0/N
      strVal = dayOfMonthToke.split( "/" )[1];
      break;
    case DayNOfMonth:
      // token 3 is N
      // fall through to next
    case EveryMonthNameN:
      // token 3 is N
      strVal = dayOfMonthToke;
      break;
    default:
      //oops
      throw new CronParseException( "getDayOfMonth() not valid for recurrence type: " + this.recurrenceType.toString() );
    }
    int dayOfMonth = Integer.parseInt( strVal );
    if ( !isDayOfMonth( dayOfMonth ) ) {
      throw new CronParseException( "Invalid day of month: " + strVal ); 
    }
    return dayOfMonth;
  }

  public boolean isDaysOfWeekValid() {
    switch( this.recurrenceType ) {
    case WeeklyOn:
      return true;
    default:
      return false;
    }
  }
  
  public int[] getDaysOfWeek() throws CronParseException {
    switch( this.recurrenceType ) {
    case WeeklyOn:
      // token 5 is comma separated list of 1-7 unique integers between 1 and 7
      String[] days = dayOfWeekToke.split( "," ); //$NON-NLS-1$
      int[] intDays = new int[ days.length ];
      for ( int ii=0; ii<days.length; ++ii ) {
        intDays[ii] = Integer.parseInt( days[ ii ] );
        if ( !isDayOfWeek( intDays[ii] ) ) {
          throw new CronParseException( "Invalid day of week: " + days[ii] );
        }
      }
      return intDays;
    default:
      throw new CronParseException( "getDaysOfWeek() not valid for recurrence type: " + this.recurrenceType.toString() );
    }
  }
  
  public boolean isWhichWeekOfMonthValid() {
    switch( this.recurrenceType ) {
    case NthDayNameOfMonth:
      // fall through
    case NthDayNameOfMonthName:
      // token 5 is N#DAY, want N
      return true;
    default:
      return false;
    }
  }
  
  /**
   * 
   * @return integer between 1 and 4 inclusive
   * @throws CronParseException
   */
  public int getWhichWeekOfMonth() throws CronParseException {
    String strVal;
    switch( this.recurrenceType ) {
    case NthDayNameOfMonth:
      // fall through
    case NthDayNameOfMonthName:
      // token 5 is N#DAY, want N
      strVal = dayOfWeekToke.split( N_TH )[1];
      int weekOfMonth = Integer.parseInt( strVal );
      if ( !isWeekOfMonth( weekOfMonth ) ) {
        throw new CronParseException( "Invalid week of month: " + strVal );
      }
      return weekOfMonth;
    default:
      throw new CronParseException( "getWhichWeekOfMonth() not valid for recurrence type: " + this.recurrenceType.toString() );
    }
  }

  public boolean isWhichDayOfWeekValid() {
    switch( this.recurrenceType ) {
    case NthDayNameOfMonth:
      // fall through
    case NthDayNameOfMonthName:
      // fall through
    case LastDayNameOfMonth:
      // fall through
    case LastDayNameOfMonthName:
      // fall through
      return true;
    default:
      return false;
    }
  }
  
  /**
   * 
   * @return integer between 1 and 7 inclusive
   * @throws CronParseException
   */
  public int getWhichDayOfWeek() throws CronParseException {
    int dayOfWeek;
    switch( this.recurrenceType ) {
    case NthDayNameOfMonth:
      // fall through
    case NthDayNameOfMonthName:
      // token 5 is N#DAY, want DAY
      dayOfWeek = Integer.parseInt( dayOfWeekToke.split( N_TH )[0] );
      break;
    case LastDayNameOfMonth:
      // fall through
    case LastDayNameOfMonthName:
      // token 5 is NL, want N
      String strDay = dayOfWeekToke.substring( 0, dayOfWeekToke.length() - 1 ); // trim off the trailing L
      dayOfWeek = Integer.parseInt( strDay );
      break;
    default:
      throw new CronParseException( "getWhichDayOfWeek() not valid for recurrence type: " + this.recurrenceType.toString() );
    }
    
    if ( !isDayOfWeek( dayOfWeek) ) {
      throw new CronParseException( "Invalid day of week: " + Integer.toString( dayOfWeek ) );
    }
    return dayOfWeek;
  }
  
  public boolean isWhichMonthOfYearValid() {
    switch( this.recurrenceType ) {
    case EveryMonthNameN:
      // fall through
    case NthDayNameOfMonthName:
      // fall through
    case LastDayNameOfMonthName:
      // fall through
      return true;
    default:
      return false;
    }
  }
  
  /**
   * 
   * @return integer between 1 and 12 inclusive
   * @throws CronParseException
   */
  public int getWhichMonthOfYear() throws CronParseException {
    int monthOfYear;
    switch( this.recurrenceType ) {
    case EveryMonthNameN:
      // fall through
    case NthDayNameOfMonthName:
      // fall through
    case LastDayNameOfMonthName:
      // token 4 is N
      monthOfYear = Integer.parseInt( monthToke );
      break;
    default:
      throw new CronParseException( "getWhichMonthOfYear() not valid for recurrence type: " + this.recurrenceType.toString() );
    }
    if ( !isMonthOfYear( monthOfYear ) ) {
      throw new CronParseException( "Invalid month of year: " + monthToke );
    }
    return monthOfYear;
  }
  
  /**
   * Tokens:
   * 1. The recurrence type, one of the values in the enum RecurrenceType
   * 2. start second (0-59)
   * 3. start minute (0-59)
   * 4. start hour (0-24)
   * 5. if valid, a comma separated list of integers that map to the days of the week (1-7), see isDaysOfWeekValid for valid RecurrenceTypes
   * 6. if valid, an integer representing a day of the month (1-31), see isDayOfMonthValid for valid RecurrenceTypes
   * 7. if valid, an integer representing a day of the week (1-7), see isWhichDayOfWeekValid for valid RecurrenceTypes
   * 8. if valid, an integer representing a week of the month (1-4), see isWhichWeekOfMonthValid for valid RecurrenceTypes
   * 9. if valid, an integer representing a month of the year (1-12), see isWhichMonthOfYearValid for valid RecurrenceTypes
   * All or none of the items 5-9 may be present. Tokens are order dependent. Tokens are not position dependent,
   * meaning that the same token may occur and position N in one recurrence type, and position N+1 in others.
   * @return
   * @throws CronParseException
   */
  public String getRecurrenceString() throws CronParseException {
    StringBuilder sb = new StringBuilder();
    sb.append( this.recurrenceType.toString() ).append( " " ); //$NON-NLS-1$

    sb.append( startSecond ).append( " " ).append( startMinute ).append( " " ).append( startHour ).append( " " ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    
      if ( isDaysOfWeekValid() ) {
        int[] days = getDaysOfWeek();
        for ( int ii=0; ii<days.length; ++ii ) {
          int day = days[ii];
          sb.append( day ).append( (ii<days.length-1) ? "," : "" ); //$NON-NLS-1$ //$NON-NLS-2$
        }
        sb.append( " " ); //$NON-NLS-1$
      }
      if ( isDayOfMonthValid() ) {
        sb.append( getDayOfMonth() ).append( " " ); //$NON-NLS-1$
      }
      if ( isWhichDayOfWeekValid() ) {
        sb.append( getWhichDayOfWeek() ).append( " " ); //$NON-NLS-1$
      }
      if ( isWhichWeekOfMonthValid() ) {
        sb.append( getWhichWeekOfMonth() ).append( " " ); //$NON-NLS-1$
      }
      if ( isWhichMonthOfYearValid() ) {
        sb.append( getWhichMonthOfYear() ).append( " " ); //$NON-NLS-1$
      }
    
    return sb.toString().trim(); 
  }

  private static void validateIsInt( String strInt ) throws CronParseException {
    if ( !isInt( strInt ) ) {
      throw new CronParseException( "Invalid token, must be an integer: " + strInt );
    }
  }
  
  private static void validateIsCommaSeparatedListOfInt( String strInts ) throws CronParseException {

    String[] ints = strInts.split( "," ); //$NON-NLS-1$
    for ( int ii=0; ii<ints.length; ++ii ) {
      String strInt = ints[ii];
      if ( !isInt( strInt ) ) {
        throw new CronParseException( "Invalid token, must be a list of integers: " + strInts );
      }
    }
  }
  /**
   * Given a recurrenceTokenString, generate the corresponding CRON string.
   * 
   * @param recurrenceStr String
   * @return
   * @throws CronParseException
   */
  public static String RecurrenceStringToCronString( String recurrenceTokenString ) throws CronParseException {
    StringBuilder sb = new StringBuilder();
    String cronToken3, cronToken4, cronToken5;
    String[] recurrenceTokens = recurrenceTokenString.trim().split( "\\s+" ); //$NON-NLS-1$

    validateIsInt( recurrenceTokens[1] );
    validateIsInt( recurrenceTokens[2] );
    validateIsInt( recurrenceTokens[3] );
    sb.append( recurrenceTokens[1] ).append( " " ) //$NON-NLS-1$
      .append( recurrenceTokens[2] ).append( " " ) //$NON-NLS-1$
      .append( recurrenceTokens[3] ).append( " " ); //$NON-NLS-1$
    
    RecurrenceType st = RecurrenceType.stringToScheduleType( recurrenceTokens[0] );
    switch ( st ) {
    case EveryNthDayOfMonth:
      validateIsInt( recurrenceTokens[4] );
      cronToken3 = "0/" + recurrenceTokens[4]; //$NON-NLS-1$
      cronToken4 = ALL;
      cronToken5 = DONT_CARE;
      break;
    case EveryWeekday:
      cronToken3 = DONT_CARE;
      cronToken4 = ALL;
      cronToken5 = "2-6"; //$NON-NLS-1$
      break;
    case WeeklyOn:
      validateIsCommaSeparatedListOfInt( recurrenceTokens[4] );
      cronToken3 = DONT_CARE;
      cronToken4 = ALL;
      cronToken5 = recurrenceTokens[4];
      break;
    case DayNOfMonth:
      validateIsInt( recurrenceTokens[4] );
      cronToken3 = recurrenceTokens[4];
      cronToken4 = ALL;
      cronToken5 = DONT_CARE;
      break;
    case NthDayNameOfMonth:
      validateIsInt( recurrenceTokens[4] );
      validateIsInt( recurrenceTokens[5] );
      cronToken3 = DONT_CARE;
      cronToken4 = ALL;
      cronToken5 = recurrenceTokens[4] + N_TH + recurrenceTokens[5];
      break;
    case LastDayNameOfMonth:
      validateIsInt( recurrenceTokens[4] );
      cronToken3 = DONT_CARE;
      cronToken4 = ALL;
      cronToken5 = recurrenceTokens[4] + LAST;
      break;
    case EveryMonthNameN:
      validateIsInt( recurrenceTokens[4] );
      validateIsInt( recurrenceTokens[5] );
      cronToken3 = recurrenceTokens[4];
      cronToken4 = recurrenceTokens[5];
      cronToken5 = DONT_CARE;
      break;
    case NthDayNameOfMonthName:
      validateIsInt( recurrenceTokens[4] );
      validateIsInt( recurrenceTokens[5] );
      validateIsInt( recurrenceTokens[6] );
      cronToken3 = DONT_CARE;
      cronToken4 = recurrenceTokens[6];
      cronToken5 = recurrenceTokens[4] + N_TH + recurrenceTokens[5];
      break;
    case LastDayNameOfMonthName:
      cronToken3 = DONT_CARE;
      cronToken4 = recurrenceTokens[5];
      cronToken5 = recurrenceTokens[4] + LAST;
      break;
    default:
      throw new CronParseException( "Invalid recurrenceType: " + recurrenceTokens[0] );
    }
    sb.append( cronToken3 ).append( " " ); //$NON-NLS-1$
    sb.append( cronToken4 ).append( " " ); //$NON-NLS-1$
    sb.append( cronToken5 ).append( " " ); //$NON-NLS-1$
    
    return sb.toString().trim();
  }
  
  public static void main( String[] args ) {
    
//      for (CronField f : EnumSet.range(CronField.SECONDS, CronField.YEAR)) {
//          System.out.println(f);
//          System.out.println(f.ordinal());
//      }
//      
//      for ( int ii=0; ii< 256; ++ii ) {
//        boolean b = isMultipleOfN( ii, 24 );
//        if ( b ) {
//          System.out.println( "isMultipleOfN( " + ii + ", 24): " + b );
//        }
//      }

    
    String[] cronSamples = {
        "0 59 23 ? *", // invalid # tokens //$NON-NLS-1$
        "0 59 23 ? * 2#4 2008", // invalid # tokens //$NON-NLS-1$
        "0 22 4 0/3 * ?", // EveryNthDayOfMonth Fires at 4:22am on the 1,4,7...  //$NON-NLS-1$
        "0 14 21 ? * 2-6", // EveryWeekday Fires at 2:21pm every weekday //$NON-NLS-1$

        "0 33 6 ? * 1",         //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2",       //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3",       //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4",     //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5",     //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5,6",   //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5,6,7",   //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5,6,7,8",   //WeeklyOn, must fail //$NON-NLS-1$
        
        "0 5 5 13 * ?",           // DayNOfMonth //$NON-NLS-1$

        "0 59 23 ? * 2#4",          // NthDayNameOfMonth //$NON-NLS-1$
        "0 59 23 ? * 5#4",          // NthDayNameOfMonth, should fail //$NON-NLS-1$
        "0 59 23 ? * 2#8",          // NthDayNameOfMonth, should fail //$NON-NLS-1$
        
        "0 59 23 ? * 2#4",          // NthDayNameOfMonth Fires at 11:59pm on the 2nd Wed. //$NON-NLS-1$
        "0 33 5 ? * 3L",          // LastDayNameOfMonth Fires at 5:33am on the last Tues. of the month //$NON-NLS-1$
        "0 23 4 ? * 7L",          // LastDayNameOfMonth Fires at 4:23am on the last Sat. of the month //$NON-NLS-1$
        
        "0 01 02 28 2 ?",         //EveryMonthNameN //$NON-NLS-1$
        "1 1 1 8 4 ?",            // EveryMonthNameN //$NON-NLS-1$
        
        "0 3 5 ? 4 2#1",          //NthDayNameOfMonthName 1st mon of april //$NON-NLS-1$
        "0 3 5 ? 4 7#4",          //NthDayNameOfMonthName 4th sat of april //$NON-NLS-1$
        "0 3 5 ? 4 1#1",          //NthDayNameOfMonthName 1st sun of april //$NON-NLS-1$
        
        "0 3 8 ? 6 5L",           //LastDayNameOfMonthName //$NON-NLS-1$
        "0 59 12 ? 1 1L",         //LastDayNameOfMonthName //$NON-NLS-1$
        "" //$NON-NLS-1$
    };
    
    String strInt = "bart77"; //$NON-NLS-1$
    assert !isInt( strInt ) : strInt + " is not an int."; //$NON-NLS-1$
    strInt = "2147483647"; //$NON-NLS-1$
    assert isInt( strInt ) : strInt + " is an int."; //$NON-NLS-1$
    strInt = "21474836478"; //$NON-NLS-1$
    assert !isInt( strInt ) : strInt + " is an integer, but is to large to be an int."; //$NON-NLS-1$
    strInt = "1"; //$NON-NLS-1$
    assert isInt( strInt ) : strInt + " is an int."; //$NON-NLS-1$
    
    
    for ( int ii=0; ii<cronSamples.length; ++ii ) {
      String cronStr = cronSamples[ii];
      
      try {
        CronParser cp = new CronParser( cronStr );
        cp.parse();
        System.out.println( "cront str: " + cronStr + "  --  " + cp.getSchedType().toString() ); //$NON-NLS-1$ //$NON-NLS-2$
      } catch (CronParseException e) {
        System.out.println( "cron str: " + cronStr + " " + e.getMessage() ); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    

    CronParser cp = new CronParser( "0 22 4 0/3 * ?" ); // EveryNthDayOfMonth //$NON-NLS-1$
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$ 
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert cp.isDayOfMonthValid() : "isDayOfMonthValid EveryNthDayOfMonth"; //$NON-NLS-1$
    try { assert cp.getDayOfMonth() == 3 : "cp.getDayOfMonth() == 3"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid EveryNthDayOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid EveryNthDayOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid EveryNthDayOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid EveryNthDayOfMonth"; //$NON-NLS-1$

    cp = new CronParser( "0 14 21 ? * 2-6" ); // EveryWeekday //$NON-NLS-1$
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid EveryWeekday"; //$NON-NLS-1$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid EveryWeekday"; //$NON-NLS-1$
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid EveryWeekday"; //$NON-NLS-1$
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid EveryWeekday"; //$NON-NLS-1$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid EveryWeekday"; //$NON-NLS-1$


    cp = new CronParser( "0 33 6 ? * 1,3,5" ); //WeeklyOn //$NON-NLS-1$
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid WeeklyOn"; //$NON-NLS-1$
    assert cp.isDaysOfWeekValid() : "isDaysOfWeekValid WeeklyOn"; //$NON-NLS-1$
    try { 
      int[] days = cp.getDaysOfWeek();
      assert  days[0] == 1 : "days[0] == 1";  //$NON-NLS-1$
      assert  days[1] == 3 : "days[0] == 3";  //$NON-NLS-1$
      assert  days[2] == 5 : "days[0] == 5";  //$NON-NLS-1$
    } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid WeeklyOn"; //$NON-NLS-1$
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid WeeklyOn"; //$NON-NLS-1$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid WeeklyOn"; //$NON-NLS-1$


    cp = new CronParser( "0 5 5 13 * ?" );  // DayNOfMonth //$NON-NLS-1$
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert cp.isDayOfMonthValid() : "isDayOfMonthValid DayNOfMonth"; //$NON-NLS-1$
    try { assert cp.getDayOfMonth() == 13 : "cp.getDayOfMonth() == 13"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid DayNOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid DayNOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid DayNOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid DayNOfMonth"; //$NON-NLS-1$


    cp = new CronParser( "0 59 23 ? * 2#4" ); // NthDayNameOfMonth //$NON-NLS-1$ 
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid NthDayNameOfMonth"; //$NON-NLS-1$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid NthDayNameOfMonth"; //$NON-NLS-1$
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid NthDayNameOfMonth"; //$NON-NLS-1$
    try { assert cp.getWhichDayOfWeek() == 2 : "cp.getWhichDayOfWeek() == 2"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid NthDayNameOfMonth"; //$NON-NLS-1$
    assert cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid NthDayNameOfMonth"; //$NON-NLS-1$
    try { assert cp.getWhichWeekOfMonth() == 4 : "cp.getWhichWeekOfMonth() == 4"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$  //$NON-NLS-2$


    cp = new CronParser( "0 33 5 ? * 3L" );// LastDayNameOfMonth //$NON-NLS-1$ 
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid LastDayNameOfMonth"; //$NON-NLS-1$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid LastDayNameOfMonth"; //$NON-NLS-1$
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid LastDayNameOfMonth"; //$NON-NLS-1$
    try { assert cp.getWhichDayOfWeek() == 3 : "cp.getWhichDayOfWeek() == 3"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid LastDayNameOfMonth"; //$NON-NLS-1$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid LastDayNameOfMonth"; //$NON-NLS-1$
    
    
    cp = new CronParser( "0 1 2 28 2 ?" );//EveryMonthNameN //$NON-NLS-1$ 
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assert cp.isDayOfMonthValid() : "isDayOfMonthValid EveryMonthNameN"; //$NON-NLS-1$
    try { assert cp.getDayOfMonth() == 28 : "cp.getDayOfMonth() == 28"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid EveryMonthNameN"; //$NON-NLS-1$
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid EveryMonthNameN"; //$NON-NLS-1$
    assert cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid EveryMonthNameN"; //$NON-NLS-1$
    try { assert cp.getWhichMonthOfYear() == 2 : "cp.getWhichMonthOfYear() == 2"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid EveryMonthNameN"; //$NON-NLS-1$


    cp = new CronParser( "0 3 5 ? 12 7#3" );//NthDayNameOfMonthName //$NON-NLS-1$
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid NthDayNameOfMonthName"; //$NON-NLS-1$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid NthDayNameOfMonthName"; //$NON-NLS-1$
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid NthDayNameOfMonthName"; //$NON-NLS-1$
    try { assert cp.getWhichDayOfWeek() == 7 : "cp.getWhichDayOfWeek() == 7"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid NthDayNameOfMonthName"; //$NON-NLS-1$
    try { assert cp.getWhichMonthOfYear() == 12 : "cp.getWhichMonthOfYear() == 12"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid NthDayNameOfMonthName"; //$NON-NLS-1$
    try { assert cp.getWhichWeekOfMonth() == 3 : "cp.getWhichWeekOfMonth() == 3"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$

    cp = new CronParser( "0 3 8 ? 6 5L" );//LastDayNameOfMonthName //$NON-NLS-1$
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }    
    try {
      String recurrenceTokens = cp.getRecurrenceString();
      System.out.println( "recurrenceTokens: " + recurrenceTokens + " [" + RecurrenceStringToCronString(recurrenceTokens ) + "]  (" + cp.getCronString()  + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      assert cp.getCronString().equals( RecurrenceStringToCronString( recurrenceTokens ) ) : "Failed on: " + recurrenceTokens; //$NON-NLS-1$
    } catch (CronParseException e) {
      e.printStackTrace();
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid LastDayNameOfMonthName"; //$NON-NLS-1$
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid LastDayNameOfMonthName"; //$NON-NLS-1$
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid LastDayNameOfMonthName"; //$NON-NLS-1$
    try { assert cp.getWhichDayOfWeek() == 5 : "cp.getWhichDayOfWeek() == 5"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid LastDayNameOfMonthName"; //$NON-NLS-1$ 
    try { assert cp.getWhichMonthOfYear() == 6 : "cp.getWhichMonthOfYear() == 6"; } catch (CronParseException e1) {assert false : "CronParseException"; } //$NON-NLS-1$ //$NON-NLS-2$
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid LastDayNameOfMonthName"; //$NON-NLS-1$

    RecurrenceType st = RecurrenceType.stringToScheduleType( RecurrenceType.DayNOfMonth.toString() );
    System.out.println( "DayNOfMonth=" + st.toString() ); //$NON-NLS-1$
    st = RecurrenceType.stringToScheduleType( RecurrenceType.NthDayNameOfMonth.toString() );
    System.out.println( "NthDayNameOfMonth=" + st.toString() ); //$NON-NLS-1$
    st = RecurrenceType.stringToScheduleType( RecurrenceType.EveryMonthNameN.toString() );
    System.out.println( "EveryMonthNameN=" + st.toString() ); //$NON-NLS-1$

    String r;
    boolean bThrewException = false;
    try {
      r = RecurrenceStringToCronString( "EveryNthDayOfMonth 0 22 4 toke" );
    } catch (CronParseException e) {
      bThrewException = true;
    }
    assert bThrewException : "Should have thrown exception";
    bThrewException = false;
    try {
      r = RecurrenceStringToCronString( "WeeklyOn 0 33 6 1,toke,5" );
    } catch (CronParseException e) {
      bThrewException = true;
    }
    assert bThrewException : "Should have thrown exception";
    bThrewException = false;
    try {
      r = RecurrenceStringToCronString( "DayNOfMonth 0 5 toke 13" );
    } catch (CronParseException e) {
      bThrewException = true;
    }
    assert bThrewException : "Should have thrown exception";
    bThrewException = false;
    try {
      r = RecurrenceStringToCronString( "NthDayNameOfMonthName 0 3 5 7 3 toke" );
    } catch (CronParseException e) {
      bThrewException = true;
    }
    assert bThrewException : "Should have thrown exception";
    bThrewException = false;
  }
}
