package org.schedual.server.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.entity.Task;

public class TaskPoolManager {
	static Logger log=Logger.getLogger(TaskPoolManager.class);
	
	static Object rmLock=new Object();
	/** 
	 * 调度器-》任务池 
	 * 为什么用set，因为两个task id相同，不要让它同时存在调度器中
	 */
	private static Map<String,Set<Task>> scheduel_task_pool=new HashMap<String,Set<Task>>();
	
	static{
		log.info("init scheduel_task");
		/**
		 * 某多少分钟执行
		 */
		for(RateMinute scheduel:RateMinute.values()){
			scheduel_task_pool.put(scheduel.toString(), Collections.synchronizedSet(new HashSet<Task>()));
		}
		/**
		 * 特地某分钟执行（其他不确定）
		 */
		for(FixMinute scheduel:FixMinute.values()){
			scheduel_task_pool.put(scheduel.toString(), Collections.synchronizedSet(new HashSet<Task>()));
		}
		
	}
	
	/**
	 * 根据任务，找到合适的调度器对应的pool
	 * @return
	 */
	public static void findAndPutPool(Task task){
		/**
		 * 校验task是否过期 ,  根据task的startTime和endtime
		 */
		String scheduel=findScheduel(task);
		task.setScheduelname(scheduel);
		if(scheduel==null){
			log.info("根据task查找的scheduel为空，task："+task.getRate_minutes()+"--time:"+task.getExecutetime());
			return;
		}
		synchronized (scheduel) {
			//log.info("jion task scheduel:"+scheduel);
			scheduel_task_pool.get(scheduel).add(task);
		}
	}
	
	
	/**
	 * 清空任务池，从DB中load进内存之前需要清空
	 */
	public static void clearTask(){
		for(RateMinute scheduel:RateMinute.values()){
			scheduel_task_pool.get(scheduel.toString()).clear();
		}
	}
	
	/**
	 * 不同的调度器对应不同的规则，task符合某种规则就运用某种调度器
	 * @param task
	 * @return
	 */
	private static String findScheduel(Task task) {
		// TODO Auto-generated method stub
		
		//分钟-频率
		if(task.getExecutetime()!=null&&task.getRate_hours()==0&&task.getRate_days()==0&&task.getRate_weeks()==0&&task.getRate_months()==0&&task.getRate_minutes()!=0){
			return getminuteRateSchedual(task.getRate_minutes(),task.getExecutetime().getMinutes());
		}
		//小时，日期，星期，月，年的频率
		String scheduel=getOhterRateSchedual(task);
		
		if(scheduel!=null) return scheduel;
		
		//如果不是分钟的频率，则用定点的，固定某分钟执行
		String strminute=StringUtils.defaultIfEmpty(task.getMinute(), "0");//没设分钟则设0，按道理必须设分钟
		
		return getFixMinuteScheduel(Integer.parseInt(strminute));//根据minute决定哪个调度器
		
	}
	/**
	 * 每多少小时，多少天，多少星期执行一次，以executetime 的分钟作为决定哪个调度器
	 * @param task
	 * @return
	 */
	private static String getOhterRateSchedual(Task task) {
		if(task.getRate_hours()==0 && task.getRate_days()==0 && task.getRate_weeks()==0 && task.getRate_months()==0){
			return null;
		}
		if(task.getExecutetime()==null) return null;
		
		int exeminute=task.getExecutetime().getMinutes();
		
		if(exeminute%5==0){
			return "fixin"+exeminute;
		}
		
		return "fixin10_"+exeminute%10;
		// TODO Auto-generated method stub
		
	}


	private static String getFixMinuteScheduel(int minute) {
		if(minute%5==0){
			return "fixin"+minute;
		}
		
		return "fixin10_"+minute%10;
	}


	/**
	 * 获取该任务该由哪个调度器执行
	 * @param rate
	 * @param curr_minute
	 * @return
	 */
	private static String getminuteRateSchedual(int rate,int curr_minute) {
		// TODO Auto-generated method stub
		 if(rate%5==0){//能除5就用5的频率
			  int mol=curr_minute%5;
			  switch (mol) {
				case 0:
					return RateMinute.EVERY5_0.toString();
				case 1:
					return RateMinute.EVERY5_1.toString();
				case 2:
					return RateMinute.EVERY5_2.toString();
				case 3:
					return RateMinute.EVERY5_3.toString();
				case 4:
					return RateMinute.EVERY5_4.toString();
				default:
					break;
			}
		}else if(rate%3==0){//能除3就用3的频率
			  int mol=curr_minute%3;
			  switch (mol) {
				case 0:
					return RateMinute.EVERY3_0.toString();
				case 1:
					return RateMinute.EVERY3_1.toString();
				case 2:
					return RateMinute.EVERY3_2.toString();
				default:
					break;
				}
		}else if(rate%2==0){//能除2就用2的频率
			  int mol=curr_minute%2;
			  switch (mol) {
				case 0:
					return RateMinute.EVERY2_0.toString();
				case 1:
					return RateMinute.EVERY2_1.toString();
				default:
					break;
				}
		}else{//都不能被上面的整 除，则用1
			return RateMinute.EVERY1_0.toString();
		}
		 
		 return null;
	}
	
	/**
	 * 根据task的主键id移除task
	 * @param id
	 */
	public static void findAndRemoveTask(int id){
		synchronized (rmLock) {//防止同步删除，用同步map，在map的其他操作上耗性能
			for(RateMinute scheduel:RateMinute.values()){
				scheduel_task_pool.get(scheduel.toString()).remove(new Task(id));
			}
			/**
			 * 特地某分钟执行（其他不确定）
			 */
			for(FixMinute scheduel:FixMinute.values()){
				scheduel_task_pool.get(scheduel.toString()).remove(new Task(id));
			}
		}
	}
	
	/**
	 * 先移除task，再添加task
	 * @param id
	 */
	public static void updateTask(Task task){
		synchronized (rmLock) {//防止同步删除，用同步map，在map的其他操作上耗性能
			boolean isRemove=false;//不用遍历两次
			for(RateMinute scheduel:RateMinute.values()){
				//如果查到了，就删除他，并添加task
				if(scheduel_task_pool.get(scheduel.toString()).contains(task)){
					scheduel_task_pool.get(scheduel.toString()).remove(task);//移除
					isRemove=true;
					break;
				}			
			}
			/**
			 * 特地某分钟执行（其他不确定）
			 */
			if(!isRemove){
				for(FixMinute scheduel:FixMinute.values()){
					if(scheduel_task_pool.get(scheduel.toString()).contains(task)){
						scheduel_task_pool.get(scheduel.toString()).remove(task);//移除
						break;
					}
				}
			}
			
			findAndPutPool(task);//新增
		}
	}


	public static Map<String, Set<Task>> getScheduel_task_pool() {
		return scheduel_task_pool;
	}


	public static void setScheduel_task_pool(
			Map<String, Set<Task>> scheduel_task_pool) {
		TaskPoolManager.scheduel_task_pool = scheduel_task_pool;
	}
	
	
	
}
