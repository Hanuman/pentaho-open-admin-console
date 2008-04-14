package org.pentaho.pac.client.common.ui;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageDialog extends DialogBox implements ClickListener {
  
  private Label msgLabel = null;
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

  // TODO delete these

  public static final int UNKNOWN_BTN = 0;
  public static final int OK_BTN = 1;
  public static final int CANCEL_BTN = 2;
  public static final int YES_BTN = 4;
  public static final int NO_BTN = 8;
  
  public Button okBtn = new Button(MSGS.ok());

  // delete me
  /**
   * @deprecated
   */
  public MessageDialog( String title, String msg, int[] btns ) {
    this( title, msg );
  }
  // delete me
  /**
   * @deprecated
   */
  public MessageDialog(  int[] btns ) {
    this( );
  }
  // delete me
  /**
   * @deprecated
   */
  public MessageDialog( String x, int[] btns ) {
    this( x );
  }

  // delete me
  /**
   * @deprecated
   */
  public int getButtonPressed() {
    return 1;
  }
  
  public MessageDialog( String title, String msg ) {
    super();
    
    //this.setStylePrimaryName( "messageDialog" );
    msgLabel = new Label( msg );
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.setStylePrimaryName( "messageDialog.messagePanel" );  //$NON-NLS-1$
    verticalPanel.add(msgLabel);
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.setStylePrimaryName( "messageDialog.buttonPanel" );  //$NON-NLS-1$
    horizontalPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
    horizontalPanel.add(okBtn);
    okBtn.addClickListener(this);
    
    verticalPanel.setCellWidth(msgLabel, "100%"); //$NON-NLS-1$
    verticalPanel.setCellHeight(msgLabel, "100%"); //$NON-NLS-1$
    verticalPanel.setSpacing(10);
    verticalPanel.setWidth("250px"); //$NON-NLS-1$
    verticalPanel.setHeight("150px"); //$NON-NLS-1$
    
    verticalPanel.add(horizontalPanel);
    
    setWidget(verticalPanel);
    setText(title);
  }
  
  public MessageDialog(String msg) {
    this( "", msg ); //$NON-NLS-1$
  }
  
  public MessageDialog() {
    this(""); //$NON-NLS-1$
  }

  public String getMessage() {
    return msgLabel.getText();
  }

  public void setMessage(String msg) {
    msgLabel.setText(msg);
  }
  
  public void onClick(Widget sender) {
    if (sender == okBtn) {
      hide();
    }else{
      // no-op
    }
  }
}