package org.pentaho.pac.ui.gwt.table.model;

import java.util.Map;

import org.pentaho.pac.ui.gwt.table.TableStyles;

import com.google.gwt.user.client.ui.Widget;

public interface ColumnModel {
	public abstract Widget getHeader();
	public abstract Widget getCell(int row,Map<String,String[]> data);
	int getWidth();
	public abstract void setTableStyles(TableStyles styles);
	public String getName();
}
