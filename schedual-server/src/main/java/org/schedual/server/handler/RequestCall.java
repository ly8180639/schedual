package org.schedual.server.handler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.sf.json.JSONObject;

import org.schedual.server.controller.socket.response.SocketRespCode;
import org.schedual.server.controller.socket.response.SocketResponse;
import org.schedual.server.task.CustomRequest;

public class RequestCall {
	
	static ExecutorService threadPool=null;
	
	static{
		threadPool=Executors.newCachedThreadPool();
	}
	/**
	 * 执行socket发过来的同步请求
	 * 它至少包括三部分（以空格隔开） id  timeout url
	 * @param req
	 * @return
	 */
	public static String execute(String req){
		String[] reqParts=req.split("\\s+");
		StringBuilder result=new StringBuilder(reqParts[0]).append(" ");
		JSONObject data=new JSONObject();
		try {
			Integer.parseInt(reqParts[0]);
		} catch (NumberFormatException e) {
			//格式不正确
			data.put(SocketRespCode.HTTPERR_CODE, "id format error");
			return result.append(data.toString()).toString();
		}
		
		long timeout=0;
		try {
			timeout=Long.parseLong(reqParts[1]);
		} catch (NumberFormatException e) {
			data.put(SocketRespCode.HTTPERR_CODE, "timeout format error");
			return result.append(data.toString()).toString();
			//return result.append("timeout format error").toString();
		}
		//1 2000 url?abc test=abc& 
		String url=null;
		if(reqParts[2].indexOf("?")>0){
			url=reqParts[2].substring(0,reqParts[2].indexOf("?"));
		}else{
			url=reqParts[2];
		}
		String param=null;
		if(reqParts.length>3){//优先后面的参数为主
			param=req.substring(req.indexOf(reqParts[2])+reqParts[2].length()+1);
		}else{
			if(reqParts[2].indexOf("?")>0){
				param=reqParts[2].substring(reqParts[2].indexOf("?")+1);	
			}
		}
		Future<SocketResponse> future= threadPool.submit(new CustomRequest(url, param));
		try {
			SocketResponse resp= future.get(timeout, TimeUnit.MILLISECONDS);
			return result.append(JSONObject.fromObject(resp)).toString();
		} catch (InterruptedException | ExecutionException e) {
			data.put(SocketRespCode.SOCKETERR_CODE, "socket eror");
			return result.append(data.toString()).toString();
		} catch (TimeoutException e) {
			data.put(SocketRespCode.TIMEOUT_CODE, "request time out");
			return result.append(data.toString()).toString();
		}
		
	}
}
