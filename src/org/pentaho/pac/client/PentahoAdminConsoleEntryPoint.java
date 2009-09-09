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

import org.pentaho.gwt.widgets.client.utils.i18n.IResourceBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class PentahoAdminConsoleEntryPoint implements EntryPoint, IResourceBundleLoadCallback{

  public void onModuleLoad() {
 // just some quick sanity setting of the platform effective locale based on the override
    // TODO:
    // which comes from the url parameter
//    if (!StringUtils.isEmpty(Window.Location.getParameter("locale"))) {
//        MantleServiceCache.getService().setLocaleOverride(Window.Location.getParameter("locale"), null);
//    }
    
    ResourceBundle messages = new ResourceBundle();
    Messages.setResourceBundle(messages); 
    messages.loadBundle("messages/", "messages", true, PentahoAdminConsoleEntryPoint.this); //$NON-NLS-1$ //$NON-NLS-2$
  }

  // Make sure the resource bundle is loaded before doing anything else
  public void bundleLoaded(String bundleName) {
    //attach all to the page
    RootPanel.get("canvas").add(new PentahoAdminConsole());  //$NON-NLS-1$
  }
}
