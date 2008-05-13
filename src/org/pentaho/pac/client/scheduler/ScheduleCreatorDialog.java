package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.ui.ConfirmDialog;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class ScheduleCreatorDialog extends ConfirmDialog {

  private ScheduleEditor scheduleEditor = new ScheduleEditor();
  
  public ScheduleCreatorDialog() {
    super();
    setTitle( "Schedule Creator" );
    setClientSize( "430px", "300px" );
    
    addWidgetToClientArea( scheduleEditor );
  }

  public ScheduleEditor getScheduleEditor() {
    return scheduleEditor;
  }
}
