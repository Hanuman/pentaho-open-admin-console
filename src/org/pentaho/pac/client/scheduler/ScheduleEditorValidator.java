package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.util.StringUtils;

public class ScheduleEditorValidator {

  public ScheduleEditor schedEd = null;
  
  public ScheduleEditorValidator( ScheduleEditor schedEd ) {
    this.schedEd = schedEd;
  }
  
  public boolean isValid() {
    boolean isValid = true;
    String errorMsg = null;
    
    String name = schedEd.getName();
    errorMsg = StringUtils.isEmpty( name ) ? "Name cannot be empty." : null;
    isValid &= errorMsg == null;
    schedEd.setNameError( errorMsg );

    String gName = schedEd.getGroupName();
    errorMsg = StringUtils.isEmpty( gName ) ? "Group cannot be empty." : null;
    isValid &= errorMsg == null;
    schedEd.setGroupNameError( errorMsg );
    
    return isValid;
  }  
}
