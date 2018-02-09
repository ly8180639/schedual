package org.schedual.server.service;

import org.schedual.server.controller.in.TaskInfo;
import org.schedual.server.controller.out.Page;
import org.schedual.server.dao.tkset.entity.Task;


public interface ITaskManagerService {
	
	/**
	 * 
	 * @param 注册task
	 * @return
	 */
	public boolean registTask(Task task) throws Exception;
	
	/**
	 * 分页查找任务
	 * @param taskInfo
	 * @return
	 */
	public Page<Task> findTasksByPage(TaskInfo taskInfo)  throws Exception;
	
	
	/**
	 * 根据id删除任务，逻辑删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void delTaskById(int id) throws Exception;
	
	/**
	 * 根据taskId删除任务，客户端传过来的逻辑删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void delTaskByTaskId(String taskid) throws Exception;
	
	/**
	 * 根据id更新task
	 * @param id
	 * @throws Exception
	 */
	public int updateTaskById(Task task) throws Exception;
	
	/**
	 * 根据taskid更新task
	 * @param id
	 * @throws Exception
	 */
	public int updateTaskByTaskId(Task task) throws Exception;
	
	/**
	 * 根据taskid更新task
	 * @param id
	 * @throws Exception
	 */
	public void updateTaskRemindTimeByTaskId(Task task) throws Exception;
	
	
}
