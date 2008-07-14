package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.common.util.TimeUtil;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.DailyRecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.HourlyRecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.MinutelyRecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.MonthlyRecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.SecondlyRecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.WeeklyRecurrenceEditor;
import org.pentaho.pac.client.scheduler.view.RecurrenceEditor.YearlyRecurrenceEditor;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class RecurrenceEditorValidator implements IUiValidator {
  
  private RecurrenceEditor recurrenceEditor = null;
  private DateRangeEditorValidator dateRangeEditorValidator = null;
  private static final String MUST_BE_A_NUMBER = "must be a number greater than 0 and less than 2147483647.";
  
  public RecurrenceEditorValidator( RecurrenceEditor recurrenceEditor ) {
    this.recurrenceEditor = recurrenceEditor; 
    this.dateRangeEditorValidator = new DateRangeEditorValidator( recurrenceEditor.getDateRangeEditor() );
  }
  
  public boolean isValid() {
    boolean isValid = true;
    switch ( recurrenceEditor.getTemporalState() ) {
      case SECONDS:
        SecondlyRecurrenceEditor sEd = recurrenceEditor.getSecondlyEditor();
        String seconds = sEd.getValue();
        if ( !StringUtils.isPositiveInteger( seconds ) 
            || ( Integer.parseInt( seconds ) <= 0 ) ) {
          isValid = false;
        }
        if ( Integer.parseInt( seconds ) > TimeUtil.MAX_SECOND_BY_MILLISEC ) {
          isValid = false;
        }
        if ( !isValid ) {
          sEd.setValueError( "Seconds must be a number <= " + TimeUtil.MAX_SECOND_BY_MILLISEC );
        }
        break;
      case MINUTES:
        MinutelyRecurrenceEditor mEd = recurrenceEditor.getMinutelyEditor();
        String minutes = mEd.getValue();
        if ( !StringUtils.isPositiveInteger( minutes ) 
            || ( Integer.parseInt( minutes ) <= 0 ) ) {
          isValid = false;
        }
        if ( Integer.parseInt( minutes ) > TimeUtil.MAX_MINUTE_BY_MILLISEC ) {
          isValid = false;
        }
        if ( !isValid ) {
          mEd.setValueError( "Minues must be a number <= " + TimeUtil.MAX_MINUTE_BY_MILLISEC );
        }
        break;
      case HOURS:
        HourlyRecurrenceEditor hEd = recurrenceEditor.getHourlyEditor();
        String hours = hEd.getValue();
        if ( !StringUtils.isPositiveInteger( hours ) 
            || ( Integer.parseInt( hours ) <= 0 ) ) {
          isValid = false;
        }
        if ( Integer.parseInt( hours ) > TimeUtil.MAX_HOUR_BY_MILLISEC ) {
          isValid = false;
        }
        if ( !isValid ) {
          hEd.setValueError( "Hours must be a number <= " + TimeUtil.MAX_HOUR_BY_MILLISEC );
        }
        break;
      case DAILY:
        DailyRecurrenceEditor dEd = recurrenceEditor.getDailyEditor();
        if ( dEd.isEveryNDays() ) {
          String days = dEd.getRepeatValue();
          if ( !StringUtils.isPositiveInteger( days ) 
              || ( Integer.parseInt( days ) <= 0 ) ) {
            isValid = false;
            dEd.setRepeatError( "Days " + MUST_BE_A_NUMBER );
          }
        }
        break;
      case WEEKLY:
        WeeklyRecurrenceEditor wEd = recurrenceEditor.getWeeklyEditor();
        if ( wEd.getNumCheckedDays() < 1 ) {
          isValid = false;
          wEd.setEveryDayOnError( "One or more days must be checked." );
        }
        break;
      case MONTHLY:
        MonthlyRecurrenceEditor monthlyEd = recurrenceEditor.getMonthlyEditor();
        if ( monthlyEd.isDayNOfMonth() ) {
          String dayNOfMonth = monthlyEd.getDayOfMonth();
          if ( !StringUtils.isPositiveInteger( dayNOfMonth ) 
              || !TimeUtil.isDayOfMonth( Integer.parseInt( dayNOfMonth ) ) ) {
            isValid = false;
            monthlyEd.setDayNOfMonthError( "Day of month must be a number between 1 and 31." );
          }
        }
        break;
      case YEARLY:
        YearlyRecurrenceEditor yearlyEd = recurrenceEditor.getYearlyEditor();
        if ( yearlyEd.isEveryMonthOnNthDay() ) {
          String dayNOfMonth = yearlyEd.getDayOfMonth();
          if ( !StringUtils.isPositiveInteger( dayNOfMonth ) 
              || !TimeUtil.isDayOfMonth( Integer.parseInt( dayNOfMonth ) ) ) {
            isValid = false;
            yearlyEd.setDayOfMonthError( "Day of month must be a number between 1 and 31." );
          }
        }
        break;
      default:
        throw new RuntimeException( "Unrecognized ScheduleType in RecurrenceEditor.isValid():"
          + recurrenceEditor.getTemporalState().toString() );
    }
    isValid &= dateRangeEditorValidator.isValid();
    return isValid;
  }

  public void clear() {
    recurrenceEditor.getSecondlyEditor().setValueError( null );
    recurrenceEditor.getMinutelyEditor().setValueError( null );
    recurrenceEditor.getHourlyEditor().setValueError( null );
    recurrenceEditor.getDailyEditor().setRepeatError( null );
    recurrenceEditor.getWeeklyEditor().setEveryDayOnError( null );
    recurrenceEditor.getMonthlyEditor().setDayNOfMonthError( null );
    recurrenceEditor.getYearlyEditor().setDayOfMonthError( null );
    dateRangeEditorValidator.clear();
  }
}
