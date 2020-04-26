package cn.zilanxuan.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述:关于异常的工具类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午10:57:28
 */
public class Exceptions {

	
	/**
	 * 该方法由问题
	 */
	public static Throwable unchecked(Exception e) {
		return null;
	}
	
	
	
	/**
	 * 将ErrorStack转化String
	 * @param e
	 * @return
	 */
	public static String getStackTraceAsString(Throwable e) {
		if(e == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	
	/**
	 * 判断异常是否由某些底层的异常引起
	 * @param ex
	 * @param causeExceptionClasses
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex,Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while(cause != null) {
			for(Class<? extends Exception> causeClass : causeExceptionClasses) {
				if(causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
	
	
	/**
	 * 在request中获取异常类
	 * @param request
	 * @return
	 */
	public static Throwable getThrowable(HttpServletRequest request) {
		Throwable ex = null;
		if(request.getAttribute("exception") != null) {
			ex = (Throwable)request.getAttribute("exception");
		}else if(request.getAttribute("javax.servlet.error.exception") != null) {
			ex = (Throwable)request.getAttribute("javax.servlet.error.exception");
		}
		return ex;
	}
}
