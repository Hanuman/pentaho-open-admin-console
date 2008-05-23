package org.pentaho.pac.client.scheduler;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pentaho.pac.client.common.EnumException;
import org.pentaho.pac.client.common.ui.DatePickerEx;
import org.pentaho.pac.client.common.ui.SimpleGroupBox;
import org.pentaho.pac.client.common.ui.TimePicker;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.common.util.TimeUtil.DayOfWeek;
import org.pentaho.pac.client.common.util.TimeUtil.MonthOfYear;
import org.pentaho.pac.client.common.util.TimeUtil.WeekOfMonth;
import org.pentaho.pac.client.scheduler.CronParser.RecurrenceType;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RecurrenceEditor extends VerticalPanel {

  private TimePicker startTimePicker = null;

  private DatePickerEx startDatePicker = null;

  private DatePickerEx endDatePicker = null;
  
  private ListBox temporalCombo = null;

  private SecondlyRecurrencePanel secondlyPanel = null;

  private MinutelyRecurrencePanel minutelyPanel = null;

  private HourlyRecurrencePanel hourlyPanel = null;

  private DailyRecurrencePanel dailyPanel = null;

  private WeeklyRecurrencePanel weeklyPanel = null;

  private MonthlyRecurrencePanel monthlyPanel = null;

  private YearlyRecurrencePanel yearlyPanel = null;
  
  private static final String SPACE = " "; //$NON-NLS-1$

  //private Panel customPanel = null;
  private Map<TemporalValue, Panel> temporalPanelMap = new HashMap<TemporalValue, Panel>();

  enum TemporalValue {
    SECONDS(0, "Seconds"), 
    MINUTES(1, "Minutes"), 
    HOURS(2, "Hours"), 
    DAILY(3, "Daily"), 
    WEEKLY(4, "Weekly"), 
    MONTHLY(5, "Monthly"), 
    YEARLY(6, "Yearly");

    TemporalValue(int value, String name) {
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

  // TODO sbarkdull, move to TimeUtil
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

  private static final String WEEKDAY = "weekday";

  private static final String DAILY_RB_GROUP = "daily-group"; //$NON-NLS-1$

  private static final String MONTHLY_RB_GROUP = "monthly-group"; //$NON-NLS-1$

  private static final String END_DATE_RB_GROUP = "end-date-group"; //$NON-NLS-1$
  
  private static final String DEFAULT_RECURRENCE_STR = "";

  public RecurrenceEditor() {
    super();

    Panel p = createStartTimePanel();
    add(p);

    p = createRecurrencePanel();
    add(p);

    p = createRangePanel();
    add(p);
    
    inititalizeWithRecurrenceString( DEFAULT_RECURRENCE_STR );
  }
  
  public void inititalizeWithRecurrenceString( String recurrenceStr ) {
    
    
  }
  
  /**
   * 
   * @param strRepeatInSecs
   */
  public void inititalizeWithRepeat( int repeatInSecs ) {

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
    temporalCombo.setSelectedIndex( currentVal.value );
    selectTemporalPanel( currentVal );
  }

  
  private Panel createStartTimePanel() {
    SimpleGroupBox startTimeGB = new SimpleGroupBox("Start Time");

    // add calendar control for start time
    startTimePicker = new TimePicker();
    startTimeGB.add(startTimePicker);

    return startTimeGB;
  }

  private Panel createRecurrencePanel() {

    SimpleGroupBox recurrenceGB = new SimpleGroupBox("Recurrence pattern");

    VerticalPanel p = new VerticalPanel();
    recurrenceGB.add(p);

    secondlyPanel = new SecondlyRecurrencePanel();
    minutelyPanel = new MinutelyRecurrencePanel();
    hourlyPanel = new HourlyRecurrencePanel();
    dailyPanel = new DailyRecurrencePanel();
    weeklyPanel = new WeeklyRecurrencePanel();
    monthlyPanel = new MonthlyRecurrencePanel();
    yearlyPanel = new YearlyRecurrencePanel();

    // must come after creation of temporal panels
    assert null != dailyPanel : "";
    temporalCombo = createTemporalCombo();
    p.add(temporalCombo);

    p.add(secondlyPanel);
    p.add(minutelyPanel);
    p.add(hourlyPanel);
    p.add(dailyPanel);
    p.add(weeklyPanel);
    p.add(monthlyPanel);
    p.add(yearlyPanel);

    return recurrenceGB;
  }

  private Panel createRangePanel() {
    SimpleGroupBox rangeGB = new SimpleGroupBox("Range of recurrence");

    HorizontalPanel hp = new HorizontalPanel();
    rangeGB.add(hp);

    Label l = new Label("Start:");
    l.setStyleName("startLabel");
    hp.add(l);
    startDatePicker = new DatePickerEx();
    Date now = new Date();
    startDatePicker.setSelectedDate(now);
    startDatePicker.setYoungestDate(now);
    hp.add(startDatePicker);

    VerticalPanel vp = new VerticalPanel();
    hp.add(vp);

    // add end time radio buttons to vp
    HorizontalPanel endByHP = new HorizontalPanel();
    vp.add(endByHP);
    // add end by radio button and calendar control to endByHp

    Panel p = createEndDatePanel();
    vp.add(p);

    return rangeGB;
  }

  private void addTemporalListItem(ListBox lb, final TemporalValue temporalVal, Panel temporalPanel) {

    String name = temporalVal.toString();
    lb.addItem( name );
    temporalPanelMap.put(temporalVal, temporalPanel);
  }

  private ListBox createTemporalCombo() {
    assert dailyPanel != null : "Temporal panels must be initialized before calling createTemporalCombo.";
    
    final RecurrenceEditor localThis = this;
    ListBox lb = new ListBox();
    lb.setVisibleItemCount( 1 );
    lb.setStyleName("temporalCombo"); //$NON-NLS-1$
    lb.addChangeListener( new ChangeListener() {
      public void onChange(Widget sender) {
        ListBox localLb = (ListBox)sender;
        String strTemporalVal = localLb.getItemText( localLb.getSelectedIndex() );
        try {
          TemporalValue t = TemporalValue.stringToTemporalValue( strTemporalVal );
          selectTemporalPanel( t );
        } catch (EnumException e) {
          // TODO sbarkdull, popup dialog
          e.printStackTrace();
        }
      }
    });
    
    addTemporalListItem( lb, TemporalValue.SECONDS, secondlyPanel );
    lb.setItemSelected( 0, true );
    secondlyPanel.setVisible(true);

    addTemporalListItem( lb, TemporalValue.MINUTES, minutelyPanel );
    addTemporalListItem( lb, TemporalValue.HOURS, hourlyPanel );
    addTemporalListItem( lb, TemporalValue.DAILY, dailyPanel );
    addTemporalListItem( lb, TemporalValue.WEEKLY, weeklyPanel);
    addTemporalListItem( lb, TemporalValue.MONTHLY, monthlyPanel);
    addTemporalListItem( lb, TemporalValue.YEARLY, yearlyPanel);

    return lb;
  }

  private class SimpleRecurrencePanel extends HorizontalPanel {
    private TextBox valueTb = new TextBox();
    
    public SimpleRecurrencePanel( String strLabel ) {
      setVisible(false);

      Label l = new Label( "Every");
      l.setStyleName("startLabel");
      add(l);

      valueTb.setWidth("3em");
      valueTb.setTitle("Number of " + strLabel + " to repeat.");
      add(valueTb);

      l = new Label( strLabel );
      l.setStyleName("endLabel");
      add(l);
    }
    
    public String getValue() {
      return valueTb.getText();
    }
    
    public void setValue( String val ) {
      valueTb.setText( val );
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
      add(hp);

      everyWeekdayRb.setStyleName("recurrenceRadioButton");
      add(everyWeekdayRb);
    }
    
    public String getRepeatValue() {
      return repeatValueTb.getText();
    }
    
    public void setRepeatValue( String repeatValue ) {
      repeatValueTb.setText( repeatValue );
    }
    
    public void setEveryNDays() {
      everyNDaysRb.setChecked( true );
    }
    
    public boolean isEveryNDays() {
      return everyNDaysRb.isChecked();
    }
    
    public void setEveryWeekday() {
      everyWeekdayRb.setChecked( true );
    }
    
    public boolean isEveryWeekday() {
      return everyWeekdayRb.isChecked();
    }
  }

  private class WeeklyRecurrencePanel extends VerticalPanel {

    private Map<DayOfWeek, CheckBox> dayToCheckBox = new HashMap<DayOfWeek, CheckBox>();
    
    public WeeklyRecurrencePanel() {
      setStyleName("weeklyRecurrencePanel");
      setVisible(false);

      Label l = new Label("Recur every week on:");
      l.setStyleName("startLabel");
      add(l);

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
    
    /**
     * 
     * @param valueOfSunday int used to adjust the starting point of the weekday sequence.
     * If this value is 0, Sun-Sat maps to 0-6, if this value is 1, Sun-Sat maps to 1-7, etc.
     * @return
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
  }

  private class MonthlyRecurrencePanel extends VerticalPanel {
    
    private RadioButton dayNOfMonthRb = new RadioButton(MONTHLY_RB_GROUP, "Day");
    private RadioButton nthDayNameOfMonthRb = new RadioButton(MONTHLY_RB_GROUP, "The");
    private TextBox dayOfMonthTb = new TextBox();
    private ListBox whichWeekLb = createWhichWeekListBox();
    private ListBox dayOfWeekLb = createDayOfWeekListBox();
    
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
      add(hp);

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
    
    public void setDayNOfMonth() {
      dayNOfMonthRb.setChecked( true );
    }
    
    public boolean isDayNOfMonth() {
      return dayNOfMonthRb.isChecked();
    }
    
    public String getDayOfMonth() {
      return dayOfMonthTb.getText();
    }
    
    public void setDayOfMonth( String dayOfMonth ) {
      dayOfMonthTb.setText( dayOfMonth );
    }
    
    public void setNthDayNameOfMonth() {
      nthDayNameOfMonthRb.setChecked( true );
    }
    
    public boolean isNthDayNameOfMonth() {
      return nthDayNameOfMonthRb.isChecked();
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
  }

  private class YearlyRecurrencePanel extends VerticalPanel {
    
    private RadioButton everyMonthOnNthDayRb = new RadioButton(YEARLY_RB_GROUP, "Every");
    private RadioButton nthWeekOfMonthOfDayOfWeekOfMonth = new RadioButton(YEARLY_RB_GROUP, "The");
    private TextBox dayOfMonthTb = new TextBox();
    private ListBox monthOfYearLb0 = createMonthOfYearListBox();
    private ListBox monthOfYearLb1 = createMonthOfYearListBox();
    private ListBox whichWeekLb = createWhichWeekListBox();
    private ListBox dayOfWeekLb = createDayOfWeekListBox();

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
      add(p);

      p = new HorizontalPanel();
      nthWeekOfMonthOfDayOfWeekOfMonth.setStyleName("recurrenceRadioButton"); //$NON-NLS-1$
      p.add(nthWeekOfMonthOfDayOfWeekOfMonth);
      p.add(whichWeekLb);
      p.add(dayOfWeekLb);
      Label l = new Label("of");
      l.setStyleName("middleLabel"); //$NON-NLS-1$
      p.add(l);
      p.add( monthOfYearLb1 );
      add(p);
    }
    
    public boolean isEveryMonthOnNthDayRb() {
      return everyMonthOnNthDayRb.isChecked();
    }
    
    public void setEveryMonthOnNthDayRb() {
      everyMonthOnNthDayRb.setChecked( true );
    }
    
    public boolean isNthWeekOfMonthOfDayOfWeekOfMonth() {
      return nthWeekOfMonthOfDayOfWeekOfMonth.isChecked();
    }
    
    public void setNthWeekOfMonthOfDayOfWeekOfMonth() {
      nthWeekOfMonthOfDayOfWeekOfMonth.setChecked( true );
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
  }

  public class RecurrenceEditorException extends Exception {
    private static final long serialVersionUID = 666L;

    public RecurrenceEditorException( String msg ) {
      super( msg );
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

  private Panel createEndDatePanel() {
    final RecurrenceEditor localThis = this;

    VerticalPanel vp = new VerticalPanel();

    RadioButton noEndDateRb = new RadioButton(END_DATE_RB_GROUP, "No end date");
    noEndDateRb.setStyleName("recurrenceRadioButton");
    noEndDateRb.setChecked(true);
    vp.add(noEndDateRb);
    HorizontalPanel hp = new HorizontalPanel();
    vp.add(hp);

    RadioButton endByRb = new RadioButton(END_DATE_RB_GROUP, "End by:");
    endByRb.setStyleName("recurrenceRadioButton");
    hp.add(endByRb);
    endDatePicker = new DatePickerEx();
    endDatePicker.setEnabled(false);
    hp.add(endDatePicker);

    noEndDateRb.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        localThis.endDatePicker.setEnabled(false);
      }
    });

    endByRb.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        localThis.endDatePicker.setEnabled(true);
      }
    });

    return vp;

  }

  private void selectTemporalPanel(TemporalValue selectedTemporalValue) {
    Set<TemporalValue> keys = temporalPanelMap.keySet();
    Iterator<TemporalValue> keysIt = keys.iterator();
    while (keysIt.hasNext()) {
      TemporalValue key = keysIt.next();
      Panel p = temporalPanelMap.get(key);
      boolean bShow = key.equals(selectedTemporalValue);
      p.setVisible(bShow);
    }
  }

  private boolean isValidNumOfDaysForMonth(int numDays, MonthOfYear month) {
    if (numDays < 1) {
      return false;
    } else {
      return validNumDaysOfMonth.get(month) <= numDays;
    }
  }

  public String getRepeatInSecs() throws RecurrenceEditorException {
    int selIdx = temporalCombo.getSelectedIndex();
    TemporalValue tv = TemporalValue.get( selIdx );    
    
    switch ( tv ) {
      case SECONDS:
        return secondlyPanel.getValue();
      case MINUTES:
        return Integer.toString( TimeUtil.minutesToSecs( Integer.parseInt( minutelyPanel.getValue() ) ) );
      case HOURS:
        return Integer.toString( TimeUtil.hoursToSecs( Integer.parseInt( hourlyPanel.getValue() ) ) );
      case DAILY:
        return Integer.toString( TimeUtil.daysToSecs( Integer.parseInt( dailyPanel.getRepeatValue() ) ) );
      default:
        throw new RecurrenceEditorException( "Calling getRepeatInSec() is invalid for TemporalType: " + tv.toString() );
    }
  }

  // TODO sbarkdull
//public void setRepeatInSecs(String repeatInSec) {
//
//}

  /**
   * 
   * @return null if the selected schedule does not support CRON, otherwise
   * return the CRON string.
   * @throws RecurrenceEditorException
   */
  public String getCronString() throws RecurrenceEditorException {
    TemporalValue tv = TemporalValue.get( temporalCombo.getSelectedIndex() );
    switch ( tv ) {
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
        throw new RecurrenceEditorException( "Invalid TemporalValue in UI: " + tv );
    }
  }
  
  private String getDailyCronString() throws RecurrenceEditorException {
    String cronStr;
    StringBuilder recurranceSb = new StringBuilder();
    if ( dailyPanel.isEveryNDays() ) {
      return null;
    } else {
      // must be every weekday
      recurranceSb.append( RecurrenceType.EveryWeekday ).append( SPACE )
        .append( getTimeOfRecurrance() );
      try {
        cronStr = CronParser.recurrenceStringToCronString( recurranceSb.toString() );
      } catch (CronParseException e) {
        throw new RecurrenceEditorException( "Invalid recurrence string: " + recurranceSb.toString() );
      }
      return cronStr;
    }
  }
  
  private String getWeeklyCronString() throws RecurrenceEditorException {
    String cronStr;
    StringBuilder recurranceSb = new StringBuilder();
    // WeeklyOn 0 33 6 1,3,5
    recurranceSb.append( RecurrenceType.WeeklyOn ).append( SPACE )
      .append( getTimeOfRecurrance() ).append( SPACE )
      .append( weeklyPanel.getCheckedDaysAsString( 1 ) );
    try {
      cronStr = CronParser.recurrenceStringToCronString( recurranceSb.toString() );
    } catch (CronParseException e) {
      throw new RecurrenceEditorException( "Invalid recurrence string: " + recurranceSb.toString() );
    }
    return cronStr;
    
  }
  
  private String getMonthlyCronString() throws RecurrenceEditorException {
    String cronStr;
    StringBuilder recurranceSb = new StringBuilder();
    if ( monthlyPanel.isDayNOfMonth() ) {
      recurranceSb.append( RecurrenceType.DayNOfMonth ).append( SPACE )
        .append( getTimeOfRecurrance() ).append( SPACE )
        .append( monthlyPanel.getDayOfMonth() );
    } else if ( monthlyPanel.isNthDayNameOfMonth() ) {
      if ( monthlyPanel.getWeekOfMonth() != WeekOfMonth.LAST ) {
        String weekOfMonth = Integer.toString( monthlyPanel.getWeekOfMonth().value() + 1 );
        String dayOfWeek = Integer.toString( monthlyPanel.getDayOfWeek().value() + 1 );
        recurranceSb.append( RecurrenceType.NthDayNameOfMonth ).append( SPACE )
          .append( getTimeOfRecurrance() ).append( SPACE )
          .append( dayOfWeek ).append( SPACE )
          .append( weekOfMonth );
      } else {
        String dayOfWeek = Integer.toString( monthlyPanel.getDayOfWeek().value() + 1 );
        recurranceSb.append( RecurrenceType.LastDayNameOfMonth ).append( SPACE )
          .append( getTimeOfRecurrance() ).append( SPACE )
          .append( dayOfWeek );
      }
    } else {
      throw new RecurrenceEditorException( "There seems to not be any radio button selected, which is theoretically impossible." );
    }
    try {
      cronStr = CronParser.recurrenceStringToCronString( recurranceSb.toString() );
    } catch (CronParseException e) {
      throw new RecurrenceEditorException( "Invalid recurrence string: " + recurranceSb.toString() );
    }
    return cronStr;
  }
  
  private String getYearlyCronString() throws RecurrenceEditorException {
    String cronStr;
    StringBuilder recurranceSb = new StringBuilder();
    if ( yearlyPanel.isEveryMonthOnNthDayRb() ) {
      String monthOfYear = Integer.toString( yearlyPanel.getMonthOfYear0().value() + 1 );
      recurranceSb.append( RecurrenceType.EveryMonthNameN ).append( SPACE )
      .append( getTimeOfRecurrance() ).append( SPACE )
      .append( yearlyPanel.getDayOfMonth() ).append( SPACE )
      .append( monthOfYear );
    } else if ( yearlyPanel.isNthWeekOfMonthOfDayOfWeekOfMonth() ) {
      if ( yearlyPanel.getWeekOfMonth() != WeekOfMonth.LAST ) {
        String monthOfYear = Integer.toString( yearlyPanel.getMonthOfYear1().value() + 1 );
        String dayOfWeek = Integer.toString( yearlyPanel.getDayOfWeek().value() + 1 );
        String weekOfMonth = Integer.toString( yearlyPanel.getWeekOfMonth().value() + 1 );
        recurranceSb.append( RecurrenceType.NthDayNameOfMonthName ).append( SPACE )
          .append( getTimeOfRecurrance() ).append( SPACE )
          .append( dayOfWeek ).append( SPACE )
          .append( weekOfMonth ).append( SPACE )
          .append( monthOfYear );
      } else {
        String monthOfYear = Integer.toString( yearlyPanel.getMonthOfYear1().value() + 1 );
        String dayOfWeek = Integer.toString( yearlyPanel.getDayOfWeek().value() + 1 );
        recurranceSb.append( RecurrenceType.LastDayNameOfMonthName ).append( SPACE )
          .append( getTimeOfRecurrance() ).append( SPACE )
          .append( dayOfWeek ).append( SPACE )
          .append( monthOfYear );
      }
    } else {
      throw new RecurrenceEditorException( "There seems to not be any radio button selected, which is theoretically impossible." );
    }
    try {
      cronStr = CronParser.recurrenceStringToCronString( recurranceSb.toString() );
    } catch (CronParseException e) {
      throw new RecurrenceEditorException( "Invalid recurrence string: " + recurranceSb.toString() );
    }
    return cronStr;
  }
  
  private StringBuilder getTimeOfRecurrance() {
    int timeOfDayAdjust = ( startTimePicker.getTimeOfDay().equals( TimeUtil.TimeOfDay.AM ) )
      ? TimeUtil.MIN_HOUR   // 0
      : TimeUtil.MAX_HOUR;  // 12
    String strHour = Integer.toString( Integer.parseInt( startTimePicker.getHour() ) + timeOfDayAdjust );
    return new StringBuilder().append( "00" ).append( SPACE )
    .append( startTimePicker.getMinute() ).append( SPACE )
    .append( strHour );
  }
  
  public String getStartDateTime() {
    String strDate = startDatePicker.getText();
    String[] dateParts = strDate.split( "/" );
    int month = Integer.parseInt( dateParts[0] ) - 1;
    String strMonth = MonthOfYear.get( month ).toString();
    return TimeUtil.getDateTimeString(strMonth, dateParts[1], dateParts[2], 
        startTimePicker.getHour(), startTimePicker.getMinute(), "00", startTimePicker.getTimeOfDay());
  }
  
  public String getEndDate() {
    String strDate = endDatePicker.getText();
    String[] dateParts = strDate.split( "/" );
    int month = Integer.parseInt( dateParts[0] ) - 1;
    String strMonth = MonthOfYear.get( month ).toString();
    return TimeUtil.getDateString( strMonth, dateParts[1], dateParts[2] );
  }
}
