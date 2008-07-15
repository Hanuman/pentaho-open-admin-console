package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.scheduler.view.DateRangeEditor;

public class DateRangeEditorValidator implements IUiValidator {

  private DateRangeEditor dateRangeEditor = null;
  public DateRangeEditorValidator( DateRangeEditor dateRangeEditor ) {
    this.dateRangeEditor = dateRangeEditor; 
  }
  
  public boolean isValid() {
    boolean isValid = true;
    
    if ( null == dateRangeEditor.getStartDate() ) {
      isValid = false;
      dateRangeEditor.setStartDateError( "Specify a start date." );
    }

    if ( dateRangeEditor.isEndBy() 
        && ( null == dateRangeEditor.getEndDate() ) ) {
      isValid = false;
      dateRangeEditor.setEndByError( "Specify an end date." );
    }
    return isValid;
  }

  public void clear() {
    dateRangeEditor.setStartDateError( null );
    dateRangeEditor.setEndByError( null );
  }
}
