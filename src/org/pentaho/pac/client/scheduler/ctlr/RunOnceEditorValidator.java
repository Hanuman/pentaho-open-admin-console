package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.scheduler.view.RunOnceEditor;


public class RunOnceEditorValidator implements IUiValidator {

  private RunOnceEditor editor = null;
  
  public RunOnceEditorValidator( RunOnceEditor editor ) {
    this.editor = editor;
  }
  
  public boolean isValid() {
    boolean isValid = true;
    if ( null == editor.getStartDate() ) {
      isValid = false;
      editor.setStartDateError( "Specify a start date." );
    }
    return isValid;
  }

  public void clear() {
    editor.setStartDateError( null );
  }
}
