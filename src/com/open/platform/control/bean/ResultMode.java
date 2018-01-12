package com.open.platform.control.bean;

/**
 * 返回数据类型
 * @author zhangbin
 *
 */
public class ResultMode {

	/**
	 * 返回码
	 */
	private String code;
	
	/**
	 * 描述
	 */
	private String describe;
	
	/**
	 * 返回值
	 */
	private Object result;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
