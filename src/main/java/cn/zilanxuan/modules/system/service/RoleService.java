package cn.zilanxuan.modules.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.zilanxuan.modules.system.entity.Role;

/**
 * 
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月17日 下午4:30:35
 */
public interface RoleService extends IService<Role>{

	//根据角色名称统计记录
	int getRoleNameCount(String roleName);
	
	//保存角色信息
	Role saveRole(Role role);
	
	//更新角色信息
	void updateRole(Role role);
	
	//根据角色id获取角色信息
	Role getRoleById(Long id);
	
	//删除角色
	void deleteRole(Role role);
	
	//获取所有角色信息
	List<Role> selectAll();
}
