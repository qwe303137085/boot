package cn.zilanxuan.modules.system.service;


import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.entity.User;

public interface UserService extends IService<User>{

	//根据登陆名获取用户信息
	User findUserByLoninName(String loginName);
	
	//根据id获取用户信息
	User findUserById(Long id);
	
	//根据登录名查询用户的个数
	int userCount(String name);
	
	//保存用户信息
	User saveUser(User user);
	
	//保存用户和角色的关系
	void saveUserRoles(Long id,Set<Role> roles);
	
	//更新用户信息
	User updateUser(User user);
	
	//删除用户和角色的关系
	void dropUserRolesByUserId(Long id);
	
	//删除单条记录
	void deleteUser(User user);
}
