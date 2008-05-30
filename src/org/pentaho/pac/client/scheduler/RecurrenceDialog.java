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
package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class RecurrenceDialog extends ConfirmDialog {

  private RecurrenceEditor recurrenceEditor = new RecurrenceEditor();
  private ICallback<Object> onRemoveRecurrence = null;
  
  public RecurrenceDialog() {
    super();
    setClientSize("470px", "300px");  //$NON-NLS-1$//$NON-NLS-2$
    setTitle("Schedule Recurrence");

    addWidgetToClientArea( recurrenceEditor );

    final RecurrenceDialog localThis = this;
    Button removeRecurrenceBtn = new Button( "Remove Recurrence", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onRemoveRecurrence ) {
          onRemoveRecurrence.onHandle( null );
        }
        localThis.hide();
      }
    });
    addButton(removeRecurrenceBtn);
  }
  
  public RecurrenceEditor getRecurrenceEditor() {
    return recurrenceEditor;
  }
  
  public void setOnRemoveRecurrence( ICallback<Object> callback ) {
    onRemoveRecurrence = callback;
  }
}
