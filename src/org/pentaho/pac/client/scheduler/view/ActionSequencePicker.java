package org.pentaho.pac.client.scheduler.view;

import org.pentaho.gwt.filechooser.client.FileChooser;

import com.google.gwt.xml.client.Document;

public class ActionSequencePicker extends FileChooser {
  public ActionSequencePicker() {
    super( FileChooserMode.OPEN_READ_ONLY, "/", true ); //$NON-NLS-1$
  }
}