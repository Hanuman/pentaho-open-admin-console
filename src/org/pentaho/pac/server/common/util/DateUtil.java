package org.pentaho.pac.server.common.util;

import java.text.DateFormat;
import java.util.Locale;

public class DateUtil {

  // static class, hide ctor
  private DateUtil() {
    
  }
  
  public static DateFormat getDateTimeFormatter() {
    return DateFormat.getDateTimeInstance(DateFormat.LONG,
        DateFormat.MEDIUM,
        Locale.getDefault());
  }
}
