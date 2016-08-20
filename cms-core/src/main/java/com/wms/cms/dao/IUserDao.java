package com.wms.cms.dao;

import java.util.List;

import com.wms.basic.dao.IBaseDao;
import com.wms.basic.model.Pager;
import com.wms.cms.model.Group;
import com.wms.cms.model.Role;
import com.wms.cms.model.RoleType;
import com.wms.cms.model.User;
import com.wms.cms.model.UserGroup;
import com.wms.cms.model.UserRole;

public interface IUserDao extends IBaseDao<User>{

	/**
	 * 获取用户所有的角色信息
	 * @param userId
	 * @return
	 */
	public List<Role> listRoleByUserId(int userId);
	
	/**
	 * 获取用户所有的角色id
	 * @param userId
	 * @return
	 */
	public List<Integer> listRoleIdByUserId(int userId);
	
	/**
	 * 获取用户所有的组信息
	 * @param userId
	 * @return
	 */
	public List<Group> listGroupByUserId(int userId);
	
	/**
	 * 获取用户所有组的id
	 * @param userId
	 * @return
	 */
	public List<Integer> listGroupIdByUserId(int userId);
	
	/**
	 * 根据用户的id和用户角色id获取用户角色对象
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public UserRole loadUserRoleByUserIdAndRoleId(int userId, int roleId);
	
	/**
	 * 根据用户的id和用户组id获取用户组对象
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public UserGroup loadUserGroupByUserIdAndGroupId(int userId, int groupId);
	
	/**
	 * 根据用户id获取用户对象
	 * @param userId
	 * @return
	 */
	public User loadUserByUsername(String username);
	
	/**
	 * 根据角色id获取用户列表
	 * @param roleId
	 * @return
	 */
	public List<User> listUserByRoleId(int roleId);
	
	/**
	 * 根据角色类型获取用户列表
	 * @param roleType
	 * @return
	 */
	public List<User> listUserByRoleType(RoleType roleType);
	
	/**
	 * 根据组id获取用户列表
	 * @param groupId
	 * @return
	 */
	public List<User> listUserByGroupId(int groupId);
	
	/**
	 * 添加用户角色对象
	 * @param user
	 * @param role
	 */
	public void addUserRole(User user, Role role);
	
	/**
	 * 添加用户组对象
	 * @param user
	 * @param role
	 */
	public void addUserGroup(User user, Group group);
	
	/**
	 * 根据用户id删除用户角色对象
	 * @param userId
	 */
	public void deleteUserRoleByUserId(int userId);
	
	/**
	 * 根据用户id删除用户组对象
	 * @param userId
	 */
	public void deleteUserGroupByUserId(int userId);
	
	/**
	 * 根据用户id和用户角色id删除用户角色对象
	 * @param userId
	 * @param roleId
	 */
	public void deleteUserRoleByUserIdAndRoleId(int userId, int roleId);
	
	/**
	 * 根据用户id和用户组id删除用户组对象
	 * @param userId
	 * @param groupId
	 */
	public void deleteUserGroupByUserIdAndGroupId(int userId, int groupId);
	
	/**
	 * 获取所有用户的分页对象
	 * @return
	 */
	public Pager<User> findUser();
}
