package org.schedual.server.service;

import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.dao.tkset.entity.TaskService;

public interface ITaskService {
	
	void saveTask(Task task);
	
	void saveTaskService(Task log);
	
	void saveService(Task service);
	
	void confirmService(int id,long recivetime);
	
	TaskService queryTaskService(int serviceId);
	
}
