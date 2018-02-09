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
public class MiniteEvery1Scheduel extends BaseScheduel{
	public static boolean isClosed=true;
	@Scheduled(cron="0 0/1 * * * ?")
	public void every1in0minute(){
		//if(isClosed) return;
		Calendar l=Calendar.getInstance();
		log.info("every One Minite Scheduel");
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(RateMinute.EVERY1_0.toString());
		Set<Task> executeTasks=TaskUtil.getRateTriggerTask(tasks,l);	
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
	}
}
