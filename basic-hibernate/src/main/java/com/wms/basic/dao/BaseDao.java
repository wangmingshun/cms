package com.wms.basic.dao;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

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

	private String initSort(String hql) {
		String sort = SystemContext.getSort();
		String order = SystemContext.getOrder();
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
		if(alias != null && alias.size() > 0) {
			Iterator<Map.Entry<String, Object>> iterator = alias.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if(value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
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
//		String rep = hql.replaceFirst("^(select|SELECT)\\s+(.*)\\s+(from|FROM).*", "$2");
//		hql = hql.replaceFirst(rep, "count(*)");
//		if(isHql) {
//			hql = hql.replaceAll("fetch", "");
//		}
//		return hql;
		String e = hql.substring(hql.indexOf("from"));
		String c = "select count(*) " + e;
		if(isHql)
			c.replaceAll("fetch", "");
		return c;
	}
	
	private void setPager(Query query, Pager pager) {
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
		hql = this.initSort(hql);
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
		hql = this.initSort(hql);
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
	
	//查询单个对象
	public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
		Query query = getSession().createQuery(hql);
		setParameter(query, args);
		setAliasParameter(query, alias);
		return query.uniqueResult();
	}
	
	public Object queryObject(String hql, Object[] args) {
		return queryObject(hql, args, null);
	}
	
	public Object queryObject(String hql, Object arg) {
		return queryObject(hql, new Object[]{arg});
	}
	
	public Object queryObjectByAlias(String hql, Map<String, Object> alias) {
		return queryObject(hql, null, alias);
	}
	
	public Object queryObject(String hql) {
		return queryObject(hql, null);
	}
	
	//更新对象
	public void updateByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		setParameter(query, args);
		query.executeUpdate();
	}
	
	public void updateByHql(String hql, Object arg) {
		this.updateByHql(hql, new Object[]{arg});
	}
	
	public void updateByHql(String hql) {
		updateByHql(hql, null);
	}
	
	//根据sql来查询
	public <N extends Object>List<N> listBySql(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean isEntity) {
		sql = initSort(sql);
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		setParameter(sqlQuery, args);
		setAliasParameter(sqlQuery, alias);
		if(isEntity) {
			//查询返回的实体
			sqlQuery.addEntity(clz);
		} else {
			//能够将查询结果依别名注入到clz实体中
			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
		}
		return sqlQuery.list();
	}
	
	public <N extends Object>List<N> listBySql(String sql, Object[] args, Class<?> clz, boolean isEntity) {
		return this.listBySql(sql, args, null, clz, isEntity);
	}
	
	public <N extends Object>List<N> listBySql(String sql, Object arg, Class<?> clz, boolean isEntity) {
		return this.listBySql(sql, new Object[]{arg}, null, clz, isEntity);
	}
	
	public <N extends Object>List<N> listBySql(String sql, Class<?> clz, boolean isEntity) {
		return this.listBySql(sql, null, null, clz, isEntity);
	}
	
	public <N extends Object>List<N> listByAliasSql(String sql, Map<String, Object> alias, Class<?> clz, boolean isEntity) {
		return this.listBySql(sql, null, alias, clz, isEntity);
	}
	
	//根据sql查询分页
	public <N extends Object>Pager<N> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean isEntity) {
		sql = initSort(sql);
		String cq = getCountSql(sql, false);
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		SQLQuery cQuery = getSession().createSQLQuery(cq);
		setAliasParameter(sqlQuery, alias);
		setParameter(sqlQuery, args);
		setAliasParameter(cQuery, alias);
		setParameter(cQuery, args);
		Pager<N> pager = new Pager<N>();
		setPager(sqlQuery, pager);
		if(isEntity) {
			sqlQuery.addEntity(clz);
		} else {
			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
		}
		pager.setDatas(sqlQuery.list());
		pager.setTotal(((BigInteger) cQuery.uniqueResult()).longValue());
		return pager;
	}
	
	public <N extends Object>Pager<N> findBySql(String sql, Object[] args, Class<?> clz, boolean isEntity) {
		return this.findBySql(sql, args, null, clz, isEntity);
	}
	
	public <N extends Object>Pager<N> findBySql(String sql, Object arg, Class<?> clz, boolean isEntity) {
		return this.findBySql(sql, new Object[]{arg}, null, clz, isEntity);
	}
	
	public <N extends Object>Pager<N> findBySql(String sql, Class<?> clz, boolean isEntity) {
		return this.findBySql(sql, null, null, clz, isEntity);
	}
	
	public <N extends Object>Pager<N> findByAliasSql(String sql, Map<String, Object> alias, Class<?> clz, boolean isEntity) {
		return this.findBySql(sql, null, alias, clz, isEntity);
	}
	
	public static void main(String[] args) {
		String hql = "select aa,bb  FROM A";
//		System.out.println(getCountSql(hql));
	}
}
