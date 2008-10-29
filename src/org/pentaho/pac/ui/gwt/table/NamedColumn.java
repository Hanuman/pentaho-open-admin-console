package org.pentaho.pac.ui.gwt.table;

import java.util.Map;

import org.pentaho.pac.ui.gwt.table.model.ColumnModel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NamedColumn implements ColumnModel {

	private String name;

	public NamedColumn(String name) {
		this.name = name;
	}

	public Widget getHeader() {
		Label label = new Label(name);
		return label;
	}

	public Widget getCell(int row, Map<String, String[]> data) {

		String[] colData = data.get(name);
		if (row >= colData.length)
			return null;

		String rowData = colData[row];
		CellLabel widget = new CellLabel(rowData);
				
		return widget;

	}

	public int getWidth() {
		return 150;
	}

	public String getName() {
		return name;
	}
	
	class CellLabel extends HTML {
		  public CellLabel(String name) {
		        setHTML(
		          "<a onmouseover=\"status='';return true;\" href=\"#\">" + name + "</a>" //$NON-NLS-1$ //$NON-NLS-2$
		        );

		        
		    }
		}
}
