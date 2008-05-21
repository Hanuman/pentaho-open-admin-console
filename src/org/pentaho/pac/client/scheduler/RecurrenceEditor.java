package org.pentaho.pac.client.scheduler;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pentaho.pac.client.common.ui.SimpleGroupBox;
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

  private TimePicker runTimePicker = null;

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

  enum DayOfWeek {
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

  enum MonthOfYear {
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

  //  private static final String[] INT_TO_DAY_OF_WEEK = {
  //    "Sunday",
  //    "Monday",
  //    "Tuesday",
  //    "Wednesday",
  //    "Thursday",
  //    "Friday",
  //    "Saturday"};

  private Map<DayOfWeek, CheckBox> dayToCheckBox = new HashMap<DayOfWeek, CheckBox>();

  private static final String[] WHICH_WEEK = { "first", "second", "third", "fourth" };

  private static final String WEEKDAY = "weekday";

  private static final String LAST = "last";

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
    runTimePicker = new TimePicker();
    startTimeGB.add(runTimePicker);

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

    public TextBox repeatValueTb = new TextBox();
    
    public DailyRecurrencePanel() {
      setVisible(false);

      HorizontalPanel hp = new HorizontalPanel();
      RadioButton rb = new RadioButton(DAILY_RB_GROUP, "Every");
      rb.setStyleName("recurrenceRadioButton");
      rb.setChecked(true);
      hp.add(rb);

      repeatValueTb.setWidth("3em");
      repeatValueTb.setTitle("Number of days to repeat.");
      hp.add(repeatValueTb);

      Label l = new Label("day(s)");
      l.setStyleName("endLabel");
      hp.add(l);
      add(hp);

      rb = new RadioButton(DAILY_RB_GROUP, "Every weekday");
      rb.setStyleName("recurrenceRadioButton");
      add(rb);
    }
    
    public String getRepeatValue() {
      return repeatValueTb.getText();
    }
    
    public void setRepeatValue( String repeatValue ) {
      repeatValueTb.setText( repeatValue );
    }
  }

  private class WeeklyRecurrencePanel extends VerticalPanel {
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
      CheckBox defaultCb = dayToCheckBox.get(DayOfWeek.MON);
      defaultCb.setChecked(true);
      add(gp);
    }
  }

  private class MonthlyRecurrencePanel extends VerticalPanel {
    public MonthlyRecurrencePanel() {
      setVisible(false);

      HorizontalPanel hp = new HorizontalPanel();
      RadioButton rb = new RadioButton(MONTHLY_RB_GROUP, "Day");
      rb.setStyleName("recurrenceRadioButton");
      hp.add(rb);
      TextBox tb = new TextBox();
      tb.setWidth("3em");
      hp.add(tb);
      Label l = new Label("of every month");
      l.setStyleName("endLabel");
      hp.add(l);
      add(hp);

      hp = new HorizontalPanel();
      rb = new RadioButton(MONTHLY_RB_GROUP, "The");
      rb.setStyleName("recurrenceRadioButton");
      hp.add(rb);
      ListBox lb = new ListBox();
      for (int ii = 0; ii < WHICH_WEEK.length; ++ii) {
        String week = WHICH_WEEK[ii];
        lb.addItem(week);
      }
      hp.add(lb);

      lb = createDayOfWeekListBox();
      hp.add(lb);
      l = new Label("of every month");
      l.setStyleName("endLabel");
      hp.add(l);
      add(hp);

      hp = new HorizontalPanel();
      rb = new RadioButton(MONTHLY_RB_GROUP, "The last");
      rb.setStyleName("recurrenceRadioButton");
      hp.add(rb);
      lb = createDayOfWeekListBox();
      lb.addItem(WEEKDAY);
      hp.add(lb);
      l = new Label("of every month");
      l.setStyleName("endLabel");
      hp.add(l);
      add(hp);
    }
  }

  private class YearlyRecurrencePanel extends VerticalPanel {
    private RadioButton rb = null;

    private TextBox tb = null;

    private static final String YEARLY_RB_GROUP = "yearly-group";

    public YearlyRecurrencePanel() {
      setVisible(false);

      HorizontalPanel p = new HorizontalPanel();
      rb = new RadioButton(YEARLY_RB_GROUP, "Every");
      rb.setStyleName("recurrenceRadioButton");
      rb.setChecked(true);
      p.add(rb);
      ListBox lb = createMonthOfYearListBox();
      p.add(lb);
      tb = new TextBox();
      tb.setWidth("3em");
      p.add(tb);
      add(p);

      p = new HorizontalPanel();
      rb = new RadioButton(YEARLY_RB_GROUP, "The");
      rb.setStyleName("recurrenceRadioButton");
      p.add(rb);
      lb = createWhichWeekListBox();
      p.add(lb);
      lb = createDayOfWeekListBox();
      p.add(lb);
      Label l = new Label("of");
      l.setStyleName("middleLabel");
      p.add(l);
      lb = createMonthOfYearListBox();
      p.add(lb);
      add(p);

      p = new HorizontalPanel();
      rb = new RadioButton(YEARLY_RB_GROUP, "The last");
      rb.setStyleName("recurrenceRadioButton");
      p.add(rb);
      lb = createDayOfWeekListBox();
      p.add(lb);
      l = new Label("of");
      l.setStyleName("middleLabel");
      p.add(l);
      lb = createMonthOfYearListBox();
      p.add(lb);
      add(p);
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
    for (int ii = 0; ii < WHICH_WEEK.length; ++ii) {
      String which = WHICH_WEEK[ii];
      l.addItem(which);
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
}
