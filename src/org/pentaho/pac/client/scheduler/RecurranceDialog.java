package org.pentaho.pac.client.scheduler;

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

  private TimePicker startTimePicker = null;
  private DatePickerEx endTimeDatePicker = null;
  private Panel dailyPanel = null;
  private Panel weeklyPanel = null;
  private Panel monthlyPanel = null;
  private Panel yearlyPanel = null;
  //private Panel customPanel = null;
  private Map<TemporalValue, Panel> temporalPanelMap = new HashMap<TemporalValue, Panel>();
  private enum TemporalValue {
    DAILY, WEEKLY, MONTHLY, YEARLY/*, CUSTOM*/
  }
  
  enum DayOfWeek {
    SUN( 0, "Sunday" ),
    MON( 1, "Monday" ),
    TUES( 2, "Tuesday" ),
    WED( 3, "Tuesday" ),
    THUR( 4, "Tuesday" ),
    FRI( 5, "Tuesday" ),
    SAT( 6, "Tuesday" );
    
    DayOfWeek( int value, String name ) {
      this.value = value;
      this.name = name;
    }
    private final int value;
    private final String name;
    private static DayOfWeek[] week = { SUN, MON, TUES, WED, THUR, FRI, SAT };
    
    public int value() { return value; }
    public String toString() { return name; }
    public static DayOfWeek get( int idx ) { return week[idx]; }
    public static int length() { return week.length; }
  }
  
  private static Map<TemporalValue, String> temporalStringMap = new HashMap<TemporalValue, String>();
  static {
    temporalStringMap.put( TemporalValue.DAILY, "Daily" );
    temporalStringMap.put( TemporalValue.WEEKLY, "Weekly" );
    temporalStringMap.put( TemporalValue.MONTHLY, "Monthly" );
    temporalStringMap.put( TemporalValue.YEARLY, "Yearly" );
    //temporalStringMap.put( TemporalValue.CUSTOM, "Custom" );
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
  
  private static final String[] WHICH_WEEK = {
    "first", "second", "third", "fourth" };
  private static final String WEEKDAY = "weekday";
  private static final String LAST = "last";
  
  private static final String TEMPORAL_RB_GROUP = "temporal-group";
  private static final String DAILY_RB_GROUP = "daily-group";
  private static final String MONTHLY_RB_GROUP = "monthly-group";
  private static final String YEARLY_RB_GROUP = "yearly-group";
  private static final String END_DATE_RB_GROUP = "end-date-group";
  
  
  public RecurranceDialog() {
    super();
    setClientSize( "430px", "300px" );
    
    Panel p = createStartTimePanel();
    addWidgetToClientArea( p );
    setTitle( "Schedule Recurrence" );
    
    p = createRecurrancePanel();
    addWidgetToClientArea( p );
    
    p = createRangePanel();
    addWidgetToClientArea( p );
  }

  private Panel createStartTimePanel() {
    SimpleGroupBox startTimeGB = new SimpleGroupBox( "Start Time");

    // add calendar control for start time
    startTimePicker = new TimePicker();
    startTimeGB.add( startTimePicker );
    
    return startTimeGB;
  }
  
  private Panel createRecurrancePanel() {
    
    SimpleGroupBox recurranceGB = new SimpleGroupBox( "Recurrence pattern" );
    
    HorizontalPanel hp = new HorizontalPanel();
    recurranceGB.add( hp );

    dailyPanel = createDailyRecurrancePanel();
    weeklyPanel = createWeeklyRecurrancePanel();
    monthlyPanel = createMonthlyRecurrancePanel();
    yearlyPanel = createYearlyRecurrancePanel();
    //customPanel = createCustomRecurrancePanel();
    
    // must come after creation of temporal panels
    assert null != dailyPanel: "";
    Panel p = createTemporalRadioGroup();
    p.setStyleName( "temporalRadioGroup");
    hp.add( p );
    
    hp.add( dailyPanel );
    hp.add( weeklyPanel );
    hp.add( monthlyPanel );
    hp.add( yearlyPanel );
    //hp.add( customPanel );
    
    return recurranceGB;
  }
  
  private Panel createRangePanel() {
    SimpleGroupBox rangeGB = new SimpleGroupBox( "Range of recurrence");
    
    HorizontalPanel hp = new HorizontalPanel();
    rangeGB.add( hp );

    hp.add( new Label( "Start:" ) );
    // add calendar control for start time
    startTimePicker = new TimePicker();
    hp.add( startTimePicker );
    
    VerticalPanel vp = new VerticalPanel();
    hp.add( vp );
    
    // add end time radio buttons to vp
    HorizontalPanel endByHP = new HorizontalPanel();
    vp.add( endByHP );
    // add end by radio button and calendar control to endByHp
    
    Panel p = createEndDatePanel();
    vp.add( p );
    
    return rangeGB;
  }
  
  private RadioButton createTemporalRadioButton( final TemporalValue temporalVal, Panel temporalPanel ) {

    String name = temporalStringMap.get( temporalVal );
    RadioButton rb = new RadioButton( TEMPORAL_RB_GROUP, name );
    rb.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        selectTemporalPanel( temporalVal );
      }
    });
    temporalPanelMap.put( temporalVal, temporalPanel );
    
    return rb;
  }
  
  private Panel createTemporalRadioGroup() {
    assert dailyPanel != null : "Temporal panels must be initialized before calling createTemporalRadioGroup.";
    VerticalPanel vp = new VerticalPanel();
    
    RadioButton rb = createTemporalRadioButton( TemporalValue.DAILY, dailyPanel );
    rb.setChecked( true );
    dailyPanel.setVisible( true );
    vp.add( rb );
    
    rb = createTemporalRadioButton( TemporalValue.WEEKLY, weeklyPanel );
    vp.add( rb );
    
    rb = createTemporalRadioButton( TemporalValue.MONTHLY, monthlyPanel );
    vp.add( rb );
    
    rb = createTemporalRadioButton( TemporalValue.YEARLY, yearlyPanel );
    vp.add( rb );
    
//    rb = createTemporalRadioButton( TemporalValue.CUSTOM, customPanel );
//    vp.add( rb );
    
    return vp;
  }
  
  private Panel createDailyRecurrancePanel() {
    VerticalPanel vp = new VerticalPanel();
    vp.setVisible( false );
    
    HorizontalPanel hp = new HorizontalPanel();
    RadioButton rb = new RadioButton( DAILY_RB_GROUP, "Every" );
    hp.add( rb );
    TextBox tb = new TextBox();
    tb.setWidth( "3em" );
    hp.add( tb );
    hp.add( new Label( "day(s)" ) );
    vp.add( hp );
    
    rb = new RadioButton( DAILY_RB_GROUP, "Every weekday" );
    vp.add( rb );
    
    return vp;
  }
  
  private Panel createWeeklyRecurrancePanel() {
    VerticalPanel vp = new VerticalPanel();
    vp.setStyleName( "weeklyRecurrancePanel" );
    vp.setVisible( false );

    Label l = new Label( "Recur every week on:" );
    vp.add( l );
    
    FlexTable gp = new FlexTable();
    gp.setCellPadding( 0 );
    gp.setCellSpacing( 0 );
    // add Sun - Wed
    final int ITEMS_IN_ROW = 4;
    for ( int ii=0; ii<ITEMS_IN_ROW; ++ii ) {
      DayOfWeek day = DayOfWeek.get( ii );
      CheckBox cb = new CheckBox( day.toString() );
      gp.setWidget( 0 , ii, cb );
      dayToCheckBox.put( day, cb );
    }
    // Add Thur - Sat
    for ( int ii=ITEMS_IN_ROW; ii<DayOfWeek.length(); ++ii ) {
      DayOfWeek day = DayOfWeek.get( ii );
      CheckBox cb = new CheckBox( day.toString() );
      gp.setWidget( 1 , ii-4, cb );
      dayToCheckBox.put( day, cb );
    }
    vp.add(gp);
    
    return vp;
  }
  
  private Panel createMonthlyRecurrancePanel() {
    VerticalPanel vp = new VerticalPanel();
    vp.setVisible( false );

    HorizontalPanel hp = new HorizontalPanel();
    RadioButton rb = new RadioButton( MONTHLY_RB_GROUP, "Day " );
    hp.add( rb );
    TextBox tb = new TextBox();
    tb.setWidth( "3em" );
    hp.add( tb );
    hp.add( new Label( " of every month" ) );
    vp.add( hp );

    hp = new HorizontalPanel();
    rb = new RadioButton( MONTHLY_RB_GROUP, "The " );
    hp.add( rb );
    ListBox l = new ListBox();
    for ( int ii=0; ii<WHICH_WEEK.length; ++ii ) {
      String week = WHICH_WEEK[ ii ];
      l.addItem( week );
    }
    hp.add( l );
    l = new ListBox();
    for ( int ii=0; ii<DayOfWeek.length(); ++ii ) {
      DayOfWeek day = DayOfWeek.get( ii );
      l.addItem( day.toString() );
    }
    hp.add( l );
    hp.add( new Label( " of every month" ) );
    vp.add( hp );

    hp = new HorizontalPanel();
    rb = new RadioButton( MONTHLY_RB_GROUP, "The last " );
    hp.add( rb );
    l = new ListBox();
    for ( int ii=0; ii<DayOfWeek.length(); ++ii ) {
      DayOfWeek day = DayOfWeek.get( ii );
      l.addItem( day.toString() );
    }
    l.addItem( WEEKDAY );
    hp.add( l );
    hp.add( new Label( " of every month" ) );
    vp.add( hp );
    
    return vp;
  }
  
  private Panel createYearlyRecurrancePanel() {
    VerticalPanel vp = new VerticalPanel();
    vp.setVisible( false );
    vp.add( new Label( "createYearlyRecurrancePanel:" ) );
    return vp;
  }
  /*
  private Panel createCustomRecurrancePanel() {
    VerticalPanel vp = new VerticalPanel();
    vp.setVisible( false );
    vp.add( new Label( "createCustomRecurrancePanel:" ) );
    return vp;
  }
  */
  private Panel createEndDatePanel() {
    final RecurranceDialog localThis = this;
    
    VerticalPanel vp = new VerticalPanel();

    RadioButton noEndDateRb = new RadioButton( END_DATE_RB_GROUP, "No end date" );
    noEndDateRb.setChecked( true );
    vp.add( noEndDateRb );
    HorizontalPanel hp = new HorizontalPanel();
    vp.add( hp );
    
    RadioButton endByRb = new RadioButton( END_DATE_RB_GROUP, "End by:" );
    hp.add( endByRb );
    endTimeDatePicker = new DatePickerEx();
    endTimeDatePicker.setEnabled( false );
    hp.add( endTimeDatePicker );


    noEndDateRb.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        localThis.endTimeDatePicker.setEnabled( false );
      }
    });
    
    endByRb.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        localThis.endTimeDatePicker.setEnabled( true );
      }
    });
    
    return vp;
    
  }
  
  private void selectTemporalPanel( TemporalValue selectedTemporalValue ) {
    Set<TemporalValue> keys = temporalPanelMap.keySet();
    Iterator<TemporalValue> keysIt = keys.iterator();
    while ( keysIt.hasNext() ) {
      TemporalValue key = keysIt.next();
      Panel p = temporalPanelMap.get( key );
      boolean bShow = key.equals( selectedTemporalValue );
      p.setVisible( bShow );
    }
  }
}
