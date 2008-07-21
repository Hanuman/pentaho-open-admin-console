/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * Created  
 * @author Steven Barkdull
 */
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor;

public class ScheduleEditorValidator implements IUiValidator {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  protected ScheduleEditor schedEd;
  protected SchedulesModel schedulesModel;
  protected RecurrenceEditorValidator recurrenceEditorValidator;
  protected RunOnceEditorValidator runOnceEditorValidator;
  protected CronEditorValidator cronEditorValidator;
  
  public ScheduleEditorValidator( ScheduleEditor schedEd, SchedulesModel schedulesModel ) {
    this.schedEd = schedEd;
    this.schedulesModel = schedulesModel;
    this.recurrenceEditorValidator = new RecurrenceEditorValidator( this.schedEd.getRecurrenceEditor() );
    this.runOnceEditorValidator = new RunOnceEditorValidator( this.schedEd.getRunOnceEditor() );
    this.cronEditorValidator = new CronEditorValidator( this.schedEd.getCronEditor() );
  }
  
  public boolean isValid() {
    boolean isValid = true;
    
    if ( StringUtils.isEmpty( schedEd.getName() ) ) {
      isValid = false;
      schedEd.setNameError( MSGS.specifyName() );
    }
    if ( StringUtils.isEmpty( schedEd.getGroupName() ) ) {
      isValid = false;
      schedEd.setGroupNameError( MSGS.specifyGroupName() );
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
        throw new RuntimeException( MSGS.unrecognizedSchedTypeInValidator( schedEd.getScheduleType().toString() ) );
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
