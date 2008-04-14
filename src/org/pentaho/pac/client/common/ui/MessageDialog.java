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
  protected static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private HorizontalPanel btnPanel = null; 
  protected int buttonPressed = NONE_BTN;
  
  public static final int NONE_BTN = 0;
  public static final int OK_BTN = 1;
  
  protected Button okBtn = new Button(MSGS.ok());

  public int getButtonPressed() {
    return buttonPressed;
  }
  
  public MessageDialog( String title, String msg ) {
    super();
    
    msgLabel = new Label( msg );
    VerticalPanel clientPanel = new VerticalPanel();
    clientPanel.setStylePrimaryName( "messageDialog.clientPanel" );  //$NON-NLS-1$
    clientPanel.add(msgLabel);
    btnPanel = new HorizontalPanel();
    btnPanel.setStylePrimaryName( "messageDialog.buttonPanel" );  //$NON-NLS-1$
    btnPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );

    okBtn.addClickListener(this);
    addBtn(okBtn);
    
    clientPanel.setCellWidth(msgLabel, "100%"); //$NON-NLS-1$
    clientPanel.setCellHeight(msgLabel, "100%"); //$NON-NLS-1$
    clientPanel.setSpacing(10);
    clientPanel.setWidth("250px"); //$NON-NLS-1$
    clientPanel.setHeight("150px"); //$NON-NLS-1$
    
    clientPanel.add(btnPanel);
    
    setWidget(clientPanel);
    setText(title);
  }
  
  public MessageDialog( String title ) {
    this( title, "" ); //$NON-NLS-1$
  }
  
  public MessageDialog() {
    this(""); //$NON-NLS-1$
  }

  public void addBtn( Button btn ) {
    btnPanel.add( btn );
  }
  
  public String getMessage() {
    return msgLabel.getText();
  }

  public void setMessage(String msg) {
    msgLabel.setText(msg);
  }
  
  public void onClick(Widget sender) {
    if (sender == okBtn) {
      buttonPressed = OK_BTN;
      hide();
    }
  }
}