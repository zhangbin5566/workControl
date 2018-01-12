package com.open.platform.control.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

import com.open.platform.control.bean.ResultMode;
import com.open.platform.control.execute.ExecuteCallableThread;
import com.open.platform.control.pool.CustomExecutors;

/**
 * @author zhangbin
 * @version 1.0
 * @since jdk1.6
 * @date 2017-2-8
 * @todo 调度模块工具类
 */
public class ScheduleUtil {
	
	private static int poolsize = Integer.parseInt(IniSysDate.prop.getProperty(ScheduleConstants.schedule_thread_maxnum));
	
	private static int queuesize = Integer.parseInt(IniSysDate.prop.getProperty(ScheduleConstants.schedule_queue_size));
	
	private static long waitetime = Long.parseLong(IniSysDate.prop.getProperty(ScheduleConstants.schedule_work_waitetime));

	final static Logger logger = LoggerFactory.getLogger("ScheduleUtil.class");
	/**
     * 返回异常数据结构
     * @return
     */
    public ResultMode exceptionSYSResult(String code,String describe){
    	ResultMode result = new ResultMode();
    	result.setCode(code);
    	result.setDescribe(describe);
    	return result;
    }
    
    /**
     * 重入线程池 
     * @param poolSize   当前线程池任务队列大小
     * @param activeThread   当前线程池执行线程大小
     * @param callThread   任务
     * @return
     */
    public Object reentrantPool(int poolSize,int activeThread,ExecuteCallableThread callThread,Exception e){
    	
    	Future<Object> result = null;
    	Object backMsg = null ;
    	if(poolsize<=activeThread&&queuesize==poolSize){  //资源已满
    		try {
				Thread.sleep(waitetime);
				result = IniSysDate.getInstance().getThreadPool().submit(callThread);
				backMsg = result.get();
			}  catch (Exception e1) { //记录日志
				ResultMode rm = new ResultMode();
	    		rm.setCode(ExConstants.pool_full_RefuseException);
	    		rm.setDescribe(ExConstants.pool_full_RefuseException_Msg);
	    		rm.setResult(null);
	    		backMsg = rm;
				e1.printStackTrace();
				logger.error(getExceptionString(e1));
			}
    	}else{//记录日志
    		
    		ResultMode rm = new ResultMode();
    		rm.setCode(ExConstants.pool_collapse_Exception);
    		rm.setDescribe(ExConstants.pool_collapse_Exception_Msg);
    		rm.setResult(null);
    		backMsg = rm;
    		logger.error(getExceptionString(e)+" poolSize:"+poolSize+"poolsize:"+poolsize+"  activeThread:"+activeThread+" queuesize:"+queuesize);
    	}
    	return backMsg;
    }
    
	/**
	 * 打印异常
	 * @param t
	 * @return
	 */
	public  String getExceptionString(Throwable t){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String str = "";
		try {
			t.printStackTrace(pw);
			str = sw.toString();
		} catch (Exception e) {
			logger.error(this.getExceptionString(e));
		}finally{
			try {
				sw.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
}
