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

import java.util.NoSuchElementException;

import org.pentaho.pac.client.PacServiceAsync;
import org.pentaho.pac.client.PacServiceFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.ui.GroupBox;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that contains HTML, and which can attach child widgets to identified
 * elements within that HTML.
 */
public class HomePanel extends SimplePanel {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private static int sUid;
  private String htmlContent = null;
  private GroupBox groupbox;
  
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
    groupbox = new GroupBox("Welcome");
    groupbox.setStylePrimaryName("homeGroupBox");
    this.add(groupbox);
    
	 PacServiceAsync pacService = PacServiceFactory.getPacService();
	 pacService.getHomePage(url, new AsyncCallback() {
		 public void onFailure(Throwable caught) {
		   
       MessageDialog messageDialog = new MessageDialog(
           MSGS.error(),
           MSGS.failedToLoadHome( caught.getMessage() ) );
       messageDialog.center();
		 }
		 public void onSuccess(Object result) {
      htmlContent = (String) result;
      SimplePanel tempPanel = new SimplePanel();
      
      DOM.setInnerHTML(tempPanel.getElement(), htmlContent);
      groupbox.setContent(tempPanel);
		 }
	 });

  }

}
