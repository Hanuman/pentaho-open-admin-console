package org.pentaho.pac.client.scheduler.view;

import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TableEditor extends VerticalPanel {

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  private Button deleteBtn = new Button( "-" );
  private Button addBtn = new Button( "+" );
  private ListBoxEx actionLb = new ListBoxEx();
  private ErrorLabel errorLabel = null;
  private static int DEFAULT_NUM_VISIBLE_ITEMS = 10;
  private ICallback<TableEditor> onAddHandler = null;
  private ICallback<TableEditor> onDeleteHandler = null;
  
  public TableEditor( String labelText ) {

    DockPanel buttonPanel = new DockPanel();
    buttonPanel.add(deleteBtn, DockPanel.EAST);
    buttonPanel.add(addBtn, DockPanel.EAST);
    
    errorLabel = new ErrorLabel( new Label( labelText ) );
    buttonPanel.add(errorLabel, DockPanel.WEST);
    buttonPanel.setCellWidth(errorLabel, "100%"); //$NON-NLS-1$
    
    add( buttonPanel );
    
    actionLb.setWidth( "100%" ); //$NON-NLS-1$  // TODO sbarkdull, move to css
    actionLb.setVisibleItemCount( DEFAULT_NUM_VISIBLE_ITEMS );
    actionLb.setMultipleSelect( true );
    add( actionLb );
    
    final TableEditor localThis = this;
    
    addBtn.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        localThis.handleAdd();
      }
    });
    
    deleteBtn.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        localThis.handleDeleteSelectedItems();
      }
    });
  }
  
  private void handleAdd() {
    if ( null != onAddHandler ) {
      onAddHandler.onHandle( this );
    }
  }
  
  private void handleDeleteSelectedItems() {
    removeSelectedItems();
    if ( null != onDeleteHandler ) {
      onDeleteHandler.onHandle( this );
    }
  }
  
  public void removeSelectedItems() {
    for ( int ii=getItemCount()-1; ii>=0; --ii ) {
      if ( actionLb.isItemSelected( ii ) ) {
        actionLb.removeItem( ii );
      }
    }
  }
  
  public void setVisibleItemCount( int numVisibleItems) {
    actionLb.setVisibleItemCount( numVisibleItems );
  }
  
  public int getItemCount() {
    return actionLb.getItemCount();
  }
  
  public void removeAll() {
    actionLb.removeAll();
  }
  
  public void addItem( String item, String value ) {
    actionLb.addItem( item );
    actionLb.setValue( actionLb.getItemCount()-1, value );
  }
  
  public String getItemText( int idx ) {
    return actionLb.getItemText( idx );
  }
  
  public String getItemValue( int idx ) {
    return actionLb.getValue( idx );
  }
  
//  public void setValue( int idx, String value ) {
//    actionLb.setValue( idx, value );
//  }
  
  public void setOnAddClickedHandler( ICallback<TableEditor> handler ) {
    onAddHandler = handler;
  }
  
  public void setOnDeleteClickedHandler( ICallback<TableEditor> handler ) {
    onDeleteHandler = handler;
  }

  public void setErrorMsg( String errorMsg ) {
    errorLabel.setErrorMsg( errorMsg );
  }
}


//FixedWidthFlexTable
// http://code.google.com/p/google-web-toolkit-incubator/wiki/ScrollTable
// http://code.google.com/p/google-web-toolkit-incubator/wiki/ScrollTable