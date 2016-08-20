package com.wms.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wms.basic.dao.BaseDao;
import com.wms.basic.model.Pager;
import com.wms.cms.model.Group;
import com.wms.cms.model.Role;
import com.wms.cms.model.RoleType;
import com.wms.cms.model.User;
import com.wms.cms.model.UserGroup;
import com.wms.cms.model.UserRole;

@Repository("userDao")
@SuppressWarnings("unchecked")
public class UserDao extends BaseDao<User> implements IUserDao {

	@Override
	public List<Role> listRoleByUserId(int userId) {
		String hql = "select ur.role from UserRole ur where ur.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public List<Integer> listRoleIdByUserId(int userId) {
		String hql = "select ur.id from UserRole ur where ur.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public List<Group> listGroupByUserId(int userId) {
		String hql = "select ug.group from UserGroup ug where ug.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public List<Integer> listGroupIdByUserId(int userId) {
		String hql = "select ug.id from UserGroup ug where ug.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public UserRole loadUserRoleByUserIdAndRoleId(int userId, int roleId) {
		String hql = "select ur from UserRole ur where ur.user.id=? and ur.role.id=?";
		return (UserRole) this.getSession().createQuery(hql)
					.setParameter(0, userId).setParameter(1, roleId).uniqueResult();
	}

	@Override
	public UserGroup loadUserGroupByUserIdAndGroupId(int userId, int groupId) {
		String hql = "select ug from UserGroup ug where ug.user.id=? and ug.group.id=?";
		return (UserGroup) this.getSession().createQuery(hql)
					.setParameter(0, userId).setParameter(1, groupId).uniqueResult();
	}

	@Override
	public User loadUserByUsername(String username) {
		String hql = "from User where username=?";
		return (User) this.queryObject(hql, username);
	}

	@Override
	public List<User> listUserByRoleId(int roleId) {
		String hql = "select ur.user from UserRole ur where ur.role.id=?";
		return this.list(hql, roleId);
	}

	@Override
	public List<User> listUserByRoleType(RoleType roleType) {
		String hql = "select ur.user from UserRole ur where ur.role.roleType=?";
		return this.list(hql, roleType);
	}

	@Override
	public List<User> listUserByGroupId(int groupId) {
		String hql = "select ug.user from UserGroup ug where ug.group.id=?";
		return this.list(hql, groupId);
	}

	@Override
	public void addUserRole(User user, Role role) {
		UserRole ur = this.loadUserRoleByUserIdAndRoleId(user.getId(), role.getId());
		if(ur != null) {
			return;
		}
		ur = new UserRole();
		ur.setUser(user);
		ur.setRole(role);
		this.getSession().save(ur);
	}

	@Override
	public void addUserGroup(User user, Group group) {
		UserGroup ug = this.loadUserGroupByUserIdAndGroupId(user.getId(), group.getId());
		if(ug != null) {
			return;
		}
		ug = new UserGroup();
		ug.setUser(user);
		ug.setGroup(group);
		this.getSession().save(ug);

	}

	@Override
	public void deleteUserRoleByUserId(int userId) {
		String hql = "delete UserRole ur where ur.user.id=?";
		this.updateByHql(hql, userId);
	}

	@Override
	public void deleteUserGroupByUserId(int userId) {
		String hql = "delete UserGroup ug where ug.user.id=?";
		this.updateByHql(hql, userId);
	}

	@Override
	public void deleteUserRoleByUserIdAndRoleId(int userId, int roleId) {
		String hql = "delete UserRole ur where ur.user.id=? and ur.role.id=?";
		this.updateByHql(hql, new Object[]{userId, roleId});
	}

	@Override
	public void deleteUserGroupByUserIdAndGroupId(int userId, int groupId) {
		String hql = "delete UserGroup ug where ug.user.id=? and ug.group.id=?";
		this.updateByHql(hql, new Object[]{userId, groupId});
	}

	@Override
	public Pager<User> findUser() {
		return this.find("from User");
	}
}
