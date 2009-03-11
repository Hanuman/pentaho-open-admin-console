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
