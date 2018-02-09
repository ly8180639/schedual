<%@page import="org.schedual.server.*"%>
<%@page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	/* out.println("进入池子大小:"+TaskIntoPoolAndDbRunnable.getQueueSize());
	out.write("<br>"); */
	out.println("各调度池大小：");
	out.write("<br>");
	for(RateMinute scheduel:RateMinute.values()){
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(scheduel.toString());
		if(tasks.size()!=0){
			out.println("scheduel:"+scheduel+"--size:<a href=\"task.jsp?scheduel="+scheduel.toString()+"\">"+tasks.size()+"</a>");
		}else{
			//out.println("scheduel:"+scheduel+"--size:<a href=\"task.jsp?scheduel="+scheduel.toString()+"\">"+tasks.size()+"</a>");
			out.println("scheduel:"+scheduel+"--size:"+tasks.size());
		}
		out.write("<br>");
	}
	for(FixMinute scheduel:FixMinute.values()){
		Set<Task> tasks=TaskPoolManager.getScheduel_task_pool().get(scheduel.toString());
		if(tasks.size()!=0){
			out.println("scheduel:"+scheduel+"--size:<a href=\"task.jsp?scheduel="+scheduel.toString()+"\">"+tasks.size()+"</a>");
		}else{
			out.println("scheduel:"+scheduel+"--size:"+tasks.size());
		}
		out.write("<br>");
	}
	/* out.write("<br>");
	out.println("service pool:"+ServiceSendToClientRunnable.getQueueSize());
	out.write("<br>");
	ApplicationContext actx=WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	
	out.println("成功发送总数:");
	out.write("<br>");
	out.println(SocketPoolManager.count); */
	out.println("在线连接:<br>");
	out.println(SocketPoolManager.showConn());
	
%>
