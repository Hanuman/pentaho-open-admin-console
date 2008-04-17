package org.pentaho.pac.client.common.ui;

import java.util.Iterator;
import java.util.List;

import org.pentaho.pac.client.PentahoAdminConsole;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccumulatorPanel extends HorizontalPanel implements ClickListener {
  Button addToAccumulationBtn = new Button("X");
  Button removeFromAccumulationBtn = new Button("Y");
  ObjectListBox availableItemsListBox = new ObjectListBox(true);
  ObjectListBox accumulatedItemsListBox = new ObjectListBox(true);
  Label availableItemsLabel = new Label(PentahoAdminConsole.getLocalizedMessages().availableItemsTitle());
  Label accumulatedItemsLabel = new Label(PentahoAdminConsole.getLocalizedMessages().assignedItemsTitle());

  public AccumulatorPanel() {
    this(new ObjectListBox(true), new ObjectListBox(true));
  }
  
  public AccumulatorPanel(ObjectListBox availableItemsListBox, ObjectListBox accumulatedItemsListBox) {
    this.accumulatedItemsListBox = accumulatedItemsListBox;
    this.availableItemsListBox  = availableItemsListBox;
    
    VerticalPanel centerVerticalPanel = new VerticalPanel();
    centerVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    centerVerticalPanel.add(addToAccumulationBtn);
    centerVerticalPanel.add(removeFromAccumulationBtn);
    centerVerticalPanel.setCellWidth(addToAccumulationBtn, "100%");
    centerVerticalPanel.setCellWidth(removeFromAccumulationBtn, "100%");
    centerVerticalPanel.setSpacing(5);
    
    VerticalPanel leftVerticalPanel = new VerticalPanel();
    leftVerticalPanel.add(availableItemsLabel);
    leftVerticalPanel.add(availableItemsListBox);
    leftVerticalPanel.setCellWidth(availableItemsListBox, "100%");
    leftVerticalPanel.setCellHeight(availableItemsListBox, "100%");
    
    VerticalPanel rightVerticalPanel = new VerticalPanel();
    rightVerticalPanel.add(accumulatedItemsLabel);
    rightVerticalPanel.add(accumulatedItemsListBox);
    rightVerticalPanel.setCellWidth(accumulatedItemsListBox, "100%");
    rightVerticalPanel.setCellHeight(accumulatedItemsListBox, "100%");
    
    setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    add(leftVerticalPanel);
    add(centerVerticalPanel);
    add(rightVerticalPanel);
    
    setCellWidth(leftVerticalPanel, "50%");
    setCellWidth(rightVerticalPanel, "50%");
    setCellHeight(leftVerticalPanel, "100%");
    setCellHeight(rightVerticalPanel, "100%");
    
    leftVerticalPanel.setWidth("100%");
    leftVerticalPanel.setHeight("100%");
    rightVerticalPanel.setWidth("100%");
    rightVerticalPanel.setHeight("100%");
    availableItemsListBox.setWidth("100%");
    availableItemsListBox.setHeight("100%");
    accumulatedItemsListBox.setWidth("100%");
    accumulatedItemsListBox.setHeight("100%");
    
    addToAccumulationBtn.addClickListener(this);
    removeFromAccumulationBtn.addClickListener(this);
  }

  public Button getAddToAccumulationBtn() {
    return addToAccumulationBtn;
  }

  public Button getRemoveFromAccumulationBtn() {
    return removeFromAccumulationBtn;
  }

  public ListBox getAvailableItemsListBox() {
    return availableItemsListBox;
  }

  public ListBox getAccumulatedItemsListBox() {
    return accumulatedItemsListBox;
  }

  public void onClick(Widget sender) {
    if (sender == addToAccumulationBtn) {
      addSelectedItemsToAccumulation();
    } else if (sender == removeFromAccumulationBtn) {
      removeSelectedItemsFromAccumulation();
    }
  }
  
  protected void removeSelectedItemsFromAccumulation() {
    List selectedObjects = accumulatedItemsListBox.getSelectedObjects();
    accumulatedItemsListBox.removeSelectedObjects();
    for (Iterator iter = selectedObjects.iterator(); iter.hasNext();) {
      availableItemsListBox.addObject(iter.next());
    }
  }
  
  protected void addSelectedItemsToAccumulation() {
    List selectedObjects = availableItemsListBox.getSelectedObjects();
    availableItemsListBox.removeSelectedObjects();
    for (Iterator iter = selectedObjects.iterator(); iter.hasNext();) {
      accumulatedItemsListBox.addObject(iter.next());
    }
  }
}
