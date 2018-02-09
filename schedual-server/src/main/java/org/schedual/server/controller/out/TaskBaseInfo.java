package org.schedual.server.controller.out;

import java.util.Date;

public class TaskBaseInfo {
	private int id;
	
	private String schedule;
	
	private String rate;
	
	private Date rateExetime;
	
	private String fixtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Date getRateExetime() {
		return rateExetime;
	}

	public void setRateExetime(Date rateExetime) {
		this.rateExetime = rateExetime;
	}

	public String getFixtime() {
		return fixtime;
	}

	public void setFixtime(String fixtime) {
		this.fixtime = fixtime;
	}
	
	
}
