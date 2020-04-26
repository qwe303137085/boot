package cn.zilanxuan.modules.system.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.modules.system.entity.Role;

public interface RoleMapper extends BaseMapper<Role>{
	
	//保存角色和菜单关系
	void saveRoleMenus(@Param("roleId") Long id,@Param("menus") Set<Menu> menus);
	
	//删除角色和菜单关系
	void dropRoleMenus(@Param("roleId") Long roleId);
	
	//根据角色id获取角色信息
	Role selectRoleById(@Param("id") Long id);
	
	//删除角色和用户的关系
	void dropRoleUsers(@Param("roleId") Long roleId);

}
