package com.open.platform.control.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.open.platform.control.http.HttpConnUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class HttpUtil {

	private ScheduleUtil util = new ScheduleUtil();
	
	/**
	 * 组 http get方式发送参数
	 *    正常返回 ：List<String> 
	 *    异常返回 ：ResultMode
	 * @param json
	 * @param paramArr
	 * @return
	 */
	public  Object httpGetforJSON(JSONArray json,String[] paramArr){
		
		int plength = paramArr.length;
		List<String> list = new ArrayList<String>();
		if(json==null){//入参为空
			return list;
		}
		int length = json.size();
		JSONObject data = null;
		for(int i = 0;i<length;i++){
			data = json.getJSONObject(i);
			StringBuilder sb = new StringBuilder();
			for(int j = 0;j<plength;j++){
				if(data.containsKey(paramArr[j])){ //请求字段包含 API注册的字段
					if(!StringUtils.isEmpty(paramArr[j])){ //过滤无入参API
						sb.append("&").append(paramArr[j]).append("=").append(data.get(paramArr[j])); //组url参数
					}
				}else{//请求字段不包含包含 API注册的字段<非校验字段缺失>
					return util.exceptionSYSResult(ExConstants.input_Data_Type, ExConstants.input_Data_param_Msg5);
				}
			}
			list.add(sb.toString());
		}
		return list;
	}
	
	/**
	 * 单请求
	 *    HTTP GET 方式
	 * @param listRequest
	 * @return
	 */
	public Object SendSingleGetRequest(String url,String isSync){
		long t = System.currentTimeMillis();
		Object result = null;
		HttpConnUtil hutil = new HttpConnUtil();
		if(!StringUtils.isEmpty(url)){
			if("0".equals(isSync)){//同步调用
				result = hutil.SendGetSyncRequest(url);
			}else if("1".equals(isSync)){//异步调用
				result = hutil.SendGetAsyncRequest(url);
			}
		}
		System.out.println("HttpUtil:"+" time:"+(System.currentTimeMillis()-t));
		return result;
	}
	
	/**
	 * 单请求
	 *   HTTP POST 方式
	 * @param url
	 * @param param
	 * @param isSync
	 * @return。
	 */
	public Object SendSinglePostRequest(String url,String param,String isSync,String postKey){
		Object result = null;
		HttpConnUtil hutil = new HttpConnUtil();
		if(!StringUtils.isEmpty(url)){
			if("0".equals(isSync)){//同步调用
				result = hutil.SendPostSyncRequest(url, param,postKey);
			}
//			else if("1".equals(isSync)){//异步调用
//				result = hutil.SendGetAsyncRequest(url);
//			}
		}
		return result;
	}
}
