package org.schedual.server.schedual.fix;

import java.util.Calendar;
import java.util.Set;

import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.handler.FixMinute;
import org.schedual.server.handler.TaskPoolManager;
import org.schedual.server.schedual.BaseScheduel;
import org.schedual.server.utils.TaskUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MinuteIn20Scheduel extends BaseScheduel{
	@Scheduled(cron="0 20 * * * ?")
	public void in5Minite(){
		log.info("In 20 Minite Scheduel");
		Calendar l=Calendar.getInstance();
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(FixMinute.FIXIN20.toString());
		Set<Task> executeTasks=TaskUtil.getFixTriggerTask(tasks,l);
		for(Task t:executeTasks){
			TaskUtil.sendTask(t);
		}
		executeTasks.clear();
		executeTasks=null;
	}
}
