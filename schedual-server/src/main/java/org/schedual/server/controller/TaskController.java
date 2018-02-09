package org.schedual.server.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.schedual.server.controller.in.TaskInfo;
import org.schedual.server.controller.out.Page;
import org.schedual.server.controller.out.SimpleResponse;
import org.schedual.server.controller.out.TaskBasePool;
import org.schedual.server.controller.out.status.CommonSatus;
import org.schedual.server.controller.out.status.TaskStatus;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.dao.tkset.entity.TaskService;
import org.schedual.server.handler.TaskPoolManager;
import org.schedual.server.rpc.SocketPoolManager;
import org.schedual.server.service.ITaskManagerService;
import org.schedual.server.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/task")
public class TaskController {
	Logger log=Logger.getLogger(TaskController.class);
	
	@Autowired
	private ITaskService serviceDao;
	
	@Autowired
	private ITaskManagerService manageService;
	
	public TaskController() {
	}
	/**
	 * 注册任务
	 * @return
	 */
	@RequestMapping(value="/regist",method=RequestMethod.POST)
	public SimpleResponse registTask(@RequestBody TaskInfo taskInfo){
		TaskStatus status= validateParam(taskInfo);
		if(!status.getCode().equals(CommonSatus.SUCCESS.getCode())) return new SimpleResponse(status.getCode(), status.getMsg());
		Task task=changeToTask(taskInfo);
		
		try {
			if(manageService.registTask(task)){
				return new SimpleResponse(CommonSatus.SUCCESS.getCode(),CommonSatus.SUCCESS.getMsg());
			}
		} catch (Exception e) {
			log.error("regist exeption ",e);
		}
		
		return new SimpleResponse(TaskStatus.TASK_REGIST_FAILED.getCode(),TaskStatus.TASK_REGIST_FAILED.getMsg());
	}
	
	
	
	/**
	 * 分页查询统计
	 * @param taskInfo
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public SimpleResponse getTasksByPage(@RequestBody TaskInfo taskInfo){
		if(taskInfo!=null) taskInfo.setStatus(1);//查询可用的
		try {
			Page<Task> result= manageService.findTasksByPage(taskInfo);
			return new SimpleResponse(CommonSatus.SUCCESS.getCode(), CommonSatus.SUCCESS.getCode(),result);
		} catch (Exception e) {
			log.error("query tasks exception ",e);
			return new SimpleResponse(CommonSatus.SERVICE_ERROR.getCode(), CommonSatus.SERVICE_ERROR.getCode(),null);
		}
	}
	
	
	/**
	 * 手动触发，重发任务
	 * @return
	 */
	@RequestMapping(value="/trigger",method=RequestMethod.PUT)
	public SimpleResponse triggerTask(Integer serviceId){
		if(serviceId==null) return new SimpleResponse(CommonSatus.PARAM_NULL.getCode(), CommonSatus.PARAM_NULL.getMsg());
		try {
			TaskService service=serviceDao.queryTaskService(serviceId);
			if(service==null) return new SimpleResponse(TaskStatus.TASK_NULL.getCode(), TaskStatus.TASK_NULL.getMsg());
			if(StringUtils.isBlank(service.getService())) return new SimpleResponse(TaskStatus.TASK_SERVICE_NULL.getCode(), TaskStatus.TASK_SERVICE_NULL.getCode());
			if(StringUtils.isBlank(service.getClientid())) return new SimpleResponse(TaskStatus.TASK_CLIENTID_NULL.getCode(),TaskStatus.TASK_CLIENTID_NULL.getMsg());
			SocketPoolManager.sendtoConsumer(service.getClientid(), service.getService());
			return new SimpleResponse(CommonSatus.SUCCESS.getCode(), CommonSatus.SUCCESS.getMsg());
		} catch (Exception e) {
			log.error("trigger exception ",e);
			return new SimpleResponse(TaskStatus.TASK_TRIGGER_FAILED.getCode(), TaskStatus.TASK_TRIGGER_FAILED.getMsg());
		}
	}
	
	/**
	 * 根据id删除task,逻辑删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delById",method=RequestMethod.DELETE)
	public SimpleResponse delTaskById(Integer id){
		if(id==null) return new SimpleResponse(CommonSatus.PARAM_NULL.getCode(), CommonSatus.PARAM_NULL.getMsg());
		try {
			manageService.delTaskById(id);
			return new SimpleResponse(CommonSatus.SUCCESS.getCode(), CommonSatus.SUCCESS.getMsg());
		} catch (Exception e) {
			log.error("del task exception",e);
			return new SimpleResponse(TaskStatus.TASK_DEL_FAILED.getCode(), TaskStatus.TASK_DEL_FAILED.getMsg());
		}
	}
	
	/**
	 * 删除任务(逻辑删除)
	 * @return
	 */
	@RequestMapping(value="/delByTaskId",method=RequestMethod.DELETE)
	public SimpleResponse delTaskByTaskId(String taskId){
		if(StringUtils.isBlank(taskId)) return new SimpleResponse(CommonSatus.PARAM_NULL.getCode(), CommonSatus.PARAM_NULL.getMsg());
		try {
			manageService.delTaskByTaskId(taskId);
			return new SimpleResponse(CommonSatus.SUCCESS.getCode(), CommonSatus.SUCCESS.getMsg());
		} catch (Exception e) {
			log.error("delete exception ",e);
			return new SimpleResponse(TaskStatus.TASK_DEL_FAILED.getCode(), TaskStatus.TASK_DEL_FAILED.getMsg());
		}
	}
	
	/**
	 * 查询池子信息
	 * @return
	 */
	@RequestMapping(value="/pool/list",method=RequestMethod.GET)
	public SimpleResponse queryTaskPool(){
		SimpleResponse resp= new SimpleResponse(CommonSatus.SUCCESS.getCode(),CommonSatus.SUCCESS.getMsg());
		List<TaskBasePool> pools=new ArrayList<TaskBasePool>();
		for(String schedule:TaskPoolManager.getScheduel_task_pool().keySet()){
			pools.add(new TaskBasePool(schedule,TaskPoolManager.getScheduel_task_pool().get(schedule).size()));
		}
		resp.setResult(pools);
		return resp;
	}
	/**
	 * 查询每个任务池的详细任务
	 * @return
	 */
	@RequestMapping(value="/pool/tasks",method=RequestMethod.GET)
	public SimpleResponse queryPoolTasks(String schedule){
		if(StringUtils.isBlank(schedule)) return new SimpleResponse(CommonSatus.PARAM_NULL.getCode(), CommonSatus.PARAM_NULL.getMsg());
		if(!TaskPoolManager.getScheduel_task_pool().containsKey(schedule)) return new SimpleResponse(CommonSatus.PARAM_AUTH_FAILED.getCode(), CommonSatus.PARAM_AUTH_FAILED.getMsg());
		List<Task> pools=new ArrayList<Task>();
		SimpleResponse resp= new SimpleResponse(CommonSatus.SUCCESS.getCode(),CommonSatus.SUCCESS.getMsg());
		pools.addAll(TaskPoolManager.getScheduel_task_pool().get(schedule));
		resp.setResult(pools);
		//可以用subList来返回一定数量的list
		return resp;
	}
	
	
	/**
	 * 参数转换成dao的实体对象
	 * @param taskInfo
	 * @return
	 */
	private Task changeToTask(TaskInfo taskInfo) {
		Task task=new Task();
		task.setClientid(taskInfo.getClientid());
		task.setCreateType(task.getCreateType());
		task.setDay(task.getDay());
		task.setExecutetime(taskInfo.getExecutetime());
		task.setFix_range_days(taskInfo.getFix_range_days());
		task.setFix_range_detail(taskInfo.getFix_range_detail());
		task.setFix_range_hours(taskInfo.getFix_range_hours());
		task.setFix_range_month(taskInfo.getFix_range_month());
		task.setFix_range_minutes(taskInfo.getFix_range_minutes());
		task.setFix_range_weeks(taskInfo.getFix_range_weeks());
		task.setHour(taskInfo.getHour());
		task.setMonth(taskInfo.getMonth());
		task.setMinute(taskInfo.getminute());
		task.setRange_endtime(taskInfo.getRange_endtime());
		task.setRange_starttime(taskInfo.getRange_starttime());
		task.setRate_days(taskInfo.getRate_days());
		task.setRate_hours(taskInfo.getRate_hours());
		task.setRate_months(taskInfo.getRate_months());
		task.setRate_minutes(taskInfo.getRate_minutes());
		task.setRate_weeks(taskInfo.getRate_weeks());
		task.setService(taskInfo.getService());
		task.setStatus(taskInfo.getStatus());
		task.setTaskid(taskInfo.getTaskid());
		task.setWeek(taskInfo.getWeek());
		task.setYear(taskInfo.getYear());
		
		return task;
	}
	
	/**
	 * 注册任务 校验任务参数是否有效
	 * @param taskInfo
	 * @return
	 */
	private TaskStatus validateParam(TaskInfo taskInfo) {
		if(taskInfo==null) return TaskStatus.TASK_NULL;
		
		if(StringUtils.isBlank(taskInfo.getClientid())) return TaskStatus.TASK_CLIENTID_NULL;
		
		if(StringUtils.isBlank(taskInfo.getService())) return TaskStatus.TASK_SERVICE_NULL;
		
		//频率都为空
		if(taskInfo.getRate_days()==0 && taskInfo.getRate_hours()==0 &&taskInfo.getRate_months()==0 && taskInfo.getRate_minutes()==0 && taskInfo.getRate_weeks() ==0){
			//并且定点时间也为空，则参数不正确
			if(StringUtils.isBlank(taskInfo.getminute())&& StringUtils.isBlank(taskInfo.getHour()) && StringUtils.isBlank(taskInfo.getDay()) && StringUtils.isBlank(taskInfo.getWeek()) && StringUtils.isBlank(taskInfo.getMonth())){
				return TaskStatus.TASK_RATE_FIX_NULL;
			}
			if(StringUtils.isBlank(taskInfo.getminute()))taskInfo.setminute("0");
		}
		if(taskInfo.getExecutetime()==null) taskInfo.setExecutetime(new Date());
		
		//校验时间范围是否有效
		if(taskInfo.getRange_starttime()!=null && taskInfo.getRange_endtime()!=null){
			if(taskInfo.getRange_endtime().before(taskInfo.getRange_starttime())) return TaskStatus.TASK_RANGE_TIME_ERROR;
		}
		
		//校验是否过期
		if(taskInfo.getRange_endtime()!=null){
			if(taskInfo.getRange_endtime().before(new Date())) return TaskStatus.TASK_RANGE_EXPIRED;
		}
		
		//task设置status和createtype
		taskInfo.setStatus(1);//可用的
		taskInfo.setCreateType(3);//后台管理注册
		return TaskStatus.TASK_SUCCESS;
		
	}
	
	
	
	
}
