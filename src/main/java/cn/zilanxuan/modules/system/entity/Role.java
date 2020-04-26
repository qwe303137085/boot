package cn.zilanxuan.modules.system.entity;

import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.zilanxuan.base.DataEntity;

/**
 * 功能描述：角色实体类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午9:26:30
 */
@TableName("sys_role")
public class Role extends DataEntity<Role>{

	private static final long serialVersionUID = 1L;

	/**
	 * 角色名称
	 */
	private String name;
	
	/**
	 * 角色的菜单列表
	 */
	@TableField(exist = false)
	private Set<Menu> menuSet;
	
	/**
	 * 角色的用户列表
	 */
	@TableField(exist = false)
	private Set<User> userSet;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Menu> getMenuSet() {
		return menuSet;
	}

	public void setMenuSet(Set<Menu> menuSet) {
		this.menuSet = menuSet;
	}

	public Set<User> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}
	
	
	
	
	
}
