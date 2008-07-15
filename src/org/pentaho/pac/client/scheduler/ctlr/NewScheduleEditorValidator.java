package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor;

public class NewScheduleEditorValidator extends ScheduleEditorValidator {

  public NewScheduleEditorValidator(ScheduleEditor schedEd, SchedulesModel schedulesModel ) {
    super(schedEd, schedulesModel );
  }
  
  public boolean isValid() {
    boolean isValid = super.isValid();
    
    if ( !StringUtils.isEmpty( schedEd.getName() ) && !StringUtils.isEmpty( schedEd.getGroupName() ) ) {
      Schedule s = schedulesModel.get( schedEd.getName() );
      if ( null != s ) {
        isValid = false;
        schedEd.setNameError( "Schedule with name \"" + schedEd.getName() + "\" already exists. Select another name." );
      }
    }
    
    return isValid;
  }
}
