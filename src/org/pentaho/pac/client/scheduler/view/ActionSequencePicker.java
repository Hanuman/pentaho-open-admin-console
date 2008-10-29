package org.pentaho.pac.client.scheduler.view;

import org.pentaho.gwt.widgets.client.filechooser.FileChooser;

public class ActionSequencePicker extends FileChooser {
  public ActionSequencePicker() {
    super( FileChooserMode.OPEN_READ_ONLY, "/", true ); //$NON-NLS-1$
  }
}
