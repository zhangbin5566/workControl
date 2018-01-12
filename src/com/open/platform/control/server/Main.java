package com.open.platform.control.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.open.platform.control.bean.ResultMode;
import com.open.platform.control.http.HttpConnUtil;
import com.open.platform.control.pool.CustomExecutors;
import com.open.platform.control.util.IniSysDate;

import net.sf.json.JSONObject;

public class Main {

	final static Logger logger = LoggerFactory.getLogger("Main.class");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		for(int i = 0;i<50;i++){
//			new Thread(new Runnable() {
//				public void run() {
//					Map<String,String> in_data = new HashMap<String,String>();
//					in_data.put("url", "http://192.168.210.119:8677/portal/allow/qrbarcodes/findTzUserinfo.action");
//					in_data.put("callMode", "HTTP/GET");
//					in_data.put("isbatch", "1");
//					in_data.put("data", "[{\"code\":\"59CY8F\",\"validator\":\"348142\",\"signid\":\"9cdf03113ea3a9420cf068df0ad9b5e5\"}]");
//					in_data.put("params", "code,validator,signid");
//					in_data.put("isSync", "0");
//					in_data.put("priority", "0");
//					ScheduleServices ss = new ScheduleServices();
//					long t = System.currentTimeMillis();
//					Object result = ss.execute(in_data);
//					if(result instanceof ResultMode){
//						logger.error("result:"+JSONObject.fromObject(result)+" time:"+(System.currentTimeMillis()-t));
//
//					}else{
//						logger.error("result:"+result.toString()+" time:"+(System.currentTimeMillis()-t));
//					}
//				}
//			}).start();
//		}

		
		for(int i=0;i<1000;i++){
			new Thread(new Runnable() {
				public void run() {
					long p = System.currentTimeMillis();
					HttpConnUtil hu = new HttpConnUtil();
					System.out.println(System.currentTimeMillis()-p);
				}
			}).start();
			
		}
		
	}

}
