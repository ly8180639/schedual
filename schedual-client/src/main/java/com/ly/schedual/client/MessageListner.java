package com.ly.schedual.client;

import java.net.Socket;

public interface MessageListner { 
	
	public void onConnect(Socket socket);
	
	public void onMessage(String message);
	
	public void onClosed();
}
