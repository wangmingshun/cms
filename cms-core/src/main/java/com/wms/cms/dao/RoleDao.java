package com.wms.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wms.basic.dao.BaseDao;
import com.wms.basic.model.Pager;
import com.wms.cms.model.Role;

@Repository
public class RoleDao extends BaseDao<Role> implements IRoleDao {

	@Override
	public List<Role> listRole() {
		return this.list("from Role");
	}

	@Override
	public Pager<Role> findRole() {
		return this.find("from Role");
	}

	@Override
	public void deleteUserRoleByRoleId(int roleId) {
		this.updateByHql("delete from UserRole ur where ur.role.id=?", roleId);
	}
}
