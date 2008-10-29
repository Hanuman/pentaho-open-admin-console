package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class ExternalHyperlink extends Widget{
  
  public ExternalHyperlink(String label, String link){
    this(label, link, false);
  }
  
  public ExternalHyperlink(String label, String link, boolean isOpenInNewWindow){
    super();
     
    Element a = DOM.createAnchor();
    
    a.setAttribute("href", link); //$NON-NLS-1$
    
    if (isOpenInNewWindow){
      a.setAttribute("target", "_blank"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    a.setInnerHTML(label);
    
    this.setElement(a);
  }
  
}
   