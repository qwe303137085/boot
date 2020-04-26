package cn.zilanxuan.modules.system.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.mapper.RoleMapper;
import cn.zilanxuan.modules.system.service.RoleService;

@Service
@Transactional(readOnly=true,rollbackFor=Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {

	/**
	 * 根据角色名称统计记录
	 */
	@Override
	public int getRoleNameCount(String roleName) {
		QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
		wrapper.eq("name", roleName);
		return baseMapper.selectCount(wrapper);
	}
	
	/**
	 * 保存角色信息
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public Role saveRole(Role role) {
		baseMapper.insert(role);
		baseMapper.saveRoleMenus(role.getId(),role.getMenuSet());
		return role;
	}

	/**
	 * 更新角色信息
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateRole(Role role) {
		baseMapper.updateById(role);
		baseMapper.dropRoleMenus(role.getId());
		baseMapper.saveRoleMenus(role.getId(),role.getMenuSet());
		
	}

	
	/**
	 * 根据角色id获取角色信息
	 */
	
	@Override
	public Role getRoleById(Long id) {
		Role role = baseMapper.selectRoleById(id);
		return role;
	}

	/**
	 * 删除角色
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteRole(Role role) {
		role.setDelFlag(true);
		//更新角色
		baseMapper.updateById(role);
		//删除角色和菜单的关系
		baseMapper.dropRoleMenus(role.getId());
		//删除角色和用户的关系
		baseMapper.dropRoleUsers(role.getId());
	}

	/**
	 * 获取所有角色
	 */
	@Override
	public List<Role> selectAll() {
		QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
		wrapper.eq("del_flag", false);
		List<Role> roles = baseMapper.selectList(wrapper);
		return roles;
	}
	
	
}
