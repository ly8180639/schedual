package com.ly.schedual.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.ly.schedual.client.response.SocketResponse;


/**
 * 同步客户端,同步发送请求的作用
 * @author dell
 *
 */
public class SyncClient  implements Runnable{
	
	Logger log = Logger.getLogger(SyncClient.class);
	
	PrintWriter writer; 
	
	BufferedReader reader;
	
	Socket socket;
	
	private String ip;
	
	private int port;
	
	private String clientid;
	
	private boolean isShutdown;
	
	ArrayBlockingQueue<String> response=new ArrayBlockingQueue<String>(1);
	
	int index=0;
	boolean isReconnect=false;
	
	Object lock=new Object();
	
	public SyncClient(String ip,int port,String clientid) {
		// TODO Auto-generated constructor stub
		this.ip=ip;
		this.port=port;
		this.clientid=clientid;
	}
	
	
	
	/**
	 * 发送请求
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void initSocket() throws UnknownHostException, IOException{
		socket=new Socket(ip, port);
		this.log.info("sync socket connected...." + this.ip + ":" + this.port);
	}
	
	 private static PrintWriter getWriter(Socket socket) throws IOException
	  {
	    OutputStream socketOut = socket.getOutputStream();
	    return new PrintWriter(socketOut, true);
	  }
	
	/**
	 * 同步发送数据
	 * data:发送的数据
	 * timeout:读取的时间
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 * @throws SocketException 
	 */
	public synchronized SocketResponse sendData(String url,long timeout) throws InterruptedException, TimeoutException, SocketException{
			long startTime=System.currentTimeMillis();
			String sendData=new StringBuffer().append(++index).append(" ").append(timeout).append(" ")+url;
			long msecs =timeout;
			//发送数据
			if(socket==null || socket.isClosed() || writer==null) throw new SocketException("socket is closed");
			//log.info("send data:"+sendData);
			writer.println(sendData);
			do {
				//如果数据正确，直接返回
				String rsp=response.poll(msecs,TimeUnit.MILLISECONDS);
				if(rsp==null){//再等一次
					timeout = System.currentTimeMillis() - startTime;
		    		continue;
				}
				int respIndex=Integer.parseInt(rsp.split(" ")[0]);
		    	if(respIndex==index){
		    		String rspBody=rsp.substring(rsp.indexOf(" "));
		    		return SocketResponse.bulidByJSONStr(rspBody);
		    	}
				timeout = System.currentTimeMillis() - startTime;
				
			} while (timeout < msecs);
			throw new TimeoutException();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!isShutdown){
			if(isReconnect){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				initSocket();
				
				//ping breath package
				final Timer beathTimer=new Timer();
				
				beathTimer.schedule(new TimerTask() {
					
					@Override
					public void run() {
							//send a empty str
							if(log.isDebugEnabled()){
								log.debug("client-sync send ping....");
							}
							try {
								writer.println("");
							}catch(Exception e){
								log.error("sync client breath package send error",e);
								closeSocket();
								beathTimer.cancel();
							}
					}
				}, 1000,Producer.PING_PERIOD);
				
				reciveMsg();
			}catch (Exception e) {
				log.error("sync socket exception "+e.getMessage());
			}finally{
				closeSocket();
				isReconnect=true;
			}
		}
		
	}
	
	 private void closeSocket()
	  {
	    if (this.writer != null) {
	      this.writer.close();
	      this.writer = null;
	    }
	    if (this.reader != null) {
	      try {
	        this.reader.close();
	      }
	      catch (IOException e) {
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
	
	
	  private void reciveMsg()
			    throws Exception
	  {
	    InputStreamReader stream = new InputStreamReader(this.socket.getInputStream());
	    this.writer=getWriter(socket);
	    this.reader = new BufferedReader(stream);
	    //发送一条注册消息
	    writer.println("clientid:" + this.clientid+"-sync");
	    
	    String line;
	    while ((line = this.reader.readLine()) != null)
	    {
	    	if(line.equals("clientid exists")){//客户端已存在
				break;
			}
	    	int respIndex=Integer.parseInt(line.split(" ")[0]);
	    	if(respIndex==index){
	    		response.put(line);	
	    	}
	    }
	  }



	public boolean isShutdown() {
		return isShutdown;
	}



	public void setShutdown(boolean isShutdown) {
		this.isShutdown = isShutdown;
	}
	
	  
	
}
