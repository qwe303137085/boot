package cn.zilanxuan.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import cn.zilanxuan.base.CaptchaFormAuthenticationFilter;
import cn.zilanxuan.base.MySysUser;

/**
 * 功能描述：自定义填充公共字段，如create_data,update_data，使用当前日期即可，不需自己填写
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 下午7:31:56
 */
@Component
public class SysMetaObjectHandler implements MetaObjectHandler{

	public static final  Logger log = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);

	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("正在调用该insert填充字段方法");
		Object createDate = getFieldValByName("createDate",metaObject);
		Object updateDate = getFieldValByName("updateDate",metaObject);
		Object createId = getFieldValByName("createId",metaObject);
		Object updateId = getFieldValByName("updateId",metaObject);
						 
		
		if(null == createDate) {
			setFieldValByName("createDate", new Date(), metaObject);
		}
		if(null == createId) {
			if(null != MySysUser.ShiroUser()) {
				setFieldValByName("createId",MySysUser.id() , metaObject);
			}
		}
		if(null == updateDate) {
			setFieldValByName("updateDate", new Date(), metaObject);
		}
		if(null == updateId) {
			if(null != MySysUser.ShiroUser()) {
				setFieldValByName("updateId",MySysUser.id() , metaObject);
			}
		}
		
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("正在调用该update填充字段方法");
		setFieldValByName("updateDate", new Date(),metaObject);
		Object updateId = getFieldValByName("updateId",metaObject);
		if(null == updateId) {
			setFieldValByName("updateId",MySysUser.id(),metaObject);
		}
		
	}
	
	
	
}
