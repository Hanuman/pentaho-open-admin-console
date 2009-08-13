/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
 *
 * Created  
 * @author Steven Barkdull
 */
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;

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
    if ( StringUtils.isEmpty( schedEd.getDescription() ) ) {
      isValid = false;
      schedEd.setDescriptionError( MSGS.specifyDescription() );
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
    schedEd.setDescriptionError( null );
    recurrenceEditorValidator.clear();
    runOnceEditorValidator.clear();
    cronEditorValidator.clear();
  }  
}
