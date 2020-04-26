package cn.zilanxuan.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import cn.zilanxuan.base.MySysUser;
import cn.zilanxuan.modules.system.entity.User;

/**
 * 功能描述:缓存工具类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月9日 下午10:51:48
 */
public class CacheUtils {

	/**
	 * 清除当前用户redis缓存
	 */
	@Caching(evict = {
		@CacheEvict(value = "user", key = "'user_id_' + T(String).valueOf(#result.id)",condition = "#result.id != null and #result.id != 0 "),
		@CacheEvict(value = "user", key = "'user_name_' + #result.loginName",condition = "#result.loginName != null and #result.loginName != ''	"),
		@CacheEvict(value = "user", key = "'user_email_' + #result.email",condition = "#result.email != null and #result.email != ''" ),
		@CacheEvict(value = "user", key = "'user_tel_' + #result.tel",condition = "#result.tel != null and #result.tel != ''" ),
	})
	public User clearUserCache() {
		User user = new User();
		user.setId(MySysUser.id());
		return user.selectById();
	}
}
