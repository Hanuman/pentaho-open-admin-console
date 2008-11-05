package org.pentaho.pac.client.common.keyfilters;

import com.google.gwt.user.client.ui.KeyboardListener;

public class KeyListenerFactory {
  
  public enum FILTER_TYPE {INTEGER_POSITIVE, INTEGER, FLOAT_POSITIVE, FLOAT, NUMERIC};
  
  public static KeyboardListener getKeyboardListener(FILTER_TYPE filter_type){
    KeyboardListener result = null;
    
    switch(filter_type){
      case INTEGER_POSITIVE:{ result = NumericFilterPositiveInteger.getInstance(); }break;
      case INTEGER:{ result = NumericFilterInteger.getInstance(); }break;
      case FLOAT_POSITIVE:{ result = NumericFilterPositiveFloat.getInstance(); }break;
      case FLOAT:{ result = NumericFilterPositiveFloat.getInstance(); }break;
      case NUMERIC:{ result = NumericFilterGeneric.getInstance(); }break;
      
      default:{}break;
    }
    
    return result;
  }
}
