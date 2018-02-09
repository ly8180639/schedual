package org.schedual.server.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.schedual.server.dao.tkset.entity.Task;

public class JSONUtils {
	/** 
	 * 轻量级转换，满足项目基本转换功能
     * JSON字符串转成Object对象<br> 
     * @param json 
     * @param clazz 
     * @return 
     */ 
	public static Object toObject(String json, Class<?> clazz){
		 if (json == null || json.equals("")) {  
	            return null;  
	        }
		 JSONObject jsObj=JSONObject.fromObject(json);
		 
		 Object object = null;
		 try {
			object = clazz.newInstance();
			Field[] fileds =clazz.getDeclaredFields();
			for (int i = 0; i < fileds.length; i++) {
				 Field f = fileds[i];
				 if(!jsObj.containsKey(f.getName()) || StringUtils.isBlank(jsObj.getString(f.getName()))) continue;
				 f.setAccessible(true);
				 String type = f.getGenericType().toString();
				 if (type.equals("class java.lang.Integer") || type.equals("int")){
					f.set(object, jsObj.getInt(f.getName())); 
				 }else if(type.equals("class java.lang.Long") || type.equals("long")){
						f.set(object, jsObj.getLong(f.getName())); 
				 }else if(type.equals("class java.lang.String")){
						f.set(object, jsObj.getString(f.getName())); 
				 }else if(type.equals("class java.util.Date")){
					 	//date类型传long类型即可
					 	Long d=jsObj.getLong(f.getName());
						f.set(object, new Date(d)); 
				 }  
				 
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	/** 
	 * 轻量级转换，满足项目基本转换功能
     * JSON字符串转成Map对象<br> 
     * @param json 
     * @param clazz 
     * @return 
     */ 
	public static Object toMap(String json){
		 return toMap(json,null);
	}
	/** 
	 * 轻量级转换，满足项目基本转换功能
     * JSON字符串转成Map对象<br> 
     * @param json 
     * @param clazz 
     * @return 
     */ 
	public static Map<String,String> toMap(String json, Map<String,String> m){
		if(m==null)m=new HashMap<String,String>();
		 if (json == null || json.equals("")) {  
	            return null;  
	        }
		 JSONObject jsObj=JSONObject.fromObject(json);
		 
		 for (Iterator<String> keys = jsObj.keys(); keys.hasNext();) {
			 String key=keys.next();
			 m.put(key, jsObj.getString(key));
		 }
		 return m;
	}
	
	public static boolean isJSONstr(String jsonstr){
		return jsonstr!=null && jsonstr.startsWith("{") &&jsonstr.endsWith("}");
	}
	
	public static void main(String[] args) {
		//String data="{\"clientid\":\"fuxing\",\"executetime\":\"\",\"service\":\"lyservice\",\"state\":1,\"rate_minutes\":3,\"lexecutetime\":\"\"}";
		String data="{\"clientid\":\"fuxing\",\"service\":\"lyservice1\",\"state\":1,\"rate_minutes\":1,\"executetime\":\"1491379091550\"}";
		Task t=(Task) toObject(data,Task.class);
		System.out.println(JSONObject.fromObject(t));
		
	}
	
}
