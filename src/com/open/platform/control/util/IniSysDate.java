package com.open.platform.control.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.open.platform.control.pool.CustomExecutors;

public class IniSysDate {

	private  InputStream in = ScheduleUtil.class.getResourceAsStream("/com/open/platform/control/config/schedule_data.properties");
	public final static ReentrantReadWriteLock sysrwl = new ReentrantReadWriteLock();
	public  static Properties prop = new Properties(); 
	
	public  static BlockingQueue<Thread> queue = null;
	private  ThreadPoolExecutor pool = null;//ReExecutors.newCachedThreadPool(handle_vec);
	public static IniSysDate iniSysData = null;
	
	public static IniSysDate getInstance(){
		sysrwl.readLock().lock();
		if(iniSysData==null){
			sysrwl.readLock().unlock();
			sysrwl.writeLock().lock();
				if(iniSysData==null){
					iniSysData = new IniSysDate();
				}
			sysrwl.readLock().lock();
			sysrwl.writeLock().unlock(); 
		}
		sysrwl.readLock().unlock();
		return iniSysData;
	}
	
	private IniSysDate(){
		try {
			prop.load(in);
			iniQueue();
			iniThreadPool();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 初始化任务队列
	 * @return
	 */
	private  void iniQueue(){
		if(ScheduleConstants.LinkedBlockingDeque.equalsIgnoreCase(prop.getProperty(ScheduleConstants.schedule_queue))){
			queue = new LinkedBlockingDeque<Thread>(Integer.parseInt(prop.getProperty(ScheduleConstants.schedule_queue_size)));
		}else if(ScheduleConstants.RePriorityBlockingQueue.equalsIgnoreCase(prop.getProperty(ScheduleConstants.schedule_queue))){
			
		}
	}
	
	/**
	 * 初始化自定义线程池
	 */
	private  void iniThreadPool(){
		pool = CustomExecutors.newCachedThreadPool(Integer.parseInt(prop.getProperty(ScheduleConstants.schedule_thread_corenum)),
												   Integer.parseInt(prop.getProperty(ScheduleConstants.schedule_thread_maxnum)),
												   queue);
	}
	
	/**
	 * 返回线程池对象
	 * @return
	 */
	public ThreadPoolExecutor getThreadPool(){
		return pool;
	}
}
