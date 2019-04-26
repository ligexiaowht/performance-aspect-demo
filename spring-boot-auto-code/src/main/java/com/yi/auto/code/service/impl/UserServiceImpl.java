package com.yi.auto.code.service.impl;


import com.yi.auto.code.dao.RoleDao;
import com.yi.auto.code.bean.Role;



import com.yi.auto.code.dao.ClazzDao;
import com.yi.auto.code.bean.User;
import com.yi.auto.code.bean.Clazz;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yi.auto.code.dao.UserDao;
import com.yi.auto.code.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;


/**
 *测试用户 serverImpl
 */
@Service
@Transactional
public class UserServiceImpl   implements UserService {



	/**
	 * 班级
	 */
	@Resource
	private RoleDao roleDao;






	/**
	 * 班级
	 */
	@Resource
	private ClazzDao clazzDao;



	/**
	 * 注入dao
	 */
	@Resource
	private UserDao userDao;
	/**
	 * 初始化
	 */
	@Override
	public UserDao initDao(){
		return userDao;
	}


	/**
	 * 级联查询(带分页) 用户--班级
	 */
	@Override
	public User selectUserAndClazz(User user){
		user = this.selectAllByPaging(user);
		if(user!=null && user.getRows()!=null){
			user.getRows().forEach(t->{
				User data= (User) t;
				Clazz clazz=new Clazz();
				clazz.setUserId(data.getId());
				List<Clazz> lists = clazzDao.selectByCondition(clazz);
				if(lists!=null && lists.size()>0){
					data.setClazz(lists.get(0));
				}
			});
		}
		return user;

	}


	/**
	 * 级联条件查询 用户--班级
	 */
	@Override
	public List<User> selectUserAndClazzByCondition(User user){
		List<User> datas = this.selectByCondition(user);
		if(datas!=null){
			datas.forEach(t->{
				Clazz clazz=new Clazz();
				clazz.setUserId(t.getId());
				List<Clazz> lists = clazzDao.selectByCondition(clazz);
				if(lists!=null && lists.size()>0){
					t.setClazz(lists.get(0));
				}
			});
		}
		return datas;

	}


	/**
	 * 级联删除(根据主表删除) 用户--班级
	 */
	@Override
	public Integer deleteUserAndClazz(User user){
		Clazz clazz=new Clazz();
		clazz.setUserId(user.getId());
		clazzDao.deleteClazzByUser(clazz);
		return userDao.deleteByPrimaryKey(user);

	}





	/**
	 * 多对多重写insert
	 */
	@Override
	public int insert(User user){
		int insert = userDao.insert(user);
		String id = user.getRoleId();
		if(id !=null && !"".equals(id)){
			String[] split = id.split(",");
			userDao.insertRelation(user.getId().toString(),split);
		}
		return insert;

	}


	/**
	 * 多对多重写Update
	 */
	@Override
	public int update(User user){
		Integer update = userDao.update(user);
		String id = user.getRoleId();
		if(id !=null && !"".equals(id)){
			Role d=new Role();
			d.setUserId(user.getId().toString());
			roleDao.deleteRoleByUser(d);
			String[] split = id.split(",");
			userDao.insertRelation(user.getId().toString(),split);
		}
		return update;

	}


	/**
	 * 级联查询(带分页) 用户--班级
	 */
	@Override
	public User selectUserAndRole(User user){
		user = this.selectAllByPaging(user);
		if(user!=null && user.getRows()!=null){
			user.getRows().forEach(t->{
				User data= (User) t;
				Role role=new Role();
				role.setUserId(data.getId().toString());
				List<Role> datas=roleDao.selectRoleByUser(role);
				data.setRoleList(datas);
			});
		}
		return user;

	}


	/**
	 * 级联条件查询 用户--班级
	 */
	@Override
	public List<User> selectUserAndRoleByCondition(User user){
		List<User> datas = this.selectByCondition(user);
		if(datas!=null){
			datas.forEach(t->{
				Role role=new Role();
				role.setUserId(t.getId().toString());
				List<Role> lists=roleDao.selectRoleByUser(role);
				t.setRoleList(lists);
			});
		}
		return datas;

	}


	/**
	 * 级联删除(根据主表删除) 用户--班级
	 */
	@Override
	public Integer deleteUserAndRole(User user){
		Role role=new Role();
		user = userDao.selectByPrimaryKey(user);
		role.setUserId(user.getId().toString());
		roleDao.deleteRoleByUser(role);
		return userDao.deleteByPrimaryKey(user);

	}


	/**
	 * 根据外表id查询主表所有数据(带分页)
	 */
	@Override
	public User selectUserByRole(User t){
		PageHelper.startPage(t.getPage(), t.getPageSize());
		List<User> lists = userDao.selectUserByRole(t);
		PageInfo pageInfo = new PageInfo(lists);
		t.setRows(lists);
		t.setTotal((new Long(pageInfo.getTotal())).intValue());
		return t;

	}




}
