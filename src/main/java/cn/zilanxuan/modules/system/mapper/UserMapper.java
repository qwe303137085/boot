package cn.zilanxuan.modules.system.mapper;

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.entity.User;

public interface UserMapper extends BaseMapper<User>{
	
	//根据map，k,v获取用户信息
	User selectUserByMap(Map<String,Object> map);
	
	//保存用户和角色关系
	void saveUserRoles(@Param("userId") Long id,@Param("roleIds") Set<Role> roles);
	
	//删除用户和角色的关系
	void dropUserRolesByUserId(@Param("userId")Long userId);
}
