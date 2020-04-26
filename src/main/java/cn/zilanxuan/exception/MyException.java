package cn.zilanxuan.exception;

/**
 * 功能描述:自定义异常处理
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月8日 下午6:57:09
 */
public class MyException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String msg;
	
	private Integer code;
	
	public MyException() {
		this.code= 500;
	}
	
	public MyException(String msg, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
	}
	
	
	public MyException(String msg, Integer code) {
		super(msg);
		this.code = code;
	}
	
	public MyException(String msg, Integer code, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
		this.code = code;
		
	}
	
	public MyException(String msg) {
		this.code = 500;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	
}
