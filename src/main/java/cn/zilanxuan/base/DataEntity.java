package cn.zilanxuan.base;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.zilanxuan.modules.system.entity.User;

/**
 * 功能描述:数据实体类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午6:30:51
 */
public abstract class DataEntity <T extends Model> extends BaseEntity<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4467842772120940882L;
	
	/**
	 * 创建者
	 */
	@TableField(value = "create_by",fill = FieldFill.INSERT)
	protected Long createId;
	
	/**
	 * 创建日期
	 */
	@TableField(value = "create_date",fill = FieldFill.INSERT)
	protected Date createDate;
	
	/**
	 * 更新者
	 */
	@TableField(value = "update_by",fill = FieldFill.INSERT)
	protected Long updateId;
	
	/**
	 * 更新日期
	 */
	@TableField(value = "update_date",fill = FieldFill.INSERT)
	protected Date updateDate;
	
	/**
	 * 删除标记 ，使用逻辑删除  Y:正常    N:删除   A:审核
	 */
	@TableField(value = "del_flag")
	protected Boolean delFlag;
	
	
	/**
	 * 创建者
	 */
	@TableField(exist = false)
	protected User createUser;
	
	
	/**
	 * 修改者
	 */
	@TableField(exist = false)
	protected User updateUser;
	
	
	
	/**
	 * 备注
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	protected String remarks;

	public DataEntity(){
		super();
		this.delFlag = false;
	}
	
	public DataEntity(Long id){
		super(id);
	}
	
	
	
	
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}
