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
   