package com.wms.basic.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wms.basic.model.Pager;
import com.wms.basic.model.User;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {

	@Override
	public List<User> listUserBySql(String string, Object[] objects, Map<String, Object> alias, Class<User> class1,
			boolean b) {
		return this.listBySql(string, objects, alias, class1, b);
	}

	@Override
	public List<User> listUserBySql(String string, Object[] objects, Class<User> class1, boolean b) {
		return this.listBySql(string, objects, class1, b);
	}

	@Override
	public Pager<User> findUserBySql(String string, Object[] objects, Class<User> class1, boolean b) {
		return this.findBySql(string, objects, null, class1, b);
	}

	@Override
	public Pager<User> findUserBySql(String string, Object[] objects, Map<String, Object> alias, Class<User> class1,
			boolean b) {
		return this.findBySql(string, objects, alias, class1, b);
	}

}
