package org.pentaho.pac.server.datasources;

import java.io.Serializable;

/**
 * Class used for storing parameters that are identified solely by their names.
 * Overrides equals() method so that <B>ONLY PARAMETER NAMES</b> are checked for equality.
 * @author Alex Silva
 *
 */
public class NamedParameter implements Serializable{
  private String name;
  private Object value;

  public NamedParameter() {
  }
  
  public NamedParameter(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public boolean equals(Object o) {
    if (!(o instanceof NamedParameter))
      return false;

    // just name matters
    String oname = ((NamedParameter) o).getName();
    return oname == null && name == null || oname.equals(name);
  }

  public int hashCode() {
    return name == null ? 0 : name.hashCode();
  }

}