<%@page import="com.fosun.cloudjet.task.dao.tkset.entity.Task"%>
<%@page import="java.util.Set"%>
<%@page import="com.fosun.cloudjet.task.manager.TaskPoolManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String scheduel=request.getParameter("scheduel");
	out.println("调度池--"+scheduel+"中的任务:<br><br>");
	Set<Task> tasks= TaskPoolManager.getScheduel_task_pool().get(scheduel);
	if(tasks!=null && tasks.size()>0){
		for(Task tk:tasks){
			out.println(tk);
			out.println("------------------------------------------------------------------------<br>");
		}	
	}
	
	out.println("<br>");
	
	out.println("<a href=\"index.jsp\">返回</a>");
	
%>