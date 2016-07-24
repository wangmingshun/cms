package com.wms.basic.dao;

public interface IBaseDao<T> {
	
	/**
	 * 添加对象
	 * @param t
	 * @return
	 */
	public T add(T t);
	/**
	 * 更新对象
	 * @param t
	 */
	public void update(T t);
	/**
	 * 删除对象
	 * @param id
	 */
	public void delete(int id);
	
	/**
	 * 根据id加载对象
	 */
	public T load(int id);
}
