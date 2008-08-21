package org.pentaho.pac.client.scheduler.view;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

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
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  public DualModeScheduleEditor() {
    super();
    HorizontalPanel p = new HorizontalPanel();
    Label l = new Label( MSGS.scheduleTypeCheckboxLabel() );
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
