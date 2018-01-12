package com.open.platform.control.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 执行GET http发送 返回数据
 * @author zhangbin
 *
 */
public class HttpGetResponse implements Callable{
	
	private CloseableHttpClient clinet;
	private HttpGet hget;
	
	public HttpGetResponse(CloseableHttpClient clinet,HttpGet hget){
		this.clinet = clinet;
		this.hget = hget;
	}

	public Object call() throws Exception {
		String result = "";
//		long t = System.currentTimeMillis();
		CloseableHttpResponse response = clinet.execute(this.hget,HttpClientContext.create());
		if(response.getStatusLine().getStatusCode()!=200){
			throw new Exception();
		}
//		t = System.currentTimeMillis()-t;
		HttpEntity entity = response.getEntity();
		result = EntityUtils.toString(entity, Consts.UTF_8) ;
        EntityUtils.consume(entity);
		return result;
	}
}
