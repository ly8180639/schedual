package org.schedual.server.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 星任务   task.fosun.com</p> 
 * <p>Description:用不上，不过放着 </p> 
 * <p>Author:liuyang</p>
 * 2018年2月9日
 */
//@Component
public class RedisCache {

	 Logger logger=LoggerFactory.getLogger(RedisCache.class);
	 
	 private JedisPool pool = null;
	 
	 @Value("${redis.maxIdle}")
	 private  int maxIdle;
	 
	 @Value("${redis.maxTotal}")
	 private  int maxTotal;
	 
	 @Value("${redis.maxWaitMillis}")
	 private  long maxWaitMillis;
	 
	 @Value("${redis.ip}")
	 private  String ip;
	 
	 @Value("${redis.port}")
	 private  int port;
	 
	 @Value("${redis.password}")
	 private  String password;
	
	/** 
     * 初始化jedis连接池 
     */  
	@PostConstruct 
    public  void init()  
    {  
    	
    	logger.info("redis init..."+"ip->"+ip+"  port->"+port);
    	if(ip!=null){
    		ip=ip.trim();
    	}
    	if(password!=null){
    		password=password.trim();
    	}
    	JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
    	jedisPoolConfig.setMaxIdle(maxIdle);
    	jedisPoolConfig.setMaxTotal(maxTotal);
    	jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
    	
        // 构造连接池  
        pool = new JedisPool(jedisPoolConfig, ip, port);
        
        validate(jedisPoolConfig.getMaxTotal());
        
        logger.info("password is OK");
        
        logger.info("redis inited");
        
    } 

    /**
     * 
     * <br/>Description:校验并且把
     * <p>Author:liuyang</p>
     * 2017年9月15日
     */
	private  void validate(int num) {
		// TODO Auto-generated method stub
		List<Jedis> allJedis=new ArrayList<Jedis>();
		for(int i=0;i<num;i++){
			Jedis jedis = pool.getResource();
			jedis.auth(password);
			jedis.ping();
			allJedis.add(jedis);
		}
		
		//释放jedis
		for(Jedis jedis: allJedis){
			if(jedis!=null)jedis.close();
		}
		
	}

    
    /** 
     * <p>向redis存入key和value,并释放连接资源</p> 
     * <p>如果key已经存在 则覆盖</p> 
     * @param key 
     * @param value 
     * @return 成功 返回OK 失败返回 0 
     * @throws RedisException 
     */  
    public  void setInSet(String key,String ... value) throws RedisException{  
    	if(value==null) return;
    	Jedis jedis=null;
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			jedis.sadd(key,value);
		} catch(Exception e){
			logger.error("setKeyInList  exception",e);
			throw new RedisException("setKey Exception",e);
		} finally{
			if(jedis!=null)jedis.close();
		}
    }
    
    
    /** 
     * <p>向redis存入key和value,并释放连接资源</p> 
     * <p>如果key已经存在 则覆盖</p> 
     * @param key 
     * @param value 
     * @return 成功 返回OK 失败返回 0 
     * @throws RedisException 
     */  
    public  void set(String key,String value) throws RedisException{  
    	Jedis jedis=null;
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			String result=jedis.set(key,value);
			if(!"OK".equals(result)){
				throw new RedisException("setValue result is not OK");
			}
		}catch(Exception e){
			logger.error("setKey  exception",e);
			throw new RedisException("result  Exception",e);
		}finally{
			if(jedis!=null)jedis.close();
		}
    }
    
    /**
     * 删除
     * @param key
     * @throws RedisException
     */
    public  void del(String key) throws RedisException{  
    	Jedis jedis=null;
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			jedis.del(key);
		}catch(Exception e){
			logger.error("delKey  exception",e);
			throw new RedisException("result  Exception",e);
		}finally{
			if(jedis!=null)jedis.close();
		}
    }
    
    public  String get(String key) throws RedisException{  
    	Jedis jedis=null;
    	String val=null;
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			val=jedis.get(key);
		}catch(Exception e){
			logger.error("delKey  exception",e);
			throw new RedisException("result  Exception",e);
		}finally{
			if(jedis!=null)jedis.close();
		}
		return val;
    }
    
    /**
     * 
     * <br/>Description:模糊查询
     * <p>Author:liuyang</p>
     * 2017年12月13日
     */
    public  Map<String,String> keyLike(String key) throws RedisException{  
    	Jedis jedis=null;
    	Map<String,String> keyVal=new HashMap<String,String>();
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			Set<String> keys= jedis.keys(key);
			if(keys!=null && keys.size()>0){
 				for(String k:keys){
 					String value=jedis.get(k);
 					keyVal.put(k, value);
 				}
 			}
		}catch(Exception e){
			logger.error("keylike  exception",e);
			throw new RedisException("result  Exception",e);
		}finally{
			if(jedis!=null)jedis.close();
		}
		return keyVal;
    }
    
    
    /** 
     * <p>向redis存入key和value,并释放连接资源</p> 
     * <p>如果key已经存在 则覆盖</p> 
     * @param key 
     * @param value 
     * @return 成功 返回OK 失败返回 0 
     * @throws RedisException 
     */  
    public  void setUser(String key,UserSession session,int expired) throws RedisException{  
    	if(session==null) return;
    	String userid=session.getUserId();
    	Jedis jedis=null;
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			if(!jedis.exists(userid)){
				jedis.set(userid, JSONObject.toJSONString(session));
			}
			String result=jedis.set(key,JSONObject.toJSONString(session));//暂用老的方案
			if(!"OK".equals(result)){
				throw new RedisException("setValue result is not OK");
			}
		} catch(Exception e){
			logger.error("setKey  exception",e);
			throw new RedisException("setKey Exception",e);
		} finally{
			if(jedis!=null)jedis.close();
		}
    }
    
}