package com.yi.auto.code.dao;


import java.util.List;
import org.apache.ibatis.annotations.Param;



import com.zengtengpeng.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import com.yi.auto.code.bean.User;


/**
 *测试用户 dao
 */
@Mapper
public interface UserDao  extends BaseDao<User>{















	/**
	 * 根据班级查询用户
	 */
	public List<User> selectUserByRole(User user);
	/**
	 * 根据班级删除用户
	 */
	public Integer deleteUserByRole(User user);
	/**
	 * 级联新增
	 */
	public Integer insertRelation(@Param("id") String id,@Param("roleId") String[] roleId);


}
