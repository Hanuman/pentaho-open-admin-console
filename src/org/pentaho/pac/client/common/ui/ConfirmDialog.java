package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmDialog extends MessageDialog {

  protected Button cancelBtn = null;
  private ICallbackHandler cancelHandler = null;
  
  public ConfirmDialog( String title, String msg ) {
    super( title, msg );
    final ConfirmDialog localThis = this;
    cancelBtn = new Button(MSGS.cancel(), new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != cancelHandler ) {
          cancelHandler.onHandle( sender );
        }
        localThis.hide();
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
