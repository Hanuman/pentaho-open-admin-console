package org.pentaho.pac.ui.gwt.table;

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

	public ColumnSupport(TableModel model, TableStyles styles) {
		this.model = model;
		int colCount = model.getColumnCount();
		this.colCount = colCount;
		columnAbsPos = new int[colCount];
		columns = new ColumnModel[colCount];

		ColumnModel[] mcolumns = model.getColumnModel();

		for (int i = 0; i < mcolumns.length; i++) {
			columns[i] = mcolumns[i];
			columnAbsPos[i] = aggregateWidth;
			columns[i].setTableStyles(styles);
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
