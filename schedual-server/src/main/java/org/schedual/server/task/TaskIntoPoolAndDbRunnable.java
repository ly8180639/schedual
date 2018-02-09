package org.schedual.server.task;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.service.ITaskService;
import org.schedual.server.utils.JSONUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 服务端收到任务信息，需要计算加入到不同的调度池中
 * @author dell
 *
 */
@Component
public class TaskIntoPoolAndDbRunnable implements InitializingBean{
	
	static Logger log=Logger.getLogger(ServiceConfirmRunnable.class);
	
	private static ITaskService taskService;
	
	@Autowired
	private TaskExecutor executor;
	
	@Value("${TaskIntoPoolAndDb.threadsize}")
	private int coreThreadSize;
	
	static LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<String>();
	
	
	public boolean putData(String data){
		return queue.offer(data);
	}
	public static int getQueueSize(){
		return queue.size();
	}
	
	@Autowired
	public  void setTaskService(ITaskService taskService) {
		TaskIntoPoolAndDbRunnable.taskService = taskService;
	}
	
	public void setExecutor(TaskExecutor executor) {
		this.executor = executor;
	}
	
	
	
	static class DealTaskInfoRunnable extends Thread{
		
		public DealTaskInfoRunnable(String threadName) {
			super(threadName);
		}
		@Override
		public void run() {
			String taskmsg="";
			for (;;) {
				try {
					taskmsg = queue.take();
					Task task=(Task)JSONUtils.toObject(taskmsg,Task.class);
					task.setCreateType(1);//通过客户端注册进来的
					taskService.saveTask(task);
				}catch (InterruptedException e1) {
					log.error("thread stoped");
					break;
				} catch (Exception e) {
					log.error("client task exception:"+taskmsg,e);
				}
			}
		  }
	 }
	/*public static void main(String[] args) {
		String data="{\"clientid\":\"fuxing\",\"executetime\":\"\",\"service\":\"lyservice\",\"state\":1,\"rate_minutes\":3,\"lexecutetime\":\"\"}";
		Task task=(Task) JSONObject.toBean(JSONObject.fromObject(data), Task.class);
		//System.out.println(JSONObject.fromObject(task,JsonConfig));
		String data2="{\"executetime\":\"\",\"fix_range_days\":\"\",\"fix_range_hours\":\"\",\"fix_range_weeks\":\"\",\"lendtime\":0,\"lstarttime\":0,\"range_endtime\":\"\",\"range_starttime\":\"\",\"rate_minutes\":10}";
		Task task2=(Task) JSONObject.toBean(JSONObject.fromObject(data2), Task.class);
		System.out.println(JSONObject.fromObject(task2));
	}*/

	@Override
	public void afterPropertiesSet() throws Exception {
		for(int i=0;i<coreThreadSize;i++){
			executor.execute(new DealTaskInfoRunnable("TaskIntoPoolAndDBThread"+i));
		}
	}
	

}
