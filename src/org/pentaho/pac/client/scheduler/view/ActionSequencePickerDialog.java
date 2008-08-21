package org.pentaho.pac.client.scheduler.view;

import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;

public class ActionSequencePickerDialog extends ConfirmDialog {

  private ActionSequencePicker actionSequencePicker = new ActionSequencePicker();
  public static final String OPEN_LABEL = "Open";
  public static final String SELECT_LABEL = "Select";
  
  public ActionSequencePickerDialog() {
    this.setNoBorderOnClientPanel();
    this.addWidgetToClientArea( actionSequencePicker );
    setOkBtnLabel( SELECT_LABEL );
  }
  
  public ActionSequencePicker getActionSequencePicker() {
    return actionSequencePicker;
  }
}
