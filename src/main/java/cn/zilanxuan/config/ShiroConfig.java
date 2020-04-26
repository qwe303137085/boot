package cn.zilanxuan.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.google.common.collect.Maps;
import com.xiaoleilu.hutool.codec.Base64;

import cn.zilanxuan.base.CaptchaFormAuthenticationFilter;
import cn.zilanxuan.realm.AuthRealm;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;

/**
 * 功能描述：shiro配置类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 上午10:02:18
 */
@Configuration
public class ShiroConfig {
	
	private Logger log = LoggerFactory.getLogger(ShiroConfig.class);
	
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private Integer redisPort;
	
	@Value("${spring.redis.password}")
	private String redisPassword;
	
	
	

	
	
	@Bean
	public FilterRegistrationBean delegatingFilterProxy() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		DelegatingFilterProxy  proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true); //开启生命周期
		proxy.setTargetBeanName("shiroFilter");
		filterRegistrationBean.setFilter(proxy);
		filterRegistrationBean.setDispatcherTypes(DispatcherType.ERROR,DispatcherType.REQUEST,DispatcherType.FORWARD,DispatcherType.INCLUDE);
		return filterRegistrationBean;
	}
	
	@Bean(value="shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("authRealm") AuthRealm authRealm) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager(authRealm));
		Map<String,Filter> map = Maps.newHashMap();
		map.put("authc", new CaptchaFormAuthenticationFilter());
		bean.setFilters(map);
		//配置访问权限
		LinkedHashMap<String,String> filterChainDefinitionMap = Maps.newLinkedHashMap();
		filterChainDefinitionMap.put("/api/log/**","anon");
		filterChainDefinitionMap.put("/api/login/main", "anon");
		filterChainDefinitionMap.put("/api/genCaptcha", "anon");
		filterChainDefinitionMap.put("/api/getCaptchaVoice", "anon");
		filterChainDefinitionMap.put("/api/systemLogout","anon");
		filterChainDefinitionMap.put("/page/**","anon");
		filterChainDefinitionMap.put("/api/**","anon");
		filterChainDefinitionMap.put("/css/**","anon");
		filterChainDefinitionMap.put("/images/**","anon");
		filterChainDefinitionMap.put("/js/**","anon");
		filterChainDefinitionMap.put("/lib/**","anon");
		filterChainDefinitionMap.put("/index.html","anon");
		filterChainDefinitionMap.put("/","anon");
		
		filterChainDefinitionMap.put("/**", "authc");
		bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return bean;
	}
	
	@Bean
	public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
		log.info("----------shiro开始加载-----------");
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(authRealm);
		defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
		defaultWebSecurityManager.setSessionManager(webSessionManager());
		defaultWebSecurityManager.setCacheManager(cacheManager());
		return defaultWebSecurityManager;
	}
	
	@Bean("authRealm")
	public AuthRealm authRealm() {
		AuthRealm authRealm = new AuthRealm();
		authRealm.setCredentialsMatcher(credentialsMatcher());
		return authRealm;
	}
	
	/**
	 * 凭证匹配器
	 * @return
	 */
	@Bean(name = "credentialsMatcher")
	public CredentialsMatcher credentialsMatcher() {
		return new CredentialsMatcher();
	}
	
	
	@Bean
	public SimpleCookie rememberMeCookie() {
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(2592000);
		return cookie;
		
	}
	
	@Bean
	public CookieRememberMeManager rememberMeManager () {
		CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
		rememberMeManager.setCookie(rememberMeCookie());
		rememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUsOFSA3SDFAdag=="));
		return rememberMeManager;
	}
	
	/**
	 * aop方式权限检查
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator DefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}
	
	
	/**
	 * 保证实现了shiro内部lifecycle函数的bean执行
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	
	
	@Bean 
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("authRealm") AuthRealm authRealm) {
		SecurityManager manager  =  securityManager(authRealm);
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(manager);
		return advisor;
	}
	
	/**
	 * 设置session过期时间
	 * @return
	 */
	@Bean
	public SessionManager webSessionManager() {
		DefaultWebSessionManager manager = new DefaultWebSessionManager();
		manager.setGlobalSessionTimeout(60 * 60 * 1000);
		manager.setSessionValidationSchedulerEnabled(true);
		manager.setSessionDAO(redisSessionDao());
		return manager;
	}
	
	
	@Bean
	public RedisManager redisManager() {
		RedisManager manager = new RedisManager();
		manager.setHost(redisHost);
		manager.setPort(redisPort);
		manager.setExpire(60 * 60);
		manager.setPassword(redisPassword);
		return manager;
	}
	
	@Bean 
	public RedisSessionDAO redisSessionDao() {
		RedisSessionDAO sessionDao = new RedisSessionDAO();
		sessionDao.setKeyPrefix("zilanxuan_");
		sessionDao.setRedisManager(redisManager());
		return sessionDao;
	}
	
	@Bean("myCacheManager")
	public RedisCacheManager cacheManager() {
		RedisCacheManager a  = new RedisCacheManager();
		a.setRedisManager(redisManager());
		return a;
	}
}
