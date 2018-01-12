package com.open.platform.control.http;

import java.util.Map;

import javax.annotation.Resource;


/**
 * HTTP同步连接
 *   普通HTTP连接 非连接安全校验等操作
 * @author zhangbin
 *
 */
public class HttpSyncGETConnect {	
	/**
	 * 发送 GET 请求
	 * @param url
	 * @return
	 */
	public Object httpSyncGETRequest(String url){
		HttpConnUtil hutil = new HttpConnUtil();
		return hutil.SendGetSyncRequest(url);
	}
}
