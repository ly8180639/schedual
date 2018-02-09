package com.ly.schedual.client.listner;

import java.net.Socket;

import org.apache.log4j.Logger;

import com.ly.schedual.client.MessageListner;


public class DefaultMessageListner implements MessageListner{
	
	Logger log = Logger.getLogger(DefaultMessageListner.class);
	
	public void onConnect(Socket socket) {
		// TODO Auto-generated method stub
	}
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		log.info("defaultListner reciveMsg:"+message);
		
	}
	@Override
	public void onClosed() {
		log.info("closed");
	}

}
