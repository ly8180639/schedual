package org.schedual.server.rpc;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.schedual.server.task.ServiceConfirmRunnable;
import org.schedual.server.task.TaskIntoPoolAndDbRunnable;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class CronServerSocket implements Runnable,InitializingBean,DisposableBean{
	
	Logger log=Logger.getLogger(CronServerSocket.class);
	
	@Value("${listener.port:9999}")
	private int port;
	
	@Autowired
	private TaskExecutor executor;
	
	//@Autowired
	private ServiceConfirmRunnable serviceConfirm;
	
	@Autowired
	private TaskIntoPoolAndDbRunnable taskPool;
	
	List<DealClientSocketHandler> sockets=new ArrayList<DealClientSocketHandler>();
	
	ServerSocket server=null;
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setExecutor(TaskExecutor executor) {
		this.executor = executor;
	}
	public CronServerSocket() {
	}


	public void acceptConnections(){
		try{
			server=new ServerSocket(port);
			while(true){
				Socket incomeSocket=server.accept();
				handleConnection(incomeSocket);
			}
		}catch(BindException e){
			log.error("unnable bind port "+port,e);
		}catch(IOException e){
			log.error("IOException ",e);
		}catch (Exception e) {
			log.error("exception ",e);
		}
		
	}

	private void handleConnection(Socket incomeSocket) {
		DealClientSocketHandler socket= new DealClientSocketHandler(incomeSocket,serviceConfirm,taskPool);
		sockets.add(socket);
		executor.execute(socket); 
	}

	public void run() {
		acceptConnections();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		executor.execute(this);
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		/**
		 * 关闭所有socket资源
		 */
		for(DealClientSocketHandler socket:sockets){
			socket.closeSocket();
		}
		if(server!=null){
			server.close();
		}
		//关闭容器的时候关闭socket
		//SocketPoolManager.close();
		
	}
	
	
}
