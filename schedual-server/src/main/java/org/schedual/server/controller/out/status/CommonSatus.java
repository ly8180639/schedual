package org.schedual.server.controller.out.status;

public enum CommonSatus {
	SUCCESS("000000","成功"),
	PARAM_AUTH_FAILED("000001","参数校验失败"),
	LOGIN_AUTH_FAILED("000002","登录权限校验失败"),
	SERVICE_ERROR("000003","业务执行失败"),
	PARAM_NULL("000004","参数为空");
	
	
	private String code;
	
	private String msg;
	
	private CommonSatus(String code, String msg) {
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
