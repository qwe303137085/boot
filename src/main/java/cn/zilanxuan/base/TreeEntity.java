package cn.zilanxuan.base;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;



public abstract class TreeEntity <T extends Model> extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 父id
	 */
	@TableField("parent_id")
	protected Long parentId;
	
	/**
	 * 节点层次（第一层、第二层、第三层）
	 */
	protected Long level;
	
	/**
	 * varchar(1000) Null路径
	 */
	@TableField("parent_ids")
	protected String parentIds;
	
	/**
	 * 排序
	 */
	protected Integer sort;
	
	/**
	 * 子节点信息  exist = false 表示数据库中没有，不按数据库来
	 */
	@TableField(exist = false)
	protected List<T> children;
	
	/**
	 * 父节点信息
	 */
	@TableField(exist = false)
	protected T parentTree;
	
	
	public TreeEntity() {
		super();
		this.sort= 30;
	}
	
	public TreeEntity(Long id) {
		super(id);
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		this.children = children;
	}

	public T getParentTree() {
		return parentTree;
	}

	public void setParentTree(T parentTree) {
		this.parentTree = parentTree;
	}
	
	

}
