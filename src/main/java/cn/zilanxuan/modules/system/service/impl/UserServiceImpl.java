package cn.zilanxuan.modules.system.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;

import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.modules.system.mapper.UserMapper;
import cn.zilanxuan.modules.system.service.UserService;
import cn.zilanxuan.util.PasswordUtil;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{

	
	/**
	 * 根据登陆名获取用户信息
	 */
	public User findUserByLoninName(String loginName) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("loginName", loginName);
		User user = baseMapper.selectUserByMap(map);
		return user;
	}

	/**
	 * 根据id获取用户信息
	 */
	@Override
	public User findUserById(Long id) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("id", id);
		User user = baseMapper.selectUserByMap(map);
		return user;
	}

	/**
	 * 根据登录名查询用户的个数
	 */
	@Override
	public int userCount(String name) {
		QueryWrapper<User> q = new QueryWrapper<User>();
		q.eq("login_name", name).or().eq("tel", name).or().eq("email", name);
		int count = baseMapper.selectCount(q);
		return count;
	}

	/**
	 * 保存用户信息
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public User saveUser(User user) {
		try {
			String authPassowrd = PasswordUtil.encrypt(user.getPassword(), user.getLoginName());
			user.setPassword(authPassowrd);
			user.setLocked(false);
			baseMapper.insert(user);
		} catch (Exception e) {
			log.error("保存用户信息失败...",e);
		}
		return findUserById(user.getId());
	}

	/**
	 * 保存用户和角色关系
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public void saveUserRoles(Long id, Set<Role> roles) {
		System.out.println(roles.toString()+"---userRoless-------");
		baseMapper.saveUserRoles(id,roles);
	}

	/**
	 * 更新用户信息
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public User updateUser(User user) {
		baseMapper.updateById(user);
		return user;
	}

	/**
	 * 删除用户和角色的关系
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public void dropUserRolesByUserId(Long id) {
		baseMapper.dropUserRolesByUserId(id);
	}

	/**
	 * 删除单条记录
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteUser(User user) {
		user.setDelFlag(true);
		baseMapper.updateById(user);
	}
} 
