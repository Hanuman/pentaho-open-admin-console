package org.pentaho.pac.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageDialog extends DialogBox implements ClickListener {
  
  int result = 0;
  HTML msgLabel = null;
  
  public static final int OK_BTN = 1;
  public static final int CANCEL_BTN = 2;
  public static final int YES_BTN = 4;
  public static final int NO_BTN = 8;
  public Button okBtn = new Button(PentahoAdminConsole.getLocalizedMessages().ok());
  public Button cancelBtn = new Button(PentahoAdminConsole.getLocalizedMessages().cancel());
  public Button yesBtn = new Button(PentahoAdminConsole.getLocalizedMessages().yes());
  public Button noBtn = new Button(PentahoAdminConsole.getLocalizedMessages().no());
  
  public MessageDialog(String msg, int[] buttons) {
    this("", msg, buttons); //$NON-NLS-1$
  }
  
  public MessageDialog(String title, String msg, int[] buttons) {
    super();
    
    msgLabel = new HTML(msg);
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(msgLabel);
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    for (int i = 0; i < buttons.length; i++) {
      if (buttons[i] == OK_BTN) {
        horizontalPanel.add(okBtn);
        okBtn.addClickListener(this);
      } else if (buttons[i] == CANCEL_BTN) {
        horizontalPanel.add(cancelBtn);
        cancelBtn.addClickListener(this);
      } else if (buttons[i] == YES_BTN) {
        horizontalPanel.add(yesBtn);
        yesBtn.addClickListener(this);
      } else if (buttons[i] == NO_BTN) {
        horizontalPanel.add(noBtn);
        noBtn.addClickListener(this);
      }
    }
    
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
    this(msg, new int[]{OK_BTN});
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

  public void show() {
    result = 0;
    super.show();
  }
  
  public void onClick(Widget sender) {
    if (sender == okBtn) {
      result = OK_BTN;
    } else if (sender == cancelBtn) {
      result = CANCEL_BTN;
    } else if (sender == yesBtn) {
      result = YES_BTN;
    } else if (sender == noBtn) {
      result = NO_BTN;
    }
    hide();
  }
  
  public int getButtonPressed() {
    return result;
  }

  public Label getMsgLabel() {
    return msgLabel;
  }

  public Button getOkBtn() {
    return okBtn;
  }

  public Button getCancelBtn() {
    return cancelBtn;
  }

  public Button getYesBtn() {
    return yesBtn;
  }

  public Button getNoBtn() {
    return noBtn;
  }
}
