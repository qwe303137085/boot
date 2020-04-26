package cn.zilanxuan.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.zilanxuan.modules.system.entity.User;

/**
 * 功能描述：工具类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午10:19:51
 */
public class ToolUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(ToolUtil.class);
	
	/*
	public static void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(Constants.SALT_SIZE);
	}
	
	public static String entryptPassword(String paramStr, String salt) {
		if(StringUtils.isNotEmpty(paramStr)) {
			byte[] saltStr = Encodes.decodeHex(salt);
			byte[] hashPassword = Digests.sha1(paramStr.getBytes(),saltStr,Constants.HASH_INTERATIONS);
			String password = Encodes.encodeHex(hashPassword);
			return password;
		}else {
			return null;
		}
	}
	*/
	
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		LOGGER.info("ip : "+ ip);
		return ip;
	}
	
	/**
	 * 判断请求是否是ajax
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		return accept != null && accept.contains("application/json") || "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}
	
	
	
	public static Map<String,Object> getSourceByIp(String ip){
		String a = RestClientUtil.get("http://ip-api.com/json/"+ip+"?lang=zh-CN");
		Map<String,Object> m = JSON.parseObject(a);
		return m;
	}
	
	
	
	
	
	
	
	/**
	 * 获取操作系统以及浏览器信息
	 * @param request
	 * @return
	 */
	public static Map<String,String> getOsAndBrowserInfo(HttpServletRequest request){
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();
        Map<String,String> map = new HashMap<String,String>();
        String os = "";
        String browser = "";
 
        //=================OS Info=======================
        if (userAgent.toLowerCase().indexOf("windows") >= 0 )
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().indexOf("android") >= 0)
        {
            os = "Android";
        } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        //===============Browser===========================
        if (user.contains("edge"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }
 
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  ||
                (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) ||
                (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            browser = "Netscape-?";
 
        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + IEVersion.substring(0,IEVersion.length() - 1);
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
 
        LOGGER.info( os +" --- "+ browser );
        map.put("os", os);
        map.put("browser", browser);
        return map;
    }
	
	/**
	 * 通过IP地址获取位置
	 * @param ip
	 * @return
	 */
	public Map<String,Object> getAddressByIp(String ip){
		return null;
	}
	
	/**
	 * 通过阿里云接口通过IP查位置
	 * @param ip
	 * @return
	 */
	public Map<String,Object> getAddressFromAliyunByIp(String ip){
		return null;
	}

}
