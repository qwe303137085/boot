package cn.zilanxuan.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import com.alibaba.fastjson.JSONObject;

import cn.zilanxuan.util.Constants;
import cn.zilanxuan.util.RestResponse;

public class ShiroRolesAuthorizationFilter extends RolesAuthorizationFilter{

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", Constants.ALLOWEDORIGINS);
		res.setStatus(HttpServletResponse.SC_OK);
		res.setCharacterEncoding("UTF-8");
		RestResponse restResponse = RestResponse.failure("您没有权限，请联系管理员放行").setCode(-1);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter pw = res.getWriter();
		System.out.println(restResponse.toString()+"----");
		pw.write(JSONObject.toJSONString(restResponse));
		pw.close();
		return false;
	}
	
	

}
