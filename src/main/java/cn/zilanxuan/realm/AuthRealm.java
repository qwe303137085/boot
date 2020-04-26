package cn.zilanxuan.realm;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.modules.system.service.MenuService;
import cn.zilanxuan.modules.system.service.UserService;
import cn.zilanxuan.util.Constants;
import cn.zilanxuan.util.Encodes;
/**
 * 功能描述：自定义认证，授权
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 下午1:53:38
 */

public class AuthRealm extends AuthorizingRealm{

	@Autowired
	@Lazy
	private UserService userService;
	
	@Autowired
	@Lazy
	private MenuService menuService;
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		ShiroUser shiroUser = (ShiroUser)pc.getPrimaryPrincipal();
		User user = userService.findUserByLoninName(shiroUser.loginName);
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<Role> roles = user.getRoleLists();
		Set<String> roleNames = Sets.newHashSet();
		for(Role s : roles) {
			if(StringUtils.isNotBlank(s.getName())) {
				roleNames.add(s.getName());
			}
		}
		Set<Menu> menus = user.getMenus();
		Set<String> permissions = Sets.newHashSet();
		if("root".equals(user.getLoginName())) {
			for(Menu mm :menuService.list()) {
				if(StringUtils.isNotBlank(mm.getPermission())) {
					permissions.add(mm.getPermission());
				}
			}
		}else {
			for(Menu m : menus) {
				
				if(StringUtils.isNotBlank(m.getPermission())) {
					
					String per = m.getPermission();
					if(per.indexOf(",") != -1) {  //判断权限中是否有,号，有就分别加入到shiro权限中
						String[] ps = per.split(",");
						for(String s : ps) {
							permissions.add(s);
						}
					}else{
						permissions.add(per);
						
						if(null != m.getChildren() && m.getChildren().size() > 0) {
							for(Menu ms : m.getChildren()) {
								String perc = ms.getPermission();
								if(perc.indexOf(",") != -1) {
									String[] psc = perc.split(",");
									for(String sc : psc) {
										permissions.add(sc);
									}
								}else {
									permissions.add(perc);
								}
							}
						}
					}
					
				}
			}
		}
		
		for(String p : permissions) {
			System.out.println("---per----"+p);
		}
		info.setRoles(roleNames);
		info.setStringPermissions(permissions);
		
		return info;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)arg0;
		String username = (String)token.getPrincipal();
		User user = userService.findUserByLoninName(username);
		if(null == user) {
			throw new UnknownAccountException(); //用户名不存在
		}
		if(Boolean.TRUE.equals(user.getLocked())) {
			throw new LockedAccountException(); //账号锁定
		}
		//byte[] salt = Encodes.decodeHex(user.getSalt());
		
		SimpleAuthenticationInfo ainfo = new SimpleAuthenticationInfo(
				 	new ShiroUser(user.getId(),user.getLoginName(),user.getNickName(),user.getIcon())
				 	,user.getPassword(),
				 	ByteSource.Util.bytes(user.getLoginName()),
				 	getName());
		
		
		
				
		return ainfo;
	}
	
	/**
	 * 根据用户名清除授权信息
	 * @param username
	 */
	public void removeUserAuthorizationInfoCache(String username) {
		SimplePrincipalCollection pc = new SimplePrincipalCollection();
		pc.add(username, super.getName());
		super.clearCachedAuthorizationInfo(pc);;
	}
	
	/**
	 * 设置password校验的hash算法与迭代次数
	 */
	/*
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Constants.HASH_ALGORITHM);
		matcher.setHashIterations(Constants.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	*/
	
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public static class ShiroUser implements Serializable{

		private static final long serialVersionUID = -7512839142086851125L;
		public Long id;
		public String loginName;
		public String nickName;
		public String icon;
		
		
		public ShiroUser(Long id, String loginName, String nickName, String icon) {
			this.id = id;
			this.loginName = loginName;
			this.nickName = nickName;
			this.icon = icon;
		}


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public String getLoginName() {
			return loginName;
		}


		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}


		public String getNickName() {
			return nickName;
		}


		public void setNickName(String nickName) {
			this.nickName = nickName;
		}


		public String getIcon() {
			return icon;
		}


		public void setIcon(String icon) {
			this.icon = icon;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((icon == null) ? 0 : icon.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
			result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ShiroUser other = (ShiroUser) obj;
			if (icon == null) {
				if (other.icon != null)
					return false;
			} else if (!icon.equals(other.icon))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (loginName == null) {
				if (other.loginName != null)
					return false;
			} else if (!loginName.equals(other.loginName))
				return false;
			if (nickName == null) {
				if (other.nickName != null)
					return false;
			} else if (!nickName.equals(other.nickName))
				return false;
			return true;
		}
		
		
	}

}
