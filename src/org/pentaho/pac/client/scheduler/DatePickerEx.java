package org.pentaho.pac.client.scheduler;

import java.util.Date;

import com.google.gwt.user.client.Event;

public class DatePickerEx extends org.zenika.widget.client.datePicker.DatePicker {

  public DatePickerEx() {
    super();
  }

  /**
   * Create a DatePicker which show a specific Date.
   * @param selectedDate Date to show
   */
  public DatePickerEx(Date selectedDate) {
    super( selectedDate );
  }
  
  /**
   * Create a DatePicker which uses a specific theme.
   * @param theme Theme name
   */
  public DatePickerEx(String theme) {
    super( theme );
  }
  
  /**
   * Create a DatePicker which specifics date and theme.
   * @param selectedDate Date to show
   * @param theme Theme name
   */
  public DatePickerEx(Date selectedDate, String theme) {
    super(selectedDate, theme);
  }
}
