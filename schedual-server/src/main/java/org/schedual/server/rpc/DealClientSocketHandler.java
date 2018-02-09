package org.schedual.server.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.schedual.server.handler.RequestCall;
import org.schedual.server.task.ServiceConfirmRunnable;
import org.schedual.server.task.TaskIntoPoolAndDbRunnable;
import org.schedual.server.utils.JSONUtils;


public class DealClientSocketHandler implements Runnable{
	
	
	Socket handerSocket;
	
	/**
	 * isInPool
	 * 是否加入SocketPool中，未加入socket池中的socket无法进行通信及数据处理
	 * 
	 */
	boolean isInPool;
	
	
	//该socket的id标示
	String clientId;
	
	BufferedReader br;
	
	PrintWriter pw;
	/**
	 * 收到客户端确认信息扔到此任务中
	 */
	ServiceConfirmRunnable servicepool;
	
	/**
	 * 收到客户端请求注册任务信息
	 */
	TaskIntoPoolAndDbRunnable taskPool;
	
	Logger log=Logger.getLogger(DealClientSocketHandler.class);
	
	public DealClientSocketHandler(Socket handerSocket,ServiceConfirmRunnable serviceConfirm,TaskIntoPoolAndDbRunnable taskpool) {
		this.handerSocket=handerSocket;
		this.servicepool=serviceConfirm;
		this.taskPool=taskpool;
	}
	
	
	public void run() {
		 try {
			pw=new PrintWriter(handerSocket.getOutputStream(),true);
			br = getReader(handerSocket);
			String msg = null;
			/**
			 * 客户端发的数据包括以下几类
			 * 1.注册信息   格式：clientid:xxx
			 * 2.task任务信息：{xxx:xx}
			 * 3.确认信息（服务端发业务信息给客户端，客户端返回一条确认信息):confirm:xxx
			 */
			while ((msg = br.readLine()) != null) {
				
				//设置当前
				SocketPoolManager.setCurrTime(clientId);
				
				if(isEmpty(msg)) continue;
				
				log.info("server recive data:"+msg);
				if(!isInPool){//未加入池，未注册
					if(msg.startsWith("clientid:")){//处理注册信息 
						clientId =msg.substring(9);
						if(StringUtils.isBlank(clientId)){
							log.warn("clientid can not be null");
							continue;
						}
						 boolean joinRes=SocketPoolManager.joinPool(clientId,handerSocket);//加入池子中，方便服务器发送数据到指定client中
						 if(!joinRes){
							 log.warn("clientid exists");
							 pw.println("clientid exists");
							 return;
						 }
						isInPool=true;
					}	
					continue;
				}
				/**
				 * 处理服务确认信息，摒弃掉了
				 */
				/*if(msg.startsWith("confirm:")){
					String serviceConform=msg.substring(8);
					if(!servicepool.putData(serviceConform)){//发送失败
						log.error("service confirm info add to pool failed:"+serviceConform+"  quesize:"+ServiceConfirmRunnable.getQueueSize());
					}
					continue;
				}*/
				//类似异步，把客户端注册的任务请求信息放入池中
				if(JSONUtils.isJSONstr(msg)&&!taskPool.putData(msg))
					log.error("regist task info into pool failed:"+msg+"  quesize:"+TaskIntoPoolAndDbRunnable.getQueueSize());
				
				if(msg.split("\\s+").length>2){//同步请求
					String result=RequestCall.execute(msg);
					pw.println(result);
					//Executors.newCachedThreadPool().submit(task)
				}
            }
			
		} catch (Exception e) {
			log.error("server read data error ",e);
		}finally{
			closeSocket();
			if(isInPool){
				SocketPoolManager.removeSocket(clientId);
			}
		}

	}

	private boolean isEmpty(String msg) {
		return msg==null || msg.equals("");
	}


	/**
	 *关闭socket
	 */
	public void closeSocket() {
		if(pw!=null){
			pw.close();
			pw=null;
		}
		if(br!=null){
			try {
				br.close();
				br=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		if(handerSocket!=null){
		     try {
		    	 handerSocket.close();
		    	 handerSocket=null;
		     } catch (IOException e) {
		         e.printStackTrace();
		     }
		}
	}
	
	private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }
	 
	
	

}
