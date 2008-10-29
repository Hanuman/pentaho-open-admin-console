package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.pac.client.common.ui.AccumulatorPanel;
import org.pentaho.pac.client.common.ui.GenericObjectListBox;

import com.google.gwt.user.client.ui.Button;


public class AccumulatorDialog<T> extends ConfirmDialog {
  AccumulatorPanel<T> accumulatorPanel;
  

  public AccumulatorDialog(GenericObjectListBox<T> availableItemsListBox, GenericObjectListBox<T> accumulatedItemsListBox) {
    accumulatorPanel = new AccumulatorPanel<T>(availableItemsListBox, accumulatedItemsListBox);
    initDialog();
  }
  
  public AccumulatorDialog() {
    accumulatorPanel = new AccumulatorPanel<T>();
    initDialog();
  }
  
  private void initDialog() {
    accumulatorPanel.setWidth("100%"); //$NON-NLS-1$
    accumulatorPanel.setHeight("100%");//$NON-NLS-1$
    addWidgetToClientArea(accumulatorPanel);
    setTitle(""); //$NON-NLS-1$
  }

  public GenericObjectListBox<T> getAccumulatedItemsListBox() {
    return accumulatorPanel.getAccumulatedItemsListBox();
  }

  public Button getAddToAccumulationBtn() {
    return accumulatorPanel.getAddToAccumulationBtn();
  }

  public GenericObjectListBox<T> getAvailableItemsListBox() {
    return accumulatorPanel.getAvailableItemsListBox();
  }

  public Button getRemoveFromAccumulationBtn() {
    return accumulatorPanel.getRemoveFromAccumulationBtn();
  }

  public AccumulatorPanel<T> getAccumulatorPanel() {
    return accumulatorPanel;
  }
  
}
