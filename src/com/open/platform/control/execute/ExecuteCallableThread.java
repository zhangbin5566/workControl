package com.open.platform.control.execute;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;

/**
 * @author zhangbin
 * @version 1.0
 * @since jdk1.6
 * @date 2017-2-8
 * @todo 任务线程模型
 *   当线程池用优先级模式时 ，传入的优先级参数才有意义
 */
public class ExecuteCallableThread extends RunnablePriorityBase implements Callable<Object>,Comparable<RunnablePriorityBase> {

	private int i = 0;
	
	private Map<String,String> in_data = null;
	
	public ExecuteCallableThread(int i,Map<String,String> in_data){
		super(i);
		this.in_data = in_data;
	}
	
	public int compareTo(RunnablePriorityBase o) {
		if(this.i<o.priority){
			return -1;
		}else if(this.i<o.priority){
			return 1;
		}
		return 0;
	}

	public Object call() throws Exception {
		
		String url = in_data.get("url");
		String callMode = in_data.get("callMode");
		String isbatch = in_data.get("isbatch");
		String params = in_data.get("params");
		String JSONData = in_data.get("data");
		String isSync = in_data.get("isSync");
		String postKey = in_data.get("postKey");
		
		String[] paramArr = null;
		JSONArray json = null;
		if(!StringUtils.isEmpty(params)){
			paramArr = params.split(",");
		}
		if(!StringUtils.isEmpty(JSONData)){
			json = JSONArray.fromObject(JSONData); 
		}
		
		SendDataForHttp senData = new SendDataForHttp();
		return senData.doSendType(url, callMode, isbatch, json, paramArr, isSync,postKey);
	}
}

class RunnablePriorityBase {
    protected int priority;
 
    public RunnablePriorityBase(int priority) {
        this.priority = priority;
    }
}