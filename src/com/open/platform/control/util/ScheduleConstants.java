package com.open.platform.control.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zhangbin
 * @version 1.0
 * @since jdk1.6
 * @date 2017-2-8
 * @todo 存放静态数据  包括key值
 */
public class ScheduleConstants {
	//线程池静态参数
	public final static String schedule_queue = "schedule.queue";
	public final static String schedule_queue_size = "schedule.queue.size";
	public final static String schedule_thread_maxnum = "schedule.thread.maxnum";
	public final static String schedule_thread_corenum = "schedule.thread.corenum";
	public final static String schedule_work_waitetime = "schedule.work.waitetime";
	
	public final static String LinkedBlockingDeque = "LinkedBlockingDeque";
	public final static String RePriorityBlockingQueue = "RePriorityBlockingQueue";
	
	
	//HTTP调用静态参数
	public final static String HTTP_GET = "HTTP/GET";
	public final static String HTTP_POST = "HTTP/POST";
	public final static String oneRequest = "1"; //单条数据请求
	public final static String cParams = "params";

	//HTTP池化参数
	public final static int maxTotal = 200;
	public final static int DefaultMaxPerRoute = 200;
	public final static int IOConnectTimeout = 3000;
	public final static int IOSoTimeout = 30000;
	public final static int MaxHeaderCount = 30;
	public final static int MaxLineLength = 2000;
	public final static int ConnectTimeout = 1000;
	public final static int SocketTimeout = 3000;
}
