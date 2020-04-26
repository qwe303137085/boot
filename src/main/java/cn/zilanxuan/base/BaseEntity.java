package cn.zilanxuan.base;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * entity基础类
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午6:18:29
 */
public abstract class BaseEntity<T extends Model> extends Model<T>{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 实体编号
	 */
	protected Long id;

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BaseEntity() {
		
	}
	
	public BaseEntity(Long id) {
		super();
		this.id = id;
	}
	
	protected Serializable pkVal() {
		return this.id;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
