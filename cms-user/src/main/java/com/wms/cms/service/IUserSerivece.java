package com.wms.cms.service;

import java.util.List;

import com.wms.basic.model.Pager;
import com.wms.cms.model.Group;
import com.wms.cms.model.Role;
import com.wms.cms.model.User;

public interface IUserSerivece {
	
	/**
	 * 添加用户
	 * 如果用户存在,则抛出异常
	 * @param user
	 * @param roles
	 * @param groups
	 */
	void add(User user, int[] roleIds, int[] groupIds);
	
	/**
	 * 删除用户
	 * 先删除用户角色关联信息，再删除用户组关联信息，如果用户存在文章则不能删除
	 * @param userId
	 */
	void delete(int userId);
	
	/**
	 * 修改用户
	 * 如果roles中的角色不存在于用户中，则添加。如果用户中的角色不存在于roles中则删除。
	 * @param user
	 * @param roles
	 * @param groups
	 */
	void update(User user, int[] roleIds, int[] groupIds);
	
	/**
	 * 更改用户状态
	 * @param userId
	 */
	void updateStatus(int userId);
	
	/**
	 * 获取用户的分页列表
	 * @return
	 */
	Pager<User> findUser();
	
	/**
	 * 获取用户对象
	 * @param userId
	 * @return
	 */
	User load(int userId);
	
	/**
	 * 根据用户id获取用户所有角色对象
	 * @param userId
	 * @return
	 */
	List<Role> listRolesByUserId(int userId);
	
	/**
	 * 根据用户id获取用户所用的组对象
	 * @param userId
	 * @return
	 */
	List<Group> listGroupsByUserId(int userId);
	
	/**
	 * 根据用户id获取用户所有的角色id
	 * @param userId
	 * @return
	 */
	List<Integer> listRoleIdsByUserId(int userId);
	
	/**
	 * 根据用户id获取用户所有的组id
	 * @param userId
	 * @return
	 */
	List<Integer> listGroupIdsByUserId(int userId);
	
	/**
	 * 根据角色id获取所用的用户对象
	 * @param roleId
	 * @return
	 */
	List<User> listUserByRoleId(int roleId);
	
	/**
	 * 根据组id获取所有的用户对象
	 * @param groupId
	 * @return
	 */
	List<User> listUserByGroupId(int groupId);
	
}
