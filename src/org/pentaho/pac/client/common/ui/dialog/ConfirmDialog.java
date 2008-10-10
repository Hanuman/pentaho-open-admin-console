package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.gwt.widgets.client.ui.ICallback;

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
    cancelBtn = new Button(MSGS.cancel(), new ClickListener() {
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
