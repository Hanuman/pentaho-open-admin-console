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
import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarIndicator  extends HorizontalPanel{
  protected Label statusLabel;
  protected VerticalPanel imageHolderPanel;
  private Image serverAliveImage;
  private Image serverDeadImage;
  
  public ToolbarIndicator() {
    imageHolderPanel = new VerticalPanel();
    serverAliveImage = PacImageBundle.getBundle().serverAliveIcon().createImage();
    serverAliveImage.setTitle(Messages.getString("biServerAlive")); //$NON-NLS-1$
    serverDeadImage = PacImageBundle.getBundle().serverDeadIcon().createImage();
    serverDeadImage.setTitle(Messages.getString("biServerDead")); //$NON-NLS-1$
    statusLabel = new Label(Messages.getString("toolbarStatus")); //$NON-NLS-1$
    statusLabel.setStyleName("indicators_label"); //$NON-NLS-1$
    imageHolderPanel.add(serverDeadImage);
    setStyleName("indicators"); //$NON-NLS-1$
  }

  protected void buildToolbarIndicator() {
    add(statusLabel);
    add(imageHolderPanel);    
  }
  
  public void setStatusLabel(Label statusLabel) {
    this.statusLabel = statusLabel;
  }
  
  public Label getStatusLabel() {
    return statusLabel;
  }
  
  public void displayServerAlive() {
      serverDeadImage.removeFromParent();
      imageHolderPanel.add(serverAliveImage);
  }

  public void displayServerDead() {
    serverAliveImage.removeFromParent();
    imageHolderPanel.add(serverDeadImage);
  }

}
