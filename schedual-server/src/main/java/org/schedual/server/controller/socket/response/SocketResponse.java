package org.schedual.server.controller.socket.response;


public class SocketResponse {
	private String code;
	
	private String msg;
	
	private String result;
	
	
	
	
	public SocketResponse() {
		super();
	}

	public SocketResponse(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public SocketResponse(String code, String msg, String result) {
		super();
		this.code = code;
		this.msg = msg;
		this.result = result;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public boolean isSuccess(){
		return SocketRespCode.SUCCESS_CODE.equals(this.code);
	}
	
	
	
}
