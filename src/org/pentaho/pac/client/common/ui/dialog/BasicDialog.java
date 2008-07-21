package org.pentaho.pac.client.common.ui.dialog;

import org.pentaho.pac.client.common.ui.ICallback;
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

public class BasicDialog extends DialogBox {

  private HorizontalPanel btnPanel = null;
  private VerticalPanel clientPanel = null;
  
  private static int idCounter = 0;
  private String titleBarSpanId = null;
  private String title = ""; //$NON-NLS-1$
  private RootPanel titleBarSpan = null;
  private Label titleBarLabel = new Label();

  public BasicDialog() {
    this( "" ); //$NON-NLS-1$
  }

  public BasicDialog( String title ) {
    super();

    VerticalPanel rootPanel = new VerticalPanel();
    rootPanel.setSpacing( 0 );
    rootPanel.setStyleName("basicDialog.rootPanel"); //$NON-NLS-1$
    
    clientPanel = new VerticalPanel();
    setClientSize( "250px", "140px" ); //$NON-NLS-1$ //$NON-NLS-2$
    clientPanel.setStyleName("basicDialog.clientPanel"); //$NON-NLS-1$
    rootPanel.add( clientPanel );
    
    btnPanel = new HorizontalPanel();
    btnPanel.setSpacing( 0 );
    btnPanel.setStyleName("basicDialog.buttonPanel"); //$NON-NLS-1$
    rootPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
    rootPanel.add(btnPanel);
    
    setTitle( title );
    initTitleBar();
    
    setWidget(rootPanel);
  }
  
  protected void setNoBorderOnClientPanel() {
    clientPanel.setStyleName("basicDialog.clientPanelNoBorder"); //$NON-NLS-1$
  }
  
  protected void setClientSize( String width, String height ) {
    clientPanel.setWidth( width );
    clientPanel.setHeight( height );
  }
  
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
  
  public void show() {
    super.show();

    if ( null == titleBarSpan ) {
      titleBarSpan = RootPanel.get(titleBarSpanId);
      final BasicDialog localThis = this;
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
      HorizontalPanel p = new HorizontalPanel();
      p.setWidth( "99%" ); //$NON-NLS-1$
      p.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_LEFT );
      p.add( titleBarLabel );
      p.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
      p.add( img );
      titleBarSpan.add( p );
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
  
  public void setText(String text) {
    setTitle( text );
  }
}