package cn.zilanxuan.base;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.zilanxuan.util.Constants;
import cn.zilanxuan.util.RestResponse;

/**
 * 功能描述：自定义表单的拦截器
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月10日 上午10:44:00
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter{

	public static final  Logger log = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);

	/**
	 * 功能描述
	 * 解决跨域问题-放行options
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", Constants.ALLOWEDORIGINS);
		res.setHeader("Access-Control-Allow-Credentials","true");
		if(request instanceof HttpServletRequest) {
			if(((HttpServletRequest)request).getMethod().toUpperCase().equals("OPTIONS")){
				res.addHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH");
				res.addHeader("Access-Control-Allow-Headers", "Content-Type,Accept,Authorization");
				res.setStatus(HttpServletResponse.SC_OK);
				res.setCharacterEncoding("UTF-8");
				return true;
			}
		}
		return super.isAccessAllowed(request, response, mappedValue);
	}

	
	/**
	 * 功能描述：用户未登陆返回信息,不进行页面的跳转
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", Constants.ALLOWEDORIGINS);
		res.setStatus(HttpServletResponse.SC_OK);
		res.setCharacterEncoding("UTF-8");
		RestResponse restResponse = RestResponse.failure("未登陆").setCode(-1);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter pw = res.getWriter();
		System.out.println(restResponse.toString()+"----");
		pw.write(JSONObject.toJSONString(restResponse));
		pw.close();
		return false;
	}


	
	
	
	
	
	
	
	
}
