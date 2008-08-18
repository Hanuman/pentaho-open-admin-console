package org.pentaho.pac.client.scheduler.view;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class DualModeScheduleEditor extends ScheduleEditor {

  private CheckBox subscriptionCb = new CheckBox();
  
  public DualModeScheduleEditor() {
    super();
    HorizontalPanel p = new HorizontalPanel();
    Label l = new Label( "Subscription Schedule" );
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
