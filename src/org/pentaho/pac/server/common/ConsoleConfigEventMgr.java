package org.pentaho.pac.server.common;


public class ConsoleConfigEventMgr {

  private ConsoleConfigEventListenerCollection consoleConfigListeners;
  public static final String CONSOLE_CONFIG_EVENT_MGR = "ConsoleConfigEventMgr"; //$NON-NLS-1$
  public void addConfigListener(IConsoleConfigEventListener listener) {
    if (consoleConfigListeners == null) {
      consoleConfigListeners = new ConsoleConfigEventListenerCollection();
    }
    consoleConfigListeners.add(listener);
  }

  public void removeConfigListener(IConsoleConfigEventListener listener) {
    if (consoleConfigListeners != null) {
      consoleConfigListeners.remove(listener);
    }
  }

  /**
   * Fire all current {@link IConsoleConfigEventListener}.
   */
  public void fireConfigChanged() {

    if (consoleConfigListeners != null) {
      consoleConfigListeners.fireConfigChanged();
    }
  }

}
