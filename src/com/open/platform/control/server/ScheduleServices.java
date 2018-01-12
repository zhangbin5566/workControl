package com.open.platform.control.server;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.open.platform.control.execute.ExecuteCallableThread;
import com.open.platform.control.pool.CustomExecutors;
import com.open.platform.control.util.IniSysDate;
import com.open.platform.control.util.ScheduleUtil;

/**
 * 
 * @author zhangbin
 * @version 1.0
 * @since jdk1.6
 * @date 2017-2-8
 * @todo 调度服务总入口
 *       调度数据类型与开放平台数据模型一致
 */
public class ScheduleServices {

	final static Logger logger = LoggerFactory.getLogger("ScheduleServices.class");
	/**
	 * 执行任务
	 *    同步方式
	 * @param in_data
	 *    类型：Map<String,String>，包括：
	 *    url   API 地址
	 *    callMode  调用协议：HTTP/GET,HTTP/POST
	 *    isbatch   是否批量：0：是；1：否；
	 *    data,     入参数据：JSON    XML入参会转成JSON
	 *    params  HTTP/GET 方式需要解析其中的入参项
	 *    isSync    是否同步调用：0：是，1：否；
	 *    priority  优先级：只区分普通，紧急两种：0：普通，1：紧急
	 * @return
	 */
	public Object execute(Map<String,String> in_data){
		int priority = Integer.parseInt(in_data.get("priority"));
		ExecuteCallableThread callThread = new ExecuteCallableThread(priority,in_data);
		Future<Object> future = null;
		Object result = null;
		try {
//			long t = System.currentTimeMillis();
			future =  IniSysDate.getInstance().getThreadPool().submit(callThread);
			result = future.get();
//			System.out.println(" time:"+(System.currentTimeMillis()-t));
		} catch (InterruptedException e) {
			ScheduleUtil su = new ScheduleUtil();
			logger.error(su.getExceptionString(e));
		} catch (ExecutionException e) {
			ScheduleUtil su = new ScheduleUtil();
			logger.error(su.getExceptionString(e));
		} catch (Exception e){
			//任务队列满了
			ScheduleUtil su = new ScheduleUtil();
			result = su.reentrantPool(IniSysDate.queue.size(), CustomExecutors.re_thread.getActiveCount(), callThread, e);
			e.printStackTrace();
		}
		return result;
	}
	
}
