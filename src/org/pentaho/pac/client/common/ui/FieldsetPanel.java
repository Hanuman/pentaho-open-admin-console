/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
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

  