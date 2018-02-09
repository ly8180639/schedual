package org.schedual.server.controller.in;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


public class TaskInfo extends PageRequest{
	
	
	
	private String clientid;
	
	private String taskid;
	
	private String service;
	
	/**
	 * 频率
	 */
	private int rate_minutes;
	
	private int rate_hours;
	
	private int rate_days;
	
	private int rate_weeks;
	
	private int rate_months;
	
	/**
	 * 下面的属性，不确定则为空或者*表示。
	 */
	private String year;
	private String month;
	private String day;
	private String week;
	
	private String hour;
	private String minute;
	
	/**
	 * 范围
	 */
	private String fix_range_detail;
	
	private String fix_range_hours;
	
	private String fix_range_days;
	
	private String fix_range_weeks;
	
	private String fix_range_month;
	
	private String fix_range_minutes;
	
	
	private int status;
	
	private int createType;
	
	
	
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCreateType() {
		return createType;
	}

	public void setCreateType(int createType) {
		this.createType = createType;
	}

	/**
	 * 执行时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date executetime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date range_starttime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date range_endtime;
	
	
	
	

	public String getFix_range_detail() {
		return fix_range_detail;
	}

	public void setFix_range_detail(String fix_range_detail) {
		this.fix_range_detail = fix_range_detail;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}



	public int getRate_minutes() {
		return rate_minutes;
	}

	public void setRate_minutes(int rate_minutes) {
		this.rate_minutes = rate_minutes;
	}
	
	
	
	public Date getExecutetime() {
		return executetime;
	}

	public void setExecutetime(Date executetime) {
		this.executetime = executetime;
	}

	public void setRange_starttime(Date range_starttime) {
		this.range_starttime = range_starttime;
	}

	public void setRange_endtime(Date range_endtime) {
		this.range_endtime = range_endtime;
	}

	public String getFix_range_hours() {
		return fix_range_hours;
	}

	public void setFix_range_hours(String fix_range_hours) {
		this.fix_range_hours = fix_range_hours;
	}

	public String getFix_range_days() {
		return fix_range_days;
	}

	public void setFix_range_days(String fix_range_days) {
		this.fix_range_days = fix_range_days;
	}

	public String getFix_range_weeks() {
		return fix_range_weeks;
	}

	public void setFix_range_weeks(String fix_range_weeks) {
		this.fix_range_weeks = fix_range_weeks;
	}
	
	
	
	
	public Date getRange_starttime() {
		return range_starttime;
	}

	public Date getRange_endtime() {
		return range_endtime;
	}

	public int getRate_hours() {
		return rate_hours;
	}

	public void setRate_hours(int rate_hours) {
		this.rate_hours = rate_hours;
	}

	public int getRate_days() {
		return rate_days;
	}

	public void setRate_days(int rate_days) {
		this.rate_days = rate_days;
	}

	public int getRate_weeks() {
		return rate_weeks;
	}

	public void setRate_weeks(int rate_weeks) {
		this.rate_weeks = rate_weeks;
	}

	public int getRate_months() {
		return rate_months;
	}

	public void setRate_months(int rate_months) {
		this.rate_months = rate_months;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getminute() {
		return minute;
	}

	public void setminute(String minute) {
		this.minute = minute;
	}

	public String getFix_range_month() {
		return fix_range_month;
	}

	public void setFix_range_month(String fix_range_month) {
		this.fix_range_month = fix_range_month;
	}

	public String getFix_range_minutes() {
		return fix_range_minutes;
	}

	public void setFix_range_minutes(String fix_range_minutes) {
		this.fix_range_minutes = fix_range_minutes;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	
	
}	
