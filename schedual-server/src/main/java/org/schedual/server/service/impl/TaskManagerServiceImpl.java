package org.schedual.server.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.schedual.server.controller.in.TaskInfo;
import org.schedual.server.controller.out.Page;
import org.schedual.server.dao.tkset.TaskDao;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.handler.TaskPoolManager;
import org.schedual.server.service.ITaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskManagerServiceImpl implements ITaskManagerService{
	Logger log=Logger.getLogger(TaskManagerServiceImpl.class);
	
	@Autowired
	private TaskDao taskDao;
	
	/**
	 * 分页步骤
	 * 0.前端传过来的，当前页数
	 * 1.查询总记录数
	 * 2.根据数量算出总页数，当前的startIndex
	 * 3.根据startIndex,每页数量，获取当前页记录数
	 * 4.返回Page对象  总页数，总数量，当前页，详细记录
	 */
	@Override
	public Page<Task> findTasksByPage(TaskInfo taskInfo) throws Exception{
		
		
		int allCount=taskDao.findCountByPage(taskInfo);
		
		List<Task> content=taskDao.findTasksByPage(taskInfo);
		
		Page<Task> p=new Page<Task>(allCount,taskInfo,content);
		
		return p;
	}
	/**
	 * 注册之后需要把task信息注册进任务池中
	 * 
	 */
	@Override
	public boolean registTask(Task task) throws Exception{
		int n=taskDao.saveTask(task);
		try {
			TaskPoolManager.findAndPutPool(task);
		} catch (Exception e) {
			log.error("put into pool exception",e);
		}
		return n==1;
	}
	
	
	
	/**
	 * 根据id删除任务
	 * 
	 */
	@Override
	public void delTaskById(int id) throws Exception {
		// TODO Auto-generated method stub
		taskDao.delTaskById(id);
		TaskPoolManager.findAndRemoveTask(id);
	}
	
	/**
	 * 根据taskId删除任务
	 */
	@Override
	public void delTaskByTaskId(String taskid) throws Exception {
		// TODO Auto-generated method stub
		taskDao.delTaskByTaskId(taskid);
		
		List<Integer> ids=taskDao.findIdByTaskId(taskid);
		for (Integer id : ids) {
			TaskPoolManager.findAndRemoveTask(id);
		}
	}
	/**
	 * 更新task
	 */
	@Override
	public int updateTaskById(Task task) throws Exception {
		int updateCount=taskDao.updateTaskById(task);
		if(updateCount==0) return 0;
		TaskPoolManager.updateTask(task);
		return updateCount;
	}
	
	
	/**
	 * 
	 * <p>Description:根据taskId先找到id，然后利用上面的方法更新task</p>
	 * <p>Author:liuyang</p>
	 * @Title: updateTaskByTaskId
	 * 2017年11月30日
	 */
	@Override
	public int updateTaskByTaskId(Task task) throws Exception {
		List<Integer> ids= taskDao.findIdByTaskId(task.getTaskid());
		if(ids!=null && ids.size()>0){
			task.setId(ids.get(0));
			return updateTaskById(task);
		}else{
			log.error("updateTaskByTaskId failed,can't find id by taskid=>"+task.getTaskid());
		}
		return 0;
	}
	
	
	/**
	 * 
	 * <p>Description:</p>
	 * <p>Author:liuyang</p>
	 * @Title: updateTaskRemindTimeByTaskId
	 * 2017年12月1日
	 */
	@Override
	public void updateTaskRemindTimeByTaskId(Task task)
			throws Exception {
		
			Task tk=taskDao.findTaskByTaskId(task.getTaskid());
			task.setService(tk.getService());
			task.setClientid(tk.getClientid());
			task.setId(tk.getId());
			
			if(task.getStatus()==2)//过期的任务
			{
				TaskPoolManager.findAndRemoveTask(tk.getId());
			}else{//未过期的任务,更新时间
				TaskPoolManager.updateTask(task);
			}
			taskDao.updateTaskTimeById(task);
		
	}
	public static void main(String[] args) {
		Map<String,String> m=new HashMap<String,String>();
		m.put("liuyang", "ly");
		m.put("liuwei", "lw");
		
		
		m.put("liuyang", "zhongguo");
		
		System.out.println(m);
		
		
		HashSet<Task> t=new HashSet<Task>();
		t.add(new Task(1));
		t.add(new Task(2));
		t.add(new Task(3));
		t.add(new Task(4));
		
		Task t1=new Task();
		t1.setId(1);
		t1.setService("liuyang");
		t.add(t1);
		System.out.println(t.contains(t1));
		t.remove(t1);
		System.out.println(JSONArray.fromObject(t));
		
		System.out.println(JSONObject.fromObject(t1));
		
		
	}
	
	
	
	
	
	

}
