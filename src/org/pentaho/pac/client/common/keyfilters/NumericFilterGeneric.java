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

public class NumericFilterGeneric implements KeyboardListener{
  private static NumericFilterGeneric instance = null;
  
  private NumericFilterGeneric(){
    super();
  }
  
  public static synchronized NumericFilterGeneric getInstance(){
    if(instance == null){
      instance = new NumericFilterGeneric();
    }
    return instance;
  }
  
  public void onKeyDown(Widget arg0, char arg1, int arg2) {
  }

  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
     if ((!Character.isDigit(keyCode)) && (!(
        (keyCode == KEY_TAB) ||
        (keyCode == KEY_ENTER) ||
        (keyCode == KEY_BACKSPACE) ||
        (keyCode == KEY_DELETE) ||
        (keyCode == KEY_LEFT) ||
        (keyCode == KEY_RIGHT) ||
        (keyCode == KEY_UP) ||
        (keyCode == KEY_DOWN) ||
        (keyCode == KEY_HOME) ||
        (keyCode == KEY_END) ||
        (keyCode == '-')
        ))) {
       
      TextBox textBox = (TextBox)sender;
      textBox.cancelKey();
    }
  }

  public void onKeyUp(Widget arg0, char arg1, int arg2) {
  }
}

