package com.ly.schedual.client.request;

public enum RequestURL {
	REGIST("/registTask"),
	DELBYID("/delById"),
	EDITBYTASKID("/editTask"),
	EDITREMINDTIME("/editRemindTime"),
	DELBYTASKID("/delByTaskId");
	
	
	private String url;

	private RequestURL(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.url;
	}
}
