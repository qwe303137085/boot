package cn.zilanxuan.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import cn.zilanxuan.util.RestResponse;

/**
 * 功能描述:统一异常处理类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午6:58:16
 */

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Log log = LogFactory.get();
	
	/**
	 * 500 -Bad Request
	 */
	@ExceptionHandler({
		HttpMessageNotReadableException.class,
		HttpRequestMethodNotSupportedException.class,
		HttpMediaTypeNotSupportedException.class,
		SQLException.class
	})
	public void handleHttpMessageNotReadableException(HttpServletRequest request,
														HttpServletResponse response,
														Exception e) {
		RestResponse restResponse = RestResponse.failure(e.getMessage());
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(JSONObject.toJSONString(restResponse));
			pw.flush();
			pw.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
	}
	
	
	
	@ExceptionHandler(value = Exception.class)
    public void exception(HttpServletRequest request,
			HttpServletResponse response,Exception e) {
		RestResponse restResponse = RestResponse.failure(e.getMessage());
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(JSONObject.toJSONString(restResponse));
			pw.flush();
			pw.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
    }

    

    @ExceptionHandler(value = UnauthorizedException.class)
    public void handleUnauthorizedException(HttpServletRequest request,
			HttpServletResponse response,UnauthorizedException e) {
        log.debug("UnauthorizedException", e);
        RestResponse restResponse = RestResponse.failure("您没有权限,请联系管理员");
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(JSONObject.toJSONString(restResponse));
			pw.flush();
			pw.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public void handleAuthenticationException(HttpServletRequest request,
			HttpServletResponse response,AuthenticationException e) {
        log.debug("AuthenticationException", e);
        RestResponse restResponse = RestResponse.failure(e.getMessage());
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(JSONObject.toJSONString(restResponse));
			pw.flush();
			pw.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public void handleAuthorizationException(HttpServletRequest request,
			HttpServletResponse response,AuthorizationException e) {
        log.debug("AuthorizationException", e);
        RestResponse restResponse = RestResponse.failure(e.getMessage());
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(JSONObject.toJSONString(restResponse));
			pw.flush();
			pw.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
    }

    @ExceptionHandler(value = ExpiredSessionException.class)
    public void handleExpiredSessionException(HttpServletRequest request,
			HttpServletResponse response,ExpiredSessionException e) {
        log.debug("ExpiredSessionException", e);
        RestResponse restResponse = RestResponse.failure(e.getMessage());
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(JSONObject.toJSONString(restResponse));
			pw.flush();
			pw.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
    }

}
