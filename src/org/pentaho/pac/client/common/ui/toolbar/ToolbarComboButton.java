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
package org.pentaho.pac.client.common.ui.toolbar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarComboButton extends ToolbarButton{
  
  private String COMBO_STYLE = "toolbar-combo-button";   //$NON-NLS-1$
  private MenuBar menu;
  
  /**
   * Constructs a toolbar button with an image and a label
   * 
   * @param img GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarComboButton(Image img, String label){
    super(img, label);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  
  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarComboButton(Image img, Image disabledImage, String label){
    super(img, disabledImage, label);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  

  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarComboButton(Image img, Image disabledImage){
    super(img, disabledImage);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  
  /**
   * Constructs a toolbar button with an image
   * 
   * @param img GWT Image object 
   */
  public ToolbarComboButton(Image img){
    super(img);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  
  private void addDropdownControl(){
    
    
  }
  
  @Override
  public void setCommand(Command cmd) {
    throw new UnsupportedOperationException("Not implemented in this class");   //$NON-NLS-1$
  }
  
  private PopupPanel createPopup(){
    
    PopupPanel popup = new PopupPanel(true);
    popup.setWidget(menu);
    return popup;
  }
  
  @Override
  protected void addStyleMouseListener(){
    eventWrapper.addMouseListener(new MouseListener(){
      private PopupPanel popup;
      public void onMouseDown(Widget w, int x, int y) {
        if(!enabled){
          return;
        }
        if(this.popup != null){
          popup.hide();
          popup.removeFromParent();
        }
        
        this.popup = createPopup();
        
        popup.setPopupPosition(w.getAbsoluteLeft(), w.getAbsoluteTop() + w.getOffsetHeight());
        popup.show();
      }
      public void onMouseEnter(Widget w) {
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$
      }
      public void onMouseLeave(Widget w) {
        button.removeStyleName(stylePrimaryName+"-hovering");   //$NON-NLS-1$
      }
      public void onMouseUp(Widget w, int x, int y) {
      }
      public void onMouseMove(Widget w, int x, int y) {}
    });
  }
  
  public void setMenu(MenuBar bar){
    this.menu = bar;
  }
}
