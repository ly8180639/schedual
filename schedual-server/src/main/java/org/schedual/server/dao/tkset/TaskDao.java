package org.schedual.server.dao.tkset;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.schedual.server.controller.in.TaskInfo;
import org.schedual.server.dao.tkset.entity.Task;

public interface TaskDao {
	/**
	 * 获取任务数量
	 * @param status 1表示可用的，2表示逻辑删除的
	 * @return
	 */
	public int findCount(int status);
	
	/**
	 * 返回所有任务数量
	 * @return
	 */
	public int findAllCount();
	
	/**
	 * 返回所有可用的任务
	 * @return
	 */
	public List<Task> findAvailableTask();
	
	/**
	 * 新增一个任务
	 * @param task
	 * @return
	 */
	public int saveTask(Task task);
	
	/**
	 * 
	 * @param toState 更新成什么state
	 * @param id  主键 
	 * @return
	 */
	public int updateTaskStatus(@Param("id") int id,@Param("state") int state);
	/**
	 * 
	 * @param limit 从哪个limit开始load
	 * @param count load多少数量
	 * @return
	 */
	public List<Task> findPerCountTasks(@Param("start") int limit, @Param("count") int count);
	
	/**
	 * 批量更新超时的task
	 * @param ids
	 * @return
	 */
	public int updateExpiredTasks(List<Integer> ids);
	
	
	
	/**
	 * 分页获取总数
	 * @return
	 */
	public int findCountByPage(TaskInfo info);
	
	/**
	 * 分页查询具体
	 * @return
	 */
	public List<Task> findTasksByPage(TaskInfo info);
	
	/**
	 * @param 根据taskid 删除task 逻辑删除，更新字段
	 * @param id  主键 
	 * @return
	 */
	public int delTaskByTaskId(String taskid);
	
	/**
	 * @param 根据taskid 找到task的Id
	 * 
	 * @param id  主键 
	 * @return
	 */
	public List<Integer> findIdByTaskId(String taskid);
	
	/**
	 * @param 根据id 删除task 逻辑删除，更新字段
	 * @param id  主键 
	 * @return
	 */
	public int delTaskById(int id);
	
	/**
	 * 
	 * @param task 根据id更新task信息
	 * @return
	 */
	public int updateTaskById(Task task);
	
	/**
	 * <br/>Description:更新定点提醒时间，顺便更新状态
	 * <p>Author:liuyang</p>
	 * 2017年12月1日
	 */
	public int updateTaskTimeById(Task task);
	
	/**
	 * 
	 * <br/>Description:根据任务提醒id获取任务
	 * <p>Author:liuyang</p>
	 * 2017年12月1日
	 */
	public Task findTaskByTaskId(String taskid);
	
	
	
}
