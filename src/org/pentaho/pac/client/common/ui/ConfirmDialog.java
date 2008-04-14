package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmDialog extends MessageDialog {

  protected Button cancelBtn = new Button(MSGS.cancel());
  public static final int CANCEL_BTN = 2;
  
  public ConfirmDialog( String title, String msg ) {
    super( title, msg );
    cancelBtn.addClickListener(this);
    addBtn(cancelBtn);
  }
  
  public ConfirmDialog( String title ) {
    this( title, "" );
  }
  
  public ConfirmDialog() {
    this( "", "" );
  }
  
  public void onClick(Widget sender) {
    if (sender == cancelBtn) {
      buttonPressed = CANCEL_BTN;
      hide();
    } else {
      super.onClick( sender );
    }
  }
}
