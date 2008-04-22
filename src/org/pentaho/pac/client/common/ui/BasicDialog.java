package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BasicDialog extends DialogBox {

  private HorizontalPanel btnPanel = null;
  private VerticalPanel clientPanel = null;

  public BasicDialog() {
    this( "" ); //$NON-NLS-1$
  }

  public BasicDialog( String title ) {
    super();

    setTitle( title );
    VerticalPanel rootPanel = new VerticalPanel();
    rootPanel.setSpacing( 0 );
    rootPanel.setStyleName("basicDialog.rootPanel"); //$NON-NLS-1$
    
    clientPanel = new VerticalPanel();
    clientPanel.setSpacing( 10 );
    setClientSize( "250px", "140px" ); //$NON-NLS-1$ //$NON-NLS-2$
    clientPanel.setStyleName("basicDialog.clientPanel"); //$NON-NLS-1$

    rootPanel.add( clientPanel );
    
    btnPanel = new HorizontalPanel();
    btnPanel.setSpacing( 0 );
    btnPanel.setStyleName("basicDialog.buttonPanel"); //$NON-NLS-1$
    rootPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
    rootPanel.add(btnPanel);
    
    setWidget(rootPanel);
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
    setText( title );
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
}