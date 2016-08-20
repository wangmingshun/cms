package com.wms.cms.dao;

import java.util.List;

import com.wms.basic.dao.IBaseDao;
import com.wms.basic.model.Pager;
import com.wms.cms.model.Group;

public interface IGroupDao extends IBaseDao<Group>{
	public List<Group> listGroup();
	public Pager<Group> findGroup();
	public void deleteUserGroupByGroupId(int groupId);
}
