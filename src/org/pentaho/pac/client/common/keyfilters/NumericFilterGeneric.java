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

