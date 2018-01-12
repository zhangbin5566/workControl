package com.open.platform.control.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import net.sf.json.JSONObject;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParser;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
import org.apache.http.impl.nio.conn.ManagedNHttpClientConnectionFactory;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.nio.NHttpMessageParser;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.conn.NHttpConnectionFactory;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.open.platform.control.bean.ResultMode;
import com.open.platform.control.util.ExConstants;
import com.open.platform.control.util.ScheduleConstants;

/**
 * HTTP 调用工具类
 * @author zhangbin
 *
 */
public class HttpConnUtil {

	final static Logger logger = LoggerFactory.getLogger("HttpConnUtil.class");
	public final static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private static PoolingHttpClientConnectionManager poolConnManager = null;
	
	public HttpConnUtil(){
		rwl.readLock().lock();
		if(poolConnManager==null){
			rwl.readLock().unlock();
			rwl.writeLock().lock();
				if(poolConnManager==null){
					poolConnManager = getHttpConnection();
				}
			rwl.readLock().lock();
			rwl.writeLock().unlock(); 
		}
		rwl.readLock().unlock();
	}
	
	/**
	 * 初始化连接线程池<同步>
	 *  获取其中空闲连接
	 * @return
	 */
	public static  PoolingHttpClientConnectionManager getHttpConnection(){
		System.out.println("初始化连接线程池");
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
	         .register("http", plainsf)
	         .build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
	        // 将最大连接数增加到200
	    cm.setMaxTotal(ScheduleConstants.maxTotal);
	        // 将每个路由基础的连接增加到200
	    cm.setDefaultMaxPerRoute(ScheduleConstants.DefaultMaxPerRoute);
        return cm;
	}
	
	//请求重试处理 <同步>
	public static  HttpRequestRetryHandler httpSyncHandler(){
		   //请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
                if (executionCount >= 2) {// 如果已经重试了2次，就放弃          
                	logger.error("重试："+exception.getMessage());
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试           
                	logger.error("连接丢失："+exception.getMessage());
                    return true;
                }
                if (exception instanceof InterruptedIOException) {// 超时         
                	logger.error("连接超时："+exception.getMessage());
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达      
                	logger.error("主机找不到："+exception.getMessage());
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝      
                	logger.error("连接拒绝："+exception.getMessage());
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {   
                	logger.error("如果请求是幂等的，就再次尝试："+exception.getMessage());
                    return true;
                }
                return false;
            }
        };  
        return httpRequestRetryHandler;
	}
	
	/**
	 * 创建连接池<异步>
	 * @return
	 */
	public static PoolingNHttpClientConnectionManager getPoolconnectManager(){
		
		 NHttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
		 NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory = new ManagedNHttpClientConnectionFactory(
	                requestWriterFactory, getMessageFactory(), HeapByteBufferAllocator.INSTANCE);
		 Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
			         .register("http", NoopIOSessionStrategy.INSTANCE)
			         .build();
		 PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(
				 getIOconfig(), connFactory, sessionStrategyRegistry );
		 connManager.setDefaultConnectionConfig(getConnectConfig());
		  /**
         	为持续连接配置总最大或每个路由限制
			可以保存在池中或由连接管理器租用。
	      */
         connManager.setMaxTotal(ScheduleConstants.maxTotal);
         connManager.setDefaultMaxPerRoute(ScheduleConstants.DefaultMaxPerRoute);
	     return  connManager;
	}
	
	/**
	 * 使用自定义消息解析器
	 * @return
	 */
	public static NHttpMessageParserFactory<HttpResponse> getMessageFactory(){
        NHttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public NHttpMessageParser<HttpResponse> create(
                    final SessionInputBuffer buffer,
                    final MessageConstraints constraints) {
                    LineParser lineParser = new BasicLineParser() {
	                    @Override
	                    public Header parseHeader(final CharArrayBuffer buffer) {
	                        try {
	                            return super.parseHeader(buffer);
	                        } catch (ParseException ex) {
	                            return new BasicHeader(buffer.toString(), null);
	                        }
	                    }
                   };
                return new DefaultHttpResponseParser(
                        buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints);
            }
        };
        return responseParserFactory;
	}
	
	/**
	 * 创建I / O反应器配置
	 * @return
	 */
	public static ConnectingIOReactor getIOconfig(){
		 ConnectingIOReactor ioReactor = null;
		 IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
		         .setIoThreadCount(Runtime.getRuntime().availableProcessors())//硬件处理器个数  不可靠
		         .setConnectTimeout(ScheduleConstants.IOConnectTimeout)
		         .setSoTimeout(ScheduleConstants.IOSoTimeout)
		         .build();
		 //创建自定义I / O反应器
	     try {
	    	 ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
		} catch (IOReactorException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return ioReactor;
	}
	
	/**
	 * 连接配置
	 * @return
	 */
	public static  ConnectionConfig getConnectConfig(){
		  //创建消息约束
        MessageConstraints messageConstraints = MessageConstraints.custom()
            .setMaxHeaderCount(ScheduleConstants.MaxHeaderCount)
            .setMaxLineLength(ScheduleConstants.MaxLineLength)
            .build();
        //创建连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)  //格式错误的输入操作
            .setUnmappableInputAction(CodingErrorAction.IGNORE) //不可映射的输入操作
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        return connectionConfig;
	}
	
	/**
	 * 初始化GET连接
	 * @return
	 */
	public HttpGet HttpGETConnect(String url){
		HttpGet hget = new HttpGet(url);
		 // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(ScheduleConstants.ConnectTimeout)
                .setSocketTimeout(ScheduleConstants.SocketTimeout)
                .build();
        hget.setConfig(requestConfig);  
        return hget;
	}
	
	/**
	 * 初始化Post连接
	 * @param url
	 * @return
	 */
	public HttpPost HttpPostConnect(String url){
		HttpPost hpost = new HttpPost(url);
		 // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(ScheduleConstants.ConnectTimeout)
                .setSocketTimeout(ScheduleConstants.SocketTimeout)
                .build();
        hpost.setConfig(requestConfig);
        return hpost;
	}
	
	/**
	 * 获取GET 返回业务结果
	 * @param clinet
	 * @param hget
	 * @return
	 */
	public Object HttpResponesString(CloseableHttpClient clinet,HttpGet hget){
		HttpGetResponse hr = null;
		String res = "";
		try {
			hr = new HttpGetResponse(clinet,hget);
			res = String.valueOf(hr.call());
		} catch (Exception e) {
			logger.error(e.getMessage());
			ResultMode result = new ResultMode();
	    	result.setCode(ExConstants.http_invoke_Interrupt);
	    	result.setDescribe(ExConstants.http_invoke_Interrupt_Msg);
	    	return result;
		}finally{
			try {
				clinet.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	/**
	 * 获取POST 返回业务结果
	 * @param clinet
	 * @param hpost
	 * @return
	 */
	public Object HttpResponesString(CloseableHttpClient clinet,HttpPost hpost){
//		List<NameValuePair> params=new ArrayList<NameValuePair>();
//		//建立一个NameValuePair数组，用于存储欲传送的参数
//		params.add(new BasicNameValuePair("pwd","2544"));
		HttpPostResponse hr = null;
		String obj = null;
		try {
			hr = new HttpPostResponse(clinet,hpost);
			obj = String.valueOf(hr.call());
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			ResultMode result = new ResultMode();
	    	result.setCode(ExConstants.http_invoke_Interrupt);
	    	result.setDescribe(ExConstants.http_invoke_Interrupt_Msg);
	    	return result;
		}finally{
			try {
				clinet.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	
	/**
	 * 发送HTTP GET请求 <同步>
	 * @return
	 */
	public Object SendGetSyncRequest(String url){
		logger.info("同步 get url:"+url);
		HttpGet geth = HttpGETConnect(url);
		CloseableHttpClient httpClient = HttpClients.custom()
						        .setConnectionManager(getHttpConnection())
						        .setRetryHandler(httpSyncHandler())
						        .build();
		Object obj = HttpResponesString(httpClient, geth);
		return obj;
	}
	
	/**
	 * 发送HTTP POST请求 <同步>
	 * @param url
	 * @param param
	 * @return
	 */
	public Object SendPostSyncRequest(String url,String param,String postKey){
		logger.info("同步 post url:"+url);
		HttpPost hpost = HttpPostConnect(url);
		CloseableHttpClient httpClient = HttpClients.custom()
									        .setConnectionManager(getHttpConnection())
									        .setRetryHandler(httpSyncHandler())
									        .build();
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		Object obj = null;
		try {
			params.add(new BasicNameValuePair(postKey,java.net.URLEncoder.encode(param, HTTP.UTF_8)));
			hpost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			obj = HttpResponesString(httpClient,hpost);
			obj = JSONObject.toBean(JSONObject.fromObject(obj), ResultMode.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ResultMode result = new ResultMode();
	    	result.setCode(ExConstants.code_Type);
	    	result.setDescribe(ExConstants.code_Msg1);
	    	return result;
		} catch (Exception e) {
			e.printStackTrace();
			ResultMode result = new ResultMode();
	    	result.setCode(ExConstants.input_Data_Type);
	    	result.setDescribe(ExConstants.input_Data_param_Msg8);
	    	return result;
		}
		return obj;
	}
	
	
	/**
	 * http 异步调用入口
	 * @param url
	 * @return
	 */
	public Object SendGetAsyncRequest(String url){
		logger.info("异步 url:"+url);
		CloseableHttpAsyncClient Asclient = getAsyncHTTPClient();
		HttpGet hget = HttpGETConnect(url);
		Asclient.start();
		Future<HttpResponse> future = null;
		HttpResponse response;
		String result = "";
		try {
			future = Asclient.execute(hget, null);
			response = future.get();
			if(response.getStatusLine().getStatusCode()!=200){
				throw new Exception();
			}
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, Consts.UTF_8);
            EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			ResultMode eresult = new ResultMode();
	    	eresult.setCode(ExConstants.http_invoke_Interrupt);
	    	eresult.setDescribe(ExConstants.http_invoke_Interrupt_Msg);
	    	return eresult;
		} finally{
			try {
				Asclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
       return result;
	}
	/**
	 * 获取异步客户端
	 * @return
	 */
	public CloseableHttpAsyncClient getAsyncHTTPClient(){
		 CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
			         .setConnectionManager(getPoolconnectManager())
			         .build();
		 return httpclient;
	}
}
