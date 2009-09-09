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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminConsoleToolbar extends HorizontalPanel {
  ToolbarIndicator toolbarIndicator;
  private Timer statusTimer = null;
  HorizontalPanel buttonsPanel = new HorizontalPanel();
  HorizontalPanel indicatorsRight = new HorizontalPanel();
  public void addImageButton(Image image) {
    buttonsPanel.setStyleName("buttons"); //$NON-NLS-1$
    buttonsPanel.add(image);
  }
  public AdminConsoleToolbar(final IRefreshableAdminConsole console){
    this(console, null);
  }
  public AdminConsoleToolbar(final IRefreshableAdminConsole console, final String helpUrlOverride){
    super();

    setStyleName("adminconsole-toolbar"); //$NON-NLS-1$
    
    //Left end-cap
    SimplePanel leftCap = new SimplePanel();
    leftCap.setStyleName("adminconsole-toolbar_left"); //$NON-NLS-1$
    add(leftCap);
    this.setCellWidth(leftCap, "5px"); //$NON-NLS-1$
    
    //the body of the toolbar
    HorizontalPanel centerPanel = new HorizontalPanel();
    centerPanel.setStyleName("adminconsole-toolbar_center"); //$NON-NLS-1$
    add(centerPanel);
    
    //Right end-cap
    SimplePanel rightCap = new SimplePanel();
    rightCap.setStyleName("adminconsole-toolbar_right"); //$NON-NLS-1$
    add(rightCap);
    this.setCellWidth(rightCap, "6px"); //$NON-NLS-1$
    
    SimplePanel indicatorsPanel = new SimplePanel();
    indicatorsPanel.setStyleName("toolBarIndicators"); //$NON-NLS-1$
    centerPanel.add(indicatorsPanel);
    
    SimplePanel indicatorsLeft = new SimplePanel();
    indicatorsLeft.setStyleName("indicators_left"); //$NON-NLS-1$
    indicatorsPanel.add(indicatorsLeft);
    
    indicatorsRight.setStyleName("indicators_right"); //$NON-NLS-1$
    indicatorsLeft.add(indicatorsRight);
    contructToolbarIndicator();
    setIndicators(toolbarIndicator);

    Image refreshConsoleImage = PacImageBundle.getBundle().refreshIcon().createImage();
    refreshConsoleImage.setTitle(Messages.getString("resetServer")); //$NON-NLS-1$
    refreshConsoleImage.addClickListener(new ClickListener(){
      public void onClick(Widget sender) {
        console.refresh();
      }
    });

    addImageButton(refreshConsoleImage);
    
    Image helpImage = PacImageBundle.getBundle().helpIcon().createImage();
    helpImage.setTitle(Messages.getString("help")); //$NON-NLS-1$
    helpImage.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        if (helpUrlOverride != null && helpUrlOverride.length() > 0){
          Window.open(helpUrlOverride, Messages.getString("userGuide"), ""); //$NON-NLS-1$ //$NON-NLS-2$
        }else{
          PacServiceFactory.getPacService().getHelpUrl(new AsyncCallback<String>(){

            public void onFailure(Throwable arg0) {
              //TODO show error message
            }

            public void onSuccess(String helpUrl) {
              Window.open(helpUrl, Messages.getString("userGuide"), ""); //$NON-NLS-1$ //$NON-NLS-2$ 
            }          
          });
        }
      }
    });

    addImageButton(helpImage);
    
    centerPanel.add(buttonsPanel);
    centerPanel.setCellHorizontalAlignment(buttonsPanel, HorizontalPanel.ALIGN_RIGHT);
    centerPanel.setCellVerticalAlignment(buttonsPanel, HorizontalPanel.ALIGN_MIDDLE);
    
    statusTimer = new Timer() {
      public void run()
      {
        PacServiceFactory.getPacService().isBiServerAlive(
            new AsyncCallback<Object>() {
              public void onSuccess( Object isAlive ) {
                toolbarIndicator.displayServerAlive();
              }
              public void onFailure(Throwable caught) {
                toolbarIndicator.displayServerDead();
              }
            }
          );
      }
    };
    
    PacServiceFactory.getPacService().getBiServerStatusCheckPeriod(
      new AsyncCallback<Integer>() {
        public void onSuccess( Integer checkPeriod ) {
          if ( checkPeriod > 0 ) {
            statusTimer.scheduleRepeating( checkPeriod );
          }
        }
        public void onFailure(Throwable caught) {
          // otherwise we don't know what the status check period is, so don't schedule anything
        }
      }
    );
  }
  
  public HorizontalPanel getButtonPanel() {
    return buttonsPanel;
  }
  public void setToolbarIndicator(ToolbarIndicator toolbarIndicator) {
    this.toolbarIndicator = toolbarIndicator;
  }
  public ToolbarIndicator getToolbarIndicator() {
    return this.toolbarIndicator;
  }
  public void contructToolbarIndicator() {
   ToolbarIndicator toolbarIndicator = new ToolbarIndicator();
   toolbarIndicator.buildToolbarIndicator();
   setToolbarIndicator(toolbarIndicator);
  }
  public void setIndicators(ToolbarIndicator toolbarIndicator) {
    indicatorsRight.add(toolbarIndicator);    
  }
  
}
