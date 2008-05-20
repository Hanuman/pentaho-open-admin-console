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
  private ScheduleType schedType = ScheduleType.Invalid;
  private int startSecond = -1;
  private int startMinute = -1;
  private int startHour = -1;
  
  // for use by internal algorithms
  private String dayOfMonthToke = null; // from cronString[3]
  private String monthToke = null;    // from cronString[4]
  private String dayOfWeekToke = null;  // from cronString[5]
  
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
  enum ScheduleType {
    Invalid,
    EveryNthDayOfMonth,
    EveryWeekday,
    WeeklyOn,
    DayNOfMonth,
    NthDayNameOfMonth,
    LastDayNameOfMonth,
    EveryMonthNameN,
    NthDayNameOfMonthName,
    LastDayNameOfMonthName
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
  private static final String IS_NUM_RE = "\\d{1,10}";
  // NOTE: does not properly handle negative integers
  private static boolean isInt( String sample ) {
    return sample.matches( IS_NUM_RE );
  }
  
  public final static int REQUIRED_NUM_TOKENS = 6;

  public int getStartSecond() {
    return startSecond;
  }
  public int getStartMinute() {
    return startMinute;
  }
  public int getStartHour() {
    return startHour;
  }
  
  private static final String EVERY_N_DAYS_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+0/\\d{1,2}\\s+\\*\\s+\\?$";
  /**
   * Abstract cron string: 0 MM HH 0/N * ?    fires at hour HH and minute MM, starting on the 1st, every N days.
   * @param cronStr
   * @return
   */
  private boolean isEveryNthDayOfMonthToken() {

    return cronStr.matches( EVERY_N_DAYS_OF_MONTH_RE );
  }
  
  private static final String EVERY_WEEK_DAY_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\*\\s+2\\-6$";
  /**
   * Abstract cron string: 0 MM HH ? * 2-6
   * @param cronStr
   * @return
   */
  private boolean isEveryWeekdayToken() {
    return cronStr.matches( EVERY_WEEK_DAY_RE );
  }
  
  private static final String WEEKLY_ON_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\*\\s+\\d(,\\d){0,6}$";
  /**
   * Abstract cron string: 0 MM HH ? * DAY-LIST, where DAY-LIST is like 1,4,5
   * @param cronStr
   * @return
   */
  private boolean isWeeklyOnToken() {
    return cronStr.matches( WEEKLY_ON_RE );
  }
  
  private static final String DAY_N_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\*\\s+\\?$";
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
  private static final String NTH_DAY_NAME_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s\\*\\s+[1-4]{1}#[1-7]{1}$";
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

  private static final String LAST_DAY_NAME_OF_MONTH_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\*\\s+[1-7]{1}L$";
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
  
  private static final String EVERY_MONTH_NAME_N_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?$";
  /**
   * Abstract cron string: 0 MM HH N MONTH ? (N ε {1-31} MONTH ε {1-12})
   * @param cronStr
   * @return
   */
  private boolean isEveryMonthNameNToken() {
    boolean bReMatch = cronStr.matches( EVERY_MONTH_NAME_N_RE );
    return bReMatch;
  }
  
  private static final String N_DAY_NAME_OF_MONTH_NAME_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\d{1,2}\\s+\\d{1}#\\d{1}$";
  /**
   * Abstract cron string: 0 MM HH ? MONTH N#DAY (MONTH ε {1-12}, N ε {1,2,3,4} DAY ε {1,2,3,4,5,6,7})
   * @param cronStr
   * @return
   */
  private boolean isNthDayNameOfMonthNameToken() {
    boolean bReMatch = cronStr.matches( N_DAY_NAME_OF_MONTH_NAME_RE );
    return bReMatch;
  }
  
  private static final String LAST_DAY_NAME_OF_MONTH_NAME_RE = "^\\d{1,2}\\s+\\d{1,2}\\s+\\d{1,2}\\s+\\?\\s+\\d{1,2}\\s+\\d{1}L$";
  /**
   * Abstract cron string: 0 MM HH ? MONTH DAYL (MONTH ε {1-12}, DAY ε {1,2,3,4,5,6,7})
   * @param cronStr
   * @return
   */
  private boolean isLastDayNameOfMonthNameToken() {
    boolean bReMatch = cronStr.matches( LAST_DAY_NAME_OF_MONTH_NAME_RE );
    return bReMatch;
  }
  
  public ScheduleType getSchedType() {
    return schedType;
  }
  
  private ScheduleType getScheduleType() throws CronParseException {
    if ( isEveryNthDayOfMonthToken() ) {
      return ScheduleType.EveryNthDayOfMonth;
    } else if ( isEveryWeekdayToken() ) {
      return ScheduleType.EveryWeekday;
    } else if ( isWeeklyOnToken() ) {
      return ScheduleType.WeeklyOn;
    } else if ( isDayNOfMonthToken() ) {
      return ScheduleType.DayNOfMonth;
    } else if ( isNthDayNameOfMonthToken() ) {
      return ScheduleType.NthDayNameOfMonth;
    } else if ( isLastDayNameOfMonthToken() ) {
      return ScheduleType.LastDayNameOfMonth;
    } else if ( isEveryMonthNameNToken() ) {
      return ScheduleType.EveryMonthNameN;
    } else if ( isNthDayNameOfMonthNameToken() ) {
      return ScheduleType.NthDayNameOfMonthName;
    } else if ( isLastDayNameOfMonthNameToken() ) {
      return ScheduleType.LastDayNameOfMonthName;
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
    
    schedType = getScheduleType();
  }
  
  public boolean isDayOfMonthValid() {
    switch( this.schedType ) {
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
    switch( this.schedType ) {
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
      throw new CronParseException( "getDayOfMonth() not valid for recurrance type: " + this.schedType.toString() );
    }
    int dayOfMonth = Integer.parseInt( strVal );
    if ( !isDayOfMonth( dayOfMonth ) ) {
      throw new CronParseException( "Invalid day of month: " + strVal ); 
    }
    return dayOfMonth;
  }

  public boolean isDaysOfWeekValid() {
    switch( this.schedType ) {
    case WeeklyOn:
      return true;
    default:
      return false;
    }
  }
  
  public int[] getDaysOfWeek() throws CronParseException {
    switch( this.schedType ) {
    case WeeklyOn:
      // token 5 is comma separated list of 1-7 unique integers between 1 and 7
      String[] days = dayOfWeekToke.split( "," );
      int[] intDays = new int[ days.length ];
      for ( int ii=0; ii<days.length; ++ii ) {
        intDays[ii] = Integer.parseInt( days[ ii ] );
        if ( !isDayOfWeek( intDays[ii] ) ) {
          throw new CronParseException( "Invalid day of week: " + days[ii] );
        }
      }
      return intDays;
    default:
      throw new CronParseException( "getDaysOfWeek() not valid for recurrance type: " + this.schedType.toString() );
    }
  }
  
  public boolean isWhichWeekOfMonthValid() {
    switch( this.schedType ) {
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
    switch( this.schedType ) {
    case NthDayNameOfMonth:
      // fall through
    case NthDayNameOfMonthName:
      // token 5 is N#DAY, want N
      strVal = dayOfWeekToke.split( "#" )[1];
      int weekOfMonth = Integer.parseInt( strVal );
      if ( !isWeekOfMonth( weekOfMonth ) ) {
        throw new CronParseException( "Invalid week of month: " + strVal );
      }
      return weekOfMonth;
    default:
      throw new CronParseException( "getWhichWeekOfMonth() not valid for recurrance type: " + this.schedType.toString() );
    }
  }

  public boolean isWhichDayOfWeekValid() {
    switch( this.schedType ) {
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
    switch( this.schedType ) {
    case NthDayNameOfMonth:
      // fall through
    case NthDayNameOfMonthName:
      // token 5 is N#DAY, want DAY
      dayOfWeek = Integer.parseInt( dayOfWeekToke.split( "#" )[0] );
      break;
    case LastDayNameOfMonth:
      // fall through
    case LastDayNameOfMonthName:
      // token 5 is NL, want N
      String strDay = dayOfWeekToke.substring( 0, dayOfWeekToke.length() - 1 ); // trim off the trailing L
      dayOfWeek = Integer.parseInt( strDay );
      break;
    default:
      throw new CronParseException( "getWhichDayOfWeek() not valid for recurrance type: " + this.schedType.toString() );
    }
    
    if ( !isDayOfWeek( dayOfWeek) ) {
      throw new CronParseException( "Invalid day of week: " + Integer.toString( dayOfWeek ) );
    }
    return dayOfWeek;
  }
  
  public boolean isWhichMonthOfYearValid() {
    switch( this.schedType ) {
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
    switch( this.schedType ) {
    case EveryMonthNameN:
      // fall through
    case NthDayNameOfMonthName:
      // fall through
    case LastDayNameOfMonthName:
      // token 4 is N
      monthOfYear = Integer.parseInt( monthToke );
      break;
    default:
      throw new CronParseException( "getWhichMonthOfYear() not valid for recurrance type: " + this.schedType.toString() );
    }
    if ( !isMonthOfYear( monthOfYear ) ) {
      throw new CronParseException( "Invalid month of year: " + monthToke );
    }
    return monthOfYear;
  }
  
  public static void main( String[] args ) {
    
      for (CronField f : EnumSet.range(CronField.SECONDS, CronField.YEAR)) {
//          System.out.println(f);
//          System.out.println(f.ordinal());
      }
      
      for ( int ii=0; ii< 256; ++ii ) {
//        boolean b = isMultipleOfN( ii, 24 );
//        if ( b ) {
//          System.out.println( "isMultipleOfN( " + ii + ", 24): " + b );
//        }
      }

    
    String[] cronSamples = {
        "0 59 23 ? *", // invalid # tokens
        "0 59 23 ? * 2#4 2008", // invalid # tokens
        "0 22 4 0/3 * ?", // EveryNthDayOfMonth Fires at 4:22am on the 1,4,7... 
        "0 14 21 ? * 2-6", // EveryWeekday Fires at 2:21pm every weekday

        "0 33 6 ? * 1",         //WeeklyOn
        "0 33 6 ? * 1,2",       //WeeklyOn
        "0 33 6 ? * 1,2,3",       //WeeklyOn
        "0 33 6 ? * 1,2,3,4",     //WeeklyOn
        "0 33 6 ? * 1,2,3,4,5",     //WeeklyOn
        "0 33 6 ? * 1,2,3,4,5,6",   //WeeklyOn
        "0 33 6 ? * 1,2,3,4,5,6,7",   //WeeklyOn
        "0 33 6 ? * 1,2,3,4,5,6,7,8",   //WeeklyOn, must fail
        
        "0 5 5 13 * ?",           // DayNOfMonth

        "0 59 23 ? * 2#4",          // NthDayNameOfMonth
        "0 59 23 ? * 5#4",          // NthDayNameOfMonth, should fail
        "0 59 23 ? * 2#8",          // NthDayNameOfMonth, should fail
        
        "0 59 23 ? * 2#4",          // NthDayNameOfMonth Fires at 11:59pm on the 2nd Wed.
        "0 33 5 ? * 3L",          // LastDayNameOfMonth Fires at 5:33am on the last Tues. of the month
        "0 23 4 ? * 7L",          // LastDayNameOfMonth Fires at 4:23am on the last Sat. of the month
        
        "0 01 02 28 2 ?",         //EveryMonthNameN
        "1 1 1 8 4 ?",            // EveryMonthNameN
        
        "0 3 5 ? 4 2#1",          //NthDayNameOfMonthName 1st mon of april
        "0 3 5 ? 4 7#4",          //NthDayNameOfMonthName 4th sat of april
        "0 3 5 ? 4 1#1",          //NthDayNameOfMonthName 1st sun of april
        
        "0 3 8 ? 6 5L",           //LastDayNameOfMonthName
        "0 59 12 ? 1 1L",         //LastDayNameOfMonthName
        ""
    };
    
    String strInt = "bart77";
    assert !isInt( strInt ) : strInt + " is not an int.";
    strInt = "2147483647";
    assert isInt( strInt ) : strInt + " is an int.";
    strInt = "21474836478";
    assert !isInt( strInt ) : strInt + " is an integer, but is to large to be an int.";
    strInt = "1";
    assert isInt( strInt ) : strInt + " is an int.";
    
    
    for ( int ii=0; ii<cronSamples.length; ++ii ) {
      String cronStr = cronSamples[ii];
      
      try {
        CronParser cp = new CronParser( cronStr );
        cp.parse();
        System.out.println( "cront str: " + cronStr + "  --  " + cp.getSchedType().toString() );
      } catch (CronParseException e) {
        System.out.println( "cron str: " + cronStr + " " + e.getMessage() );
      }
    }
    

    CronParser cp = new CronParser( "0 22 4 0/3 * ?" ); // EveryNthDayOfMonth
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert cp.isDayOfMonthValid() : "isDayOfMonthValid EveryNthDayOfMonth";
    try { assert cp.getDayOfMonth() == 3 : "cp.getDayOfMonth() == 3"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid EveryNthDayOfMonth";
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid EveryNthDayOfMonth";
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid EveryNthDayOfMonth";
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid EveryNthDayOfMonth";

    cp = new CronParser( "0 14 21 ? * 2-6" ); // EveryWeekday
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid EveryWeekday";
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid EveryWeekday";
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid EveryWeekday";
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid EveryWeekday";
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid EveryWeekday";


    cp = new CronParser( "0 33 6 ? * 1,3,5" ); //WeeklyOn
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid WeeklyOn";
    assert cp.isDaysOfWeekValid() : "isDaysOfWeekValid WeeklyOn";
    try { 
      int[] days = cp.getDaysOfWeek();
      assert  days[0] == 1 : "days[0] == 1"; 
      assert  days[1] == 3 : "days[0] == 3"; 
      assert  days[2] == 5 : "days[0] == 5"; 
    } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid WeeklyOn";
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid WeeklyOn";
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid WeeklyOn";


    cp = new CronParser( "0 5 5 13 * ?" );  // DayNOfMonth
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert cp.isDayOfMonthValid() : "isDayOfMonthValid DayNOfMonth";
    try { assert cp.getDayOfMonth() == 13 : "cp.getDayOfMonth() == 13"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid DayNOfMonth";
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid DayNOfMonth";
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid DayNOfMonth";
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid DayNOfMonth";


    cp = new CronParser( "0 59 23 ? * 2#4" ); // NthDayNameOfMonth
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid NthDayNameOfMonth";
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid NthDayNameOfMonth";
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid NthDayNameOfMonth";
    try { assert cp.getWhichDayOfWeek() == 2 : "cp.getWhichDayOfWeek() == 2"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid NthDayNameOfMonth";
    assert cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid NthDayNameOfMonth";
    try { assert cp.getWhichWeekOfMonth() == 4 : "cp.getWhichWeekOfMonth() == 4"; } catch (CronParseException e1) {assert false : "CronParseException"; }


    cp = new CronParser( "0 33 5 ? * 3L" );// LastDayNameOfMonth
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid LastDayNameOfMonth";
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid LastDayNameOfMonth";
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid LastDayNameOfMonth";
    try { assert cp.getWhichDayOfWeek() == 3 : "cp.getWhichDayOfWeek() == 3"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid LastDayNameOfMonth";
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid LastDayNameOfMonth";
    
    
    cp = new CronParser( "0 01 02 28 2 ?" );//EveryMonthNameN
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert cp.isDayOfMonthValid() : "isDayOfMonthValid EveryMonthNameN";
    try { assert cp.getDayOfMonth() == 28 : "cp.getDayOfMonth() == 28"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid EveryMonthNameN";
    assert !cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid EveryMonthNameN";
    assert cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid EveryMonthNameN";
    try { assert cp.getWhichMonthOfYear() == 2 : "cp.getWhichMonthOfYear() == 2"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid EveryMonthNameN";


    cp = new CronParser( "0 3 5 ? 12 7#3" );//NthDayNameOfMonthName
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid NthDayNameOfMonthName";
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid NthDayNameOfMonthName";
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid NthDayNameOfMonthName";
    try { assert cp.getWhichDayOfWeek() == 7 : "cp.getWhichDayOfWeek() == 7"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid NthDayNameOfMonthName";
    try { assert cp.getWhichMonthOfYear() == 12 : "cp.getWhichMonthOfYear() == 12"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid NthDayNameOfMonthName";
    try { assert cp.getWhichWeekOfMonth() == 3 : "cp.getWhichWeekOfMonth() == 3"; } catch (CronParseException e1) {assert false : "CronParseException"; }


    cp = new CronParser( "0 3 8 ? 6 5L" );//LastDayNameOfMonthName
    try {
      cp.parse();
    } catch (CronParseException e) {
      System.out.println( e );
    }
    assert !cp.isDayOfMonthValid() : "isDayOfMonthValid LastDayNameOfMonthName";
    assert !cp.isDaysOfWeekValid() : "isDaysOfWeekValid LastDayNameOfMonthName";
    assert cp.isWhichDayOfWeekValid() : "isWhichDayOfWeekValid LastDayNameOfMonthName";
    try { assert cp.getWhichDayOfWeek() == 5 : "cp.getWhichDayOfWeek() == 5"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert cp.isWhichMonthOfYearValid() : "isWhichMonthOfYearValid LastDayNameOfMonthName";
    try { assert cp.getWhichMonthOfYear() == 6 : "cp.getWhichMonthOfYear() == 6"; } catch (CronParseException e1) {assert false : "CronParseException"; }
    assert !cp.isWhichWeekOfMonthValid() : "isWhichWeekOfMonthValid LastDayNameOfMonthName";
  }
}
