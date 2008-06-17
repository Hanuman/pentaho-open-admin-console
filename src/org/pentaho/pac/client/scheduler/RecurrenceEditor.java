package org.pentaho.pac.client.scheduler;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.pac.client.common.EnumException;
import org.pentaho.pac.client.common.ui.SimpleGroupBox;
import org.pentaho.pac.client.common.ui.TimePicker;
import org.pentaho.pac.client.common.ui.widget.ErrorLabel;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.common.util.TimeUtil.DayOfWeek;
import org.pentaho.pac.client.common.util.TimeUtil.MonthOfYear;
import org.pentaho.pac.client.common.util.TimeUtil.TimeOfDay;
import org.pentaho.pac.client.common.util.TimeUtil.WeekOfMonth;
import org.pentaho.pac.client.scheduler.CronParser.RecurrenceType;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RecurrenceEditor extends VerticalPanel {

  private TimePicker startTimePicker = null;

  private SecondlyRecurrencePanel secondlyPanel = null;
  private ErrorLabel secondlyLabel = null;

  private MinutelyRecurrencePanel minutelyPanel = null;
  private ErrorLabel minutelyLabel = null;

  private HourlyRecurrencePanel hourlyPanel = null;
  private ErrorLabel hourlyLabel = null;

  private DailyRecurrencePanel dailyPanel = null;

  private WeeklyRecurrencePanel weeklyPanel = null;

  private MonthlyRecurrencePanel monthlyPanel = null;

  private YearlyRecurrencePanel yearlyPanel = null;
  
  private DateRangeEditor dateRangeEditor = null;
  
  private TemporalValue temporalState = null;
  
  private static final String SPACE = " "; //$NON-NLS-1$
  
  private static int VALUE_OF_SUNDAY = 1;

  private Map<TemporalValue, Panel> temporalPanelMap = new HashMap<TemporalValue, Panel>();

  public enum TemporalValue {
    SECONDS(0, "Seconds"), 
    MINUTES(1, "Minutes"), 
    HOURS(2, "Hours"), 
    DAILY(3, "Daily"), 
    WEEKLY(4, "Weekly"), 
    MONTHLY(5, "Monthly"), 
    YEARLY(6, "Yearly");

    private TemporalValue(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static TemporalValue[] temporalValues = {
      SECONDS, 
      MINUTES, 
      HOURS,
      DAILY, 
      WEEKLY, 
      MONTHLY, 
      YEARLY 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static TemporalValue get(int idx) {
      return temporalValues[idx];
    }

    public static int length() {
      return temporalValues.length;
    }
    
    public static TemporalValue stringToTemporalValue( String temporalValue ) throws EnumException {
      for (TemporalValue v : EnumSet.range(TemporalValue.SECONDS, TemporalValue.YEARLY)) {
        if ( v.toString().equals( temporalValue ) ) {
          return v;
        }
      }
      throw new EnumException( "Invalid String for temporal value: " + temporalValue );
    }
  } /* end enum */

  private static Map<MonthOfYear, Integer> validNumDaysOfMonth = new HashMap<MonthOfYear, Integer>();
  static {
    validNumDaysOfMonth.put(MonthOfYear.JAN, 31);
    validNumDaysOfMonth.put(MonthOfYear.FEB, 29);
    validNumDaysOfMonth.put(MonthOfYear.MAR, 31);
    validNumDaysOfMonth.put(MonthOfYear.APR, 30);
    validNumDaysOfMonth.put(MonthOfYear.MAY, 31);
    validNumDaysOfMonth.put(MonthOfYear.JUN, 30);
    validNumDaysOfMonth.put(MonthOfYear.JUL, 31);
    validNumDaysOfMonth.put(MonthOfYear.AUG, 31);
    validNumDaysOfMonth.put(MonthOfYear.SEPT, 30);
    validNumDaysOfMonth.put(MonthOfYear.OCT, 31);
    validNumDaysOfMonth.put(MonthOfYear.NOV, 30);
    validNumDaysOfMonth.put(MonthOfYear.DEC, 31);
  }
  
  private static final String DAILY_RB_GROUP = "daily-group"; //$NON-NLS-1$

  private static final String MONTHLY_RB_GROUP = "monthly-group"; //$NON-NLS-1$

  public RecurrenceEditor() {
    super();

    Panel p = createStartTimePanel();
    add(p);

    p = createRecurrencePanel();
    add(p);

    Date now = new Date();
    dateRangeEditor = new DateRangeEditor( now );
    add( dateRangeEditor );
    
    // TODO sbarkdull, is this necessary?
    setTemporalState( TemporalValue.SECONDS );
  }
  
  public void reset( Date d ) {

    startTimePicker.setHour( "12" );
    startTimePicker.setMinute( "00" );
    startTimePicker.setTimeOfDay( TimeUtil.TimeOfDay.AM );

    dateRangeEditor.reset( d );
    
    secondlyPanel.reset();
    minutelyPanel.reset();
    hourlyPanel.reset();
    dailyPanel.reset();
    weeklyPanel.reset();
    monthlyPanel.reset();
    yearlyPanel.reset();
  }
  
  /**
   * 
   * @param recurrenceStr
   * @throws EnumException thrown if recurrenceTokens[0] is not a valid ScheduleType String.
   */
  public void inititalizeWithRecurrenceString( String recurrenceStr ) throws EnumException {
    String[] recurrenceTokens = recurrenceStr.split( "\\s" ); //$NON-NLS-1$
    
    setStartTime( recurrenceTokens[1], recurrenceTokens[2], recurrenceTokens[3] );
    
    RecurrenceType rt = RecurrenceType.stringToScheduleType( recurrenceTokens[0] );

    switch( rt ) {
      case EveryWeekday:
        setEveryWeekdayRecurrence( recurrenceTokens );
        break;
      case WeeklyOn:
        setWeeklyOnRecurrence( recurrenceTokens );
        break;
      case DayNOfMonth:
        setDayNOfMonthRecurrence( recurrenceTokens );
        break;
      case NthDayNameOfMonth:
        setNthDayNameOfMonthRecurrence( recurrenceTokens );
        break;
      case LastDayNameOfMonth:
        setLastDayNameOfMonthRecurrence( recurrenceTokens );
        break;
      case EveryMonthNameN:
        setEveryMonthNameNRecurrence( recurrenceTokens );
        break;
      case NthDayNameOfMonthName:
        setNthDayNameOfMonthNameRecurrence( recurrenceTokens );
        break;
      case LastDayNameOfMonthName:
        setLastDayNameOfMonthNameRecurrence( recurrenceTokens );
        break;
      default:
    }
  }
  
  private void setStartTime( String seconds, String minutes, String hours ) {
    TimeOfDay td = TimeUtil.getTimeOfDayBy0To23Hour( hours );
    int intHours = Integer.parseInt( hours );
    int intTwelveHour = TimeUtil.to12HourClock( intHours ); // returns 0..11
    startTimePicker.setHour( Integer.toString( TimeUtil.map0Through11To12Through11( intTwelveHour ) ) );
    startTimePicker.setMinute( minutes );
    startTimePicker.setTimeOfDay( td );
  }
  
  private void setEveryWeekdayRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.DAILY );
  }
  
  private void setWeeklyOnRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.WEEKLY );
    String days = recurrenceTokens[4];
    weeklyPanel.setCheckedDaysAsString( days, VALUE_OF_SUNDAY );
  }
  
  private void setDayNOfMonthRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.MONTHLY );
    monthlyPanel.setDayNOfMonth();
    String dayNOfMonth = recurrenceTokens[4];
    monthlyPanel.setDayOfMonth( dayNOfMonth );
  }
  
  private void setNthDayNameOfMonthRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.MONTHLY );
    monthlyPanel.setNthDayNameOfMonth();
    monthlyPanel.setWeekOfMonth( WeekOfMonth.get( Integer.parseInt( recurrenceTokens[5])-1 ) );
    monthlyPanel.setDayOfWeek( DayOfWeek.get( Integer.parseInt( recurrenceTokens[4])-1 ) );
  }
  
  private void setLastDayNameOfMonthRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.MONTHLY );
    monthlyPanel.setNthDayNameOfMonth();
    monthlyPanel.setWeekOfMonth( WeekOfMonth.LAST );
    monthlyPanel.setDayOfWeek( DayOfWeek.get( Integer.parseInt( recurrenceTokens[4])-1 ) );
  }
  
  private void setEveryMonthNameNRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.YEARLY );
    yearlyPanel.setEveryMonthOnNthDay();
    yearlyPanel.setDayOfMonth( recurrenceTokens[4] );
    yearlyPanel.setMonthOfYear0( MonthOfYear.get( Integer.parseInt( recurrenceTokens[5] )-1 ) );
  }
  
  private void setNthDayNameOfMonthNameRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.YEARLY );
    yearlyPanel.setNthDayNameOfMonthName();
    yearlyPanel.setMonthOfYear1( MonthOfYear.get( Integer.parseInt( recurrenceTokens[6] )-1 ) );
    yearlyPanel.setWeekOfMonth( WeekOfMonth.get( Integer.parseInt( recurrenceTokens[5])-1 ) );
    yearlyPanel.setDayOfWeek( DayOfWeek.get( Integer.parseInt( recurrenceTokens[4])-1 ) );
  }
  
  private void setLastDayNameOfMonthNameRecurrence( String[] recurrenceTokens ) {
    setTemporalState( TemporalValue.YEARLY );
    yearlyPanel.setNthDayNameOfMonthName();
    yearlyPanel.setMonthOfYear1( MonthOfYear.get( Integer.parseInt( recurrenceTokens[5] )-1 ) );
    yearlyPanel.setWeekOfMonth( WeekOfMonth.LAST );
    yearlyPanel.setDayOfWeek( DayOfWeek.get( Integer.parseInt( recurrenceTokens[4])-1 ) );
  }
  
  /**
   * 
   * @param strRepeatInSecs
   */
  public void inititalizeWithRepeatInSecs( int repeatInSecs ) {

    TemporalValue currentVal;
    int repeatTime;
    if ( TimeUtil.isSecondsWholeDay( repeatInSecs ) ) {
      repeatTime = TimeUtil.secsToDays( repeatInSecs );
      currentVal = TemporalValue.DAILY;
      dailyPanel.setRepeatValue( Integer.toString( repeatTime ) );
    } else { 
      SimpleRecurrencePanel p = null;
      if ( TimeUtil.isSecondsWholeHour( repeatInSecs ) ) {
        repeatTime = TimeUtil.secsToHours( repeatInSecs );
        currentVal = TemporalValue.HOURS;
      } else if ( TimeUtil.isSecondsWholeMinute( repeatInSecs ) ) {
        repeatTime = TimeUtil.secsToMinutes( repeatInSecs );
        currentVal = TemporalValue.MINUTES;
      } else {
        // the repeat time is seconds
        repeatTime = repeatInSecs;
        currentVal = TemporalValue.SECONDS;
      }
      p = (SimpleRecurrencePanel)temporalPanelMap.get(currentVal);
      p.setValue( Integer.toString( repeatTime ) );
    }
    setTemporalState( currentVal );
  }

  
  private Panel createStartTimePanel() {
    SimpleGroupBox startTimeGB = new SimpleGroupBox("Start Time");

    startTimePicker = new TimePicker();
    startTimeGB.add(startTimePicker);

    return startTimeGB;
  }

  private Panel createRecurrencePanel() {

    SimpleGroupBox recurrenceGB = new SimpleGroupBox("Recurrence pattern");

    VerticalPanel p = new VerticalPanel();
    recurrenceGB.add(p);

    secondlyPanel = new SecondlyRecurrencePanel();
    secondlyPanel.setVisible(true);
    minutelyPanel = new MinutelyRecurrencePanel();
    hourlyPanel = new HourlyRecurrencePanel();
    dailyPanel = new DailyRecurrencePanel();
    weeklyPanel = new WeeklyRecurrencePanel();
    monthlyPanel = new MonthlyRecurrencePanel();
    yearlyPanel = new YearlyRecurrencePanel();

    createTemporalMap();

    secondlyLabel = new ErrorLabel( secondlyPanel );
    p.add(secondlyLabel);
    minutelyLabel = new ErrorLabel( minutelyPanel );
    p.add(minutelyLabel);
    hourlyLabel = new ErrorLabel( hourlyPanel );
    p.add(hourlyLabel);
    
    p.add(dailyPanel);
    p.add(weeklyPanel);
    p.add(monthlyPanel);
    p.add(yearlyPanel);

    return recurrenceGB;
  }

  private void createTemporalMap() {
    // must come after creation of temporal panels
    assert dailyPanel != null : "Temporal panels must be initialized before calling createTemporalCombo.";

    temporalPanelMap.put( TemporalValue.SECONDS, secondlyPanel );
    temporalPanelMap.put( TemporalValue.MINUTES, minutelyPanel );
    temporalPanelMap.put( TemporalValue.HOURS, hourlyPanel );
    temporalPanelMap.put( TemporalValue.DAILY, dailyPanel );
    temporalPanelMap.put( TemporalValue.WEEKLY, weeklyPanel);
    temporalPanelMap.put( TemporalValue.MONTHLY, monthlyPanel);
    temporalPanelMap.put( TemporalValue.YEARLY, yearlyPanel);
  }
  
  private class SimpleRecurrencePanel extends HorizontalPanel {
    private TextBox valueTb = new TextBox();
    
    public SimpleRecurrencePanel( String strLabel ) {
      setVisible(false);

      Label l = new Label( "Every");
      l.setStyleName("startLabel"); //$NON-NLS-1$
      add(l);

      valueTb.setWidth("3em"); //$NON-NLS-1$
      valueTb.setTitle("Number of " + strLabel + " to repeat.");
      add(valueTb);

      l = new Label( strLabel );
      l.setStyleName("endLabel"); //$NON-NLS-1$
      add(l);
    }
    
    public String getValue() {
      return valueTb.getText();
    }
    
    public void setValue( String val ) {
      valueTb.setText( val );
    }
    
    public void reset() {
      setValue( "" ); //$NON-NLS-1$
    }
  }

  private class SecondlyRecurrencePanel extends SimpleRecurrencePanel {
    public SecondlyRecurrencePanel() {
      super( "second(s)" );
    }
  }

  private class MinutelyRecurrencePanel extends SimpleRecurrencePanel {
    public MinutelyRecurrencePanel() {
      super( "minute(s)" );
    }
  }

  private class HourlyRecurrencePanel extends SimpleRecurrencePanel {
    public HourlyRecurrencePanel() {
      super( "hour(s)" );
    }
  }

  private class DailyRecurrencePanel extends VerticalPanel {

    private TextBox repeatValueTb = new TextBox();
    private RadioButton everyNDaysRb = new RadioButton(DAILY_RB_GROUP, "Every");
    private RadioButton everyWeekdayRb = new RadioButton(DAILY_RB_GROUP, "Every weekday");
    private ErrorLabel repeatLabel = null;
    
    public DailyRecurrencePanel() {
      setVisible(false);

      HorizontalPanel hp = new HorizontalPanel();
      everyNDaysRb.setStyleName("recurrenceRadioButton");
      everyNDaysRb.setChecked(true);
      hp.add(everyNDaysRb);

      repeatValueTb.setWidth("3em");
      repeatValueTb.setTitle("Number of days to repeat.");
      hp.add(repeatValueTb);

      Label l = new Label("day(s)");
      l.setStyleName("endLabel");
      hp.add(l);
      repeatLabel = new ErrorLabel( hp );
      add( repeatLabel );

      everyWeekdayRb.setStyleName("recurrenceRadioButton");
      add(everyWeekdayRb);
    }
    
    public void reset() {
      setRepeatValue( "" ); //$NON-NLS-1$
      setEveryNDays();
    }
    
    public String getRepeatValue() {
      return repeatValueTb.getText();
    }
    
    public void setRepeatValue( String repeatValue ) {
      repeatValueTb.setText( repeatValue );
    }
    
    public void setEveryNDays() {
      everyNDaysRb.setChecked( true );
      everyWeekdayRb.setChecked( false );
    }
    
    public boolean isEveryNDays() {
      return everyNDaysRb.isChecked();
    }
    
    public void setEveryWeekday() {
      everyWeekdayRb.setChecked( true );
      everyNDaysRb.setChecked( false );
    }
    
    public boolean isEveryWeekday() {
      return everyWeekdayRb.isChecked();
    }
    
    public void setRepeatError( String errorMsg ) {
      repeatLabel.setErrorMsg( errorMsg );
    }
  }

  private class WeeklyRecurrencePanel extends VerticalPanel {

    private Map<DayOfWeek, CheckBox> dayToCheckBox = new HashMap<DayOfWeek, CheckBox>();
    private ErrorLabel everyWeekOnLabel = null;
    
    public WeeklyRecurrencePanel() {
      setStyleName("weeklyRecurrencePanel");
      setVisible(false);

      Label l = new Label( "Recur every week on:" );
      everyWeekOnLabel = new ErrorLabel( l );
      l.setStyleName("startLabel");
      add( everyWeekOnLabel );

      FlexTable gp = new FlexTable();
      gp.setCellPadding(0);
      gp.setCellSpacing(0);
      // add Sun - Wed
      final int ITEMS_IN_ROW = 4;
      for (int ii = 0; ii < ITEMS_IN_ROW; ++ii) {
        DayOfWeek day = DayOfWeek.get(ii);
        CheckBox cb = new CheckBox(day.toString());
        gp.setWidget(0, ii, cb);
        dayToCheckBox.put(day, cb);
      }
      // Add Thur - Sat
      for (int ii = ITEMS_IN_ROW; ii < DayOfWeek.length(); ++ii) {
        DayOfWeek day = DayOfWeek.get(ii);
        CheckBox cb = new CheckBox(day.toString());
        gp.setWidget(1, ii - 4, cb);
        dayToCheckBox.put(day, cb);
      }
      add(gp);
    }
    
    public void reset() {
      for ( DayOfWeek d : dayToCheckBox.keySet() ) {
        CheckBox cb = dayToCheckBox.get( d );
        cb.setChecked( false );
      }
    }
    
    /**
     * 
     * @param valueOfSunday int used to adjust the starting point of the weekday sequence.
     * If this value is 0, Sun-Sat maps to 0-6, if this value is 1, Sun-Sat maps to 1-7, etc.
     * @return String comma separated list of numeric days of the week.
     */
    public String getCheckedDaysAsString( int valueOfSunday ) {
      StringBuilder sb = new StringBuilder();
      for ( DayOfWeek d : EnumSet.range( DayOfWeek.SUN, DayOfWeek.SAT) ) {
        CheckBox cb = dayToCheckBox.get( d );
        if ( cb.isChecked() ) {
          sb.append( Integer.toString( d.value()+valueOfSunday ) ).append( "," );
        }
      }
      sb.deleteCharAt( sb.length()-1 );
      return sb.toString();
    }
    
    /**
     * 
     * @param valueOfSunday int used to adjust the starting point of the weekday sequence.
     * If this value is 0, Sun-Sat maps to 0-6, if this value is 1, Sun-Sat maps to 1-7, etc.
     * @return String comma separated list of numeric days of the week.
     */
    public void setCheckedDaysAsString( String strDays, int valueOfSunday ) {
      String[] days = strDays.split( "," );
      for ( String day : days ) {
        int intDay = Integer.parseInt( day ) - valueOfSunday;
        DayOfWeek dayOfWeek = DayOfWeek.get( intDay );
        CheckBox cb = dayToCheckBox.get( dayOfWeek );
        cb.setChecked( true );
      }
    }
    
    public int getNumCheckedDays() {
      int numCheckedDays = 0;
      //for ( DayOfWeek d : EnumSet.range( DayOfWeek.SUN, DayOfWeek.SAT) ) {
      for ( Map.Entry<DayOfWeek, CheckBox> cbEntry : dayToCheckBox.entrySet() ) {
        if ( cbEntry.getValue().isChecked() ) {
          numCheckedDays++;
        }
      }
      return numCheckedDays;
    }
    
    public void setEveryDayOnError( String errorMsg ) {
      everyWeekOnLabel.setErrorMsg( errorMsg );
    }
  }

  private class MonthlyRecurrencePanel extends VerticalPanel {
    
    private RadioButton dayNOfMonthRb = new RadioButton(MONTHLY_RB_GROUP, "Day");
    private RadioButton nthDayNameOfMonthRb = new RadioButton(MONTHLY_RB_GROUP, "The");
    private TextBox dayOfMonthTb = new TextBox();
    private ListBox whichWeekLb = createWhichWeekListBox();
    private ListBox dayOfWeekLb = createDayOfWeekListBox();
    private ErrorLabel dayNOfMonthLabel = null;
    
    public MonthlyRecurrencePanel() {
      setVisible(false);

      HorizontalPanel hp = new HorizontalPanel();
      dayNOfMonthRb.setStyleName("recurrenceRadioButton");
      dayNOfMonthRb.setChecked( true );
      hp.add(dayNOfMonthRb);
      dayOfMonthTb.setWidth("3em");
      hp.add(dayOfMonthTb);
      Label l = new Label("of every month");
      l.setStyleName("endLabel");
      hp.add(l);
      
      dayNOfMonthLabel = new ErrorLabel( hp );
      add( dayNOfMonthLabel );

      hp = new HorizontalPanel();
      nthDayNameOfMonthRb.setStyleName("recurrenceRadioButton");
      hp.add(nthDayNameOfMonthRb);
      hp.add(whichWeekLb);

      hp.add(dayOfWeekLb);
      l = new Label("of every month");
      l.setStyleName("endLabel");
      hp.add(l);
      add(hp);
    }
    
    public void reset() {
      setDayNOfMonth();
      setDayOfMonth( "" );
      setWeekOfMonth( WeekOfMonth.FIRST );
      setDayOfWeek( DayOfWeek.SUN );
    }
    
    public void setDayNOfMonth() {
      dayNOfMonthRb.setChecked( true );
      nthDayNameOfMonthRb.setChecked( false );
    }
    
    public boolean isDayNOfMonth() {
      return dayNOfMonthRb.isChecked();
    }
    
    public void setNthDayNameOfMonth() {
      nthDayNameOfMonthRb.setChecked( true );
      dayNOfMonthRb.setChecked( false );
    }
    
    public boolean isNthDayNameOfMonth() {
      return nthDayNameOfMonthRb.isChecked();
    }
    
    public String getDayOfMonth() {
      return dayOfMonthTb.getText();
    }
    
    public void setDayOfMonth( String dayOfMonth ) {
      dayOfMonthTb.setText( dayOfMonth );
    }
    
    public WeekOfMonth getWeekOfMonth() {
      return WeekOfMonth.get( whichWeekLb.getSelectedIndex() );
    }
    
    public void setWeekOfMonth( WeekOfMonth week ) {
      whichWeekLb.setSelectedIndex( week.value() );
    }
    
    public DayOfWeek getDayOfWeek() {
      return DayOfWeek.get( dayOfWeekLb.getSelectedIndex() );
    }
    
    public void setDayOfWeek( DayOfWeek day ) {
      dayOfWeekLb.setSelectedIndex( day.value() );
    }
    
    public void setDayNOfMonthError( String errorMsg ) {
      dayNOfMonthLabel.setErrorMsg( errorMsg );
    }
  }

  private class YearlyRecurrencePanel extends VerticalPanel {
    
    private RadioButton everyMonthOnNthDayRb = new RadioButton(YEARLY_RB_GROUP, "Every");
    private RadioButton nthDayNameOfMonthNameRb = new RadioButton(YEARLY_RB_GROUP, "The");
    private TextBox dayOfMonthTb = new TextBox();
    private ListBox monthOfYearLb0 = createMonthOfYearListBox();
    private ListBox monthOfYearLb1 = createMonthOfYearListBox();
    private ListBox whichWeekLb = createWhichWeekListBox();
    private ListBox dayOfWeekLb = createDayOfWeekListBox();
    private ErrorLabel dayOfMonthLabel = null;

    private static final String YEARLY_RB_GROUP = "yearly-group"; //$NON-NLS-1$

    public YearlyRecurrencePanel() {
      setVisible(false);

      HorizontalPanel p = new HorizontalPanel();
      everyMonthOnNthDayRb.setStyleName("recurrenceRadioButton"); //$NON-NLS-1$
      everyMonthOnNthDayRb.setChecked(true);
      p.add(everyMonthOnNthDayRb);
      p.add( monthOfYearLb0 );
      dayOfMonthTb.setWidth("3em"); //$NON-NLS-1$
      p.add(dayOfMonthTb);
      dayOfMonthLabel = new ErrorLabel( p );
      add(dayOfMonthLabel);

      p = new HorizontalPanel();
      nthDayNameOfMonthNameRb.setStyleName("recurrenceRadioButton"); //$NON-NLS-1$
      p.add(nthDayNameOfMonthNameRb);
      p.add(whichWeekLb);
      p.add(dayOfWeekLb);
      Label l = new Label("of");
      l.setStyleName("middleLabel"); //$NON-NLS-1$
      p.add(l);
      p.add( monthOfYearLb1 );
      add(p);
    }
    
    public void reset() {
      setEveryMonthOnNthDay();
      setMonthOfYear0( MonthOfYear.JAN );
      setDayOfMonth( "" );
      setWeekOfMonth( WeekOfMonth.FIRST );
      setDayOfWeek( DayOfWeek.SUN );
      setMonthOfYear1( MonthOfYear.JAN );
    }
    
    public boolean isEveryMonthOnNthDay() {
      return everyMonthOnNthDayRb.isChecked();
    }
    
    public void setEveryMonthOnNthDay() {
      everyMonthOnNthDayRb.setChecked( true );
      nthDayNameOfMonthNameRb.setChecked( false );
    }
    
    public boolean isNthDayNameOfMonthName() {
      return nthDayNameOfMonthNameRb.isChecked();
    }
    
    public void setNthDayNameOfMonthName() {
      nthDayNameOfMonthNameRb.setChecked( true );
      everyMonthOnNthDayRb.setChecked( false );
    }
    
    public String getDayOfMonth() {
      return dayOfMonthTb.getText();
    }
    
    public void setDayOfMonth( String dayOfMonth ) {
      dayOfMonthTb.setText( dayOfMonth );
    }
    
    public WeekOfMonth getWeekOfMonth() {
      return WeekOfMonth.get( whichWeekLb.getSelectedIndex() );
    }
    
    public void setWeekOfMonth( WeekOfMonth week ) {
      whichWeekLb.setSelectedIndex( week.value() );
    }
    
    public DayOfWeek getDayOfWeek() {
      return DayOfWeek.get( dayOfWeekLb.getSelectedIndex() );
    }
    
    public void setDayOfWeek( DayOfWeek day ) {
      dayOfWeekLb.setSelectedIndex( day.value() );
    }
    
    public MonthOfYear getMonthOfYear0() {
      return MonthOfYear.get( monthOfYearLb0.getSelectedIndex() );
    }
    
    public void setMonthOfYear0( MonthOfYear month ) {
      monthOfYearLb0.setSelectedIndex( month.value() );
    }
    
    public MonthOfYear getMonthOfYear1() {
      return MonthOfYear.get( monthOfYearLb1.getSelectedIndex() );
    }
    
    public void setMonthOfYear1( MonthOfYear month ) {
      monthOfYearLb1.setSelectedIndex( month.value() );
    }
    
    public void setDayOfMonthError( String errorMsg ) {
      dayOfMonthLabel.setErrorMsg( errorMsg );
    }
  }
  
  private ListBox createDayOfWeekListBox() {
    ListBox l = new ListBox();
    for (int ii = 0; ii < DayOfWeek.length(); ++ii) {
      DayOfWeek day = DayOfWeek.get(ii);
      l.addItem(day.toString());
    }
    return l;
  }

  private ListBox createMonthOfYearListBox() {

    ListBox l = new ListBox();
    for (int ii = 0; ii < MonthOfYear.length(); ++ii) {
      MonthOfYear month = MonthOfYear.get(ii);
      l.addItem(month.toString());
    }

    return l;
  }

  private ListBox createWhichWeekListBox() {

    ListBox l = new ListBox();
    for ( WeekOfMonth week : EnumSet.range(WeekOfMonth.FIRST, WeekOfMonth.LAST)) {
      l.addItem( week.toString() );
    }

    return l;
  }

  private void selectTemporalPanel(TemporalValue selectedTemporalValue) {
    for ( Map.Entry<TemporalValue, Panel> me : temporalPanelMap.entrySet() ) {
      boolean bShow = me.getKey().equals( selectedTemporalValue );
      me.getValue().setVisible( bShow );
    }
  }
  
  /**
   * 
   * @return null if the selected schedule does not support repeat-in-seconds, otherwise
   * return the number of seconds between schedule execution.
   * @throws RuntimeException if the temporal value (tv) is invalid. This
   * condition occurs as a result of programmer error.
   */
  public Integer getRepeatInSecs() throws RuntimeException {
    switch ( temporalState ) {
      case WEEKLY:
        // fall through
      case MONTHLY:
        // fall through
      case YEARLY:
        return null;
      case SECONDS:
        return Integer.parseInt( secondlyPanel.getValue() );
      case MINUTES:
        return TimeUtil.minutesToSecs( Integer.parseInt( minutelyPanel.getValue() ) );
      case HOURS:
        return TimeUtil.hoursToSecs( Integer.parseInt( hourlyPanel.getValue() ) );
      case DAILY:
        return TimeUtil.daysToSecs( Integer.parseInt( dailyPanel.getRepeatValue() ) );
      default:
        throw new RuntimeException( "Invalid TemporalValue in getRepeatInSecs(): " + temporalState );
    }
  }

  /**
   * 
   * @return null if the selected schedule does not support CRON, otherwise
   * return the CRON string.
   * @throws RuntimeException if the temporal value (tv) is invalid. This
   * condition occurs as a result of programmer error.
   */
  public String getCronString() throws RuntimeException {
    switch ( temporalState ) {
      case SECONDS:
        // fall through
      case MINUTES:
        // fall through
      case HOURS:
        return null;
      case DAILY:
        return getDailyCronString();
      case WEEKLY:
        return getWeeklyCronString();
      case MONTHLY:
        return getMonthlyCronString();
      case YEARLY:
        return getYearlyCronString();
      default:
        throw new RuntimeException( "Invalid TemporalValue in getCronString(): " + temporalState );
    }
  }
  
  /**
   * 
   * @return
   * @throws RuntimeException
   */
  private String getDailyCronString() throws RuntimeException {
    String cronStr;
    StringBuilder recurrenceSb = new StringBuilder();
    if ( dailyPanel.isEveryNDays() ) {
      return null;
    } else {
      // must be every weekday
      recurrenceSb.append( RecurrenceType.EveryWeekday ).append( SPACE )
        .append( getTimeOfRecurrence() );
      try {
        cronStr = CronParser.recurrenceStringToCronString( recurrenceSb.toString() );
      } catch (CronParseException e) {
        throw new RuntimeException( "Invalid recurrence string: " + recurrenceSb.toString() );
      }
      return cronStr;
    }
  }
  
  private String getWeeklyCronString() throws RuntimeException {
    String cronStr;
    StringBuilder recurrenceSb = new StringBuilder();
    // WeeklyOn 0 33 6 1,3,5
    recurrenceSb.append( RecurrenceType.WeeklyOn ).append( SPACE )
      .append( getTimeOfRecurrence() ).append( SPACE )
      .append( weeklyPanel.getCheckedDaysAsString( VALUE_OF_SUNDAY ) );
    try {
      cronStr = CronParser.recurrenceStringToCronString( recurrenceSb.toString() );
    } catch (CronParseException e) {
      throw new RuntimeException( "Invalid recurrence string: " + recurrenceSb.toString() );
    }
    return cronStr;
    
  }
  
  private String getMonthlyCronString() throws RuntimeException {
    String cronStr;
    StringBuilder recurrenceSb = new StringBuilder();
    if ( monthlyPanel.isDayNOfMonth() ) {
      recurrenceSb.append( RecurrenceType.DayNOfMonth ).append( SPACE )
        .append( getTimeOfRecurrence() ).append( SPACE )
        .append( monthlyPanel.getDayOfMonth() );
    } else if ( monthlyPanel.isNthDayNameOfMonth() ) {
      if ( monthlyPanel.getWeekOfMonth() != WeekOfMonth.LAST ) {
        String weekOfMonth = Integer.toString( monthlyPanel.getWeekOfMonth().value() + 1 );
        String dayOfWeek = Integer.toString( monthlyPanel.getDayOfWeek().value() + 1 );
        recurrenceSb.append( RecurrenceType.NthDayNameOfMonth ).append( SPACE )
          .append( getTimeOfRecurrence() ).append( SPACE )
          .append( dayOfWeek ).append( SPACE )
          .append( weekOfMonth );
      } else {
        String dayOfWeek = Integer.toString( monthlyPanel.getDayOfWeek().value() + 1 );
        recurrenceSb.append( RecurrenceType.LastDayNameOfMonth ).append( SPACE )
          .append( getTimeOfRecurrence() ).append( SPACE )
          .append( dayOfWeek );
      }
    } else {
      throw new RuntimeException( "There seems to not be any radio button selected, which is theoretically impossible." );
    }
    try {
      cronStr = CronParser.recurrenceStringToCronString( recurrenceSb.toString() );
    } catch (CronParseException e) {
      throw new RuntimeException( "Invalid recurrence string: " + recurrenceSb.toString() );
    }
    return cronStr;
  }
  
  private String getYearlyCronString() throws RuntimeException {
    String cronStr;
    StringBuilder recurrenceSb = new StringBuilder();
    if ( yearlyPanel.isEveryMonthOnNthDay() ) {
      String monthOfYear = Integer.toString( yearlyPanel.getMonthOfYear0().value() + 1 );
      recurrenceSb.append( RecurrenceType.EveryMonthNameN ).append( SPACE )
      .append( getTimeOfRecurrence() ).append( SPACE )
      .append( yearlyPanel.getDayOfMonth() ).append( SPACE )
      .append( monthOfYear );
    } else if ( yearlyPanel.isNthDayNameOfMonthName() ) {
      if ( yearlyPanel.getWeekOfMonth() != WeekOfMonth.LAST ) {
        String monthOfYear = Integer.toString( yearlyPanel.getMonthOfYear1().value() + 1 );
        String dayOfWeek = Integer.toString( yearlyPanel.getDayOfWeek().value() + 1 );
        String weekOfMonth = Integer.toString( yearlyPanel.getWeekOfMonth().value() + 1 );
        recurrenceSb.append( RecurrenceType.NthDayNameOfMonthName ).append( SPACE )
          .append( getTimeOfRecurrence() ).append( SPACE )
          .append( dayOfWeek ).append( SPACE )
          .append( weekOfMonth ).append( SPACE )
          .append( monthOfYear );
      } else {
        String monthOfYear = Integer.toString( yearlyPanel.getMonthOfYear1().value() + 1 );
        String dayOfWeek = Integer.toString( yearlyPanel.getDayOfWeek().value() + 1 );
        recurrenceSb.append( RecurrenceType.LastDayNameOfMonthName ).append( SPACE )
          .append( getTimeOfRecurrence() ).append( SPACE )
          .append( dayOfWeek ).append( SPACE )
          .append( monthOfYear );
      }
    } else {
      throw new RuntimeException( "There seems to not be any radio button selected, which is theoretically impossible." );
    }
    try {
      cronStr = CronParser.recurrenceStringToCronString( recurrenceSb.toString() );
    } catch (CronParseException e) {
      throw new RuntimeException( "Invalid recurrence string: " + recurrenceSb.toString() );
    }
    return cronStr;
  }
  
  private StringBuilder getTimeOfRecurrence() {
    int timeOfDayAdjust = ( startTimePicker.getTimeOfDay().equals( TimeUtil.TimeOfDay.AM ) )
      ? TimeUtil.MIN_HOUR   // 0
      : TimeUtil.MAX_HOUR;  // 12
    String strHour = StringUtils.addStringToInt( startTimePicker.getHour(), timeOfDayAdjust );
    return new StringBuilder().append( "00" ).append( SPACE )
    .append( startTimePicker.getMinute() ).append( SPACE )
    .append( strHour );
  }

  // TODO sbarkdull
  //private static DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
  
  public void setStartTime( String startTime ) {
    startTimePicker.setTime( startTime );
  }
  
  public String getStartTime() {
    return startTimePicker.getTime();
  }
  
  public void setStartDate( Date startDate ) {
    dateRangeEditor.setStartDate( startDate );
  }
  
  public Date getStartDate() {
    return dateRangeEditor.getStartDate();
  }
  
  public void setEndDate( Date endDate ) {
    dateRangeEditor.setEndDate( endDate );
  }
  
  public Date getEndDate() {
    return dateRangeEditor.getEndDate();
  }
  
  public void setNoEndDate() {
    dateRangeEditor.setNoEndDate();
  }
  
  public void setEndBy() {
    dateRangeEditor.setEndBy();
  }

  public TemporalValue getTemporalState() {
    return temporalState;
  }

  public void setTemporalState(TemporalValue temporalState) {
    this.temporalState = temporalState;
    selectTemporalPanel( temporalState );
  }
  
  public void setSecondlyError( String errorMsg ) {
    secondlyLabel.setErrorMsg( errorMsg );
  }
  
  public void setMinutelyError( String errorMsg ) {
    minutelyLabel.setErrorMsg( errorMsg );
  }
  
  public void setHourlyError( String errorMsg ) {
    hourlyLabel.setErrorMsg( errorMsg );
  }
}
