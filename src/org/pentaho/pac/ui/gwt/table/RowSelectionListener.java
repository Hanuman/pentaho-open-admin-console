package org.pentaho.pac.ui.gwt.table;

import java.util.EventListener;

import com.google.gwt.user.client.ui.Widget;

public interface RowSelectionListener extends EventListener {

	/**
	 * Fired when the currently selected row in the table changes.
	 * 
	 * @param sender
	 *            the Table widget sending the event
	 * @param row
	 *            the row identifier (primary key) of the row being selected
	 */
	void onRowSelected(Widget sender, String rowId);
}
