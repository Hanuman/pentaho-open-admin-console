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

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * "Dual mode" refers to the fact that this editor can handle both
 * subscription and regular schedules (public and private)
 * 
 * @author Steven Barkdull
 *
 */
public class DualModeScheduleEditor extends ScheduleEditor {

  private CheckBox subscriptionCb = new CheckBox();
  
  public DualModeScheduleEditor() {
    super();
    HorizontalPanel p = new HorizontalPanel();
    Label l = new Label( Messages.getString("scheduleTypeCheckboxLabel") ); //$NON-NLS-1$
    p.add( subscriptionCb );
    p.add( l );
    this.insert( p, 0 );
  }

  public boolean isSubscriptionSchedule() {
    return subscriptionCb.isChecked();
  }
  
  public void setSubscriptionSchedule( boolean isSubscription ) {
    subscriptionCb.setChecked( isSubscription );
  }
}
