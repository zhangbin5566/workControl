package com.open.platform.text.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.open.platform.control.server.ScheduleServices;

public class ControlTest extends HttpServlet {

	public ControlTest() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Map<String,String> in_data = new HashMap<String,String>();
//		in_data.put("url", "http://115.236.64.125:8800/DZFPJK/shop/buyer/allow/cxfKp/cxfServerKpOrderSync.action8");
//		in_data.put("callMode", "HTTP/POST");
//		in_data.put("isbatch", "1");
//		in_data.put("data", "[{'key':'value','key1':'value1'}]");
//		in_data.put("params", "");
//		in_data.put("isSync", "0");
//		in_data.put("priority", "0");
//		in_data.put("postKey", "order");
		
//		in_data.put("url", "http://192.168.1.66:8080/intelligent-code/esquery2openplatform.do");
		in_data.put("url", "http://uapi.axnfw.net/Ucenterthread/usercenter/syncByMq/allow/syncMultiUser.do");
		in_data.put("callMode", "HTTP/POST");
		in_data.put("isbatch", "1");
//		in_data.put("data", "[{\"param\":[{\"u_category\":\"1111\",\"u_name\":\"实用植物油（13%）\",\"u_num\":\"10\",\"u_type\":\"斤\"},{\"u_category\":\"\",\"u_name\":\"啤酒（更新税目产品）\",\"u_num\":\"11\",\"u_type\":\"\"},{\"u_category\":\"\",\"u_name\":\"应税劳务\",\"u_num\":\"12\",\"u_type\":\"\"}],\"service\":\"batchQueryCode\",\"tax_num\":\"339901999999109\"}]");
		in_data.put("data", "[{\"CAccountBlank\":\"\",\"CAddress\":\"浙江杭州\",\"CAreacode\":\"\",\"CBankAccount\":\"\",\"CCompanyname\":\"787611\",\"CEmail\":\"\",\"CFaxNum\":\"\",\"CIdnum\":\"\",\"CKpaddr\":\"\",\"CKpcode\":\"\",\"CKpname\":\"\",\"CKptel\":\"\",\"CLike\":\"\",\"CLinkMan\":\"\",\"CMarrystatus\":\"\",\"CMobile\":\"18626856522\",\"COldUserName\":\"339901999999152\",\"CPassword\":\"29114871d3b0d33876e714edc167d457\",\"CPhone\":\"\",\"CRealname\":\"\",\"CRegistertype\":\"1\",\"CServiceKeyId\":\"e33c437f13c14ef99a3931dee0274ead\",\"CServiceTexnum\":\"\",\"CServicearea\":\"\",\"CServiceid\":\"\",\"CServicetaxnum\":\"\",\"CSex\":\"\",\"CSyncstate\":\"0\",\"CSysid\":\"0\",\"CTaxauthorityid\":\"\",\"CTexnum\":\"339901999999152\",\"CUid\":\"bea9d0e4daf5497cbd298380f1831e4e\",\"CUsername\":\"339901999999152\",\"crmNo\":\"1\",\"dataSources\":\"1\",\"dbrsfz\":\"\",\"departId\":\"\",\"dtAdddate\":\"\",\"dtBirthday\":\"\",\"dtEditdate\":\"\",\"frsfz\":\"\",\"incoiceCode\":\"\",\"keyid\":\"50001886746\",\"swdjz\":\"\",\"yyzz\":\"\"}]");
		in_data.put("params", "");
		in_data.put("isSync", "0");
		in_data.put("priority", "0");
//		in_data.put("postKey", "ESPre");
		in_data.put("postKey", "param");
		
		ScheduleServices ss = new ScheduleServices();
		Object obj = ss.execute(in_data);
		out.print(obj);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
