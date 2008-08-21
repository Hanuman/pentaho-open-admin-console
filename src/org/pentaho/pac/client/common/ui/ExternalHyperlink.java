package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class ExternalHyperlink extends Widget{
  public ExternalHyperlink(String label, String link){
    super();
     
    Element a = DOM.createAnchor();
    a.setAttribute("href", link); //$NON-NLS-1$
    a.setInnerHTML(label);
    this.setElement(a);
  }
}

   