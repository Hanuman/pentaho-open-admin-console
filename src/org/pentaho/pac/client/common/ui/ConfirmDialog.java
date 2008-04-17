package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmDialog extends MessageDialog {

  protected Button cancelBtn = null;
  private ICallbackHandler cancelHandler = new ICallbackHandler() {
    public void onHandle(Object o) {
      hide();
    }
  };
  
  public ConfirmDialog( String title, String msg ) {
    super( title, msg );
    cancelBtn = new Button(MSGS.cancel(), new ClickListener() {
      public void onClick(Widget sender) {
        if (cancelHandler != null) {
          cancelHandler.onHandle(sender);
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
  
  public void setOnCancelHandler( final ICallbackHandler handler )
  {
    cancelHandler = handler;
  }
}
