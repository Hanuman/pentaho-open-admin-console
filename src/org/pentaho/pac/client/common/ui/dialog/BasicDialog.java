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
package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

public class BasicDialog extends DialogBox {

  private HorizontalPanel btnPanel = null;
  private HorizontalPanel btnOuterPanel = null;
  private VerticalPanel clientPanel = null;
  
  private static int idCounter = 0;
  private String titleBarSpanId = null;
  private String title = ""; //$NON-NLS-1$
  private RootPanel titleBarSpan = null;
  private Label titleBarLabel = new Label();
  
  private boolean showCloseButton = false;

  public BasicDialog() {
    this( "" ); //$NON-NLS-1$
  }

  public BasicDialog(String title){
    this(title, true);
  }
  
  public BasicDialog( String title, boolean showCloseButton ) {
    super();
    
    this.showCloseButton = showCloseButton;

    VerticalPanel rootPanel = new VerticalPanel();
    rootPanel.setSpacing( 0 );
    rootPanel.setStyleName("basicDialog.rootPanel"); //$NON-NLS-1$
    
    clientPanel = new VerticalPanel();
    setClientSize( "100%", "100%" ); //$NON-NLS-1$ //$NON-NLS-2$
    clientPanel.setStyleName("basicDialog.clientPanel"); //$NON-NLS-1$
    rootPanel.add( clientPanel );
    
    btnOuterPanel = new HorizontalPanel();
    btnOuterPanel.setSpacing( 0 );
    btnOuterPanel.setWidth("100%"); //$NON-NLS-1$
    btnOuterPanel.setStyleName("basicDialog.buttonPanel"); //$NON-NLS-1$
    
    btnPanel = new HorizontalPanel();
    btnPanel.setSpacing( 0 );
    btnPanel.setStyleName("basicDialog.buttonInnerPanel"); //$NON-NLS-1$
    
    btnOuterPanel.add(btnPanel);
    
    rootPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
    rootPanel.add(btnOuterPanel);
    
    setTitle( title );
    initTitleBar();
    
    setWidget(rootPanel);
  }
  
  /**
   * Removes the interior border around the dialog's client area.
   */
  protected void setNoBorderOnClientPanel() {
    clientPanel.setStyleName("basicDialog.clientPanelNoBorder"); //$NON-NLS-1$
  }
  
  protected void setClientSize( String width, String height ) {
    clientPanel.setWidth( width );
    clientPanel.setHeight( height );
  }
  
  protected void setButtonPanelAlign(HorizontalAlignmentConstant hAlign, VerticalAlignmentConstant vAlign){
    if(hAlign != null)
      btnOuterPanel.setCellHorizontalAlignment(btnPanel, hAlign);
    
    if(vAlign != null)
      btnOuterPanel.setCellVerticalAlignment(btnPanel, vAlign);
  }
  
  /**
   * Add a new button to the button panel. The new button will be the
   * right most button in the panel.
   * 
   * @param btn
   */
  protected void addButton( Button btn )
  {
    btn.addStyleName( "basicDialog.button" ); //$NON-NLS-1$
    this.btnPanel.add( btn );
  }
  
  protected void addWidgetToClientArea( Widget widget )
  {
    this.clientPanel.add( widget );
  }
  
  protected void removeRowFromClientArea( int row )
  {
    this.clientPanel.remove( row );
  }
  
  protected void insertRowIntoClientArea( Widget widget, int beforeRow )
  {
    this.clientPanel.insert( widget, beforeRow );
  }
  
  /**
   * Sets the title in the dialog's title bar
   */
  public void setTitle( String title ) {
    this.title = title;
  }
  
  private void initTitleBar() {
    titleBarLabel.setStyleName( "titleBarLabel" ); //$NON-NLS-1$
    // add a span tag to the title bar to store the title and X icon later
    titleBarSpanId = "basicDialogTitle" + Integer.toString( idCounter ); //$NON-NLS-1$
    idCounter++;
    setHTML("<span class='basicDialog.titleBarContent' id='" + titleBarSpanId + "'/>");  //$NON-NLS-1$//$NON-NLS-2$
  }

  private ICallback<BasicDialog> closeHandler = null;
  public void setOnCloseHandler( final ICallback<BasicDialog> handler )
  {
    closeHandler = handler;
  }
  
  /**
   * Displays the dialog on the screen.
   */
  public void show() {
    super.show();

    if ( null == titleBarSpan ) {
      try{
        titleBarSpan = RootPanel.get(titleBarSpanId);
      } catch (Throwable ex){
        
      }
      final BasicDialog localThis = this;
      
      HorizontalPanel p = new HorizontalPanel();
      p.setWidth( "99%" ); //$NON-NLS-1$
      p.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_LEFT );
      p.add( titleBarLabel );
      p.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
      
      if(showCloseButton){
        Image img = PacImageBundle.getBundle().closeIcon().createImage();
        img.setStyleName( "basicDialog.closeIcon" ); //$NON-NLS-1$
        img.addClickListener( new ClickListener() {
          public void onClick(Widget sender) {
            if ( null != localThis.closeHandler ) {
              localThis.closeHandler.onHandle( localThis );
            } else {
              localThis.hide();
            }
          }
        });
        
        p.add( img );
      }
      if(titleBarSpan != null){
        titleBarSpan.add( p );
      }
    }
    titleBarLabel.setText( title );
  }

  /**
   * see: http://google-web-toolkit.googlecode.com/svn-history/r229/trunk/samples/mail/src/com/google/gwt/sample/mail/client/AboutDialog.java
   */
  public boolean onKeyDownPreview(char key, int modifiers) {
    // Use the popup's key preview hooks to close the dialog when escape is pressed.
    switch (key) {
      case KeyboardListener.KEY_ESCAPE:
        hide();
        return false;
      default:
        return true;
    }
  }
  
  /**
   * Sets the title in the dialog's title bar
   */
  public void setText(String text) {
    setTitle( text );
  }
}