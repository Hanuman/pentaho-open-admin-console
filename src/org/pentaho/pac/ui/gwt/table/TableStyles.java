package org.pentaho.pac.ui.gwt.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableStyles {

	public enum Type {
		EVEN_ROW, ODD_ROW, MOUSE_OVER, SELECTED, TABLE_HEADER
	};

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

	public List<String> getStyles(Type type) {
		return styles.get(type);
	}
}
