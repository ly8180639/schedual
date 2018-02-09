package org.schedual.server.controller.out;

public class TaskBasePool {
	private String schedule;
	
	private int count;

	
	
	

	public TaskBasePool() {
		super();
	}

	public TaskBasePool(String schedule, int count) {
		super();
		this.schedule = schedule;
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	
	
	
}
