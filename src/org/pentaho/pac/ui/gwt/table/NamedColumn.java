package org.pentaho.pac.ui.gwt.table;

import java.util.List;
import java.util.Map;

import org.pentaho.pac.ui.gwt.table.model.ColumnModel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NamedColumn implements ColumnModel {

	private String name;

	private TableStyles styles;

	public NamedColumn(String name) {
		this.name = name;
	}

	public void setTableStyles(TableStyles styles) {
		this.styles = styles;
	}

	public Widget getHeader() {
		Label label = new Label(name);
		if (styles != null) {
			List<String> styleNames = styles
					.getStyles(TableStyles.Type.TABLE_HEADER);
			if (styleNames != null && styleNames.size() > 0)
				label.addStyleName(styleNames.get(0));
		}
		return label;
	}

	public Widget getCell(int row, Map<String, String[]> data) {

		String[] colData = data.get(name);
		if (row >= colData.length)
			return null;

		String rowData = colData[row];
		CellLabel widget = new CellLabel(rowData);
		
		if (styles != null) {
			List<String> styleNames = styles
					.getStyles(row % 2 == 0 ? TableStyles.Type.EVEN_ROW
							: TableStyles.Type.ODD_ROW);
			if (styleNames != null && styleNames.size() > 0)
				widget.addStyleName(styleNames.get(0));
		}
		
		
		
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
		          "<a onmouseover=\"status='';return true;\" href=\"#\">" + name + "</a>"
		        );

		        
		    }
		}
}
