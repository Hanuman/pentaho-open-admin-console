/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler.ctlr;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.scheduler.model.Schedule;
import org.pentaho.pac.client.scheduler.model.SchedulesModel;

public class NewScheduleEditorValidator extends ScheduleEditorValidator {

  public NewScheduleEditorValidator(ScheduleEditor schedEd, SchedulesModel schedulesModel ) {
    super(schedEd, schedulesModel );
  }
  
  public boolean isValid() {
    boolean isValid = super.isValid();
    
    if ( !StringUtils.isEmpty( schedEd.getName() ) && !StringUtils.isEmpty( schedEd.getGroupName() ) ) {
      Schedule s = (null == schedulesModel)
        ? null
        : schedulesModel.get( schedEd.getName() );
      if ( null != s ) {
        isValid = false;
        schedEd.setNameError( Messages.getString("scheduleNameAlreadyExists", schedEd.getName()) ); //$NON-NLS-1$
      }
    }
    
    return isValid;
  }
}
