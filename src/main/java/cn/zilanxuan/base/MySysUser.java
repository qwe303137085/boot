package cn.zilanxuan.base;

import org.apache.shiro.SecurityUtils;

import cn.zilanxuan.realm.AuthRealm;

/**
 * 功能描述:用户信息
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月9日 下午11:05:07
 */
public class MySysUser {
	
	public static String icon() {
		return ShiroUser().getIcon();
	}
	
	public static Long id() {
		return ShiroUser().getId();
	}
	
	public static String loginName() {
		return ShiroUser().getLoginName();
	}	
	
	public static String nickName() {
		return ShiroUser().getNickName();
	}
	
	
	public static AuthRealm.ShiroUser ShiroUser() {
		AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
}
