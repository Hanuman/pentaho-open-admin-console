package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @deprecated used SimpleGroupBox
 * @author Nick Baker
 *
 */
public class FieldsetPanel extends SimplePanel{
  private Element fieldset;
  private Element legend;
  public FieldsetPanel(){
    setStyleName("fieldsetPanel"); //$NON-NLS-1$
    //fieldset = DOM.createFieldSet();
    //legend = DOM.createLegend();
    //DOM.appendChild(fieldset,legend);
    //setElement(fieldset);
  }
  
  public void setLegend(String txt){
    //DOM.setInnerText(legend, txt);
  }
  public String getLegend(){
    return DOM.getInnerText(legend);
  }
}

  