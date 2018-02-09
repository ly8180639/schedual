package org.schedual.server.dao.tkset.entity;

import java.io.Serializable;
import java.util.Date;

public class TaskService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	/**
	 * task表id
	 */
	private String tid;
	
	/**
	 * 调度器名称，在RateMinute中
	 */
	private String scheduelname;
	
	private String clientid;
	
	private String service;
	
	private String status;
	
	private String errmsg;
	
	private Date clientrectime;
	
	private Date invoketime;
	
	
	

	public Date getClientrectime() {
		return clientrectime;
	}

	public void setClientrectime(Date clientrectime) {
		this.clientrectime = clientrectime;
	}

	public String getScheduelname() {
		return scheduelname;
	}

	public void setScheduelname(String scheduelname) {
		this.scheduelname = scheduelname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInvoketime() {
		return invoketime;
	}

	public void setInvoketime(Date invoketime) {
		this.invoketime = invoketime;
	}
	
}
