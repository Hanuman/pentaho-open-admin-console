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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableStyles {

	public enum Type {
		EVEN_ROW, ODD_ROW, MOUSE_OVER, SELECTED_ROW, TABLE_HEADER_PANEL,TABLE_HEADER_TEXT
	};
	
	public TableStyles()
	{
		addStyle(TableStyles.Type.TABLE_HEADER_PANEL, "pentaho-tableHeader-panel"); //$NON-NLS-1$ 
		addStyle(TableStyles.Type.TABLE_HEADER_TEXT, "pentaho-tableHeader-text"); //$NON-NLS-1$
		addStyle(TableStyles.Type.EVEN_ROW, "pentaho-tableRow-even"); //$NON-NLS-1$
		addStyle(TableStyles.Type.ODD_ROW, "pentaho-tableRow-odd"); //$NON-NLS-1$
		addStyle(TableStyles.Type.SELECTED_ROW, "pentaho-tableRow-selected"); //$NON-NLS-1$
	}

	private Map<Type, List<String>> styles = new HashMap<Type, List<String>>();

	public void addStyle(Type type, String styleName) {
		List<String> existing = styles.get(styleName);
		if (existing == null) {
			existing = new ArrayList<String>(3);
			existing.add(styleName);
			styles.put(type, existing);
		} else
			existing.add(styleName);
	}

	public List<String> getStyleNames(Type type) {
		return styles.get(type);
	}
}
