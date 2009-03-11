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
package org.pentaho.pac.client.common.keyfilters;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumericFilterFloat implements KeyboardListener{
  private static NumericFilterFloat instance = null;
  
  private NumericFilterFloat(){
    super();
  }
  
  public static synchronized NumericFilterFloat getInstance(){
    if(instance == null){
      instance = new NumericFilterFloat();
    }
    return instance;
  }
  
  public void onKeyDown(Widget arg0, char arg1, int arg2) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
        && (keyCode != (char) KEY_BACKSPACE)
        && (keyCode != (char) KEY_DELETE) && (keyCode != (char) KEY_ENTER) 
        && (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
        && (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP)
        && (keyCode != (char) KEY_RIGHT) && (keyCode != (char) KEY_DOWN)) {
      //TODO: Allow for '.' & '-'
      ((TextBox)sender).cancelKey();
    }
  }

  public void onKeyUp(Widget arg0, char arg1, int arg2) {
  }
}