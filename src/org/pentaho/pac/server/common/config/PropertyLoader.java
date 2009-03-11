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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pentaho.pac.server.common.HibernateSessionFactory;

public class PropertyLoader {

	private static PropertyLoader loader = new PropertyLoader();

	public static PropertyLoader getInstance() {
		return loader;
	}

	public <E extends PacProperty> PropertyCollection<E> getProperties(
			Class<E> module) {
		Session s = null;
		Transaction tx = null;

		try {
			s = HibernateSessionFactory.getSession("MGMT-SERVICES"); //$NON-NLS-1$
			tx = s.beginTransaction();
			@SuppressWarnings("unchecked")
			PropertyCollection<E> props = new PropertyCollection<E>(s
					.createCriteria(module).list());
			return props;
		} finally {
			if (s != null)
				s.disconnect();
			if (tx != null)
				tx.commit();
		}

	}

	public <E extends PacProperty> void persist(PropertyCollection<E> props) {
		Session s = null;
		Transaction tx = null;

		try {
			s = HibernateSessionFactory.getSession("MGMT-SERVICES"); //$NON-NLS-1$
			tx = s.beginTransaction();
			for (PacProperty prop : props) {
				s.saveOrUpdate(prop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (s != null)
				s.disconnect();
			if (tx != null)
				tx.commit();
		}
	}
}
