package com.wms.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wms.basic.dao.BaseDao;
import com.wms.basic.model.Pager;
import com.wms.cms.model.Group;

@Repository
public class GroupDao extends BaseDao<Group> implements IGroupDao {

	@Override
	public List<Group> listGroup() {
		return this.list("from Group");
	}

	@Override
	public Pager<Group> findGroup() {
		return this.find("from Group");
	}

	@Override
	public void deleteUserGroupByGroupId(int groupId) {
		this.updateByHql("delete from UserGroup ug where ug.group.id=?", groupId);
	}

}
