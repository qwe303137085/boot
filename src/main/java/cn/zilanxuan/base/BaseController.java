package cn.zilanxuan.base;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.modules.system.service.LogService;
import cn.zilanxuan.modules.system.service.MenuService;
import cn.zilanxuan.modules.system.service.RoleService;
import cn.zilanxuan.modules.system.service.UserService;
import cn.zilanxuan.realm.AuthRealm.ShiroUser;

/**
 * 基础controller
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月12日 上午10:46:58
 */
public class BaseController {
	
	//用户服务
	@Autowired
	protected UserService userService;
	
	//菜单服务
	@Autowired
	protected MenuService menuService;
	
	//权限服务
	@Autowired
	protected RoleService roleService;
	
	//日志服务
	@Autowired
	protected LogService logService;

	/**
	 * 获取当前用户信息
	 * @return
	 */
	public User currentUser() {
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		if(null == shiroUser) {
			return null;
		}
		User user =  userService.findUserById(shiroUser.getId());
		return user;
	}
}
