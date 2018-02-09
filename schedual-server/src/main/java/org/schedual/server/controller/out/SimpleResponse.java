package org.schedual.server.controller.out;

public class SimpleResponse {
	
	private String status;
	
	private String msg;
	
	private Object result;
	
	
	public SimpleResponse() {
		super();
	}


	public SimpleResponse(String status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}


	public SimpleResponse(String status, String msg, Object result) {
		super();
		this.status = status;
		this.msg = msg;
		this.result = result;
	}


	public Object getResult() {
		return result;
	}
	

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public void setResult(Object result) {
		this.result = result;
	}
	
	
	
}
