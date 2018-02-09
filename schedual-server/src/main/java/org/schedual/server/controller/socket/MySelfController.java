package org.schedual.server.controller.socket;


import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.schedual.server.annotation.MyController;
import org.schedual.server.annotation.MyRequestMapping;
import org.schedual.server.controller.socket.response.SocketRespCode;
import org.schedual.server.controller.socket.response.SocketResponse;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.service.ITaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;

@MyController
public class MySelfController{
	Logger log=Logger.getLogger(MySelfController.class);
	
	@Autowired
	private ITaskManagerService manageService;
	
	public MySelfController() {
		
	}
	@MyRequestMapping("/registTask")
	public SocketResponse registTask(Task taskInfo){
		log.info("request for socket url /registTask,param=>"+JSONObject.fromObject(taskInfo));
		SocketResponse valresp=validateTask(taskInfo);
		if(!valresp.isSuccess()) return valresp;
		taskInfo.setStatus(1);//可用的
		taskInfo.setCreateType(2);//后台管理注册
		try {
			if(manageService.registTask(taskInfo)){
				return new SocketResponse(SocketRespCode.SUCCESS_CODE,"regist success",taskInfo.getId()+"");
			}
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"regist failed","insert 0 record");
		} catch (Exception e) {
			log.error("regist exeption ",e);
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"regist failed",e.getMessage());
		}
	}
	private SocketResponse validateTask(Task taskInfo) {
		if(taskInfo==null) return new SocketResponse(SocketRespCode.NULLPARAM_CODE, "param is null");
		if(StringUtils.isBlank(taskInfo.getClientid())) return new SocketResponse(SocketRespCode.NULLPARAM_CODE, "clientid is null");
		
		if(StringUtils.isBlank(taskInfo.getService())) return new SocketResponse(SocketRespCode.NULLPARAM_CODE, "service is null");
		
		if(taskInfo.getRate_days()==0 && taskInfo.getRate_hours()==0 &&taskInfo.getRate_months()==0 && taskInfo.getRate_minutes()==0 && taskInfo.getRate_weeks() ==0){
			//并且定点时间也为空，则参数不正确
			if(StringUtils.isBlank(taskInfo.getMinute())&& StringUtils.isBlank(taskInfo.getHour()) && StringUtils.isBlank(taskInfo.getDay()) && StringUtils.isBlank(taskInfo.getWeek()) && StringUtils.isBlank(taskInfo.getMonth())){
				return  new SocketResponse(SocketRespCode.PARAMAUTHFAILED_CODE, "can't set rate and fix time");
			}
			if(StringUtils.isBlank(taskInfo.getMinute()))taskInfo.setMinute("0");
		}
		
		if(taskInfo.getExecutetime()==null) taskInfo.setExecutetime(new Date());
		//校验时间范围是否有效
		if(taskInfo.getRange_starttime()!=null && taskInfo.getRange_endtime()!=null){
			if(taskInfo.getRange_endtime().before(taskInfo.getRange_starttime())) return new SocketResponse(SocketRespCode.PARAMAUTHFAILED_CODE, "Start time greater than end time");
		}
		if(taskInfo.getRange_endtime()!=null){
			if(taskInfo.getRange_endtime().before(new Date())) return new SocketResponse(SocketRespCode.PARAMAUTHFAILED_CODE, "range time expired");
		}
		return new SocketResponse(SocketRespCode.SUCCESS_CODE, SocketRespCode.SUCCESS_MSG);
	}
	
	
	@MyRequestMapping("/editTask")
	public SocketResponse editTask(Task taskInfo){
		log.info("request for socket url /editTask,param=>"+JSONObject.fromObject(taskInfo));
		SocketResponse valresp=validateTask(taskInfo);
		if(!valresp.isSuccess()) return valresp;
		taskInfo.setStatus(1);//可用的
		taskInfo.setCreateType(2);//后台管理注册
		try {
			if(manageService.updateTaskByTaskId(taskInfo)>0){
				return new SocketResponse(SocketRespCode.SUCCESS_CODE,"edit reminder success",taskInfo.getId()+"");
			}
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"edit reminder failed","insert 0 record");
		} catch (Exception e) {
			log.error("regist exeption ",e);
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"edit reminder failed",e.getMessage());
		}
	}
	/**
	 * <br/>Description:更新定点提醒时间，如果过期了，则把stats设为2即可
	 * 时间的参数必须是Long类型的字符串
	 * <p>Author:liuyang</p>
	 * 2017年12月1日
	 */
	@MyRequestMapping("/editRemindTime")
	public SocketResponse editTask(Map<String,String> request){
		log.info("request for socket url /expiredTask,param=>"+request);
		String reminderId=request.get("taskId");
		String remindTime=request.get("remindTime");
		Long lremindTime=null;
		
		try {
			lremindTime=Long.parseLong(remindTime);
		} catch (NumberFormatException e1) {
			log.error("param remindTime is error");
			return  new SocketResponse(SocketRespCode.HANDLEERR_CODE,"edit remindTime failed,{}",remindTime);
		}
		
		Date remTime=new Date(lremindTime);
		int status=0;
		if(new Date().after(remTime)){
			log.info("remindtime is expired status is 2");
			status=2;
		}else{
			status=1;
		}
		Task task=new Task();
		task.setTaskid(reminderId);
		task.setStatus(status);
		Calendar cal=Calendar.getInstance();
		cal.setTime(remTime);
		
		
		task.setYear(String.valueOf(cal.get(Calendar.YEAR)));
		task.setMonth(String.valueOf(cal.get(Calendar.MONTH) + 1));
		task.setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		task.setHour(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
		task.setMinute(String.valueOf(cal.get(Calendar.MINUTE)));
		
		try {
			manageService.updateTaskRemindTimeByTaskId(task);
			return new SocketResponse(SocketRespCode.SUCCESS_CODE,"edit remindTime success",reminderId);
		} catch (Exception e) {
			log.error("regist exeption ",e);
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"edit remindTime failed",e.getMessage());
		}
	}
	
	
	
	/**
	 * 根据taskId删除
	 * @param request
	 * @return
	 */
	@MyRequestMapping("/delByTaskId")
	public SocketResponse delByTaskId(Map<String,String> request){
		//System.out.println("request:"+JSONObject.fromObject(request));
		String taskId=request.get("taskid");
		if(taskId==null || "".equals(taskId)) return new SocketResponse(SocketRespCode.NULLPARAM_CODE, "param is null");
		try {
			manageService.delTaskByTaskId(taskId);
		} catch (Exception e) {
			log.error("delete task byTaskId exception---"+taskId,e);
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"deletebyTaskId failed",e.getMessage());
		}
		return new SocketResponse(SocketRespCode.SUCCESS_CODE,SocketRespCode.SUCCESS_MSG);
	}
	/**
	 * 根据id删除
	 * @param request
	 * @return
	 */
	@MyRequestMapping("/delById")
	public SocketResponse delById(Map<String,String> request){
		//System.out.println("request:"+JSONObject.fromObject(request));
		String id=request.get("id");
		if(id==null || "".equals(id)) return new SocketResponse(SocketRespCode.NULLPARAM_CODE, "param is null");
		int tid=0;
		try {
			tid = Integer.parseInt(id);
		} catch (Exception e) {
			return new SocketResponse(SocketRespCode.PARAMAUTHFAILED_CODE,"regist failed","id must be a number");
		}
		try {
			manageService.delTaskById(tid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("delete task byId exception---"+id,e);
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"deletebyId failed",e.getMessage());
		}
		return new SocketResponse(SocketRespCode.SUCCESS_CODE,SocketRespCode.SUCCESS_MSG);
	}
	
	/**
	 * 根据id更新task
	 * @param request
	 * @return
	 */
	@MyRequestMapping("/updateById")
	public SocketResponse updateById(Task task){
		//System.out.println("request:"+JSONObject.fromObject(request));
		if(task==null || task.getId()==0) return new SocketResponse(SocketRespCode.NULLPARAM_CODE,"task is null or id is null");
		SocketResponse valresp=validateTask(task);
		task.setStatus(1);//因为校验通过，所以该任务的状态应该是可用的
		if(!valresp.isSuccess()) return valresp;
		try {
			int result=manageService.updateTaskById(task);
			if(result==0) return new SocketResponse(SocketRespCode.HANDLEERR_CODE, "task is not exist");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("udpate task exception---"+task.getId(),e);
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"udpate task failed",e.getLocalizedMessage());
		}
		return new SocketResponse(SocketRespCode.SUCCESS_CODE,SocketRespCode.SUCCESS_MSG);
	}
}
