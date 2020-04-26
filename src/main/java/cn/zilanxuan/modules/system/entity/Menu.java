package cn.zilanxuan.modules.system.entity;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.zilanxuan.base.TreeEntity;

/**
 * 功能描述：菜单实体类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午8:45:45
 */
@TableName("sys_menu")
public class Menu extends TreeEntity<Menu>{

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单名称
	 */
	private String name;
	
	/**
	 * 菜单图标
	 */
	private String icon;
	
	/**
	 * 链接地址
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private String href;
	
	/**
	 * 打开方式
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private String target;
	
	/**
	 * 是否显示
	 */
	@TableField(value="is_show", strategy = FieldStrategy.IGNORED)
	private Boolean isShow;
	
	/**
	 * 类型  0:菜单   1:按钮   -1：目录
	 */
	@TableField(value = "is_menu", strategy = FieldStrategy.IGNORED)
	private Integer isMenu;
	
	@TableField(value="bg_color")
	private Integer bgColor;
	
	/**
	 * 权限标识
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private String permission;
	
	@TableField(exist = false)
	private Integer dataCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 1000, message = "图标信息最大为1000个字符 ")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Integer getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}

	public Integer getBgColor() {
		return bgColor;
	}

	public void setBgColor(Integer bgColor) {
		this.bgColor = bgColor;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}
	
	
}
