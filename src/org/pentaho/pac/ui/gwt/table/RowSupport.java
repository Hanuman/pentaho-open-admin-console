package org.pentaho.pac.ui.gwt.table;

import java.util.Map;

import org.pentaho.pac.ui.gwt.table.model.ColumnModel;
import org.pentaho.pac.ui.gwt.table.model.TableDataServiceAsync;
import org.pentaho.pac.ui.gwt.table.model.TableModel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class RowSupport {
	private TableModel model;

	private Widget[][] rowWidgets;

	private int visibleRows;

	private int idxOfTopRow = 0;

	private int rowHeight;

	private TableDataServiceAsync dataService;

	public RowSupport(TableModel model, TableStyles styles,
			TableDataServiceAsync dataService) {
		this.model = model;
		visibleRows = model.visibleRows() + 1;
		rowHeight = model.getPixelRowHeight();
		this.dataService = dataService;
	}

	public void removeRows(Grid where, ColumnSupport cs) {
		if (rowWidgets == null || rowWidgets.length != visibleRows
				|| (rowWidgets.length > 0 && rowWidgets[0].length != cs.size())) {
			if (rowWidgets != null) {
				for (int x = 0; x < rowWidgets.length; x++) {
					for (int y = 0; y < rowWidgets[x].length; y++) {
						Widget existing = rowWidgets[x][y];
						if (existing != null)
							where.remove(existing);
					}
				}
			}
			rowWidgets = new Widget[visibleRows][cs.size()];
		}

	}

	public void layoutRows(final Grid where,
			final ColumnSupport colSupport, final int currentPosition) {
		if (dataService == null)
			return;

		dataService.getData(colSupport.getColumnNames(),new AsyncCallback<Map<String,String[]>>() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}

			public void onSuccess(Map<String,String[]> results) {
				for (int i = 0; i < visibleRows; i++)
					if (rowWidgets[i][0] != null)
						model.hidden(i + idxOfTopRow);

				idxOfTopRow = currentPosition / model.getPixelRowHeight();
				
				ColumnModel[] columns = colSupport.getColumns();
				
				where.resize(visibleRows, columns.length);
				
				//to get the row count it is easy.  We just need to count the length of one of the strings
				int rowCount = results.values().iterator().next().length;//model.getRowCount();
				for (int i = 0; i < visibleRows; i++) {
					int idx = idxOfTopRow + i;
					if (idx >= rowCount)
						break;

					for (int k = 0; k < columns.length; k++) {
						Widget existing = rowWidgets[i][k];
						rowWidgets[i][k] = columns[k].getCell(idx,results);
						if (existing != null && existing != rowWidgets[i][k])
							where.remove(existing);
						rowWidgets[i][k].setPixelSize(columns[k].getWidth(),
								rowHeight);
						where.setWidget(i,k,rowWidgets[i][k]);
					}
					model.visible(idx);
				}

			}

		});

	}
	
//	public void layoutRows2(final AbsolutePanel where,
//			final ColumnSupport colSupport, final int currentPosition) {
//		if (dataService == null)
//			return;
//
//		dataService.getData(colSupport.getColumnNames(),new AsyncCallback<Map<String,String[]>>() {
//
//			public void onFailure(Throwable caught) {
//				Window.alert(caught.getMessage());
//
//			}
//
//			public void onSuccess(Map<String,String[]> results) {
//				for (int i = 0; i < visibleRows; i++)
//					if (rowWidgets[i][0] != null)
//						model.hidden(i + idxOfTopRow);
//
//				idxOfTopRow = currentPosition / model.getPixelRowHeight();
//				int rowCount = model.getRowCount();
//				for (int i = 0; i < visibleRows; i++) {
//					int idx = idxOfTopRow + i;
//					if (idx >= rowCount)
//						break;
//
//					ColumnModel[] columns = colSupport.getColumns();
//					int[] columnPos = colSupport.getColPos();
//
//					for (int k = 0; k < columns.length; k++) {
//						Widget existing = rowWidgets[i][k];
//						rowWidgets[i][k] = columns[k].getCell(idx,results);
//						if (existing != null && existing != rowWidgets[i][k])
//							where.remove(existing);
//						rowWidgets[i][k].setPixelSize(columns[k].getWidth(),
//								rowHeight);
//						where.add(rowWidgets[i][k], columnPos[k],
//								(i + idxOfTopRow) * rowHeight);
//					}
//					model.visible(idx);
//				}
//
//			}
//
//		});
//
//	}
}
