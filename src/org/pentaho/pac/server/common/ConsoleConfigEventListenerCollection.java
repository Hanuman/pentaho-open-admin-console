package org.pentaho.pac.server.common;

import java.util.ArrayList;

public class ConsoleConfigEventListenerCollection extends ArrayList<IConsoleConfigEventListener> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Fires a config change event to all listeners.
   * 
   */
  public void fireConfigChanged() {
    for (IConsoleConfigEventListener listener : this) {
      listener.onConfigChanged();
    }
  }
  
}
