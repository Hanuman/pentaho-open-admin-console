package org.pentaho.pac.ui.gwt.table.model;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TableDataService extends RemoteService {
	public Map<String, String[]> getData(String[] colNames);

	/**
	 * Returns the number of rows in the model. A <code>JTable</code> uses this
	 * method to determine how many rows it should display. This method should
	 * be quick, as it is called frequently during rendering.
	 * 
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	public int getRowCount();

}
