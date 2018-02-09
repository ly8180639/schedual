package org.schedual.server.handler;


import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.schedual.server.annotation.MyController;
import org.schedual.server.annotation.MyRequestMapping;
import org.schedual.server.controller.socket.response.SocketRespCode;
import org.schedual.server.controller.socket.response.SocketResponse;
import org.schedual.server.utils.CommonUtils;
import org.schedual.server.utils.JSONUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public  class ReqHandlerInit implements ApplicationContextAware{
	
	static Logger log=Logger.getLogger(ReqHandlerInit.class);
	
	/**
	 * urlmapping  url对应object
	 */
	static Map<String,Object> urlObjectMapping=new HashMap<String,Object>();
	
	/**
	 * url对应method
	 */
	static Map<String,Method> urlMethodMapping=new HashMap<String,Method>();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Map<String, Object> rules=applicationContext.getBeansWithAnnotation(MyController.class);
		Set<String> keys=rules.keySet();
		for (String key : keys) {
			Object o=rules.get(key);
			Class<?> clazz=o.getClass();
			Method[] methods=clazz.getMethods();
			for(Method method:methods){
				MyRequestMapping req= method.getAnnotation(MyRequestMapping.class);
				if(req==null) continue;
				String requrl=req.value();
				urlObjectMapping.put(requrl, o);
				urlMethodMapping.put(requrl, method);
			}
		}
	}
	
	/**
	 * 
	 * @param url 
	 * @param params 
	 */
	public static SocketResponse invoke(String request, String params){
		
		if(!urlObjectMapping.containsKey(request)) return new SocketResponse(SocketRespCode.NOTFOUND_CODE, "Request Not Found");
		Method method=urlMethodMapping.get(request);
		Object obj=urlObjectMapping.get(request);
		Class<?>[] clazz=method.getParameterTypes();
		Object result=null;
		try {
			if(clazz.length<1){//方法的参数为空，则传任何参数都忽律
				result= method.invoke(obj);
			}else{
				result=method.invoke(obj, getMethodParams(params, clazz));
			}
		} catch(Exception e){
			e.printStackTrace();
			return new SocketResponse(SocketRespCode.HANDLEERR_CODE,"service execute error",e.getMessage());
		}
		if(result instanceof String){//String类型直接返回成功响应，result设为String值
			return new SocketResponse(SocketRespCode.SUCCESS_CODE, SocketRespCode.SUCCESS_MSG,(String)result);
		}else if(result instanceof Integer){
			return new SocketResponse(SocketRespCode.SUCCESS_CODE, "success",result+"");
		}else if(result instanceof SocketResponse){
			return (SocketResponse) result;
		}
		return new SocketResponse(SocketRespCode.HANDLEERR_CODE, "server method resulttype error");
	}
	

	/**
	 * 组装method的param，只有Map类型的参数回注入进去
	 * @param param
	 * @param clazz
	 * @return
	 */
	private static Object[] getMethodParams(String param,Class<?>[] clazz){
		Object[] params=new Object[clazz.length];
		for(int i=0;i<params.length;i++){
			params[i]=getVal(param,clazz[i]);
		}
		return params;
	}

	private static Object getVal(String val, Class<?> class1) {
		if("java.lang.String".equals(class1.getName())){
			return val;
		}
		if("int".equals(class1.getName()) ||"java.lang.Integer".equals(class1.getName())){
			return Integer.parseInt(val);
		}
		
		if("java.util.Date".equals(class1.getName())){
			return getDate(val);
		}
		if("java.util.Map".equals(class1.getName())){
			return CommonUtils.paramToMap(val);
		}
		//如果是复杂类型，则需要对应的是json字符串
		if(JSONUtils.isJSONstr(val)){
			return JSONUtils.toObject(val, class1);
		}
		return null;
	}

	private static Date getDate(String string) {
		try {
			long l=Long.parseLong(string);
			return new Date(l);
		} catch (NumberFormatException e) {
			//不是long类型，则认为是SimpleFormat
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mi:ss");
			Date d=null;
			try {
				d = sdf.parse(string);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				log.error("date format err",e1);
			}
			return d;
		}
	}
	
	

}
