package org.pentaho.pac.ui.gwt.table.model;

import org.pentaho.pac.ui.gwt.table.TableStyles;



public interface TableModel {

	/**
	 * Returns the number of columns in the model. A <code>JTable</code> uses
	 * this method to determine how many columns it should create and display by
	 * default.
	 * 
	 * @return the number of columns in the model
	 * @see #getRowCount
	 */
	public int getColumnCount();
	
	public ColumnModel[] getColumnModel();
	
	int getTotalWidth();
	
	int visibleRows();
	
	int getPixelHeaderHeight();
	
	int getPixelRowHeight();

	public void hidden(int i);

	public void visible(int i);
	
	/**
	 * This method CANNOT return null. It all goes to hell if it does.
	 * @return
	 */
	TableStyles getStyles();

}
