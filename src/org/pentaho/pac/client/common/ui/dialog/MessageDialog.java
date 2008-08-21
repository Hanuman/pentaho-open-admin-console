package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.IResponseCallback;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class MessageDialog extends BasicDialog {

  protected static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  protected Label msgLabel = null;
  protected Button okBtn = null;
  private ICallback<MessageDialog> okHandler = new ICallback<MessageDialog>() {
    public void onHandle(MessageDialog md) {
      hide();
    }
  };
  private IResponseCallback<MessageDialog, Boolean> validateHandler = null;
  
  public MessageDialog( String title, String msg ) {
    super( title );
    
    msgLabel = new Label();
    setMessage( msg );
    final MessageDialog localThis = this;
    
    okBtn = new Button(MSGS.ok(), new ClickListener() {
      public void onClick(Widget sender) {
        boolean isValid = ( null != validateHandler )
          ? validateHandler.onHandle( localThis )
          : true;
        if ( isValid ) {
          if (okHandler != null) {
            okHandler.onHandle(localThis);
          }
        }
      }
    });
    addButton(okBtn);
  }
  
  
  public MessageDialog( String title ) {
    this( title, "" ); //$NON-NLS-1$
  }
  
  public MessageDialog() {
    this(""); //$NON-NLS-1$
  }
  
  public String getMessage() {
    return msgLabel.getText();
  }

  public void setMessage(String msg) {
    String oldMsg = msgLabel.getText();
    msgLabel.setText(msg);

    if ( StringUtils.isEmpty( oldMsg ) ) {
      insertRowIntoClientArea( msgLabel, 0 );
    }
    
    if ( StringUtils.isEmpty( msg ) ) {
      removeRowFromClientArea( 0 );
    }
  }
  
  public void setOnOkHandler( final ICallback<MessageDialog> handler )
  {
    okHandler = handler;
  }
  
  /**
   * Set the onValidate handler. If the onValidate handler's onHandle() method
   * return false, the onOk handler will NOT be called. If it turns true, the
   * onOk handler will be called.
   * If the onValidate handler is null, this class will behave as if the
   * onValidate handler always returns true (i.e. the onOk handler will
   * always be called).
   * 
   * @param handler
   */
  public void setOnValidateHandler( final IResponseCallback<MessageDialog, Boolean> handler )
  {
    validateHandler = handler;
  }

  /**
   * see: http://google-web-toolkit.googlecode.com/svn-history/r229/trunk/samples/mail/src/com/google/gwt/sample/mail/client/AboutDialog.java
   */
  public boolean onKeyDownPreview(char key, int modifiers) {
    boolean eventNotHandled = super.onKeyDownPreview( key, modifiers );
    if ( eventNotHandled ) {
      switch (key) {
        case KeyboardListener.KEY_ENTER:
          okBtn.click();
          return false;
        default:
          return true;
      }
    } else {
      return false;
    }
  }

  @Override
  public void setSize(String width, String height) {
    this.setClientSize(width, height);
    super.setSize(width, height);
  }
  
  public void setOkBtnEnabled( boolean enabled ) {
    okBtn.setEnabled( enabled );
  }
  
  public void setOkBtnLabel( String newLabel ) {
    okBtn.setText( newLabel );
  }
  
  public String getOkBtnLabel() {
    return okBtn.getText();
  }
}