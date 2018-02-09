package com.ly.schedual.client.utils;

import java.lang.reflect.Field;
import java.util.Date;


public class JSONUtils {
	/**
	 * 简单的对象转jsonstr
	 * @param o
	 * @return
	 */
	public static String toJsonStr(Object o){
		StringBuilder sb=new StringBuilder("{");
		Class<?> clazz= o.getClass();
		Field[] fileds =clazz.getDeclaredFields();
		for (int i = 0; i < fileds.length; i++) {
			 Field f = fileds[i];
			 f.setAccessible(true);
			 Object val=null;
			 try {
				val=f.get(o);
				if(val==null || "".equals(val)) continue;
				String type = f.getGenericType().toString();
				if(type.equals("class java.util.Date")){
					sb.append("\"").append(f.getName()).append("\":\"").append(((Date)val).getTime()).append("\",");
				 }else if(type.equals("class java.lang.String")){
					 String strVal=val.toString();
					 if(strVal.startsWith("{")){//说明是json格式，把"转成'
						 strVal=strVal.replaceAll("\"", "'");
					 }
					 sb.append("\"").append(f.getName()).append("\":\"").append(strVal).append("\",");
				 }else{
					 sb.append("\"").append(f.getName()).append("\":\"").append(val).append("\",");
				 } 
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if(sb.length()>1){//删除最后的逗号
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.append("}").toString();
	}
}
