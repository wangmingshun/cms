package com.wms.cms.service;

import java.util.List;

import javax.inject.Inject;

import com.wms.basic.model.Pager;
import com.wms.cms.dao.IGroupDao;
import com.wms.cms.dao.IRoleDao;
import com.wms.cms.dao.IUserDao;
import com.wms.cms.model.CmsException;
import com.wms.cms.model.Group;
import com.wms.cms.model.Role;
import com.wms.cms.model.User;

public class UserService implements IUserSerivece {

	private IUserDao userDao;
	private IGroupDao groupDao;
	private IRoleDao roleDao;
	
	public IUserDao getUserDao() {
		return userDao;
	}
	@Inject
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IGroupDao getGroupDao() {
		return groupDao;
	}
	@Inject
	public void setGroupDao(IGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public IRoleDao getRoleDao() {
		return roleDao;
	}
	@Inject
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public void add(User user, int[] roleIds, int[] groupIds) {
		//1.判断用户是否存在
		User u = userDao.load(user.getId());
		if(u != null) {
			throw new CmsException("添加失败,要添加的用户已存在");
		}
		
		//2.添加用户和角色关联信息
		for(int roleId : roleIds) {
			addUserRole(user, roleId);
		}
		
		//3.添加用户和组关联信息
		for(int groupId : groupIds) {
			addUserGroup(user, groupId);
		}
	}

	private void addUserRole(User user, int roleId) {
		//1.判断用户角色是否存在，不存在则抛出异常
		Role role = roleDao.load(roleId);
		if(role == null) {
			throw new CmsException("要添加的用户角色不存在");
		}
		//2.添加用户和角色关联信息,该方法中检查用户角色对象是否存在，存在则不再添加
		userDao.addUserRole(user, role);
	}
	
	private void addUserGroup(User user, int groupId) {
		//1.判断用户组对象那个是否存在,不存在则抛出异常
		Group group = groupDao.load(groupId);
		if(group == null) {
			throw new CmsException("要添加的用户组不存在");
		}
		//2.添加用户和组关联信息,该方法检查用户组对象是否存在,存在则不添加
		userDao.addUserGroup(user, group);
	}
	
	@Override
	public void delete(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(User user, int[] roleIds, int[] groupIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatus(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Pager<User> findUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User load(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> listRolesByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> listGroupsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> listRoleIdsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> listGroupIdsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listUserByRoleId(int roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listUserByGroupId(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

}
