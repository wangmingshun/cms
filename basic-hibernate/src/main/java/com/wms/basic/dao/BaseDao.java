package com.wms.basic.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.wms.basic.model.Pager;
import com.wms.basic.model.SystemContext;

@SuppressWarnings("unchecked")
public class BaseDao<T> implements IBaseDao<T> {

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	//代替resource的注入方式
	@Inject
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 创建一个Class的对象来获取泛型的class
	 */
	private Class<?> clz;
	
	public Class<?> getClz() {
		if(clz==null) {
			//获取泛型的Class对象
			clz = ((Class<?>) (((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
		}
		return clz;
	}
	
	@Override
	public T add(T t) {
		getSession().save(t);
		return t;
	}

	@Override
	public void update(T t) {
		getSession().update(t);
	}

	@Override
	public void delete(int id) {
		getSession().delete(this.load(id));
	}

	@Override
	public T load(int id) {
		return (T) getSession().load(this.getClz(), id);
	}

	private String initSortHql(String hql) {
		String sort = SystemContext.getSort();
		String order = SystemContext.getSort();
		if(sort != null && !"".equals(sort)) {
			hql += " order by " + sort;
			if("desc".equals(order)) {
				hql += " desc";
			} else {
				hql += " asc";
			}
		}
		return hql;
	}
	
	@SuppressWarnings("rawtypes")
	private void setAliasParameter(Query query, Map<String, Object> alias) {
		if(alias != null || alias.size() > 0) {
			Iterator<Map.Entry<String, Object>> iterator = alias.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if(value instanceof Collection) {
					query.setParameter(key, (Collection) value);
				} else {
					query.setParameter(key, value);
				}
			}
		}
	}
	
	private void setParameter(Query query, Object[] args) {
		if(args != null && args.length > 0) {
			int index = 0;
			for(Object arg : args) {
				query.setParameter(index++, arg);
			}
		}
	}
	
	private String getCountSql(String hql, boolean isHql) {
		String rep = hql.replaceFirst("^(select|SELECT)\\s+(.*)\\s+(from|FROM).*", "$2");
		hql = hql.replaceFirst(rep, "count(*)");
		if(isHql) {
			hql = hql.replaceAll("fetch", "");
		}
		return hql;
	}
	
	private void setPager(Query query, Pager<T> pager) {
		Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageSize == null || pageSize < 0) {
			pageSize = 30;
		}
		if(pageOffset == null || pageOffset < 0) {
			pageOffset = 0;
		}
		pager.setSize(pageSize);
		pager.setOffset(pageOffset);
		query.setFirstResult(pageOffset).setMaxResults(pageSize);
	}
	
	
	// 不分页
	public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
		hql = this.initSortHql(hql);
		Query query = getSession().createQuery(hql);
		this.setAliasParameter(query, alias);
		this.setParameter(query, args);
		return query.list();
	}
	
	public List<T> list(String hql) {
		return this.list(hql, null, null);
	}
	
	public List<T> list(String hql, Object[] args) {
		return this.list(hql, args, null);
	}
	
	public List<T> list(String hql, Object arg) {
		return this.list(hql, new Object[]{arg}, null);
	}
	
	public List<T> listByAlias(String hql, Map<String, Object> alias) {
		return this.list(hql, null, alias);
	}
	
	// 分页
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
		hql = this.initSortHql(hql);
		String cq = getCountSql(hql, true);
		Query query = getSession().createQuery(hql);
		Query cQuery = getSession().createQuery(cq);
		setAliasParameter(cQuery, alias);
		setAliasParameter(query, alias);
		setParameter(cQuery, args);
		setParameter(query, args);
		Pager<T> pager = new Pager<T>();
		setPager(query, pager);
		pager.setDatas(query.list());
		pager.setTotal((Long) cQuery.uniqueResult());
		return pager;
	}
	
	public Pager<T> find(String hql) {
		return this.find(hql, null, null);
	}
	
	public Pager<T> find(String hql, Object arg) {
		return this.find(hql, new Object[]{}, null);
	}
	
	public Pager<T> find(String hql, Object[] args) {
		return this.find(hql, args, null);
	}
	
	public Pager<T> findByAlias(String hql, Map<String, Object> alias) {
		return this.find(hql, null, alias);
	}
	
	public static void main(String[] args) {
		String hql = "select aa,bb  FROM A";
//		System.out.println(getCountSql(hql));
	}
}
