package com.ly.schedual.client.response;

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
	/**
	 * json change to response
	 * @param result
	 * @return
	 */
	public static SocketResponse bulidByJSONStr(String result){
		SocketResponse resp=new SocketResponse();
		int codeStart=result.indexOf("\"code\":\"")+"\"code\":\"".length();
		if(codeStart>"\"code\":\"".length()){
			int codeEnd=result.indexOf("\",\"", codeStart);
			resp.setCode(result.substring(codeStart,codeEnd));
		}
		
		
		int msgStart=result.indexOf("\"msg\":\"")+"\"msg\":\"".length();
		if(msgStart>"\"msg\":\"".length()){
			int msgEnd=result.indexOf("\",\"", msgStart);
			resp.setMsg(result.substring(msgStart,msgEnd));
		}
		
		
		int resultStart=result.indexOf("\"result\":\"")+"\"result\":\"".length();
		if(resultStart>"\"result\":\"".length()){
			int resultEnd=result.indexOf("\"", resultStart);
			resp.setResult(result.substring(resultStart,resultEnd));
		}
		return resp;
	}

	@Override
	public String toString() {
		return "SocketResponse [code=" + code + ", msg=" + msg + ", result="
				+ result + "]";
	}
	
	
	
}
