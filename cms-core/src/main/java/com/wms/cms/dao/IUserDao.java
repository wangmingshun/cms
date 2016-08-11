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
	public List<Role> listUserRoles(int userId);
	
	/**
	 * 获取用户所有的角色id
	 * @param userId
	 * @return
	 */
	public List<Integer> listUserRoleIds(int userId);
	
	/**
	 * 获取用户所有的组信息
	 * @param userId
	 * @return
	 */
	public List<Group> listUserGroups(int userId);
	
	/**
	 * 获取用户所有组的id
	 * @param userId
	 * @return
	 */
	public List<Integer> listUserGroupIds(int userId);
	
	/**
	 * 根据用户的id和用户角色id获取用户角色对象
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public UserRole loadUserRole(int userId, int roleId);
	
	/**
	 * 根据用户的id和用户组id获取用户组对象
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public UserGroup loadUserGroup(int userId, int groupId);
	
	/**
	 * 根据用户id获取用户对象
	 * @param userId
	 * @return
	 */
	public User loadByUsername(String username);
	
	/**
	 * 根据角色id获取用户列表
	 * @param roleId
	 * @return
	 */
	public List<User> listRoleUsers(int roleId);
	
	/**
	 * 根据角色类型获取用户列表
	 * @param roleType
	 * @return
	 */
	public List<User> listRoleTypeUsers(RoleType roleType);
	
	/**
	 * 根据组id获取用户列表
	 * @param groupId
	 * @return
	 */
	public List<User> listGroupUsers(int groupId);
	
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
	public void deleteUserRole(int userId);
	
	/**
	 * 根据用户id删除用户组对象
	 * @param userId
	 */
	public void deleteUserGroup(int userId);
	
	/**
	 * 根据用户id和用户角色id删除用户角色对象
	 * @param userId
	 * @param roleId
	 */
	public void deleteUserRole(int userId, int roleId);
	
	/**
	 * 根据用户id和用户组id删除用户组对象
	 * @param userId
	 * @param groupId
	 */
	public void deleteUserGroup(int userId, int groupId);
	
	/**
	 * 获取所有用户的分页对象
	 * @return
	 */
	public Pager<User> findUser();
}
