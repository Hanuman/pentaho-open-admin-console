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
