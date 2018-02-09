package org.schedual.server.schedual.rate;

import java.util.Calendar;
import java.util.Set;

import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.handler.RateMinute;
import org.schedual.server.handler.TaskPoolManager;
import org.schedual.server.schedual.BaseScheduel;
import org.schedual.server.utils.TaskUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class MiniteEvery5Scheduel extends BaseScheduel{
	
	
	@Scheduled(cron="0 0/5 * * * ?")
	public void every5in0minute(){
		log.info("every Five Minite at 0 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY5_0.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 1/5 * * * ?")
	public void every5in1minute(){
		log.info("every Five Minite  at 1 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY5_1.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 2/5 * * * ?")
	public void every5in2minute(){
		log.info("every Five Minite  at 2 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY5_2.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 3/5 * * * ?")
	public void every5in3minute(){
		log.info("every Five Minite  at 3 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY5_3.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 4/5 * * * ?")
	public void every5in4minute(){
		log.info("every Five Minite  at 4 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY5_4.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
	}
}
