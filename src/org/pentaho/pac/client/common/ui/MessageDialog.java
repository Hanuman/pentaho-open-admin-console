package org.pentaho.pac.client.common.ui;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.util.StringUtils;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MessageDialog extends BasicDialog {

  protected static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  protected Label msgLabel = null;
  private Button okBtn = null;
  ICallbackHandler okHandler = new ICallbackHandler() {
    public void onHandle(Object o) {
      hide();
    }
  };
  
  public MessageDialog( String title, String msg ) {
    super( title );
    
    msgLabel = new Label();
    setMessage( msg );

    okBtn = new Button(MSGS.ok(), new ClickListener() {
      public void onClick(Widget sender) {
        if (okHandler != null) {
          okHandler.onHandle(sender);
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
  
  public void setOnOkHandler( final ICallbackHandler handler )
  {
    okHandler = handler;
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
}