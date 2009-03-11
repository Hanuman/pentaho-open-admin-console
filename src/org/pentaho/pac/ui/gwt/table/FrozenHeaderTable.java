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
package org.pentaho.pac.ui.gwt.table;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pac.ui.gwt.table.model.TableDataServiceAsync;
import org.pentaho.pac.ui.gwt.table.model.TableModel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class FrozenHeaderTable extends Composite implements ScrollListener {

	private static final int SCRL_WIDTH = 20;

	private static final int NO_ROW_SELECTED = -1;

	private TableModel model;

	private AbsolutePanel root, headerContainer, header, tablePanel;

	private Grid table;

	private ScrollPanel scroller;
	private ColumnSupport colSupport;

	private RowSupport rowSupport;

	private int gridWidth;
	private boolean horizScrolling;
	private TableDataServiceAsync dataService;

	private int selectedRowIndex = NO_ROW_SELECTED;

	private List<NameValue> discriminators = new ArrayList<NameValue>(1);

	private List<RowSelectionListener> rowSelectionListeners;

	public FrozenHeaderTable(TableModel tableModel) {
		this.model = tableModel;
	}

	public void addDiscriminator(String name, String value) {
		discriminators.add(new NameValue(name, value));
	}

	public void clearDiscriminators() {
		discriminators.clear();
	}

	public void init() {
		headerContainer = new AbsolutePanel();
		header = new AbsolutePanel();
		headerContainer.add(header, 0, 0);
		table = new Grid();
		tablePanel = new AbsolutePanel();
		tablePanel.add(table);
		scroller = new ScrollPanel(tablePanel);
		scroller.addScrollListener(this);
		root = new AbsolutePanel();
		root.add(headerContainer, 0, 0);
		root.add(scroller, 0, 20);
		refresh();
		table.addTableListener(new TableListener() {
			public void onCellClicked(SourcesTableEvents sender, int row,
					int column) {
				FrozenHeaderTable.this.cellClicked(row, column);
			}
		});

		initWidget(root);
	}

	public void addTableStyle(TableStyles.Type type, String styleName) {
		model.getStyles().addStyle(type, styleName);
	}

	private void refresh() {
		reloadMetaData();
		redrawHeaders();
		redrawData();
	}

	private void reloadMetaData() {
		colSupport = new ColumnSupport(model);
		rowSupport = new RowSupport(model, dataService);
		rowSupport.setDiscriminators(this.discriminators);

		int modelTotalWidth = model.getTotalWidth();

		gridWidth = modelTotalWidth < 0 ? colSupport.getWidth() + SCRL_WIDTH
				: modelTotalWidth;
		if (gridWidth > colSupport.getWidth() + SCRL_WIDTH)
			gridWidth = colSupport.getWidth() + SCRL_WIDTH;
		horizScrolling = gridWidth < colSupport.getWidth();

		rowSupport.removeRows(table, colSupport);
	}

	private void redrawHeaders() {
		headerContainer.setPixelSize(gridWidth, model.getPixelHeaderHeight());
		header
				.setPixelSize(colSupport.getWidth(), model
						.getPixelHeaderHeight());

		colSupport.addHeaders(header);

		int hPos = scroller.getHorizontalScrollPosition();
		if (hPos < 0)
			hPos = 0;

		headerContainer.setWidgetPosition(header, -hPos, 0);
	}

	private void redrawData() {

		dataService.getRowCount(new AsyncCallback<Integer>() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}

			public void onSuccess(Integer result) {
				int visibleRows = model.getVisibleRows();
				int rowCount = result;
				if (rowCount < visibleRows)
					visibleRows = rowCount;
				int scrollerVertSize = visibleRows * model.getPixelRowHeight()
						+ (horizScrolling ? SCRL_WIDTH : 0);

				tablePanel.setPixelSize(colSupport.getWidth(), rowCount
						* model.getPixelRowHeight());
				table.setCellPadding(0);
				table.setCellSpacing(0);
				scroller.setPixelSize(gridWidth, scrollerVertSize);
				root.setWidgetPosition(scroller, 0, model
						.getPixelHeaderHeight());
				root.setPixelSize(gridWidth, model.getPixelHeaderHeight()
						+ scrollerVertSize);

				redrawCells();

			}
		});

	}

	private void redrawCells() {
		if (scroller == null)
			return;

		rowSupport.layoutRows(table, colSupport, scroller.getScrollPosition());
	}

	public void setTableModel(TableModel tableModel) {
		this.model = tableModel;
	}

	public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
		// TODO Auto-generated method stub

	}

	public void setTableDataService(TableDataServiceAsync dataService) {
		this.dataService = dataService;
	}

	/**
	 * Change the CSS styles of all rows. The currently selected row gets
	 * different CSS style.
	 */
	private void redrawSelectedRow() {
		String selectedStyle = getSingleStyleName(TableStyles.Type.SELECTED_ROW);
		String evenStyleName = getSingleStyleName(TableStyles.Type.EVEN_ROW);
		String oddStyleName = getSingleStyleName(TableStyles.Type.ODD_ROW);
		RowFormatter gridRowFormatter = table.getRowFormatter();
		
		for (int row = 0; row <= model.getActualRowCount(); row++) {
			if (row == this.selectedRowIndex) {
				gridRowFormatter.setStyleName(row, selectedStyle);
			} else {
				gridRowFormatter.setStyleName(row, row % 2 == 0 ? evenStyleName
						: oddStyleName);
			}
		}

	}

	private String getSingleStyleName(TableStyles.Type type) {
		if (model.getStyles() == null)
			return ""; //$NON-NLS-1$
		List<String> sstyles = model.getStyles().getStyleNames(type);
		return sstyles.size() > 0 ? sstyles.get(0) : ""; //$NON-NLS-1$
	}

	private void cellClicked(int row, int column) {
		selectRow(row);
		redrawSelectedRow();
	}

	public void selectRow(int rowIndex) {
		selectedRowIndex = rowIndex;

		String rowId = "1";// getSelectedRowId(); //$NON-NLS-1$
		if (this.rowSelectionListeners != null) {
			for (int i = 0; i < this.rowSelectionListeners.size(); i++) {
				RowSelectionListener listener = (RowSelectionListener) this.rowSelectionListeners
						.get(i);
				listener.onRowSelected(this, rowId);
			}
		}
	}

	public void addRowSelectionListener(RowSelectionListener listener) {
		if (rowSelectionListeners == null) {
			rowSelectionListeners = new ArrayList<RowSelectionListener>();
		}
		rowSelectionListeners.add(listener);
	}
}
