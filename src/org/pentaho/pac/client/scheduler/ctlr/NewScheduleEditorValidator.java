/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;
import org.pentaho.pac.client.scheduler.view.ScheduleEditor;

public class NewScheduleEditorValidator extends ScheduleEditorValidator {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

  public NewScheduleEditorValidator(ScheduleEditor schedEd, SchedulesModel schedulesModel ) {
    super(schedEd, schedulesModel );
  }
  
  public boolean isValid() {
    boolean isValid = super.isValid();
    
    if ( !StringUtils.isEmpty( schedEd.getName() ) && !StringUtils.isEmpty( schedEd.getGroupName() ) ) {
      Schedule s = schedulesModel.get( schedEd.getName() );
      if ( null != s ) {
        isValid = false;
        schedEd.setNameError( MSGS.scheduleNameAlreadyExists( schedEd.getName() ) );
      }
    }
    
    return isValid;
  }
}
