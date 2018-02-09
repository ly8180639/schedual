package org.schedual.server.task;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.schedual.server.service.ITaskService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;

/**
 * 调度器触发，执行任务，任务把业务发给客户端，客户端将数据回馈给服务端
 * 回馈的数据需要进行DB处理（更新serviceLog状态）
 * @author dell
 */
//@Component
public class ServiceConfirmRunnable implements InitializingBean{
	static Logger log=Logger.getLogger(ServiceConfirmRunnable.class);
	
	private static ITaskService taskService;
	
	static LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<String>();
	
	
	@Value("${ServiceConfirm.threadsize}")
	private int threadcoresize;
	
	@Autowired
	private TaskExecutor executor;
	
	public boolean putData(String data){
		return queue.offer(data);
	}
	
	public static int getQueueSize(){
		return queue.size();
	}
	
	
	
	@Autowired
	public void setTaskService(ITaskService taskService) {
		ServiceConfirmRunnable.taskService = taskService;
	}




	/**
	 * 处理任务(将数据传输到客户端,以及同步到数据库)
	 * @author dell
	 *
	 */
	static class DealTaskRunnable extends Thread{
		

		public DealTaskRunnable(String threadName) {
			// TODO Auto-generated constructor stub
			super(threadName);
		}
		
		@Override
		public void run() {
			String  confirmService="";
			for(int i=0;;i++){
				try {
					confirmService=queue.take();
					
				} catch (InterruptedException e1) {
					log.error("thread stoped");
					break;
				}
				
				try {
					log.info("recive response:"+confirmService+"---count:"+i);
					String[] confirmServices =confirmService.split("_");
					int id=Integer.parseInt(confirmServices[0]);
					long recivetime =Long.parseLong(confirmServices[1]);
					taskService.confirmService(id, recivetime);
				} catch (Exception e) {
					log.error("deal confirm service data exception ",e);
				}
			}
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		for (int i = 0; i < threadcoresize; i++) {//启动coresize线程执行coresize的任务
			//log.info("new thread:ServiceConfirmThread"+i);
			executor.execute(new DealTaskRunnable("ServiceConfirmThread"+i));
		}
	}

}
