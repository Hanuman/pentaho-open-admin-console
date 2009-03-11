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

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarToggleButton extends ToolbarButton {

  private boolean selected = false;
  private String TOGGLE_STYLE = "toolbar-toggle-button";   //$NON-NLS-1$
  /**
   * Constructs a toolbar button with an image and a label
   * 
   * @param img GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, String label){
    super(img, label);
    super.setStylePrimaryName(TOGGLE_STYLE);
  }
  
  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, Image disabledImage, String label){
    super(img, disabledImage, label);
    super.setStylePrimaryName(TOGGLE_STYLE);
  }
  

  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, Image disabledImage){
    super(img, disabledImage);
    super.setStylePrimaryName(TOGGLE_STYLE);
  }
  
  /**
   * Constructs a toolbar button with an image
   * 
   * @param img GWT Image object 
   */
  public ToolbarToggleButton(Image img){
    super(img);
    super.setStylePrimaryName(TOGGLE_STYLE);
  }
  
  /**
   * Returns a boolean based on the selected "down" state of the button
   * 
   * @return boolean flag
   */
  public boolean isSelected(){
    return this.selected;
  }
  
  private void toggleSelectedState(){
    selected = !(this.selected);
    if(selected){
      button.removeStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$
      button.addStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
      button.addStyleName(stylePrimaryName+"-down-hovering");    //$NON-NLS-1$ 
    } else {
      button.removeStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
      button.removeStyleName(stylePrimaryName+"-down-hovering");    //$NON-NLS-1$
      button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$   
    }
  }
  
  @Override
  protected void addStyleMouseListener(){
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget arg0, int arg1, int arg2) {
        if(!enabled){
          return;
        }
        toggleSelectedState();
        command.execute();
      }
      public void onMouseEnter(Widget arg0) {
        button.addStyleName(stylePrimaryName+((selected)?"-down":"")+"-hovering");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
      public void onMouseLeave(Widget arg0) {
        button.removeStyleName(stylePrimaryName+((selected)?"-down":"")+"-hovering");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        button.removeStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$ 
      }
      public void onMouseUp(Widget arg0, int arg1, int arg2) {
      }
      public void onMouseMove(Widget arg0, int arg1, int arg2) {}
    });
  }
}
