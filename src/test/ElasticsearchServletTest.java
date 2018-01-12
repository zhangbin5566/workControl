package test;

import net.sf.json.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.open.platform.control.bean.ResultMode;
import com.open.platform.control.util.ExConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ElasticsearchServletTest {
	 public static void main(String[] args) throws IOException {
	        ExecutorService executorService = Executors.newFixedThreadPool(1);
	        for (int i = 0;i<1;i++){
	            try {
	                TimeUnit.MILLISECONDS.sleep(5);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            executorService.execute(new Thread(new ESThread()));
	        }
	        executorService.shutdown();
	    }
}

class ESThread implements Runnable{
    public void run() {
        try{
//            CloseableHttpClient client = HttpClients.createDefault();
//            CloseableHttpClient client = HttpClientFactory.createHttpsClient();
            CloseableHttpClient client = HttpClients.custom().build();
//            HttpPost httpPost = new HttpPost("http://192.168.1.66:8080/intelligent-code/esqueryname.do");
//            HttpPost httpPost = new HttpPost("https://code.jss.com.cn/intelligent-code/esqueryname.do");
//            HttpPost httpPost = new HttpPost("http://192.168.1.13:8080/intelligent-code/esqueryname.do");
//            HttpPost httpPost = new HttpPost("http://192.168.1.66:8080/intelligent-code/esquery2openplatform.do");
            HttpPost httpPost = new HttpPost("http://192.168.210.229:8080/intelligent-code/esquery2openplatform.do");

//          httpPost.setHeader("Accept", "application/json");
//          httpPost.setHeader("Content-type", "application/json");
            long timeStamp = System.currentTimeMillis();
            Map map = new HashMap();
            map.put("service","batchQueryCode");
//            Map param = new HashMap();
            
//            Product p1 = new Product();
//            p1.setNum("111");
//            p1.setName("其它业务费4%");
            Map<String,String> map_s = new HashMap<String,String>();
            map_s.put("u_num", "10");
            map_s.put("u_name", "其它业务费4%");
            map_s.put("u_type", "斤");
            map_s.put("u_category", "1111");
//            p1.setType("KXB");

//            Product p2 = new Product();
//            p2.setNum("112");
//            p2.setName("dfdsfa");
//            p2.setType("KXB");

            List<Map> list = new ArrayList();
            list.add(map_s);
//            list.add(p2);

//            param.put("products",list);
//            param.put("name","打印机");
            map.put("param",list);
            map.put("tax_num","1324");
            map.put("timestamp",System.currentTimeMillis());
//            String json = JSON.toJSONString(map);
            String json = JSONObject.fromObject(map).toString();
            System.out.println(json);
            String s = "{\"param\":[{\"u_category\":\"1111\",\"u_name\":\"实用植物油（13%）\",\"u_num\":\"10\",\"u_type\":\"斤\"},{\"u_category\":\"\",\"u_name\":\"啤酒（更新税目产品）\",\"u_num\":\"11\",\"u_type\":\"\"},{\"u_category\":\"\",\"u_name\":\"应税劳务\",\"u_num\":\"12\",\"u_type\":\"\"}],\"service\":\"batchQueryCode\",\"tax_num\":\"339901999999109\"}";
//            String s = "{\"param\":[{\"u_category\":\"\",\"u_name\":\"应税劳务\",\"u_num\":\"00101\",\"u_type\":\"\"}],\"service\":\"batchQueryCode\",\"tax_num\":\"339900999999003\"}";
//            String s = "{\"name\":\"导热线\",\"service\":\"queryName\",\"tax_num\":\"339900999999003\"}";
//            String s = "{\"name\":\"视同销售\",\"service\":\"queryName\",\"tax_num\":\"56789\"}";

//            //json加密
//            byte[] buff = json.getBytes("UTF-8");
//            String result = CryptUtils.encrypt(Config.MOON,buff);

            String result = json;
           
//            System.out.println(result);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ESPre",java.net.URLEncoder.encode(result, HTTP.UTF_8)));
            httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
//			obj = HttpResponesString(client,httpPost);
            long kkd = System.currentTimeMillis();
			CloseableHttpResponse response = client.execute(httpPost,HttpClientContext.create());
			if(response.getStatusLine().getStatusCode()!=200){
				throw new Exception(ExConstants.http_invoke_Interrupt_Msg);
			}
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, Consts.UTF_8) ;
	        EntityUtils.consume(entity);
	        System.out.println((System.currentTimeMillis()-kkd)/1000);
			System.out.println(result);
			response.close();
//			Object obj = JSONObject.toBean(JSONObject.fromObject(result), ResultMode.class);

			
			
			
			
			
//            StringEntity entity = new StringEntity(result,"UTF-8");
//            httpPost.setEntity(entity);
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            long start = System.currentTimeMillis();
//            CloseableHttpResponse response = client.execute(httpPost);
//            long end = System.currentTimeMillis();
////            System.out.println(end - start);
//            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//            while((line = br.readLine())!=null){
//                sb.append(line);
//            }
//            System.out.println(sb.toString());
            client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
