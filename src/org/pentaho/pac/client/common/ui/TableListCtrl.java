package org.pentaho.pac.client.common.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Steven Barkdull
 *
 * @param <RowDataType> type of the object that can be stored as data with each row in the list.
 */
public class TableListCtrl<RowDataType> extends ScrollPanel {

  private FlexTable table = null;
  private List<RowDataType> dataList = new LinkedList<RowDataType>();
  private static final int HEADER_ROW = 0;
  private static final int FIRST_ROW = HEADER_ROW+1;
  private static final int SELECT_COLUMN = 0;
  private static final int FIRST_COLUMN = SELECT_COLUMN+1;
  private static final String BLANK = "&nbsp;"; //$NON-NLS-1$
  private static final String DEFAULT_HEIGHT = "230px"; //$NON-NLS-1$
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  public TableListCtrl( String[] columnHeaderNames )
  {
    this( textToLabel( columnHeaderNames ) );
  }
  
  public TableListCtrl( Widget[] columnHeaderWidgets )
  {
    super();

    setHeight( DEFAULT_HEIGHT );
    table = createTable( columnHeaderWidgets );
    add( table );
  }

  private static Widget[] textToLabel(  String[] columnHeaderNames  ) {
    int len = columnHeaderNames.length;
    Widget[] widgets = new Widget[ columnHeaderNames.length ];
    for ( int ii = 0; ii<len; ++ii ) {
      widgets[ ii ] = new Label( columnHeaderNames[ii] );
    }
    return widgets;
  }
  
  public void setTableStyleName( String styleName ) {
    table.setStyleName( styleName );
  }
  
  public void setTableHeaderStyleName( String styleName ) {
    table.getRowFormatter().setStyleName( 0, styleName );
  }
  
  private FlexTable createTable( Widget[] columnHeaderWidgets ) {
    
    FlexTable tmpTable = new FlexTable();
   
    tmpTable.setCellPadding( 0 );
    tmpTable.setCellSpacing( 0 );
    addTableHeader( tmpTable, columnHeaderWidgets );
    
    return tmpTable;
  }
  
  private void addTableHeader( FlexTable tmpTable, Widget[] columnHeaderWidgets )
  {
    Widget l = getEmptyWidget();
    l.setTitle( "Check to select row" );
    tmpTable.setWidget( HEADER_ROW, SELECT_COLUMN, l );
    for ( int ii=0; ii<columnHeaderWidgets.length; ++ii ) {
      tmpTable.setWidget( HEADER_ROW, ii+FIRST_COLUMN, columnHeaderWidgets[ii] );
    }
  }
  
  private Widget getEmptyWidget() {
    Label l = new Label();
    l.getElement().setInnerHTML( "<span>&nbsp;</span>" ); //$NON-NLS-1$
    return l;
  }
  
  public List<Integer> getSelectedIndexes()
  {
    List<Integer> idxs = new ArrayList<Integer>();

    for ( int rowNum=FIRST_ROW; rowNum<table.getRowCount(); ++rowNum ) {
      CheckBox cb = (CheckBox)table.getWidget( rowNum, SELECT_COLUMN );
      if ( cb.isChecked() ) {
        idxs.add( new Integer( rowNum-FIRST_ROW ) );
      }
    }
    
    return idxs;
  }
  
  /**
   * Removes all non-header items from the list
   */
  public void removeAll() {
    // don't delete row 0, it's the header
    for ( int rowNum=table.getRowCount()-1; rowNum>=FIRST_ROW; --rowNum ) {
      table.removeRow( rowNum );
    }
    dataList.clear();
  }
  
  public void remove( int rowNum ) {
    table.removeRow( rowNum+FIRST_ROW );
    dataList.remove( rowNum );
  }
  
  public void selectAll() {
    for ( int rowNum=FIRST_ROW; rowNum<table.getRowCount(); ++rowNum ) {
      CheckBox cb = getSelectCheckBox( rowNum );
      cb.setChecked( true );
    }
  }
  
  public void unselectAll() {
    for ( int rowNum=FIRST_ROW; rowNum<table.getRowCount(); ++rowNum ) {
      CheckBox cb = getSelectCheckBox( rowNum );
      cb.setChecked( false );
    }
  }
  
  public void select( int rowNum ) {
    CheckBox cb = getSelectCheckBox( rowNum+FIRST_ROW );
    cb.setChecked( true );
  }
  
  public void unselect( int rowNum ) {
    CheckBox cb = getSelectCheckBox( rowNum+FIRST_ROW );
    cb.setChecked( false );
  }

  private CheckBox getSelectCheckBox( int actualRowNum ) {
    return (CheckBox)table.getWidget( actualRowNum, SELECT_COLUMN );
  }
  
  public void addRow( Widget[] widgets ) {
    addRow( widgets, null );
  }
  
  /**
   * NOTE: it is ok for elements of the widgets array to be null.
   * 
   * @param widgets
   * @param data
   */
  public void addRow( Widget[] widgets, RowDataType data ) {
    int newRowNum = table.getRowCount();
    table.setWidget( newRowNum, 0, new CheckBox() );
    for ( int ii=0; ii<widgets.length; ++ii ) {
      Widget w = widgets[ii];
      if ( null == w ) {
        w = getEmptyWidget();
      }
      setCellWidget( newRowNum-FIRST_ROW, ii+1, w );
    }
    dataList.add( data );
    assert dataList.size() == (table.getRowCount()-FIRST_ROW) : "Number of items in data list does not equal the number of items in the list.";
  }

  public void setCellWidget( int rowNum, int colNum, Widget w ) {
    table.setWidget( rowNum+FIRST_ROW, colNum+SELECT_COLUMN, w );
  }

  public Widget getCellWidget( int rowNum, int colNum ) {
    return table.getWidget( rowNum+FIRST_ROW, colNum+SELECT_COLUMN );
  }

  public void setRowData( int rowNum, RowDataType data ) {
    dataList.remove( rowNum );
    dataList.add( rowNum, data );
  }

  public RowDataType getRowData( int rowNum ) {
    return dataList.get( rowNum );
  }
  
  public int getNumRows() {
    return table.getRowCount() - FIRST_ROW;
  }
  
  public int getNumColumns() {
    return table.getCellCount(HEADER_ROW);
  }
  
  public void clearTempMessage() {
    table.removeRow( FIRST_ROW );
  }
  
  public void setTempMessage( String msg ) {
    removeAll();
    Label l = new Label();
    l.setText( msg );

    table.setWidget( FIRST_ROW, 0, l );
    table.getFlexCellFormatter().setColSpan( FIRST_ROW, 0,  getNumColumns() );
  }
}
