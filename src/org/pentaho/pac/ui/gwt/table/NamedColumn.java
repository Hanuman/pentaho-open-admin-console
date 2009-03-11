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
