package com.wms.cms.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.wms.basic.model.Pager;
import com.wms.basic.model.SystemContext;
import com.wms.cms.model.Group;
import com.wms.cms.model.Role;
import com.wms.cms.model.RoleType;
import com.wms.cms.model.User;
import com.wms.cms.model.UserGroup;
import com.wms.cms.model.UserRole;
import com.wms.cms.test.unit.AbstractDbUnitTestCase;
import com.wms.cms.test.unit.EntitiesHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUserDao extends AbstractDbUnitTestCase{
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Inject
	private IUserDao userDao;
	
	@Inject
	private IGroupDao groupDao;
	
	@Inject
	private IRoleDao roleDao;
	
	@Before
	public void setUp() throws SQLException, IOException, DatabaseUnitException {
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
		this.backupAllTable();
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon,ds);
	}
	
	@Test
	public void testListRoleByUserId() throws DatabaseUnitException, SQLException {
		List<Role> actuals = Arrays.asList(new Role(2,"文章发布人员",RoleType.ROLE_PUBLISH),new Role(3,"文章审核人员",RoleType.ROLE_AUDIT));
		List<Role> roles = userDao.listRoleByUserId(2);
		EntitiesHelper.assertRoles(roles, actuals);
	}
	
	@Test
	public void testListRoleIdByUserId() {
		List<Integer> actuals = Arrays.asList(2, 3);
		List<Integer> roleIds = userDao.listRoleIdByUserId(2);
		EntitiesHelper.assertObjects(roleIds, actuals);
	}
	
	@Test
	public void testListGroupByUserId() {
		List<Group> actuals = Arrays.asList(new Group(1, "财务处", "负责财务部门的网页"), 
				new Group(2, "计科系", "负责财务部门的网页"), new Group(3, "宣传部", "负责财务部门的网页"));
		List<Group> groups = userDao.listGroupByUserId(2);
		EntitiesHelper.assertGroups(groups, actuals);
	}
	
	@Test
	public void testListGroupIdByUserId() {
		List<Integer> actuals = Arrays.asList(4, 5);
		List<Integer> groupIds = userDao.listGroupIdByUserId(3);
		EntitiesHelper.assertObjects(groupIds, actuals);
	}
	
	@Test
	public void testLoadUserRoleByUserIdAndRoleId() {
		UserRole userRole = userDao.loadUserRoleByUserIdAndRoleId(3, 2);
		User u = new User(3, "admin3", "admin1", "123", "admin1@admin.com", "110", 1);
		Role r = new Role(2, "文章发布人员", RoleType.ROLE_PUBLISH);
		EntitiesHelper.assertUser(u, userRole.getUser());
		EntitiesHelper.assertRole(r, userRole.getRole());
	}
	@Test
	public void testLoadUserGroupByUserIdAndGroupId() {
		UserGroup ug = userDao.loadUserGroupByUserIdAndGroupId(3, 3);
		User u = new User(3, "admin3", "admin1", "123", "admin1@admin.com", "110", 1);
		Group g = new Group(3, "宣传部", "负责财务部门的网页");
		EntitiesHelper.assertUser(u, ug.getUser());
		EntitiesHelper.assertGroup(g, ug.getGroup());
	}
	
	@Test
	public void testLoadUserByUsername() {
		User user = userDao.loadUserByUsername("admin2");
		User u = new User(2, "admin2", "admin1", "123", "admin1@admin.com", "110", 1);
		EntitiesHelper.assertUser(u, user);
	}
	
	@Test
	public void testListUserByRoleId() {
		List<User> eusers =  userDao.listUserByRoleId(2);
		List<User> ausers = Arrays
				.asList(new User(2, "admin2", "admin1", "123", "admin1@admin.com", "110", 1),
						new User(3, "admin3", "admin1", "123", "admin1@admin.com", "110", 1));
		EntitiesHelper.assertUsers(eusers, ausers);
	}
	@Test
	
	public void testListUserByRoleType() {
		List<User> eusers = userDao.listUserByRoleType(RoleType.ROLE_PUBLISH);
		List<User> ausers = Arrays
				.asList(new User(2, "admin2", "admin1", "123", "admin1@admin.com", "110", 1),
						new User(3, "admin3", "admin1", "123", "admin1@admin.com", "110", 1));
		EntitiesHelper.assertUsers(eusers, ausers);
	}
	
	@Test
	public void testListUserByGroupId() {
		List<User> eusers = userDao.listUserByGroupId(1);
		List<User> ausers = Arrays.asList(new User(2, "admin2", "admin1", "123", "admin1@admin.com", "110", 1),
				new User(3, "admin3", "admin1", "123", "admin1@admin.com", "110", 1));
		EntitiesHelper.assertUsers(eusers, ausers);
	}
	
	@Test
	public void testAddUserGroup() {
		Group group = groupDao.load(1);
		User user = userDao.load(1);
		userDao.addUserGroup(user, group);
		UserGroup userGroup = userDao.loadUserGroupByUserIdAndGroupId(user.getId(), group.getId());
		Assert.assertNotNull(userGroup);
		Assert.assertEquals(userGroup.getGroup().getId(), 1);
		Assert.assertEquals(userGroup.getUser().getId(), 1);
	}
	
	@Test
	public void testAddUserRole() {
		Role role = roleDao.load(1);
		User user = userDao.load(1);
		userDao.addUserRole(user, role);
		UserRole userRole = userDao.loadUserRoleByUserIdAndRoleId(1, 1);
		Assert.assertNotNull(userRole);
		Assert.assertEquals(userRole.getRole().getId(), 1);
		Assert.assertEquals(userRole.getUser().getId(), 1);
	}
	
	@Test
	public void testDeleteUserRoleByUserId() {
		userDao.deleteUserRoleByUserId(1);
		List<Role> roles = userDao.listRoleByUserId(1);
		Assert.assertTrue(roles.size() <= 0);
	}
	
	@Test
	public void testDeleteUserGroupByUserId() {
		userDao.deleteUserGroupByUserId(3);
		List<Group> groups = userDao.listGroupByUserId(3);
		Assert.assertTrue(groups.size() <= 0);
	}
	
	@Test
	public void testDeleteUserRoleByUserIdAndRoleId() {
		userDao.deleteUserRoleByUserIdAndRoleId(1, 1);
		Assert.assertNull(userDao.loadUserRoleByUserIdAndRoleId(1, 1));
	}
	
	@Test
	public void testDeleteUserGroupByUserIdAndGroupId() {
		userDao.deleteUserGroupByUserIdAndGroupId(2, 1);
		Assert.assertNull(userDao.loadUserGroupByUserIdAndGroupId(2, 1));
	}
	
	public void testFindUser() {
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(10);
		Pager<User> eusers = userDao.findUser();
		List<User> ausers = Arrays
				.asList(new User(1, "admin1", "admin1", "123", "admin1@admin.com", "110", 1),
						new User(2, "admin2", "admin1", "123", "admin1@admin.com", "110", 1),
						new User(3, "admin3", "admin1", "123", "admin1@admin.com", "110", 1));
		Assert.assertNotNull(eusers);
		Assert.assertEquals(eusers.getTotal(), 3);
		EntitiesHelper.assertUsers(eusers.getDatas(), ausers);
	}
	
	@After
	public void tearDown() throws FileNotFoundException, DatabaseUnitException, SQLException {
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		Session s = holder.getSession(); 
		s.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		this.resumeTable();
	}
}
