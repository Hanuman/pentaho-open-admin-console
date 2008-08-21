package org.pentaho.pac.client.scheduler.view;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

public class ActionSequencePickerDialog extends ConfirmDialog {

  private ActionSequencePicker actionSequencePicker = new ActionSequencePicker();
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  public static final String OPEN_LABEL = MSGS.openBtnLabel();
  public static final String SELECT_LABEL = MSGS.selectBtnLabel();
  
  public ActionSequencePickerDialog() {
    this.setNoBorderOnClientPanel();
    this.addWidgetToClientArea( actionSequencePicker );
    setOkBtnLabel( SELECT_LABEL );
  }
  
  public ActionSequencePicker getActionSequencePicker() {
    return actionSequencePicker;
  }
}
