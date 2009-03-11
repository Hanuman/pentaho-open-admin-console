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
	
	
	
	int getPixelHeaderHeight();
	
	int getPixelRowHeight();

	public void hidden(int i);

	public void visible(int i);
	
	/**
	 * This method CANNOT return null. It all goes to hell if it does.
	 * @return
	 */
	TableStyles getStyles();
	
	int getActualRowCount();
	void setActualRowCount(int i);
	
	int getVisibleRows();

}
