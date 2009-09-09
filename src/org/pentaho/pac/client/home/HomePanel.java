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
/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pentaho.pac.client.home;

import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.pac.client.IRefreshableAdminPage;
import org.pentaho.pac.client.PacServiceAsync;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.GroupBox;
import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.utils.ExceptionParser;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * A panel that contains HTML, and which can attach child widgets to identified
 * elements within that HTML.
 */
public class HomePanel extends SimplePanel implements IRefreshableAdminPage{

  private static int sUid;
  private GroupBox groupbox;
  private String url;
  
  /**
   * A helper method for creating unique IDs for elements within dynamically-
   * generated HTML. This is important because no two elements in a document
   * should have the same id.
   * 
   * @return a new unique identifier
   */
  public static String createUniqueId() {
    return "HomePanel_" + (++sUid); //$NON-NLS-1$
  }

  /**
   * Creates an Home panel with the specified HTML contents. Any element within
   * this HTML that has a specified id can contain a child widget.
   * 
   * @param html the panel's HTML
   */
  public HomePanel(String url) {
    groupbox = new GroupBox("Welcome"); //$NON-NLS-1$
    groupbox.setStyleName("homeGroupBox"); //$NON-NLS-1$
    this.add(groupbox);
    this.url = url;
  }
  
  public void refresh() {
    PacServiceAsync pacService = PacServiceFactory.getPacService();
    pacService.getHomePageAsHtml(url, new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        MessageDialogBox messageDialog = new MessageDialogBox(ExceptionParser.getErrorHeader(caught.getMessage()), ExceptionParser.getErrorMessage(caught.getMessage(), Messages.getString("errorGettingHomepage")), false, false, true);           //$NON-NLS-1$
        messageDialog.center();
      }
      public void onSuccess(String htmlContent) {
       SimplePanel tempPanel = new SimplePanel();
       
       DOM.setInnerHTML(tempPanel.getElement(), htmlContent);
       groupbox.setContent(tempPanel);
      }
    });    
  }

}
