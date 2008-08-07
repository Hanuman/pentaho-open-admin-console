package org.pentaho.pac.ui.gwt.table.model;

import java.util.List;
import java.util.Map;

import org.pentaho.pac.ui.gwt.table.NameValue;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TableDataServiceAsync {
	public void getData(String[] colNames,NameValue[] discriminators, AsyncCallback<Map<String, String[]>> callback);

	/**
	 * Returns the number of rows in the model. A <code>JTable</code> uses this
	 * method to determine how many rows it should display. This method should
	 * be quick, as it is called frequently during rendering.
	 * 
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	public void getRowCount(AsyncCallback<Integer> callback);
	
	public void getData(NameValue[] discriminators,AsyncCallback<List<String[]>> callback);

}
