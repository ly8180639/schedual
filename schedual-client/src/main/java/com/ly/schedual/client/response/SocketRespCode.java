package com.ly.schedual.client.response;

public interface SocketRespCode {
	 

	public static String SUCCESS_CODE="0000";//请求成功
	
	public static String NOTFOUND_CODE="0404";//请求无法找到
	
	public static String TIMEOUT_CODE="0408";//请求超时
	
	public static String AUTHFAILED_CODE="0401";//校验失败
	
	public static String HTTPERR_CODE="0400";//错误请求
	
	public static String HANDLEERR_CODE="0500";//服务器内部处理失败请求
	
	public static String SOCKETERR_CODE="0503";//Socket错误
	
	public static String NULLPARAM_CODE="04011";//参数为空
	
	public static String PARAMAUTHFAILED_CODE="04012";//参数校验有误
	
	
	public static String SUCCESS_MSG="SUCCESS";
	
	
}
