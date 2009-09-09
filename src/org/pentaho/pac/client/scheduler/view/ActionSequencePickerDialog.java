/*
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
*/
package org.pentaho.pac.client.scheduler.view;

import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.i18n.Messages;

public class ActionSequencePickerDialog extends ConfirmDialog {

  private ActionSequencePicker actionSequencePicker = new ActionSequencePicker();
  public static final String OPEN_LABEL = Messages.getString("openBtnLabel"); //$NON-NLS-1$
  public static final String SELECT_LABEL = Messages.getString("selectBtnLabel"); //$NON-NLS-1$
  
  public ActionSequencePickerDialog( String title ) {
    super( title );
    this.setNoBorderOnClientPanel();
    this.addWidgetToClientArea( actionSequencePicker );
    setOkBtnLabel( SELECT_LABEL );
  }
  
  public ActionSequencePicker getActionSequencePicker() {
    return actionSequencePicker;
  }
}
