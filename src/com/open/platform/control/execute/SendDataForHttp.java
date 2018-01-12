package com.open.platform.control.execute;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import com.open.platform.control.util.HttpUtil;
import com.open.platform.control.util.ScheduleConstants;

/**
 * 判断调用方式
 * @author zhangbin
 * @version 1.0
 * @since jdk1.6
 * @date 2017-2-9
 * @todo TODO
 */
public class SendDataForHttp {

	/**
	 * 判断发送方式
	 * @return
	 */
	public Object doSendType(String cUrl ,String type,String isbatch,JSONArray json,
					String[] paramArr,String isAsyn,String postKey){
		long t = System.currentTimeMillis();
		Object obj = null;
		Object result = null;
		HttpUtil util = null;
		// HTTP GET 单请求
		if(!StringUtils.isEmpty(type)&&ScheduleConstants.HTTP_GET.equalsIgnoreCase(type)){
			if(ScheduleConstants.oneRequest.equalsIgnoreCase(isbatch)){  //单请求发送模式
				util = new HttpUtil();
				obj = util.httpGetforJSON(json,paramArr);// 包含异常：类型 ResultMode；正常调用：list 0
				if(obj instanceof List){
					List Getdata = (List)obj;
					if(!Getdata.isEmpty()){
						String urlparam = String.valueOf(Getdata.get(0));
						result = util.SendSingleGetRequest(cUrl+"?"+urlparam.substring(1, urlparam.length()),isAsyn);
					}else{//没有入参的API
						result = util.SendSingleGetRequest(cUrl,isAsyn);
					}
				}else{
					return obj;
				}
			}
		}else if(!StringUtils.isEmpty(type)&&ScheduleConstants.HTTP_POST.equalsIgnoreCase(type)){ // HTTP POST 
			util = new HttpUtil();
			result = util.SendSinglePostRequest(cUrl, String.valueOf(json),isAsyn,postKey);
		}
		return result;
	}
}
