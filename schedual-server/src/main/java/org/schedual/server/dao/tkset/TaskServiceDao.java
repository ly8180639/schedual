package org.schedual.server.dao.tkset;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.schedual.server.dao.tkset.entity.Task;
import org.schedual.server.dao.tkset.entity.TaskService;

public interface TaskServiceDao {
	/**
	 * 获取所有的日志数量
	 * @return
	 */
	public int findAllCount();
	
	
	/**
	 * 获取所有的日志信息
	 * @return
	 */
	public List<TaskService> findAll();
	
	/**
	 * 根据Id查询service信息
	 * @return
	 */
	public TaskService findById(@Param("id") int id);
	
	/**
	 * 
	 * 保存一条服务发送的日志信息
	 * @param log
	 * @return
	 */
	public int saveInitTaskService(Task log);
	
	
	/**
	 * 更新日志状态，确认信息是否已成送达到客户端
	 * @param status
	 * @param id
	 * @return
	 */
	public int updateServiceStatus(@Param("id") int id,@Param("status") int status, @Param("errmsg") String errmsg, @Param("clientrectime") Date clientrectime);
	
	
	/**
	 * 插入新老taskid的对应关系
	 */
	public int saveOldTaskRel(List<Map<String,Object>> oldTaskRel);
	
	/**
	 * 插入新老tagkid的对应关系
	 */
	public int saveOldTagRel(Map<String,Object> oldTagRel);
	
	public int saveOldSortRel(Map<String,Object> oldSortRel);
	
	public int saveOldCommentRel(@Param("oldId") Long oldCommentId,@Param("newId") Long newCommentId);
	
	public List<Map<Long,Long>> getAllTaskIdRel();
	
	public List<Map<Long,Long>> getAllTagIdRel();
	
	public List<Map<Long,Long>> getAllSortIdRel();
	
	public List<Map<Long,Long>> getAllCommentIdRel();
	
}
