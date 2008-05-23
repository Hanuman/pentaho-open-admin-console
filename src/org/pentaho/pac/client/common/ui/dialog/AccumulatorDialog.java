package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.pac.client.common.ui.AccumulatorPanel;
import org.pentaho.pac.client.common.ui.GenericObjectListBox;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;


public class AccumulatorDialog extends ConfirmDialog {
  AccumulatorPanel accumulatorPanel;
  
  public AccumulatorDialog(GenericObjectListBox availableItemsListBox, GenericObjectListBox accumulatedItemsListBox) {
    accumulatorPanel = new AccumulatorPanel(availableItemsListBox, accumulatedItemsListBox);
    initDialog();
  }
  
  public AccumulatorDialog() {
    accumulatorPanel = new AccumulatorPanel();
    initDialog();
  }
  
  private void initDialog() {
    accumulatorPanel.setWidth("100%"); //$NON-NLS-1$
    accumulatorPanel.setHeight("100%");//$NON-NLS-1$
    addWidgetToClientArea(accumulatorPanel);
    setTitle(""); //$NON-NLS-1$
  }

  public ListBox getAccumulatedItemsListBox() {
    return accumulatorPanel.getAccumulatedItemsListBox();
  }

  public Button getAddToAccumulationBtn() {
    return accumulatorPanel.getAddToAccumulationBtn();
  }

  public ListBox getAvailableItemsListBox() {
    return accumulatorPanel.getAvailableItemsListBox();
  }

  public Button getRemoveFromAccumulationBtn() {
    return accumulatorPanel.getRemoveFromAccumulationBtn();
  }

  public AccumulatorPanel getAccumulatorPanel() {
    return accumulatorPanel;
  }
  
}
