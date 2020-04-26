package cn.zilanxuan.modules.system.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import cn.zilanxuan.annotation.SysLog;
import cn.zilanxuan.config.ShiroConfig;
import cn.zilanxuan.util.BaiduAiUtil;
import cn.zilanxuan.util.Constants;
import cn.zilanxuan.util.RestResponse;
import cn.zilanxuan.util.VerifyCodeUtil;

/**
 * 功能描述：用户登陆
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月11日 上午10:18:44
 */
@Controller
@RequestMapping("/api")
public class LoginController {

	private Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Value("${server.port}")
	private String port;
	
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/genCaptcha")
	public void genCaptcha(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
		//将验证码放到requestSession中
		request.getSession().setAttribute(Constants.VALIDATE_CODE, verifyCode);
		log.info("本次生成的验证码是 " + verifyCode + ",已经存储到HttpSession中");
		//设置输出的内容为JEPG格式
		response.setContentType("image/jpeg");
		BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode,116,36,5,true,new Color(249,205,173),null,null);
		//返回给浏览器验证码图片
		ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
	}
	
	/**
	 * 语音读取接口
	 */
	@ResponseBody
	@RequestMapping("/getCaptchaVoice")
	public RestResponse getCaptchaVoice(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(null == session) {
			return RestResponse.failure("session过期");
		}
		String captcha = (String)session.getAttribute(Constants.VALIDATE_CODE);
		if(StringUtils.isBlank(captcha)) {
			return RestResponse.failure("session过期");
		}
		log.info("语音读取接口从HttpSession中获取到验证码："+captcha);
		
		//根据验证码生成语音  百度ai文字转语音接口
		HashMap<String,Object> options = new HashMap<>(); 
		options.put("spd", 1); //语速
		options.put("pit", 5); //音调  默认5为中速
		options.put("per", "0"); //女声
		String vocie = BaiduAiUtil.textToSpeech(captcha, options);
		return RestResponse.success().setData(vocie);
		
		
	}
	
	/**
	 * 登陆功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login/main",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	@SysLog("用户登陆")
	public RestResponse loginmain(HttpServletRequest request) {
		log.info("登陆。。。");
		//用户名
		String username = request.getParameter("username");
		//密码
		String password = request.getParameter("password");
		//记住我
		String rememberMe = request.getParameter("rememberMe");
		//验证码
		String code = request.getParameter("code");
		
		Boolean isVoice = "true".equals(request.getParameter("isVoice"));
		String error = null;
		
		String textMsg = "欢迎进入紫兰轩";
		
		Map<String,Object> map = Maps.newHashMap();
		
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return RestResponse.failure("用户名或密码不能为空");
		}
		if(StringUtils.isBlank(rememberMe)) {
			return RestResponse.failure("记住我不能为空");
		}
		if(StringUtils.isBlank(code)) {
			return RestResponse.failure("验证码不能为空");
		}
		
		HttpSession session = request.getSession();
		if(null == session) {
			return RestResponse.failure("session超时");
		}
		
		String truecode = (String)session.getAttribute(Constants.VALIDATE_CODE);
		if(StringUtils.isBlank(truecode)) {
			return RestResponse.failure("验证码超时");
		}
		
		if(!code.toLowerCase().equals(truecode.toLowerCase())) {
			error = "验证码错误";
		}else {
			Subject user = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username,password,Boolean.valueOf(rememberMe));
			try {
				user.login(token);
				if(user.isAuthenticated()) {
					map.put("user",user.getPrincipal());
				}
			}catch(IncorrectCredentialsException e1) {
				error = "登陆密码错误";
			}catch(ExcessiveAttemptsException e1) {
				error = "登陆失败次数过多";
			}catch(LockedAccountException e1) {
				error = "账号已被锁定";
			}catch(DisabledAccountException e1) {
				error = "账号已被禁用";
			}catch(ExpiredCredentialsException e1) {
				error = "账号已过期";
			}catch(UnknownAccountException e1) {
				error = "账号不存在";
			}catch(UnauthorizedException e1) {
				error = "您没有得到相应的授权!";
			}
			
		}
		
		//根据开关控制
		HashMap<String,Object> options = new HashMap<String,Object>();
		options.put("spd", 5); //语速
		options.put("pit", 5); //音调  默认5为中速
		options.put("per", "0"); //女声
		request.getSession().setAttribute(Constants.ISVOICE, isVoice);
		String result = null;
		if(StringUtils.isBlank(error)) {
			if(isVoice) {
				result = BaiduAiUtil.textToSpeech(textMsg, options);
			}
			map.put("voice", result);
			return RestResponse.success("登陆成功").setData(map);
		}else {
			if(isVoice) {
				result = BaiduAiUtil.textToSpeech(error, options);
			}
			map.put("voice", result);
			return RestResponse.failure(error).setData(map);
		}
		
		
		
	}
	
	/**
	 * 退出系统
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/systemLogout")
	@SysLog("退出系统")
	public RestResponse systemLogout() {
		log.info("用户退出系统");
		SecurityUtils.getSubject().logout();
		return RestResponse.success("退出成功").setCode(0);
		
	}
	
}
