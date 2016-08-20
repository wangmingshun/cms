package com.wms.cms.dao;

import java.util.List;

import com.wms.basic.dao.IBaseDao;
import com.wms.basic.model.Pager;
import com.wms.cms.model.Role;

public interface IRoleDao extends IBaseDao<Role>{
	public List<Role> listRole();
	public Pager<Role> findRole();
	public void deleteUserRoleByRoleId(int roleId);
}
