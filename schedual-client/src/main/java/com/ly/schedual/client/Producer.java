package com.ly.schedual.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.ly.schedual.client.response.SocketResponse;

/**
 * 
 * socket 客户端初始化
 *
 */
public class Producer implements  Runnable {
	
	Logger log = Logger.getLogger(Producer.class);
	
	//ping包发送周期，3秒一次
	public static final long PING_PERIOD=3000;

	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	public MessageListner messageListner;
	int threadCoreSize;
	String clientid;
	String ip;
	int port;
	// 异步推送的socket
	Socket socket;
	// 同步推送的socket
	SyncClient syncSocket;  

	PrintWriter writer;
	BufferedReader reader;
	Object o = new Object();

	boolean isConnected = false;

	ExecutorService executor = null;

	boolean isShutDown = false;

	int count = 0;

	private void initSocket() throws Exception {
		this.socket = new Socket(this.ip, this.port);
		this.log.info("producer socket connected...." + this.ip + ":" + this.port);
		this.writer = getWriter(this.socket);
		if (this.messageListner == null)
			throw new Exception("can't find MessageListner");
		this.messageListner.onConnect(this.socket);
	}
	public boolean isConnected(){
		return isConnected;
	}

	private void reciveMsg() throws Exception {
		produce("clientid:" + this.clientid);
		InputStreamReader stream = new InputStreamReader(
				this.socket.getInputStream());

		this.reader = new BufferedReader(stream);

		String line = null;
		while ((line = this.reader.readLine()) != null) {
			if(line.equals("clientid exists")){//客户端已存在则
				log.warn("clientid exists,please close previous client or change the clientid");
				break;
			}
			count++;
			try {
				if (!queue.offer(line, 100L, TimeUnit.MILLISECONDS))
					this.log.error("take data failed:" + line + "--"
							+ queue.size());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 推送数据，类似异步
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void produce(String data) throws Exception {
		if ((!this.isConnected) || (this.socket == null)
				|| (this.socket.isClosed()) || (this.writer == null)) {
			this.log.error("socket is not establish or is closed");
			throw new Exception("socket is closed");
		}
		this.writer.println(data);
	}
	private static final long DEFAULTTIMEOUT=3000;//单位毫秒

	/**
	 * 同步发送请求，及时获取响应
	 * 
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public SocketResponse sendReq(String url) throws Exception {
		return sendReq(url, DEFAULTTIMEOUT);
	}

	/**
	 * 同步发送请求，及时获取响应
	 * 
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public SocketResponse sendReq(String url, long timeout) throws Exception {
		if (syncSocket == null)
			throw new Exception("sync socket is not initial");
			try {
				return syncSocket.sendData(url, timeout);
			} catch (SocketException e) {
				log.error("socket err", e);
				throw new Exception("socket error");
			} catch (InterruptedException e) {
				throw new Exception("thread interrupt");
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				log.error("request timeout", e);
				throw new Exception("request timeout");
			}
	}
	
	/**
	 * 同步发送请求，及时获取响应
	 * 
	 * @param data param
	 * @return
	 * @throws Exception 
	 */
	public SocketResponse sendReq(String url,String param, long timeout) throws Exception {
		if(param==null || param.equals(""))return syncSocket.sendData(url, timeout);
		return syncSocket.sendData(url+" "+param, timeout);
	}
	
	/**
	 * 同步发送请求，及时获取响应
	 * 
	 * @param data map
	 * @return
	 * @throws Exception 
	 */
	public SocketResponse sendReq(String url,Map<String,String> pmap, long timeout) throws Exception {
		if(pmap==null) return syncSocket.sendData(url, timeout);
		
		StringBuilder param=new StringBuilder();
		for(String key:pmap.keySet()){
			param.append(key).append("=").append(pmap.get(key)).append("&");
		}
		return syncSocket.sendData(url+" "+param, timeout);
	}
	
	
	/**
	 * 同步发送请求，及时获取响应
	 * 
	 * @param data param
	 * @return
	 * @throws Exception 
	 */
	public SocketResponse sendReq(String url,String param) throws Exception {
		if(param==null || param.equals(""))return syncSocket.sendData(url, DEFAULTTIMEOUT);
		return syncSocket.sendData(url+" "+param, DEFAULTTIMEOUT);
	}
	
	/**
	 * 同步发送请求，及时获取响应
	 * 
	 * @param data map
	 * @return
	 * @throws Exception 
	 */
	public SocketResponse sendReq(String data,Map<String,String> pmap) throws Exception {
		if(pmap==null) return syncSocket.sendData(data, DEFAULTTIMEOUT);
		
		StringBuilder param=new StringBuilder();
		for(String key:pmap.keySet()){
			param.append(key).append("=").append(pmap.get(key)).append("&");
		}
		return syncSocket.sendData(data+" "+param, DEFAULTTIMEOUT);
	}
	

	private static PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	public void run() {
		boolean isReConnect = false;
		while (!this.isShutDown) {
			if (isReConnect) {
				this.log.info("reconnecting...");
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				initSocket();
				this.isConnected = true;
				
				
				//ping breath package
				final Timer beathTimer=new Timer();
				
				beathTimer.schedule(new TimerTask() {
					
					@Override
					public void run() {
							//send a empty str
							if(log.isDebugEnabled()){
								log.debug("client send ping....");
							}
							try {
								writer.println("");
							} catch (Exception e) {
								log.error("client breath package send error",e);
								closeSocket();
								beathTimer.cancel();
							}
					}
				}, 1000,PING_PERIOD);
				
				reciveMsg();
			} catch (Exception e) {
				this.log.error("producer socket exeption " +e.getMessage());
			} finally {
				if (this.isConnected)
					this.messageListner.onClosed();
				this.isConnected = false;
				closeSocket();
				isReConnect = true;
			}
		}
	}

	private void closeSocket() {
		if (this.writer != null) {
			this.writer.close();
			this.writer = null;
		}
		if (this.reader != null) {
			try {
				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.socket != null)
			try {
				this.socket.close();
				this.socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	//初始化spring之后执行
	public void initPool() throws Exception {
		/**
		 * 启动异步socket，1个线程用来初始化socket，其余线程用来消费数据
		 */
		this.executor = Executors.newFixedThreadPool(this.threadCoreSize);
		for (int i = 0; i < this.threadCoreSize; i++) {
			if (i == 0)
				this.executor.execute(this);
			this.executor.execute(new Producer.DealMsgRunnable(
					this.messageListner));
		}
		/**
		 * 启动同步socket
		 */
		syncSocket = new SyncClient(ip, port,clientid);
		new Thread(syncSocket).start();
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public MessageListner getMessageListner() {
		return this.messageListner;
	}

	public void setMessageListner(MessageListner messageListner) {
		this.messageListner = messageListner;
	}

	public int getThreadCoreSize() {
		return this.threadCoreSize;
	}

	public void setThreadCoreSize(int threadCoreSize) {
		this.threadCoreSize = threadCoreSize;
	}

	
	
	public static LinkedBlockingQueue<String> getQueue() {
		return queue;
	}

	public static void setQueue(LinkedBlockingQueue<String> queue) {
		Producer.queue = queue;
	}

	public void destroy() throws Exception {
		this.isShutDown = true;
		if(syncSocket!=null){
			syncSocket.setShutdown(true);
		}
		closeSocket();
		if (this.executor != null)
			this.executor.shutdownNow();
	}

	static class DealMsgRunnable implements Runnable {

		MessageListner msglistner;

		public DealMsgRunnable(MessageListner listner) {
			this.msglistner = listner;
		}

		@Override
		public void run() {
			while (true) {
				String service = "";
				try {
					service = (String) Producer.queue.take();
					this.msglistner.onMessage(service);
				} catch (Exception exception) {
					break;
				}
			}
		}

	}

}
