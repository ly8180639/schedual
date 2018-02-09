package org.schedual.server.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtils {
	static Logger logger=LoggerFactory.getLogger(HttpUtils.class);
	
	public static JSONObject httpPost(String url,String token, String data) {
		   logger.info("url={},data={}",url,data);
	        HttpPost httpPost = new HttpPost(url);
	        CloseableHttpResponse response = null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        RequestConfig requestConfig = RequestConfig.custom().
	        		setSocketTimeout(2000).setConnectTimeout(2000).build();
	        httpPost.setConfig(requestConfig);
	        httpPost.addHeader("Content-Type", "application/json");
	        httpPost.addHeader("x-access-token",token);
	        try {
	        	StringEntity requestEntity = new StringEntity(data, "utf-8");
	            httpPost.setEntity(requestEntity);
	            
	            response = httpClient.execute(httpPost, new BasicHttpContext());

	            if (response.getStatusLine().getStatusCode() != 200) {
	            	logger.warn("request url failed, http code={}, url={}", Integer.valueOf(response.getStatusLine().getStatusCode()), url);
	                return null;
	            }
	            HttpEntity entity = response.getEntity();
	            if (entity != null) {
	                String resultStr = EntityUtils.toString(entity, "utf-8");
	                JSONObject result = JSON.parseObject(resultStr);
	                logger.info("Request Result:{}", result != null ? result.toJSONString() : "");
	                return result;
	            }
	        } catch (IOException e) {
	        	 logger.error("request url={}, exception, msg={}", url, e.getMessage());
	        	 logger.error("Http Error ==> ", e);
	        } finally {
	            if (response != null) try {
	                response.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        return null;
	    }
}
