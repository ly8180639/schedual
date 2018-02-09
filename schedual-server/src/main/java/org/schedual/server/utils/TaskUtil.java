package org.schedual.server.utils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.rpc.SocketPoolManager;
import org.schedual.server.task.ServiceSendToClientRunnable;


public class TaskUtil {
	
	static Logger log=Logger.getLogger(TaskUtil.class);
	
	/**
	 * 从任务池中获取需要执行的任务(频率，每多少分钟)
	 * @param tasks
	 * @param rate
	 * @param currtime
	 * @return
	 */
	public static Set<Task> getRateTriggerTask(Set<Task> tasks, Calendar c1){
		Set<Task> result=new HashSet<Task>();
		for(Task t:tasks){
			//校验是否是区间内的时间 
			if(!isEffectTime(t,c1)) continue;
			
			if(c1.before(t.getExecutetime())) continue;
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);
			
			Calendar c2= Calendar.getInstance();
			c2.setTime(t.getExecutetime());
			c2.set(Calendar.SECOND, 0);
			c2.set(Calendar.MILLISECOND, 0);
			
			
			long minutes=(c1.getTimeInMillis()-c2.getTimeInMillis())/(1000*60);
			if(minutes%t.getRate_minutes()==0){
				result.add(t);
			}
		}
		return result;
	}
	/**
	 * 从任务池中获取需要执行的任务(定点分钟)
	 * @param tasks
	 * @param FixMinute
	 * @param currdate
	 * @return
	 */
	public static Set<Task> getFixTriggerTask(Set<Task> tasks, Calendar currtime) {
		Set<Task> result=new HashSet<Task>();
		for(Task t:tasks){
			//校验时间区间是否符合
			try {
				if(!isEffectTime(t,currtime)) continue;
				if(t.getRate_hours()==0 && t.getRate_days() ==0 && t.getRate_weeks() ==0 &&  t.getRate_months()==0){
					//判断是不是指定的模糊时间，校验优先级，小时->天->星期->月->年
					if(!isCurrTime(t, currtime, "minute")) continue;
				}else{//判断是不是根据executetime的间隔时间，每隔多少小时，多少天，多少星期，多少月判断，每周5每两小时执行一次
					if(!isEveryTime(t,currtime)) continue;
				}
			} catch (Exception e) {
				log.error("execute time validate exception",e);
				continue;
			}
			result.add(t);
		}
		return result;
	}
	
	public static void main(String[] args) {
		Calendar cal=Calendar.getInstance();
		int week=cal.get(Calendar.DAY_OF_WEEK) - 1;
		System.out.println(cal.get(Calendar.DAY_OF_WEEK));
		if(week<0) week=7;
	}
	
	/**
	 * 是否是有效的时间，根据任务的区间范围，和当前时间对比
	 * @param t
	 * @param currtime
	 * @return
	 */
	private static boolean isEffectTime(Task t,Calendar currtime){
		//当前时间小于开始时间
		if(t.getRange_starttime()!=null && currtime.before(t.getRange_starttime())) return false;
		
		//当前时间大于结束时间
		if(t.getRange_endtime()!=null && currtime.after(t.getRange_endtime())) return false;
		
		if(StringUtils.isNotBlank(t.getFix_range_month()) && !"*".equals(t.getFix_range_month())  && !(","+t.getFix_range_month()+",").contains(","+(currtime.get(Calendar.MONTH)+1)+",") && !(","+t.getFix_range_month()+",").contains(",0"+(currtime.get(Calendar.MONTH)+1)+",")) return false;
		
		int week=currtime.get(Calendar.DAY_OF_WEEK) - 1;
		if(week==0) week=7;
		
		if(StringUtils.isNotBlank(t.getFix_range_weeks()) && !"*".equals(t.getFix_range_weeks())  && !(","+t.getFix_range_weeks()+",").contains(","+week+",") && !(","+t.getFix_range_weeks()+",").contains(","+week+",") && !(","+t.getFix_range_weeks()+",").contains(",0"+week+",")) return false;
		
		if(StringUtils.isNotBlank(t.getFix_range_days()) && !"*".equals(t.getFix_range_days())  && !(","+t.getFix_range_days()+",").contains(","+currtime.get(Calendar.DAY_OF_MONTH)+",") && !(","+t.getFix_range_days()+",").contains(",0"+currtime.get(Calendar.DAY_OF_MONTH)+",")) return false;
		
		if(StringUtils.isNotBlank(t.getFix_range_hours()) && !"*".equals(t.getFix_range_hours())  && !(","+t.getFix_range_hours()+",").contains(","+currtime.get(Calendar.HOUR_OF_DAY)+",") && !(","+t.getFix_range_hours()+",").contains(",0"+currtime.get(Calendar.HOUR_OF_DAY)+",")) return false;
		
		if(StringUtils.isNotBlank(t.getFix_range_minutes()) && !"*".equals(t.getFix_range_minutes())  && !(","+t.getFix_range_minutes()+",").contains(","+currtime.get(Calendar.MINUTE)+",") && !(","+t.getFix_range_minutes()+",").contains(",0"+currtime.get(Calendar.MINUTE)+",") ) return false;
		
		return true;
		
		
	}
	
	
	/**
	 * 根据exetime和每隔多少时间，确定是否执行
	 * @param t
	 * @param currtime
	 * @return
	 */
	private static boolean isEveryTime(Task t, Calendar c1) {
		
		//执行时间点晚于当前时间
		if(c1.before(t.getExecutetime())) return false;
		
		//定时器执行的时间（分钟）是否和执行的分钟一致
		if(t.getExecutetime().getMinutes()!=c1.get(Calendar.MINUTE)) return false;
		
		Calendar c2= Calendar.getInstance();
		c2.setTime(t.getExecutetime());
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		long minutes=(c1.getTimeInMillis()-c2.getTimeInMillis())/(1000*60);
		if(t.getRate_hours()!=0){
			return minutes%(60*t.getRate_hours())==0;
		}else if(t.getRate_days()!=0){
			return minutes%(60*24*t.getRate_days())==0;
		}else if(t.getRate_weeks()!=0){
			return minutes%(60*24*7*t.getRate_weeks())==0;
		}else if(t.getRate_months()!=0){
			//如果日，时，分都相等，在判断月
			if(c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH)  && c2.get(Calendar.HOUR_OF_DAY)==c1.get(Calendar.HOUR_OF_DAY) && c2.get(Calendar.MINUTE)==c1.get(Calendar.MINUTE) ){
				return (c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH))%t.getRate_months()==0;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param time 时间
	 * @param type 时间类型
	 */
	private static boolean isCurrTime(Task t,Calendar currtime,String type){
		
		if("minute".equals(type)){
			
			int minute=StringUtils.isBlank(t.getMinute())?0:Integer.parseInt(t.getMinute());
			//定时器执行的时间（分钟）是否和执行的分钟一致
			if(minute!=currtime.get(Calendar.MINUTE)) return false;
			return isCurrTime(t,currtime, "hour");
		}
		
		if("hour".equals(type)){
			if(StringUtils.isNotBlank(t.getHour()) && !"*".equals(t.getHour()) &&  Integer.parseInt(t.getHour())!=currtime.get(Calendar.HOUR_OF_DAY)) return false;
			return isCurrTime(t,currtime, "day");
		}
		if("day".equals(type)){
			if(StringUtils.isNotBlank(t.getDay()) && !"*".equals(t.getDay()) &&  Integer.parseInt(t.getDay())!=currtime.get(Calendar.DAY_OF_MONTH)) return false;
			return isCurrTime(t,currtime, "week");
		}
		if("week".equals(type)){
			int week=currtime.get(Calendar.DAY_OF_WEEK)-1;
			if(week<0) week=7;
			if(StringUtils.isNotBlank(t.getWeek()) && !"*".equals(t.getWeek()) &&  Integer.parseInt(t.getWeek())!=week) return false;
			return isCurrTime(t,currtime, "month");
		}
		if("month".equals(type)){
			if(StringUtils.isNotBlank(t.getMonth()) && !"*".equals(t.getMonth()) &&  Integer.parseInt(t.getMonth())!=(currtime.get(Calendar.MONTH)+1)) return false;
			return isCurrTime(t,currtime, "year");
		}
		if("year".equals(type)){
			if(StringUtils.isNotBlank(t.getYear()) && !"*".equals(t.getYear()) &&  Integer.parseInt(t.getYear())!=currtime.get(Calendar.YEAR)) return false;
			return true;
		}
		return false;
	}
	/**
	 * 发送task至客户端
	 * @param t
	 */
	public static void sendTask(Task t){
		log.info(t.getScheduelname()+" send service to client---"+t.getClientid()+"--data："+t.getService());
		t.setStatus(1);
		if(SocketPoolManager.sendtoConsumer(t.getClientid(), t.getService()))t.setStatus(0);
		if(!ServiceSendToClientRunnable.putData(t))//Queue队列满了,可能会放入池子中失败
			log.error("task service to pool failed:"+t.getService());
	}
}
