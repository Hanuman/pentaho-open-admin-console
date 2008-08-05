package org.pentaho.pac.ui.gwt.table;

import java.util.List;

import org.pentaho.pac.ui.gwt.table.model.ColumnModel;
import org.pentaho.pac.ui.gwt.table.model.TableModel;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class ColumnSupport {

	private int colCount;

	private int aggregateWidth;
	private int[] columnAbsPos;

	private ColumnModel[] columns;

	private TableModel model;

	public ColumnSupport(TableModel model) {
		this.model = model;
		int colCount = model.getColumnCount();
		this.colCount = colCount;
		columnAbsPos = new int[colCount];
		columns = new ColumnModel[colCount];

		ColumnModel[] mcolumns = model.getColumnModel();

		for (int i = 0; i < mcolumns.length; i++) {
			columns[i] = mcolumns[i];
			columnAbsPos[i] = aggregateWidth;
			aggregateWidth += columns[i].getWidth();
		}
	}

	public int getWidth() {
		return aggregateWidth;
	}

	public int size() {
		return colCount;
	}

	public void addHeaders(AbsolutePanel where) {
		for (int i = 0; i < columns.length; i++) {

			Widget widget = columns[i].getHeader();

			where.add(widget, columnAbsPos[i], 0);
			if (model.getStyles() != null) {
				List<String> styleNames = model.getStyles().getStyleNames(
						TableStyles.Type.TABLE_HEADER_PANEL);
				if (styleNames != null && styleNames.size() > 0)
					where.addStyleName(styleNames.get(0));

				List<String> lblstyleNames = model.getStyles().getStyleNames(
						TableStyles.Type.TABLE_HEADER_TEXT);
				if (lblstyleNames != null && lblstyleNames.size() > 0)
					widget.addStyleName(lblstyleNames.get(0));

			}
			widget.setPixelSize(columns[i].getWidth(), model
					.getPixelHeaderHeight());
		}
	}

	public ColumnModel[] getColumns() {
		return columns;
	}

	public int[] getColPos() {
		return columnAbsPos;
	}

	public String[] getColumnNames() {
		String[] names = new String[columns.length];
		for (int i = 0; i < columns.length; i++)
			names[i] = columns[i].getName();

		return names;
	}
}
