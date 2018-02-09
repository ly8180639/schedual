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
public class MiniteEvery3Scheduel extends BaseScheduel{
	
	@Scheduled(cron="0 0/3 * * * ?")
	public void every3in0minute(){
		log.info("every Three Minite at 0 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY3_0.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 1/3 * * * ?")
	public void every3in1minute(){
		log.info("every Three Minite  at 1 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY3_1.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 2/3 * * * ?")
	public void every3in2minute(){
		log.info("every Three Minite  at 2 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY3_2.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
}
