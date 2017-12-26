package com.scott.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class NDAO extends HibernateDaoSupport {

	@SuppressWarnings({ "rawtypes" })
	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(load(clazz, id));
	}

	public void delete(Object object) {
		getHibernateTemplate().delete(object);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer deleteByQuery(final String queryString, final Object[] parameters) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(queryString);
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				return new Integer(query.executeUpdate());
			}
		});
	}

	public List<?> find(final String queryString) {
		return getHibernateTemplate().find(queryString);
	}

	public List<?> find(final String queryString, final Object[] parameters) {
		return getHibernateTemplate().find(queryString, parameters);
	}

	public List<?> findByNamedQuery(String queryName) {
		return getHibernateTemplate().findByNamedQuery(queryName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findByNamedQuery(final String sqlname, final Class $class, final Object[] paramValues) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession) throws HibernateException {
				Query localQuery = paramSession.getNamedQuery(sqlname);
				if (paramValues != null) {
					for (int i = 0; i < paramValues.length; i++) {
						localQuery.setParameter(i, paramValues[i]);
					}
				}

				List localList = localQuery.setResultTransformer(Transformers.aliasToBean($class)).list();
				return localList;
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findByNamedQuery(final String sqlname, final Class $class, final String[] params,
			final Object[] paramValues) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession) throws HibernateException {
				Query localQuery = paramSession.getNamedQuery(sqlname);

				if (paramValues != null) {
					for (int i = 0; i < params.length; i++) {
						localQuery.setParameter(params[i], paramValues[i]);
					}
				}
				List localList = localQuery.setResultTransformer(Transformers.aliasToBean($class)).list();
				return localList;
			}
		});
	}

	public List<?> findByNamedQuery(String queryName, Object paramValue) {
		return getHibernateTemplate().findByNamedQuery(queryName, paramValue);
	}

	public List<?> findByNamedQuery(String queryName, Object[] paramValues) {
		return getHibernateTemplate().findByNamedQuery(queryName, paramValues);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List findBySQLQuery(final Class clazz, final String sql, final Object[] parameters) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(clazz));
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected int findCountBySQLQuery(final String sql, final Object[] paramValues) {
		return ((Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession) throws HibernateException {
				Query localQuery = paramSession.createSQLQuery(sql);

				for (int i = 0; i < paramValues.length; i++) {
					localQuery.setParameter(i, paramValues[i]);
				}
				Long l = Long.valueOf(((Number) localQuery.uniqueResult()).longValue());
				return Integer.valueOf(l.intValue());
			}
		})).intValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int findCountsByHql(final String hql, final Object[] values) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
				return ((Number) query.uniqueResult()).intValue();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object load(Class clazz, Serializable id) {
		return getHibernateTemplate().load(clazz, id);
	}

	public void save(Object object) {
		getHibernateTemplate().save(object);
	}

	public void update(Object object) {
		getHibernateTemplate().update(object);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateByQuery(final String queryString, final Object[] parameters) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(queryString);
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				query.executeUpdate();
				return null;
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findPageByHql(final String hql, final Object[] values, final int offset, final int pageSize) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
				List result = query.setFirstResult(offset).setMaxResults(pageSize).list();
				return result;
			}
		});
	}
	
	public List<?> findByHql(String hql) {
		return getHibernateTemplate().find(hql);
	}

	public List<?> findByHql(String hql, Object[] paramValues) {
		return getHibernateTemplate().find(hql, paramValues);
	}
	  

}
