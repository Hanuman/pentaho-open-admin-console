package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BasicDialog extends DialogBox {

  private HorizontalPanel btnPanel = null;
  private VerticalPanel clientPanel = null;

  public BasicDialog() {
    this( "" );
  }

  public BasicDialog( String title ) {
    super();

    setTitle( title );
    VerticalPanel rootPanel = new VerticalPanel();
    rootPanel.setSpacing( 0 );
    rootPanel.setStylePrimaryName("basicDialog.rootPanel"); //$NON-NLS-1$
    
    clientPanel = new VerticalPanel();
    clientPanel.setSpacing( 0 );
    clientPanel.setStylePrimaryName("basicDialog.clientPanel"); //$NON-NLS-1$
    
//    clientPanel.setCellWidth(msgLabel, "100%"); //$NON-NLS-1$
//    clientPanel.setCellHeight(msgLabel, "100%"); //$NON-NLS-1$
    clientPanel.setSpacing(10);
    clientPanel.setWidth("250px"); //$NON-NLS-1$
    clientPanel.setHeight("150px"); //$NON-NLS-1$
    rootPanel.add( clientPanel );
    
    btnPanel = new HorizontalPanel();
    btnPanel.setSpacing( 0 );
    btnPanel.setStylePrimaryName("basicDialog.buttonPanel"); //$NON-NLS-1$
    rootPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
    rootPanel.add(btnPanel);
    
    
    setWidget(rootPanel);
  }
  
  protected void addButton( Button btn )
  {
    btn.addStyleName( "basicDialog.button" );
    this.btnPanel.add( btn );
  }
  
  protected void addWidgetToClientArea( Widget widget )
  {
    this.clientPanel.add( widget );
  }
  
  public void setTitle( String title ) {
    setText( title );
  }
}