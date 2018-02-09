package org.schedual.server.task;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.service.ITaskService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 调度器触发,将任务异步将任务放到待触发的
 * @author dell
 *
 */
@Component
public class ServiceSendToClientRunnable  implements InitializingBean{
	static Logger log=Logger.getLogger(ServiceSendToClientRunnable.class);

	static LinkedBlockingQueue<Task> queue=new LinkedBlockingQueue<Task>();
	
	

	private static ITaskService taskService;
	
	@Autowired
	private TaskExecutor executor;
	
	//既是线程大小也是,任务池大小
	@Value("${ServiceSendToClient.threadsize}")
	private int coresize;
	
	public static boolean putData(Task task){
		return queue.offer(task);
	}
	
	public static int getQueueSize(){
		return queue.size();
	}
		
	public void setCoresize(int coresize) {
		this.coresize = coresize;
	}
	
	

	@Autowired
	public void setTaskService(ITaskService taskService) {
		ServiceSendToClientRunnable.taskService = taskService;
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
			Task  service=null;
			for(;;){
				try {
					service=queue.take();
					//SocketPoolManager.sendtoConsumer(service.getClientid(),service.getService());
					taskService.saveService(service);
				} catch (InterruptedException e1) {
					log.error("thread stoped");
					break;
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		for (int i = 0; i < coresize; i++) {//启动coresize线程执行coresize的任务
			executor.execute(new DealTaskRunnable("ServiceSendToClientThread"+i));
		}
	}

}
