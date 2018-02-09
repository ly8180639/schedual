package org.schedual.server.schedual.gc;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.schedual.server.dao.tkset.TaskDao;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.handler.TaskPoolManager;
import org.schedual.server.schedual.BaseScheduel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每小时gc一次,清理过期数据
 * @author dell
 */
@Component
public class GCScheduel extends BaseScheduel{
	
	@Autowired
	private TaskDao taskDao;
	
	/**
	 * 每小时清理下 调度器-》任务池的过期数据，并更新数据库
	 */
	@Scheduled(cron="0 53 * * * ?")
	public void gc(){
		log.info("gc expired task");
		Calendar now= Calendar.getInstance();
		List<Integer> gctask=new ArrayList<Integer>();
		for(String key:TaskPoolManager.getScheduel_task_pool().keySet()){
			Set<Task> poolTask=TaskPoolManager.getScheduel_task_pool().get(key);
			List<Task> delList=new ArrayList<Task>();
			for (Task task : poolTask) {
				if(task.getRange_endtime()!=null && task.getRange_endtime().before(now.getTime())){
					delList.add(task);
					gctask.add(task.getId());
					continue;
				}
			}
			poolTask.removeAll(delList);
		}
		/**
		 * 将DB中对应的task设为过期
		 */
		if(!gctask.isEmpty()){
			taskDao.updateExpiredTasks(gctask);
		}
	}

	
	public static void main(String[] args) {
		String s="1,2,3";
		String s1="03";
		System.out.println(Integer.parseInt(s1));
		Calendar now=Calendar.getInstance();
		System.out.println(now.get(Calendar.DAY_OF_WEEK));
		System.out.println(now.get(Calendar.MONTH));
		System.out.println(now.get(Calendar.DAY_OF_MONTH));
		System.out.println(now.get(Calendar.HOUR_OF_DAY));
		Date d=new Date();
		System.out.println(d.getMonth()+"--"+d.getDate()+"--"+(d.getYear()+1900)+"---"+d.getHours());
	}
}
