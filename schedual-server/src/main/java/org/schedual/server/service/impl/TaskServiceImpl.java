package org.schedual.server.service.impl;


import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.TaskDao;
import org.schedual.server.dao.tkset.TaskServiceDao;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.dao.tkset.entity.TaskService;
import org.schedual.server.handler.TaskPoolManager;
import org.schedual.server.rpc.SocketPoolManager;
import org.schedual.server.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements ITaskService{
	Logger log=Logger.getLogger(TaskServiceImpl.class);
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private TaskServiceDao serviceLogDao;
	
	/**
	 * 数据保存DB，同时加入对应调度器的任务池
	 */
	@Override
	public void saveTask(Task task) {
		log.info("task into DB");
		if(task==null) throw new RuntimeException("task is null");
		if(StringUtils.isBlank(task.getClientid()))new RuntimeException("clientid is null");
		if(StringUtils.isBlank(task.getService()))new RuntimeException("service is null");
		if(isEmpty(task)) throw new RuntimeException("task is empty");
		if(isExpire(task)) throw new RuntimeException("task is expired");
		setNullExcuteTime(task);
		task.setStatus(1);//该status为可用状态
		taskDao.saveTask(task);
		//log.info("task_id:"+task.getId());
		TaskPoolManager.findAndPutPool(task);
	}
	
	/**
	 * 将业务数据发送给客户端，并把日志持久化至DB
	 * 为了不new一个新的TaskService对象,然后属性copy，直接使用task对象
	 */
	@Override
	public void saveTaskService(Task task) {
		log.info("serviceLog into DB");
		int result=0;
		try {
			task.setStatus(0);
			result = serviceLogDao.saveInitTaskService(task);
		} catch (Exception e) {
			log.error("service saveDB exception:",e);
		}
		 /**
		  * 保存DB失败打印条log日志作为凭证
		  */
		 if(result!=1)
			 log.warn("service save DB failed");
		 /**
		  * 发送至客户端，执行相应的业务。
		  */
		boolean sendRes=false;
		 
		String errMsg="";
		 
		try {
			SocketPoolManager.sendtoConsumer(task.getClientid(),task.getId()+":"+task.getService());
			sendRes=true;
		} catch (Exception e) {
			log.error("send exception:",e);
			errMsg=e.getMessage();
		}
		 /**
		  * 发送失败，需要更新日志状态
		  */
		 if(!sendRes&& task.getTmpServiceId()!=0){//发送失败
			try {
				serviceLogDao.updateServiceStatus(task.getTmpServiceId(),1,errMsg,null);
			} catch (Exception e) {
				log.error("update exception:",e);
			}
		 }
	}
	
	/**
	 * 确认客户端已经收到业务信息
	 * recivetime:客户端收到业务的时间
	 */
	@Override
	public void confirmService(int id,long recivetime) {
		// TODO Auto-generated method stub
		Date rectime=new Date(recivetime);
		//状态为2表示客户端已接收
		serviceLogDao.updateServiceStatus(id, 2, "", rectime);
	}
	
	/**
	 * 如果task是频率类型，每多少分钟，每多少小时... executetime为空
	 * @param task
	 */
	private void setNullExcuteTime(Task task) {
		// TODO Auto-generated method stub
		if(task.getExecutetime()!=null)return;
		if(task.getRate_months()==0&&task.getRate_weeks()==0&&task.getRate_days()==0&&task.getRate_hours()==0&&task.getRate_minutes()==0) return;
		task.setExecutetime(new Date());
	}
	/**
	 * 是否过期
	 * @param task
	 * @return
	 */
	private boolean isExpire(Task task) {
		if(task.getRange_endtime()!=null) return new Date().after(task.getRange_endtime());
		return false;
	}
	/**
	 * 是否empty
	 * @param task
	 * @return
	 */
	private boolean isEmpty(Task task) {
		return task.getRate_months()==0&&task.getRate_weeks()==0&&task.getRate_days()==0&&task.getRate_hours()==0&&task.getRate_minutes()==0&&
			StringUtils.isBlank(task.getMinute());
	}
	
	/**
	 * 不作确认消息机制的service存储方式
	 */
	@Override
	public void saveService(Task task) {
		// TODO Auto-generated method stub
		try {
			 /*SocketPoolManager.sendtoConsumer(task.getClientid(),task.getService());
			 task.setStatus(0);*/
			 serviceLogDao.saveInitTaskService(task);
		} catch (Exception e) {
			log.error("save DB exception:",e);
			/* task.setStatus(1);
			 task.setErrmsg(e.getMessage());
			serviceLogDao.saveInitTaskService(task);*/
		}
	}

	@Override
	public TaskService queryTaskService(int serviceId) {
		// TODO Auto-generated method stub
		return serviceLogDao.findById(serviceId);
	}
	
	

}
