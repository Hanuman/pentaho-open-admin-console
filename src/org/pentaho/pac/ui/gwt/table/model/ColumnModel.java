package org.pentaho.pac.ui.gwt.table.model;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

public interface ColumnModel {
	public abstract Widget getHeader();
	public abstract Widget getCell(int row,Map<String,String[]> data);
	int getWidth();
	public String getName();
}
