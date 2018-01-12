package com.open.platform.control.util;

/**
 * 异常静态数据
 * @author zhangbin
 *
 */
public class ExConstants {
	
	public final static String  success_Code = "0000";
	public final static String  success_Msg = "请求成功";
	
	public final static String  unknow_Code = "-1";
	public final static String  unknow_Msg = "未知异常";
	
	public final static String code_Type = "05";
	public final static String code_Msg = "请求编码异常";
	public final static String code_Msg1 = "服务端post请求编码异常";
	
	public final static String	read_Http_Stream = "060101";
	public final static String	read_Http_Stream_Msg = "读取HTTP请求流异常";
	
	public final static String security_Decryption = "0701";
	public final static String security_Decryption_Msg = "解密异常";
	public final static String security_Decryption_Msg1 = "加密异常";
	
	public final static String security_Decryption_keyNull = "070101";
	public final static String security_Decryption_keyNull_Msg = "密钥空";
	public final static String security_Decryption_keyNull_Msg1 = "获取app 密钥失败：appkey 或无效";
	
	public final static String security_Decryption_keyShort = "070102";
	public final static String security_Decryption_keyShort_Msg = "密钥长度不够";
	
	public final static String security_Decryption_arithmeticNULL = "070103";
	public final static String security_Decryption_arithmeticNULL_Msg = "无法识别加密算法";
	
	public final static String security_Day_Call = "070201";
	public final static String security_Day_Call_Msg = "超出日点击量";
	
	public final static String security_Rate_Call = "070202";
	public final static String security_Rate_Call_Msg = "超出调用频率";
	
	public final static String security_Reject_Request = "070203";
	public final static String security_Reject_Request_Msg = "服务器忙";
	
	public final static String security_token_Exception = "070301";
	public final static String security_token_Exception_Msg = "accessToken不匹配/或appKey不匹配";
	
	public final static String security_Resource_authNull = "070302"; 
	public final static String security_Resource_authNull_Msg = "APP未申请该资源"; 
	
	public final static String access_platform_handle = "0104";
	public final static String access_platform_handle_Msg = "获取平台数据异常";
	
	public final static String input_Data_Null = "0101";
	public final static String input_Data_Null_Msg = "入参为空";
	public final static String input_Data_Null_Msg1 = "app key不存在";
	
	public final static String input_API_Null = "010403";
	public final static String input_API_Null_Msg = "API版本不符";
	
	public final static String input_Data_Type = "0201";
	public final static String input_Data_Type_Msg = "输入参数格式有误";
	public final static String input_Data_Type_Msg1 = "必填项输入参数空";
	public final static String input_Data_Type_Msg2 = "请求方法不存在";
	public final static String input_Data_Type_Msg3 = "客户端 传输格式与解析格式描述不相符";
	public final static String input_Data_param_Msg4 = "字段缺失";
	public final static String input_Data_param_Msg5 = "字段缺失(非必输字段)";
	public final static String input_Data_param_Msg6 = "包含多条请求";
	public final static String input_Data_param_Msg7 = "指定请求格式无法识别";
	public final static String input_Data_param_Msg8 = "API侧返回数据格式有误";
	
	public final static String db_Execute = "0302";
	public final static String db_Execute_Msg = "数据库执行异常";
	
	public final static String http_invoke_Interrupt = "040102";
	public final static String http_invoke_Interrupt_Msg = "API服务异常";
	
	public final static String data_Compress_Null = "0704";
	public final static String data_Compress_Null_Msg = "解压异常";
	public final static String data_Compress_Null_Msg1 = "压缩方式无法识别";
	
	public final static String pool_full_RefuseException = "080101";
	public final static String pool_full_RefuseException_Msg = "线程池满负载拒绝";
	
	public final static String pool_collapse_Exception = "0802";
	public final static String pool_collapse_Exception_Msg = "线程池奔溃";
	
}
