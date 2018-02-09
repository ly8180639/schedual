package org.schedual.server.task;

import java.util.List;

import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.TaskDao;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.handler.FixMinute;
import org.schedual.server.handler.RateMinute;
import org.schedual.server.handler.TaskPoolManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/**
 * 项目启动时，初始化将DB的task load进不同scheduel的任务池中
 * @author dell
 */
@Component
public class InitDBTaskIntoRunnable implements Runnable,InitializingBean{
	Logger log=Logger.getLogger(InitDBTaskIntoRunnable.class);
	
	@Autowired
	private TaskExecutor executor;
	
	@Autowired
	private TaskScheduler schedule;
	
	@Autowired
	private TaskDao taskDao;
	
	//每次从DB中取出5万条，然后放入内存中
	private int percount=50000;
	
	public InitDBTaskIntoRunnable() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		executor.execute(this);
		
	}

	@Override
	public void run() {
		try {
			loadDbTask();
		} catch (Exception e) {//项目启动之后，从DB中加载task，加载失败，则每1秒加载一次，直到加载成功
			log.info("load Db Task failed:",e);
			try {
				Thread.sleep(5000);//加载失败每隔5秒加载一次
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			loadDbTask();
		}
	}

	private void loadDbTask() {
		//load之前，需要先清当前调度器-》任务池的任务
		TaskPoolManager.clearTask();
		
		int selectcount=percount;
		for (int i=0;selectcount==percount;i++) {
			List<Task> tasks= taskDao.findPerCountTasks(i*percount, percount);
			selectcount=tasks.size();
			//每加载percount条，将其加入调度池对应的任务池中
			if(tasks==null || tasks.isEmpty()) continue;
			for(int j=0;j<tasks.size();j++){
				Task t=tasks.get(j);
				TaskPoolManager.findAndPutPool(t);
			}
			tasks.clear();//清内存大小
		}
		log.info("DB task load completed!");
		for(RateMinute scheduel:RateMinute.values()){
			log.info("scheduel:"+scheduel+"--size:"+TaskPoolManager.getScheduel_task_pool().get(scheduel.toString()).size());
		}
		for(FixMinute scheduel:FixMinute.values()){
			log.info("scheduel:"+scheduel+"--size:"+TaskPoolManager.getScheduel_task_pool().get(scheduel.toString()).size());
		}
	}
}
