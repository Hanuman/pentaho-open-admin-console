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

import java.util.List;
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

	private List<NameValue> discriminators;
	

	public RowSupport(TableModel model,
			TableDataServiceAsync dataService) {
		this.model = model;
		visibleRows = model.getVisibleRows() + 1;
		rowHeight = model.getPixelRowHeight();
		this.dataService = dataService;
	}

	protected void setDiscriminators(List<NameValue> discriminators) {
		this.discriminators = discriminators;
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

	public void layoutRows(final Grid where, final ColumnSupport colSupport,
			final int currentPosition) {
		if (dataService == null)
			return;

		dataService.getData(colSupport.getColumnNames(),
				discriminators == null ? new NameValue[] {} : discriminators
						.toArray(new NameValue[discriminators.size()]),
				new AsyncCallback<Map<String, String[]>>() {

					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());

					}

					public void onSuccess(Map<String, String[]> results) {
						for (int i = 0; i < visibleRows; i++)
							if (rowWidgets[i][0] != null)
								model.hidden(i + idxOfTopRow);

						idxOfTopRow = currentPosition
								/ model.getPixelRowHeight();

						ColumnModel[] columns = colSupport.getColumns();

						where.resize(visibleRows, columns.length);

						// to get the row count it is easy. We just need to
						// count the length of one of the strings
						int rowCount = results.values().iterator().next().length;
						model.setActualRowCount(rowCount);
						
						for (int i = 0; i < visibleRows; i++) {
							int idx = idxOfTopRow + i;
							if (idx >= rowCount)
								break;

							for (int k = 0; k < columns.length; k++) {
								Widget existing = rowWidgets[i][k];
								rowWidgets[i][k] = columns[k].getCell(idx,
										results);
								if (existing != null
										&& existing != rowWidgets[i][k])
									where.remove(existing);
								rowWidgets[i][k].setPixelSize(columns[k]
										.getWidth(), rowHeight);
								where.setWidget(i, k, rowWidgets[i][k]);

								if (model.getStyles() != null) {
									List<String> styleNames = model.getStyles()
											.getStyleNames(i % 2 == 0 ? TableStyles.Type.EVEN_ROW
													: TableStyles.Type.ODD_ROW);
									if (styleNames != null
											&& styleNames.size() > 0)
										where.getRowFormatter().setStyleName(i,
												styleNames.get(0));
								}

							}
							model.visible(idx);
						}

					}

				});

	}

}
