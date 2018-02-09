package org.schedual.sample;

import java.net.Socket;

import com.ly.schedual.client.MessageListner;

public class MessageConsumeImpl implements MessageListner{

	@Override
	public void onConnect(Socket socket) {
		System.out.println("连接成功");
	}
	
	//根据收到的不同的message(message是自己注册时提供给服务端的)，做响应的的业务
	@Override
	public void onMessage(String message) {
		System.out.println("收到数据："+message);
	}

	@Override
	public void onClosed() {
		System.out.println("连接关闭");
	}

	
}
