package cn.zilanxuan.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 功能描述：SHA-1/MD5加密工具类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午10:23:45
 */
public class Digests {

	private static final String SHA1 = "SHA-1";
	
	private static final String MD5 = "MD5";
	
	private static SecureRandom random = new SecureRandom();
	
	/*
	 * 对输入字符串进行md5散列
	 */
	/*
	public static byte[] md5(byte[] input) {return digest(input,MD5,null,1);}
	public static byte[] md5(byte[] input, int iterations) {return digest(input,MD5,null,iterations);}
	*/
	
	/*
	 * 对字符串进行sha1散列
	 */
	/*
	public static byte[] sha1(byte[] input) {return digest(input,SHA1,null,1);}
	public static byte[] sha1(byte[] input, byte[] salt) {return digest(input,SHA1,salt);}
	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input,SHA1,salt,iterations);
	}
	*/
	
	/*
	 * 对字符串进行散列，支持md5和sha1算法
	 */
	/*
	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			if(salt != null) {
				digest.update(salt);
			}
			byte[] result = digest.digest(input);
			for(int i=1; i<iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		}catch(GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}
	*/
	
}
