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
