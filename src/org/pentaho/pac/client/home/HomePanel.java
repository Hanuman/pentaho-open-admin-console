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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that contains HTML, and which can attach child widgets to identified
 * elements within that HTML.
 */
public class HomePanel extends HorizontalPanel {

  private static int sUid;
  private String htmlContent = null;
  /**
   * A helper method for creating unique IDs for elements within dynamically-
   * generated HTML. This is important because no two elements in a document
   * should have the same id.
   * 
   * @return a new unique identifier
   */
  public static String createUniqueId() {
    return "HomePanel_" + (++sUid);
  }

  /**
   * Creates an Home panel with the specified HTML contents. Any element within
   * this HTML that has a specified id can contain a child widget.
   * 
   * @param html the panel's HTML
   */
  public HomePanel(String url) {
	 PacServiceAsync pacService = PacServiceFactory.getPacService();
	 pacService.getHomePage(url, new AsyncCallback() {
		 public void onFailure(Throwable caught) {
		 }
		 public void onSuccess(Object result) {
  		    htmlContent = (String) result;
		    setElement(DOM.createDiv());
		    DOM.setInnerHTML(getElement(), htmlContent);
		 }
	 });

  }

  /**
   * Adds a child widget to the panel, contained within the HTML element
   * specified by a given id.
   * 
   * @param widget the widget to be added
   * @param id the id of the element within which it will be contained
   */
  public void add(Widget widget, String id) {
    Element elem = getElementById(getElement(), id);
    if (elem == null) {
      throw new NoSuchElementException(id);
    }

    super.add(widget, elem);
  }

  /*
   * Implements getElementById() downward from the given element. We need to do
   * this because {@link #add(Widget, String)} must often be called before the
   * panel is attached to the DOM, so {@link Dom#getElementById} won't yet work.
   */
  private Element getElementById(Element elem, String id) {
    String elemId = DOM.getElementProperty(elem, "id");
    if ((elemId != null) && elemId.equals(id)) {
      return elem;
    }

    Element child = DOM.getFirstChild(elem);
    while (child != null) {
      Element ret = getElementById(child, id);
      if (ret != null) {
        return ret;
      }
      child = DOM.getNextSibling(child);
    }

    return null;
  }
}
