package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.scheduler.CronParser;
import org.pentaho.pac.client.scheduler.view.CronEditor;

public class CronEditorValidator implements IUiValidator {

  private CronEditor editor = null;
  private DateRangeEditorValidator dateRangeEditorValidator = null;
  
  public CronEditorValidator( CronEditor editor ) {
    this.editor = editor;
    this.dateRangeEditorValidator = new DateRangeEditorValidator( editor.getDateRangeEditor() );
  }
  
  public boolean isValid() {
    boolean isValid = true;
    
    if ( !CronParser.isValidCronString( editor.getCronString() ) ) {
      isValid = false;
      editor.setCronError( "Cron string is invalid." );
    }
    isValid &= dateRangeEditorValidator.isValid();
    
    return isValid;
  }

  public void clear() {
    editor.setCronError( null );
    dateRangeEditorValidator.clear();
  }
}
