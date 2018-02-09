package org.schedual.server.schedual.gc;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.schedual.server.rpc.SocketPoolManager;
import org.schedual.server.schedual.BaseScheduel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *定时查看是否存在死连接
 *因为客户端会每n秒发送一次心跳包，最后一次的时间不会超过n秒
 * @author dell
 */
@Component
public class SocketExpiredScheduel extends BaseScheduel{
	
	
	/**
	 * 每小时清理下 调度器-》任务池的过期数据，并更新数据库
	 */
	@Scheduled(cron="0/4 * * * * ?")
	public void gc(){
		//时间太短，尽量优化
		if(log.isDebugEnabled()){
			log.debug("gc expired socket");
		} 
		try {
			SocketPoolManager.removeExpiredSocket();
		} catch (Exception e) {
			log.error("expired socket exception,",e);
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		for (int i = 0; i < 4; i++) {
			try {
				Thread.sleep(500);
				System.out.println("add socket");
				SocketPoolManager.joinPool("ly"+i,new Socket("127.0.0.1", 33333));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
				SocketPoolManager.removeExpiredSocket();
				System.out.println(SocketPoolManager.showConn());
				System.out.println("----------------");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
