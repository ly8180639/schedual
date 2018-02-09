package org.schedual.server.controller.out.status;

/**
 * 01开头
 * @author dell
 *
 */
public enum TaskStatus {
	TASK_SUCCESS("01000","success"),TASK_NULL("01001","任务参数为空"),TASK_RATE_FIX_NULL("01002","任务的频率和定点配置都为空"),
	TASK_RANGE_TIME_ERROR("01003","范围起始时间晚于结束时间"),TASK_RANGE_EXPIRED("01004","该任务配置已经过期"),
	TASK_SERVICE_NULL("01005","任务的service为空"),TASK_CLIENTID_NULL("01006","任务的clientId为空"),
	TASK_REGIST_FAILED("01007","任务注册失败"),TASK_TRIGGER_FAILED("01008","任务触发失败"),
	TASK_DEL_FAILED("01009","任务删除失败");
	
	
	private String code;
	
	private String msg;
	
	private TaskStatus(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	


	public String getCode() {
		return code;
	}




	public void setCode(String code) {
		this.code = code;
	}




	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
