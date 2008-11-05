package org.pentaho.pac.client.common.keyfilters;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumericFilterPositiveInteger implements KeyboardListener{
  private static NumericFilterPositiveInteger instance = null;
  
  private NumericFilterPositiveInteger(){
    super();
  }
  
  public static synchronized NumericFilterPositiveInteger getInstance(){
    if(instance == null){
      instance = new NumericFilterPositiveInteger();
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
      ((TextBox)sender).cancelKey();
    }
  }

  public void onKeyUp(Widget arg0, char arg1, int arg2) {
  }
}
