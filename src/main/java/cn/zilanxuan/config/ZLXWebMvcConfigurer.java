package cn.zilanxuan.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import cn.zilanxuan.base.MyHandlerInterceptor;

/**
 * springmvc配置
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 下午6:03:44
 */
@Configuration
public class ZLXWebMvcConfigurer implements WebMvcConfigurer{
	
	@Value("${zilanxuan.allowedOrigins}")
	private String allowedOrigins;
	
	@Override
	public void addCorsMappings(CorsRegistry regist) {
		regist.addMapping("/**") 
		//放行的原始域
		.allowedOrigins(allowedOrigins)
		//放行的请求类型
		.allowedMethods("POST","GET","PUT","OPTIONS","DELETE")
		//是否发送cookie信息
		.allowCredentials(true)
		.maxAge(3600)
		.allowedHeaders("Origin, No-Cache, X-Requested-With, If-Modified-Since, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization, Token");
		
	}
	
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
		bean.setMultipartConfig(multipartConfigElement());
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return bean;
	}
	
	/**
	 * fastjson序列化
	 */
	//@Bean
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
				SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.BrowserCompatible,SerializerFeature.WriteNonStringKeyAsString);
		converters.add(fastJsonHttpMessageConverter);
		converters.add(responseBodyConverter());
		
	}
	
	@Bean						
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("10MB");
		factory.setMaxRequestSize("50MB");
		return factory.createMultipartConfig();
	}
	
	
	@Bean
	public HttpMessageConverter<String> responseBodyConverter(){
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}
	
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyHandlerInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/api/login/main","/api/systemLogout","/api/getCaptcha","/api/getCaptchaVoice");
	}
	
}
