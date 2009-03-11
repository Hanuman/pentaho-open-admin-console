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


	public Iterator<E> iterator() {
		return props.iterator();
	}

}
