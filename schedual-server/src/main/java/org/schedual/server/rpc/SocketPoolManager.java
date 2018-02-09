package org.schedual.server.rpc;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class SocketPoolManager {
	static Logger log= Logger.getLogger(Socket.class);
	
	private static Map<String, WriterSocket> serverSocketMap=new HashMap<String,WriterSocket>(32);
	
	private static Object lock=new Object();
	
	public static int count=0;
	
	//4秒超时
	private static final long EXPIRED_TIME=4000L;
	
	
	
	/**
	 * 发送数据到指定客户端
	 * @param socket
	 * @return
	 */
	public static boolean sendtoConsumer(String targSocket,String data){
		
		if(!serverSocketMap.containsKey(targSocket)){
			log.error("can't find socket "+targSocket+",connection is not Establish");
			return false;
			//throw new RuntimeException("Socket is closed");
		}
		WriterSocket client=serverSocketMap.get(targSocket);
		if(client==null) return false;
		try {
			client.writeData(data);
			synchronized (lock) {
				count++;
			}
			return true;
		} catch (Exception e) {
			removeSocket(targSocket);
			client.close();
			return false;
			//throw new RuntimeException(e);
		}
		
	}
	
	public static boolean joinPool(String clientId,Socket socket){
		if(serverSocketMap.containsKey(clientId)) return false;
		serverSocketMap.put(clientId, new WriterSocket(socket));
		return true;
	}
	
	public static void setCurrTime(String clientId){
		if(!serverSocketMap.containsKey(clientId)) return ;
		WriterSocket wr=serverSocketMap.get(clientId);
		wr.setLastReciveTime(System.currentTimeMillis());
	}
	
	
	public static void removeSocket(String clientId){
		if(!serverSocketMap.containsKey(clientId)) return;
		synchronized (lock) {
			serverSocketMap.remove(clientId);
		}
	}
	public static void close(){
		for(WriterSocket socket:serverSocketMap.values()){
			socket.close();
		}
	}
	/**
	 * 
	 * <br/>Description:客户端会发送ping包给服务端,服务端会更新那个socket的最后一次接受时间
	 * 遍历在线的socket。最后一次接受时间如果超时了，则视为废弃socket
	 * <p>Author:liuyang</p>
	 * 2017年7月26日
	 */
	public static void removeExpiredSocket(){
		for (Iterator<Map.Entry<String, WriterSocket>> it = serverSocketMap.entrySet().iterator(); it.hasNext();){
		    Map.Entry<String, WriterSocket> item = it.next();
		    WriterSocket sock=item.getValue();
		    if((System.currentTimeMillis()-sock.getLastReciveTime())>EXPIRED_TIME){
		    	sock.close();
		    	it.remove();
		    }
		}
	}
	
	public static String showConn(){
		StringBuilder builder=new StringBuilder();
		Set<String> keys=serverSocketMap.keySet();
		for(String key:keys){
			WriterSocket socket= serverSocketMap.get(key);
			builder.append(key).append("--->").append(socket.socket).append("</br>");
		}
		return builder.toString();
	}
}
	