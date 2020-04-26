package cn.zilanxuan.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 * 功能描述：mybatisplus 分页插件 性能优化
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 下午5:57:19
 */
@MapperScan("cn.zilanxuan.modules.system.mapper")
@Configuration
public class MybatisPlusConfig {

	/**
	 * plus性能优化
	 * @return
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		performanceInterceptor.setMaxTime(100000);
		performanceInterceptor.setFormat(true);
		return performanceInterceptor;
	}
	
	
	/**
	 * plus分页插件
	 */
	@Bean
	public	PaginationInterceptor  PaginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setDialectType("mysql");
		return paginationInterceptor;
	}
	
	
	
}
