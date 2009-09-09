/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.client.common.ui;

import java.util.Iterator;
import java.util.List;

import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccumulatorPanel<T> extends HorizontalPanel implements ClickListener {
  Button addToAccumulationBtn = new Button("&gt;"); //$NON-NLS-1$
  Button removeFromAccumulationBtn = new Button("&lt;"); //$NON-NLS-1$
  GenericObjectListBox<T> availableItemsListBox = new GenericObjectListBox<T>(true);
  GenericObjectListBox<T> accumulatedItemsListBox = new GenericObjectListBox<T>(true);
  Label availableItemsLabel = new Label(Messages.getString("availableItemsTitle")); //$NON-NLS-1$
  Label accumulatedItemsLabel = new Label(Messages.getString("assignedItemsTitle")); //$NON-NLS-1$

  public AccumulatorPanel() {
    this(new GenericObjectListBox<T>(true), new GenericObjectListBox<T>(true));
  }
  
  public AccumulatorPanel(GenericObjectListBox<T> availableItemsListBox, GenericObjectListBox<T> accumulatedItemsListBox) {
    this.accumulatedItemsListBox = accumulatedItemsListBox;
    this.availableItemsListBox  = availableItemsListBox;
    
    VerticalPanel centerVerticalPanel = new VerticalPanel();
    centerVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    centerVerticalPanel.add(addToAccumulationBtn);
    centerVerticalPanel.add(removeFromAccumulationBtn);
    centerVerticalPanel.setCellWidth(addToAccumulationBtn, "100%"); //$NON-NLS-1$
    centerVerticalPanel.setCellWidth(removeFromAccumulationBtn, "100%"); //$NON-NLS-1$
    centerVerticalPanel.setSpacing(5);
    
    VerticalPanel leftVerticalPanel = new VerticalPanel();
    leftVerticalPanel.add(availableItemsLabel);
    leftVerticalPanel.add(availableItemsListBox);
    leftVerticalPanel.setCellWidth(availableItemsListBox, "100%"); //$NON-NLS-1$
    leftVerticalPanel.setCellHeight(availableItemsListBox, "100%"); //$NON-NLS-1$
    
    VerticalPanel rightVerticalPanel = new VerticalPanel();
    rightVerticalPanel.add(accumulatedItemsLabel);
    rightVerticalPanel.add(accumulatedItemsListBox);
    rightVerticalPanel.setCellWidth(accumulatedItemsListBox, "100%"); //$NON-NLS-1$
    rightVerticalPanel.setCellHeight(accumulatedItemsListBox, "100%"); //$NON-NLS-1$
    
    setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    add(leftVerticalPanel);
    add(centerVerticalPanel);
    add(rightVerticalPanel);
    
    setCellWidth(leftVerticalPanel, "50%"); //$NON-NLS-1$
    setCellWidth(rightVerticalPanel, "50%"); //$NON-NLS-1$
    setCellHeight(leftVerticalPanel, "100%"); //$NON-NLS-1$
    setCellHeight(rightVerticalPanel, "100%"); //$NON-NLS-1$
     
    leftVerticalPanel.setWidth("100%"); //$NON-NLS-1$
    leftVerticalPanel.setHeight("100%"); //$NON-NLS-1$
    rightVerticalPanel.setWidth("100%"); //$NON-NLS-1$
    rightVerticalPanel.setHeight("100%"); //$NON-NLS-1$ 
    availableItemsListBox.setWidth("100%");//$NON-NLS-1$
    availableItemsListBox.setHeight("100%"); //$NON-NLS-1$
    accumulatedItemsListBox.setWidth("100%"); //$NON-NLS-1$
    accumulatedItemsListBox.setHeight("100%"); //$NON-NLS-1$
    
    addToAccumulationBtn.addClickListener(this);
    removeFromAccumulationBtn.addClickListener(this);
  }

  public Button getAddToAccumulationBtn() {
    return addToAccumulationBtn;
  }

  public Button getRemoveFromAccumulationBtn() {
    return removeFromAccumulationBtn;
  }

  public GenericObjectListBox<T> getAvailableItemsListBox() {
    return availableItemsListBox;
  }

  public GenericObjectListBox<T> getAccumulatedItemsListBox() {
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
    List<T> selectedObjects = accumulatedItemsListBox.getSelectedObjects();
    accumulatedItemsListBox.removeSelectedObjects();
    for (Iterator<T> iter = selectedObjects.iterator(); iter.hasNext();) {
      availableItemsListBox.addObject(iter.next());
    }
  }
  
  protected void addSelectedItemsToAccumulation() {
    List<T> selectedObjects = availableItemsListBox.getSelectedObjects();
    availableItemsListBox.removeSelectedObjects();
    for (Iterator<T> iter = selectedObjects.iterator(); iter.hasNext();) {
      accumulatedItemsListBox.addObject(iter.next());
    }
  }
}
