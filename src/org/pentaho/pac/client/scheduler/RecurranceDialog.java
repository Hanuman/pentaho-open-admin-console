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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pentaho.pac.client.common.ui.ConfirmDialog;
import org.pentaho.pac.client.common.ui.SimpleGroupBox;

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

public class RecurranceDialog extends ConfirmDialog {

  private TimePicker runTimePicker = null;

  private DatePickerEx startDatePicker = null;

  private DatePickerEx endDatePicker = null;

  private Panel dailyPanel = null;

  private Panel weeklyPanel = null;

  private Panel monthlyPanel = null;

  private Panel yearlyPanel = null;

  //private Panel customPanel = null;
  private Map<TemporalValue, Panel> temporalPanelMap = new HashMap<TemporalValue, Panel>();

  enum TemporalValue {
    DAILY(0, "Daily"), 
    WEEKLY(1, "Weekly"), 
    MONTHLY(2, "Monthly"), 
    YEARLY(3, "Yearly");

    TemporalValue(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static TemporalValue[] temporalValues = { 
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

  private static final String TEMPORAL_RB_GROUP = "temporal-group";

  private static final String DAILY_RB_GROUP = "daily-group";

  private static final String MONTHLY_RB_GROUP = "monthly-group";

  private static final String END_DATE_RB_GROUP = "end-date-group";

  public RecurranceDialog() {
    super();
    setClientSize("470px", "300px");

    Panel p = createStartTimePanel();
    addWidgetToClientArea(p);
    setTitle("Schedule Recurrence");

    p = createRecurrancePanel();
    addWidgetToClientArea(p);

    p = createRangePanel();
    addWidgetToClientArea(p);
  }

  public void inititalizeWithCronString(String cronStr) {
    CronParser cp = new CronParser(cronStr);
    switch (cp.getSchedType()) {
      case EveryNthDayOfMonth:
        break;
      case EveryWeekday:
        break;
      case WeeklyOn:
        break;
      case DayNOfMonth:
        break;
      case NthDayNameOfMonth:
        break;
      case LastDayNameOfMonth:
        break;
      case EveryMonthNameN:
        break;
      case NthDayNameOfMonthName:
        break;
      case LastDayNameOfMonthName:
        break;
      default:
        break;
    }
  }

  private Panel createStartTimePanel() {
    SimpleGroupBox startTimeGB = new SimpleGroupBox("Start Time");

    // add calendar control for start time
    runTimePicker = new TimePicker();
    startTimeGB.add(runTimePicker);

    return startTimeGB;
  }

  private Panel createRecurrancePanel() {

    SimpleGroupBox recurranceGB = new SimpleGroupBox("Recurrence pattern");

    HorizontalPanel hp = new HorizontalPanel();
    recurranceGB.add(hp);

    dailyPanel = createDailyRecurrancePanel();
    weeklyPanel = createWeeklyRecurrancePanel();
    monthlyPanel = createMonthlyRecurrancePanel();
    yearlyPanel = createYearlyRecurrancePanel();
    //customPanel = createCustomRecurrancePanel();

    // must come after creation of temporal panels
    assert null != dailyPanel : "";
    Panel p = createTemporalRadioGroup();
    p.setStyleName("temporalRadioGroup");
    hp.add(p);

    hp.add(dailyPanel);
    hp.add(weeklyPanel);
    hp.add(monthlyPanel);
    hp.add(yearlyPanel);
    //hp.add( customPanel );

    return recurranceGB;
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

  private RadioButton createTemporalRadioButton(final TemporalValue temporalVal, Panel temporalPanel) {

    String name = temporalVal.toString();
    RadioButton rb = new RadioButton(TEMPORAL_RB_GROUP, name);
    rb.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        selectTemporalPanel(temporalVal);
      }
    });
    temporalPanelMap.put(temporalVal, temporalPanel);

    return rb;
  }

  private Panel createTemporalRadioGroup() {
    assert dailyPanel != null : "Temporal panels must be initialized before calling createTemporalRadioGroup.";
    VerticalPanel vp = new VerticalPanel();

    RadioButton rb = createTemporalRadioButton(TemporalValue.DAILY, dailyPanel);
    rb.setChecked(true);
    dailyPanel.setVisible(true);
    vp.add(rb);

    rb = createTemporalRadioButton(TemporalValue.WEEKLY, weeklyPanel);
    vp.add(rb);

    rb = createTemporalRadioButton(TemporalValue.MONTHLY, monthlyPanel);
    vp.add(rb);

    rb = createTemporalRadioButton(TemporalValue.YEARLY, yearlyPanel);
    vp.add(rb);

    return vp;
  }

  private class DailyRecurrancePanel extends VerticalPanel {
    public DailyRecurrancePanel() {
      setVisible(false);

      HorizontalPanel hp = new HorizontalPanel();
      RadioButton rb = new RadioButton(DAILY_RB_GROUP, "Every");
      rb.setStyleName("recurranceRadioButton");
      rb.setChecked(true);
      hp.add(rb);

      TextBox tb = new TextBox();
      tb.setWidth("3em");
      tb.setTitle("Number of days to repeat.");
      hp.add(tb);

      Label l = new Label("day(s)");
      l.setStyleName("endLabel");
      hp.add(l);
      add(hp);

      rb = new RadioButton(DAILY_RB_GROUP, "Every weekday");
      rb.setStyleName("recurranceRadioButton");
      add(rb);
    }
  }

  private class WeeklyRecurrancePanel extends VerticalPanel {
    public WeeklyRecurrancePanel() {
      setStyleName("weeklyRecurrancePanel");
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

  private class MonthlyRecurrancePanel extends VerticalPanel {
    public MonthlyRecurrancePanel() {
      setVisible(false);

      HorizontalPanel hp = new HorizontalPanel();
      RadioButton rb = new RadioButton(MONTHLY_RB_GROUP, "Day");
      rb.setStyleName("recurranceRadioButton");
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
      rb.setStyleName("recurranceRadioButton");
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
      rb.setStyleName("recurranceRadioButton");
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

  private class YearlyRecurrancePanel extends VerticalPanel {
    private RadioButton rb = null;

    private TextBox tb = null;

    private static final String YEARLY_RB_GROUP = "yearly-group";

    public YearlyRecurrancePanel() {
      setVisible(false);

      HorizontalPanel p = new HorizontalPanel();
      rb = new RadioButton(YEARLY_RB_GROUP, "Every");
      rb.setStyleName("recurranceRadioButton");
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
      rb.setStyleName("recurranceRadioButton");
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
      rb.setStyleName("recurranceRadioButton");
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

  private Panel createDailyRecurrancePanel() {
    return new DailyRecurrancePanel();
  }

  private Panel createWeeklyRecurrancePanel() {
    return new WeeklyRecurrancePanel();
  }

  private Panel createMonthlyRecurrancePanel() {
    return new MonthlyRecurrancePanel();
  }

  private Panel createYearlyRecurrancePanel() {
    return new YearlyRecurrancePanel();
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
    final RecurranceDialog localThis = this;

    VerticalPanel vp = new VerticalPanel();

    RadioButton noEndDateRb = new RadioButton(END_DATE_RB_GROUP, "No end date");
    noEndDateRb.setStyleName("recurranceRadioButton");
    noEndDateRb.setChecked(true);
    vp.add(noEndDateRb);
    HorizontalPanel hp = new HorizontalPanel();
    vp.add(hp);

    RadioButton endByRb = new RadioButton(END_DATE_RB_GROUP, "End by:");
    endByRb.setStyleName("recurranceRadioButton");
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
