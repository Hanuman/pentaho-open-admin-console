package org.pentaho.pac.server.common.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropertyCollection<E extends PacProperty> implements Iterable<E>{
	private List<E> props = new ArrayList<E>();

	public PropertyCollection() {

	}

	public PropertyCollection(List<E> properties) {
		this.props.addAll(properties);
	}

	public String getProperty(String name) {
		for (PacProperty prop : props) {
			if (prop.getName().equals(name))
				return prop.getValue();
		}

		return null;
	}

	public void setProperty(E property) {

		for (PacProperty prop : props) {
			if (prop.getName().equals(property.getName())) {
				prop.setValue(property.getValue());
				return;
			}

		}

		props.add(property);

	}

	@Override
	public Iterator<E> iterator() {
		return props.iterator();
	}

}
