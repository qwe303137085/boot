package cn.zilanxuan.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.modules.system.service.UserService;

/**
 * 功能描述：自定义拦截器
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 下午6:53:51
 */

public class MyHandlerInterceptor implements HandlerInterceptor{
	
	public static final  Logger log = LoggerFactory.getLogger(MyHandlerInterceptor.class);
	
	@Autowired
	private UserService userService;
	
	
	public boolean 	preHandle(HttpServletRequest request, HttpServletResponse response) {
		if( null == userService) {
			System.out.println("siteService is null");
			BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
			userService = (UserService) factory.getBean("userService");
		}
		User user = userService.findUserById(MySysUser.id());
		if(null != user) {
			request.setAttribute("currentUser", user);
			return true;
		}
		return false;
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	
	
}
