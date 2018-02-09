package org.schedual.server.task;

import java.util.concurrent.Callable;

import org.schedual.server.controller.socket.response.SocketResponse;
import org.schedual.server.handler.ReqHandlerInit;

public class CustomRequest implements Callable<SocketResponse>{
	//不带问号的url
	String url;
	
	String param;
	public CustomRequest(String url,String param) {
		// TODO Auto-generated constructor stub
		this.url=url;
		this.param=param;
	}
	@Override
	public SocketResponse call() throws Exception {
		/*Object result=ReqHandlerManager.invoke(url, param);
		if(!(result instanceof SocketResponse)) return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"server method returntype exception");*/
		return ReqHandlerInit.invoke(url, param);
	}

}
