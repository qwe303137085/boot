package cn.zilanxuan.modules.system.entity.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 树形菜单辅助类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午9:55:32
 */
public class TreeMenu {

	
	private Long id;
	
	private Long pid;
	
	private String title;
	
	private String icon;
	
	private String href;
	
	private Boolean spread = false;
	
	private List<ShowMenu> children = Lists.newArrayList();
	
	public TreeMenu(Boolean spread) {
		this.spread = false;
	}

	public TreeMenu(Long id, Long pid, String title, String icon, String href) {
		this.id = id;
		this.pid = pid;
		this.title = title;
		this.icon = icon;
		this.href = href;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public Boolean getSpread() {
		return spread;
	}

	public void setSpread(Boolean spread) {
		this.spread = spread;
	}

	public List<ShowMenu> getChildren() {
		return children;
	}

	public void setChildren(List<ShowMenu> children) {
		this.children = children;
	}
	
	
	
	
}
