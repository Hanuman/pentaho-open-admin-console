package org.pentaho.pac.client.scheduler.view;

import com.google.gwt.user.client.ui.ListBox;

public class ListBoxEx extends ListBox {

  public ListBoxEx() {
    super();
  }
  
  public void removeAll() {
    for ( int ii=this.getItemCount()-1; ii>=0; ii-- ) {
      this.removeItem( ii );
    }
  }
}
