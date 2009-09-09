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
package org.pentaho.pac.client;

import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommonTasksPanel extends SimplePanel {

  VerticalPanel hyperlinksPanel = new VerticalPanel();
  
  public CommonTasksPanel() {
    VerticalPanel vertPanel = new VerticalPanel();
    
    SimplePanel headerPanel = new SimplePanel();
    headerPanel.setStyleName("CommonTasksHeader"); //$NON-NLS-1$
    
    Label header = new Label(Messages.getString("commonTasks")); //$NON-NLS-1$
    header.setStyleName("commonTasksHeaderText"); //$NON-NLS-1$
    headerPanel.add(header);
    vertPanel.add(headerPanel);
    
    hyperlinksPanel.setStyleName("CommonTasksLinks"); //$NON-NLS-1$
    vertPanel.add(hyperlinksPanel);
    
    setStyleName("CommonTasks"); //$NON-NLS-1$
    add(vertPanel);
  }
  
  public void addQuickLink(Hyperlink hyperlink) {
    hyperlinksPanel.add(hyperlink);
  }
}
