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

import org.pentaho.gwt.widgets.client.controls.DateRangeEditor;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

public class DateRangeEditorValidator implements IUiValidator {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

  private DateRangeEditor dateRangeEditor = null;
  public DateRangeEditorValidator( DateRangeEditor dateRangeEditor ) {
    this.dateRangeEditor = dateRangeEditor; 
  }
  
  public boolean isValid() {
    boolean isValid = true;
    
    if ( null == dateRangeEditor.getStartDate() ) {
      isValid = false;
      dateRangeEditor.setStartDateError( MSGS.specifyStartDate() );
    }

    if ( dateRangeEditor.isEndBy() 
        && ( null == dateRangeEditor.getEndDate() ) ) {
      isValid = false;
      dateRangeEditor.setEndByError( MSGS.specifyEndDate() );
    }
    return isValid;
  }

  public void clear() {
    dateRangeEditor.setStartDateError( null );
    dateRangeEditor.setEndByError( null );
  }
}
