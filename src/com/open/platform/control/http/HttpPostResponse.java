package com.open.platform.control.http;

import java.util.concurrent.Callable;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.open.platform.control.util.ExConstants;

/**
 * post 调用 返回结果
 * @author open.jss.com
 * @version 1.0
 * @since jdk1.6
 */
public class HttpPostResponse implements Callable {

	final static Logger logger = LoggerFactory.getLogger("HttpPostResponse.class");
	private CloseableHttpClient clinet;
	private HttpPost hpost;
	
	public HttpPostResponse(CloseableHttpClient clinet,HttpPost hpost){
		this.clinet = clinet;
		this.hpost = hpost;
	}

	public Object call() throws Exception {
		String result = "";
		CloseableHttpResponse response = clinet.execute(this.hpost,HttpClientContext.create());
		if(response.getStatusLine().getStatusCode()!=200){
			logger.error("http 异常编码："+response.getStatusLine().getStatusCode());
			throw new Exception(ExConstants.http_invoke_Interrupt_Msg);
		}
		HttpEntity entity = response.getEntity();
		result = EntityUtils.toString(entity, Consts.UTF_8) ;
        EntityUtils.consume(entity);
		return result;
	}
	
}
