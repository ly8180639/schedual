package org.schedual.sample;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ly.schedual.client.Producer;
import com.ly.schedual.client.dto.TaskInfo;
import com.ly.schedual.client.request.RequestURL;
import com.ly.schedual.client.response.SocketResponse;

/**
 * Hello world!
 *
 */
public class RegSchedualExample 
{
    public static void main( String[] args ) throws Exception
    {
    	//启动连接
    	ClassPathXmlApplicationContext actx=new ClassPathXmlApplicationContext("beans.xml");
    	//获得生产消息的对象
    	Producer sendClient= actx.getBean(Producer.class);
    	while(!sendClient.isConnected()){
    		System.out.println("连接未建立，不能发送数据...");
    		Thread.sleep(10000);
    	}
    	
    	TaskInfo  task=new TaskInfo();
    	task.setClientid("client_id1");//跟配置的需要保持一致，定时调度中心会在在定时时间点发送到该客户端
    	task.setExecutetime(new Date());//执行时间点,这里设成当前时间
    	task.setRate_minutes(1);//1分钟执行一次
    	task.setService("这个服务是每1分钟做一次的");
    	
    	//同步发送类，会有响应
		SocketResponse syncResp=sendClient.sendReq(RequestURL.REGIST.toString(),task.toJSONString());
		System.out.println("同步发送响应:"+syncResp);
		if(syncResp.isSuccess()){
			System.out.println("成功注册了一个每1分钟执行的服务，MessageConsumeImpl中可查看每1分钟推过来的信息");
		}
		
		//异步，相当于消费，直接将数据发送至客户端，无返回值
		task.setService("这个服务是每2分钟做一次的");
		task.setRate_minutes(2);
		sendClient.produce(task.toJSONString());
		System.out.println("成功注册了一个每2分钟执行的服务，MessageConsumeImpl中可查看每2分钟推过来的信息");
    	
    }
}
