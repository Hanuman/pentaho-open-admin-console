package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.scheduler.ctlr.CronEditorValidator;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor;

public class ScheduleEditorValidator implements IUiValidator {

  private ScheduleEditor schedEd = null;
  private RecurrenceEditorValidator recurrenceEditorValidator = null;
  private RunOnceEditorValidator runOnceEditorValidator = null;
  private CronEditorValidator cronEditorValidator = null;
  
  public ScheduleEditorValidator( ScheduleEditor schedEd ) {
    this.schedEd = schedEd;
    this.recurrenceEditorValidator = new RecurrenceEditorValidator( this.schedEd.getRecurrenceEditor() );
    this.runOnceEditorValidator = new RunOnceEditorValidator( this.schedEd.getRunOnceEditor() );
    this.cronEditorValidator = new CronEditorValidator( this.schedEd.getCronEditor() );
  }
  
  public boolean isValid() {
    boolean isValid = true;
    
    if ( StringUtils.isEmpty( schedEd.getName() ) ) {
      isValid = false;
      schedEd.setNameError( "Specify a name." );
    }
    
    if ( StringUtils.isEmpty( schedEd.getGroupName() ) ) {
      isValid = false;
      schedEd.setGroupNameError( "Specify a group name." );
    }

    switch ( schedEd.getScheduleType() ) {
      case RUN_ONCE:
        isValid &= runOnceEditorValidator.isValid();
        break;
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        isValid &= recurrenceEditorValidator.isValid();
        break;
      case CRON:
        isValid &= cronEditorValidator.isValid();
        break;
      default:
        throw new RuntimeException( "Unrecognized ScheduleType in ScheduleEditorValidator.isValid(): "
            + schedEd.getScheduleType().toString() );
    }
    
    return isValid;
  }

  public void clear() {
    schedEd.setNameError( null );
    schedEd.setGroupNameError( null );
    recurrenceEditorValidator.clear();
    runOnceEditorValidator.clear();
    cronEditorValidator.clear();
  }  
}
