package cn.zilanxuan.aspect;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.zilanxuan.annotation.SysLog;
import cn.zilanxuan.base.MySysUser;
import cn.zilanxuan.modules.system.entity.Log;
import cn.zilanxuan.util.ToolUtil;


/**
 * 日志切面类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月21日 下午2:01:21
 */
@Aspect
@Order(5)
@Component
public class WebLogAspect {

	private ThreadLocal<Long> startTime = new ThreadLocal<>();
	
	//@Autowired
	//private GlobalExceptionHandler exceptionHandler;
	
	private Log sysLog = null;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);
	
	@Pointcut("@annotation(cn.zilanxuan.annotation.SysLog)")
	public void webLog() {
		
	}
	
	/**
	 * 前置通知 ：在方法执行之前执行
	 */
	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) {
		startTime.set(System.currentTimeMillis());
		//接受请求 记录请求的内容
		ServletRequestAttributes attribute = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest 	request = attribute.getRequest();
		HttpSession session = (HttpSession)attribute.resolveReference(RequestAttributes.REFERENCE_SESSION);
		sysLog = new Log();
		sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
		sysLog.setHttpMethod(request.getMethod());
		//传入额参数
		Object[] args = joinPoint.getArgs();
		for(int i=0;i<args.length;i++) {
			Object o = args[i];
			if(o instanceof ServletRequest || o instanceof ServletResponse || o instanceof MultipartFile) {
				args[i] = o.toString();
				
			}
		}
		String str = JSONObject.toJSONString(args);
		sysLog.setParams(str.length()>5000?JSONObject.toJSONString("请求参数数据过长不予显示"):str);
		String ip = ToolUtil.getClientIp(request);
		if("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
			ip = "127.0.0.1";
		}
		sysLog.setRemoteAddr(ip);
		sysLog.setRequestUrl(request.getRequestURL().toString());
		if(null != session) {
			sysLog.setSessionId(session.getId());
		}
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		SysLog mylog = method.getAnnotation(cn.zilanxuan.annotation.SysLog.class);
		if(null != mylog) {
			//设置注解上的描述，为日志标题 比如@SysLog("value")
			sysLog.setTitle(mylog.value());
		}
		Map<String, String> osInfo = ToolUtil.getOsAndBrowserInfo(request);
		sysLog.setBrowser(osInfo.get("os")+"-"+osInfo.get("browser"));
		if(!"127.0.0.1".equals(ip)) { //判断是否是本地，外网IP需要
			Map<String,Object> m = ToolUtil.getSourceByIp(ip);
			sysLog.setArea((String)m.get("regionName")+"-"+ (String)m.get("city"));
			sysLog.setProvince((String)m.get("regionName"));
			sysLog.setCity((String)m.get("city"));
			sysLog.setIsp((String)m.get("isp"));
		}
		sysLog.setType(ToolUtil.isAjax(request)? "ajax请求" : "普通请求");
		if(null != MySysUser.ShiroUser()) {
			sysLog.setUsername(StringUtils.isNotBlank(MySysUser.nickName())?MySysUser.nickName():MySysUser.loginName());
		}
		
	}
	
	/**
	 * 返回通知，在方法返回结果之后执行
	 * @param ret
	 */
	@AfterReturning(returning = "ret" ,pointcut="webLog()")
	public void doAfterReturning(Object ret) {
		if(null != MySysUser.ShiroUser()) {
			sysLog.setUsername(StringUtils.isNotBlank(MySysUser.nickName())?MySysUser.nickName():MySysUser.loginName());
		}
		String retString = JSONObject.toJSONString(ret);
		sysLog.setResponse(retString.length()>5000?JSONObject.toJSONString("响应数据过长不予显示"):retString);
		sysLog.setUseTime(System.currentTimeMillis()-startTime.get());
		sysLog.insert();
	}
	
	
	/**
	 * 环绕通知，围绕着方法执行
	 * @return
	 */
	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		try {
			Object obj = proceedingJoinPoint.proceed();
			return obj;
		}catch(Exception e) {
			e.printStackTrace();
			sysLog.setException(e.getMessage());
			throw e;
		}
	}
	
}
