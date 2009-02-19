package org.pentaho.pac.server.common;



/**
 * Event listener interface for click events.
 */
public interface IConsoleConfigEventListener {

  /**
   * Fired when the configuration is validated to be correct.
   * 
   * @param sender the widget sending the event.
   */
  void onConfigChanged();
}