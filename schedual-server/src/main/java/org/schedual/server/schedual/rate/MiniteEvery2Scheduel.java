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
public class MiniteEvery2Scheduel extends BaseScheduel{
	@Scheduled(cron="0 0/2 * * * ?")
	public void every2in0minute(){
		log.info("every Two Minite at 0 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY2_0.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
		
	}
	
	@Scheduled(cron="0 1/2 * * * ?")
	public void every2in1minute(){
		log.info("every Two Minite  at 1 Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY2_1.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
	}
	
	
	
	
}
