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
package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class ConfirmDialog extends MessageDialog {

  protected Button cancelBtn = null;
  private ICallback<BasicDialog> cancelHandler = new ICallback<BasicDialog>() {
    public void onHandle(BasicDialog dlg) {
      hide();
    }
  };
  
  public ConfirmDialog( String title, String msg ) {
    super( title, msg );
    
    setButtonPanelAlign(HasHorizontalAlignment.ALIGN_RIGHT, null);
    
    final ConfirmDialog localThis = this;
    cancelBtn = new Button(Messages.getString("cancel"), new ClickListener() { //$NON-NLS-1$
      public void onClick(Widget sender) {
        if (cancelHandler != null) {
          cancelHandler.onHandle(localThis);
        }
      }
    });
    addButton(cancelBtn);
  }
  
  public ConfirmDialog( String title ) {
    this( title, "" ); //$NON-NLS-1$
  }
  
  public ConfirmDialog() {
    this( "", "" ); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public void setOnCancelHandler( final ICallback<BasicDialog> handler )
  {
    cancelHandler = handler;
    setOnCloseHandler( handler );
  }
}
