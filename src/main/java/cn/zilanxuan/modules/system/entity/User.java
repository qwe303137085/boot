package cn.zilanxuan.modules.system.entity;

import java.util.Set;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Sets;

import cn.zilanxuan.base.DataEntity;

/**
 * 功能描述：用户实体类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午9:31:47
 */
@TableName("sys_user")
public class User extends DataEntity<User>{

	private static final long serialVersionUID = 1L;

	/**
	 * 登陆名
	 */
	@TableField(value="login_name")
	private String loginName;
	
	/**
	 * 昵称
	 */
	@TableField(value="nick_name", strategy = FieldStrategy.IGNORED)
	private String nickName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 加密盐值
	 */
	private String salt;
	
	/**
	 * 手机号码
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private String tel;
	
	/**
	 * 邮箱
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private String email;
	
	/**
	 * 账号是否锁定
	 */
	private Boolean locked;
	
	/**
	 * 用户头像
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private String icon;
	
	/**
	 * 用户的角色列表
	 */
	@TableField(exist = false)
	private Set<Role> roleLists = Sets.newHashSet();
	
	
	/**
	 * 用户的菜单列表
	 */
	@TableField(exist = false)
	private Set<Menu> menus = Sets.newHashSet();


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


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getSalt() {
		return salt;
	}


	public void setSalt(String salt) {
		this.salt = salt;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Boolean getLocked() {
		return locked;
	}


	public void setLocked(Boolean locked) {
		this.locked = locked;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public Set<Role> getRoleLists() {
		return roleLists;
	}


	public void setRoleLists(Set<Role> roleLists) {
		this.roleLists = roleLists;
	}


	public Set<Menu> getMenus() {
		return menus;
	}


	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}


	@Override
	public String toString() {
		return "User [loginName=" + loginName + ", nickName=" + nickName + ", password=" + password + ", salt=" + salt
				+ ", tel=" + tel + ", email=" + email + ", locked=" + locked + ", icon=" + icon + ", roleLists="
				+ roleLists + ", menus=" + menus + "]";
	}
	
	
	
	
	
}
