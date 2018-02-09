package org.schedual.server.dao.tkset.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;

public class Task {
	private int id;
	
	private String clientid;
	
	private String taskid;
	
	private String service;
	
	/**
	 * 不保存在task表中
	 */
	private String scheduelname;
	
	private int status;
	
	private int rate_minutes;
	
	private int rate_hours;
	
	private int rate_days;
	
	private int rate_weeks;
	
	private int rate_months;
	
	private Date executetime;
	
	
	
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
	 * 详细的范围，如3月7日和4月8日，没有此字段不好界定
	 */
	private String fix_range_detail;
	
	private String fix_range_minutes;
	
	private String fix_range_hours;
	
	private String fix_range_days;
	
	private String fix_range_weeks;
	
	private String fix_range_month;
	
	private Date range_starttime;
	
	
	private Date range_endtime;
	/**
	 * 1表示客户端同步注册，2.表示客户端异步注册，3表示后台管理注册，4表示手动数据库新增（跟程序没关）
	 */
	private int createType;
	
	
	private Date createtime;
	
	/**
	 * taskservice属性
	 */
	private int tmpServiceId;
	
	
	
	private String errmsg;
	
	
	
	
	
	public Task() {
		super();
	}

	public Task(int id) {
		this.id=id;
	}
	public int getCreateType() {
		return createType;
	}

	public void setCreateType(int createType) {
		this.createType = createType;
	}

	public String getFix_range_detail() {
		return fix_range_detail;
	}

	public void setFix_range_detail(String fix_range_detail) {
		this.fix_range_detail = fix_range_detail;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
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

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public int getTmpServiceId() {
		return tmpServiceId;
	}

	public void setTmpServiceId(int tmpServiceId) {
		this.tmpServiceId = tmpServiceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
	

	public String getScheduelname() {
		return scheduelname;
	}

	public void setScheduelname(String scheduelname) {
		this.scheduelname = scheduelname;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRate_minutes() {
		return rate_minutes;
	}

	public void setRate_minutes(int rate_minutes) {
		this.rate_minutes = rate_minutes;
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

	public Date getExecutetime() {
		return executetime;
	}

	public void setExecutetime(Date executetime) {
		this.executetime = executetime;
	}


	public String getFix_range_minutes() {
		return fix_range_minutes;
	}

	public void setFix_range_minutes(String fix_range_minutes) {
		this.fix_range_minutes = fix_range_minutes;
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

	public String getFix_range_month() {
		return fix_range_month;
	}

	public void setFix_range_month(String fix_range_month) {
		this.fix_range_month = fix_range_month;
	}

	public Date getRange_starttime() {
		return range_starttime;
	}

	public void setRange_starttime(Date range_starttime) {
		this.range_starttime = range_starttime;
	}

	public Date getRange_endtime() {
		return range_endtime;
	}

	public void setRange_endtime(Date range_endtime) {
		this.range_endtime = range_endtime;
	}

	
	

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Task){
			Task t=(Task)obj;
			if(t.getId()==this.id) return true;
		}
		return false;
	}
	
	
	
	@Override
	public String toString() {
		return "Task [id=" + id + ", clientid=" + clientid + ", taskid="
				+ taskid + ", service=" + service + ", scheduelname="
				+ scheduelname + ", status=" + status + ", rate_minutes="
				+ rate_minutes + ", rate_hours=" + rate_hours + ", rate_days="
				+ rate_days + ", rate_weeks=" + rate_weeks + ", rate_months="
				+ rate_months + ", executetime=" + executetime + ", year="
				+ year + ", month=" + month + ", day=" + day + ", week=" + week
				+ ", hour=" + hour + ", minute=" + minute
				+ ", fix_range_detail=" + fix_range_detail
				+ ", fix_range_minutes=" + fix_range_minutes
				+ ", fix_range_hours=" + fix_range_hours + ", fix_range_days="
				+ fix_range_days + ", fix_range_weeks=" + fix_range_weeks
				+ ", fix_range_month=" + fix_range_month + ", range_starttime="
				+ range_starttime + ", range_endtime=" + range_endtime
				+ ", createType=" + createType + ", createtime=" + createtime
				+ ", tmpServiceId=" + tmpServiceId + ", errmsg=" + errmsg + "]";
	}

	public static void main(String[] args) {
		Task t=new Task();
		t.setClientid("fuxing");
		//t.setExecutetime("2017-03-09 14:45:54");
		t.setRate_minutes(3);
		System.out.println(JSONObject.fromObject(t));
		String s="{\"clientid\":\"fuxing\",\"rate_minutes\":3,\"executetime\":\"2017-03-09 14:45:54\"}";
		System.out.println(s);
		Set<Task> sets=new HashSet<Task>();
		sets.add(t);
		System.out.println(sets);
	}
	
	
	
}
