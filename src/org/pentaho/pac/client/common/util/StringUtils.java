package org.pentaho.pac.client.common.util;

// see: http://commons.apache.org/lang/apidocs/org/apache/commons/lang/StringUtils.html

public class StringUtils {

  public static boolean isEmpty( String str ) {
    return null == str || "".equals( str ); //$NON-NLS-1$
  }
  
  public static String defaultString( String str, String xdefault )
  {
    return StringUtils.isEmpty( str ) ? xdefault : str;
  }
  
  public static String defaultString( String str )
  {
    return StringUtils.isEmpty( str ) ? "" : str; //$NON-NLS-1$
  }
  
  public static String defaultIfEmpty( String str, String xdefault )
  {
    return StringUtils.isEmpty( str ) ? xdefault : str;
  }
}
