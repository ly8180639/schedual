package org.schedual.server.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.schedual.server.dao.tkset.TaskDao;
import org.schedual.server.dao.tkset.entity.Task;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据库消费能力测试
 * @author dell
 *
 */
//@Component
public class TestSaveDBRunnable implements InitializingBean {
	@Autowired
	private TaskDao dao;

	Logger log = Logger.getLogger("SaveDB");
	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		ExecutorService service = Executors.newCachedThreadPool();
		final CountDownLatch begin = new CountDownLatch(1);

		//final CountDownLatch end = new CountDownLatch(10);

		for (int i = 0; i < 200; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					try {
						begin.await();
						while (true) {
							String service = queue.take();
							Task t = new Task();
							t.setClientid("fuxing");
							t.setService(service);
							dao.saveTask(t);
						}
						// end.countDown();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		log.info("start1....");
		for (int i = 0; i < 10000; i++) {
			queue.offer("lyservice" + i);
		}
		log.info("start2....");
		begin.countDown();
		while(true){
			Thread.sleep(100);
			if(queue.size()==0){
				log.info("finished!!");
				break;
			}
		}

		

	}

}
